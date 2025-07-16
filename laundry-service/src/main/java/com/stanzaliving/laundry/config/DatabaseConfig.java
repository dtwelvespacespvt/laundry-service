package com.stanzaliving.laundry.config;


import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.stanzaliving.laundry.util.ReplicationRoutingDataSource;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaAuditing
@EnableJpaRepositories(entityManagerFactoryRef = "primaryEntityManager", transactionManagerRef = "primaryTransactionManager", basePackages = {
        "com.stanzaliving.laundry.repository"})
@Log4j2
public class DatabaseConfig {

    @Value("${spring.primary.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.primary.datasource.username}")
    private String datasourceUsername;

    @Value("${spring.primary.datasource.password}")
    private String datasourcePassword;

    @Value("${spring.read.datasource.url}")
    private String readDatasourceUrl;

    @Value("${spring.read.datasource.username}")
    private String readDatasourceUsername;

    @Value("${spring.read.datasource.driverClassName}")
    private String readDatasourceDriverClass;

    @Value("${spring.read.datasource.password}")
    private String readDatasourcePassword;

    @Value("${c3p0.initialPoolSize}")
    private String initialPoolSize;

    @Value("${c3p0.maxPoolSize}")
    private String maxPoolSize;

    @Value("${c3p0.minPoolSize}")
    private String minPoolSize;

    @Value("${c3p0.maxIdleTime}")
    private String maxIdleTime;

    @Autowired(required = false)
    private PersistenceUnitManager persistenceUnitManager;

    @Bean
    @Primary
    @ConfigurationProperties("spring.jpa")
    public JpaProperties primaryJpaProperties() {
        return new JpaProperties();
    }

    /**
     * Main DataSource
     * <p>
     * Application must use this dataSource.
     */
    @Primary
    @Bean
   // @DependsOn({"primaryDataSource", "readDataSource", "routingDataSource"})
    @DependsOn({"primaryDataSource", "routingDataSource"})
    public javax.sql.DataSource dataSource() throws PropertyVetoException {
        return new LazyConnectionDataSourceProxy(routingDataSource());
    }

    @Bean
    public javax.sql.DataSource routingDataSource() throws PropertyVetoException {
        ReplicationRoutingDataSource routingDataSource = new ReplicationRoutingDataSource();

        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("write", primaryDataSource());
        //dataSourceMap.put("read", readDataSource());
        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(primaryDataSource());

        return routingDataSource;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.primary.datasource")
    public ComboPooledDataSource primaryDataSource() throws PropertyVetoException {

        ComboPooledDataSource primaryDataSource = createDataSource();
        primaryDataSource.setDriverClass(readDatasourceDriverClass);
        primaryDataSource.setJdbcUrl(datasourceUrl);
        primaryDataSource.setUser(datasourceUsername);
        primaryDataSource.setPassword(datasourcePassword);

        return primaryDataSource;
    }

    private ComboPooledDataSource createDataSource() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass("org.postgresql.Driver");
        } catch (PropertyVetoException pve) {
            log.error("Cannot load datasource driver org.postgresql.Driver : " + pve.getMessage(), pve);
        }
        dataSource.setInitialPoolSize(Integer.parseInt(initialPoolSize));
        dataSource.setMinPoolSize(Integer.parseInt(minPoolSize));
        dataSource.setMaxPoolSize(Integer.parseInt(maxPoolSize));
        dataSource.setMaxIdleTime(Integer.parseInt(maxIdleTime));
        dataSource.setIdleConnectionTestPeriod(2);
        dataSource.setPreferredTestQuery("SELECT 1");
        return dataSource;
    }


    @Primary
    @Bean(name = "primaryEntityManager")
    public LocalContainerEntityManagerFactoryBean primaryEntityManager(JpaProperties primaryJpaProperties) throws PropertyVetoException {
        EntityManagerFactoryBuilder builder = createEntityManagerFactoryBuilder(primaryJpaProperties);
        return builder.dataSource(dataSource())
                .packages("com.stanzaliving.laundry.entity").build();
    }

    @Primary
    @Bean(name = "primaryTransactionManager")
    public JpaTransactionManager primaryTransactionManager(
            @Qualifier("primaryEntityManager") EntityManagerFactory primaryEntityManager) {
        return new JpaTransactionManager(primaryEntityManager);
    }

    private EntityManagerFactoryBuilder createEntityManagerFactoryBuilder(JpaProperties primaryJpaProperties) {
        JpaVendorAdapter jpaVendorAdapter = createJpaVendorAdapter(primaryJpaProperties);
        return new EntityManagerFactoryBuilder(jpaVendorAdapter, primaryJpaProperties.getProperties(),
                this.persistenceUnitManager);
    }

    private JpaVendorAdapter createJpaVendorAdapter(JpaProperties jpaProperties) {
        AbstractJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(jpaProperties.isShowSql());
        adapter.setDatabase(jpaProperties.getDatabase());
        adapter.setDatabasePlatform(jpaProperties.getDatabasePlatform());
        adapter.setGenerateDdl(jpaProperties.isGenerateDdl());
        return adapter;
    }

}
