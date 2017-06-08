# Guice Unit

```xml
<dependencies>
    <dependency>
        <groupId>com.talanlabs</groupId>
        <artifactId>guice-unit</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

Add `@RunWith(GuiceJunit4ClassRunner.class)` in Junit class.
Add `@GuiceModules(...)` with guice modules
Add listner `@TestListeners`

Dbunit extension : Use DbunitRunListener.class

Example with MyBatis:

```java
public class ServicesTestModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new ServicesModule());
    }
    
    @Provides
    @Singleton
    DbSessionManager dbSessionManager(final SqlSessionManager sqlSessionManager) {
        return new DbSessionManager() {
            @Override
            public void open() {
                sqlSessionManager.startManagedSession(false);
            }

            @Override
            public void close() {
                sqlSessionManager.rollback(true);
                sqlSessionManager.close();
            }

            @Override
            public Connection getConnection() {
                return sqlSessionManager.getConnection();
            }
        };
    }
}
```

```java
@RunWith(GuiceJunit4ClassRunner.class)
@GuiceModules(ServicesTestModule.class)
@TestListeners({DbunitRunListener.class})
@DbunitDataset("dbunit/admin_dataset.xml")
public class AdminIT {

    @Inject
    private AdminService adminService;

    @Test
    public void testFind() {
        Assertions.assertThat(adminService.find(1)).isNotNull();
    }    
}
```

