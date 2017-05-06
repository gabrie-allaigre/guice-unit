package com.talanlabs.guiceunit.extensions.dbunit;

import com.google.inject.Inject;
import com.talanlabs.guiceunit.AbstractTestListener;
import com.talanlabs.guiceunit.TestContext;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbunitRunListener extends AbstractTestListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbunitRunListener.class);

    private final DbSessionManager sqlSessionManager;

    private DbunitDataset dbunitDataset;

    @Inject
    public DbunitRunListener(DbSessionManager sqlSessionManager) {
        super();

        this.sqlSessionManager = sqlSessionManager;
    }

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        dbunitDataset = testContext.getTestClass().getAnnotation(DbunitDataset.class);
    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        if (dbunitDataset != null) {
            sqlSessionManager.start();

            execute(dbunitDataset.value(), dbunitDataset.beforeType());
        }
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        if (dbunitDataset != null) {
            execute(dbunitDataset.value(), dbunitDataset.afterType());

            sqlSessionManager.rollbackAndClose();
        }
    }

    private void execute(String[] values, DbunitOperation operation) throws Exception {
        if (values == null || values.length == 0 || DbunitOperation.NONE.equals(operation)) {
            return;
        }

        FlatXmlDataSetBuilder xmlDSBuilder = new FlatXmlDataSetBuilder();
        xmlDSBuilder.setColumnSensing(true);
        xmlDSBuilder.setCaseSensitiveTableNames(false);

        DatabaseOperation databaseOperation = createDatabaseOperation(operation);

        IDatabaseConnection dbUnitConnection = null;
        try {
            dbUnitConnection = new DatabaseConnection(sqlSessionManager.getConnection());//new DatabaseDataSourceConnection(dataSource);

            for (String value : values) {
                IDataSet dataSet = xmlDSBuilder.build(DbunitRunListener.class.getClassLoader().getResourceAsStream(value));
                if (dbUnitConnection.getConnection().getAutoCommit()) {
                    DatabaseOperation.TRANSACTION(databaseOperation).execute(dbUnitConnection, dataSet);
                } else {
                    databaseOperation.execute(dbUnitConnection, dataSet);
                    //dbUnitConnection.getConnection().commit();
                }
            }
        } finally {
            if (dbUnitConnection != null) {
                //dbUnitConnection.close();
            }
        }
    }

    private DatabaseOperation createDatabaseOperation(DbunitOperation operation) {
        switch (operation) {
            case CLEAN_INSERT:
                return DatabaseOperation.CLEAN_INSERT;
            case DELETE:
                return DatabaseOperation.DELETE;
            case DELETE_ALL:
                return DatabaseOperation.DELETE_ALL;
            case INSERT:
                return DatabaseOperation.INSERT;
            case REFRESH:
                return DatabaseOperation.REFRESH;
            case TRUNCATE_TABLE:
                return DatabaseOperation.TRUNCATE_TABLE;
            case UPDATE:
                return DatabaseOperation.UPDATE;
        }
        return DatabaseOperation.NONE;
    }
}