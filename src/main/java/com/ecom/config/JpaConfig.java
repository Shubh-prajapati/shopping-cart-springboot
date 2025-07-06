//package com.ecom.config;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//
//import javax.sql.DataSource;
//import java.util.HashMap;
//
//@Configuration
////@EnableJpaRepositories(basePackages = "com.ecom.repository")  // Replace with your repository package
//public class JpaConfig {
//
//    @Primary
//    @Bean(name = "entityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
//            EntityManagerFactoryBuilder builder, @Qualifier("dataSource") DataSource dataSource) {
//        return builder.dataSource(dataSource).packages("com.ecom.model").build();
//    }
//
//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, DataSource dataSource) {
        return builder
//                .dataSource(dataSource)
//                .packages("com.ecom.model")  // Replace with your entity package
//                .build();
//    }
//
//    @Bean
//    public EntityManagerFactoryBuilder entityManagerFactory(){
//        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(),new HashMap<>(),null);
//    }
//
//}
