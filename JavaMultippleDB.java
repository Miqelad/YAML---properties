/*Главное писать jdbcurl –вместо url
Driver-class-name в таком вариент, чтобы можно было их изменять
*/
/*yml*/
spring.output.ansi.enabled: ALWAYS

spring:
  datasource:
    primary:
      Driver-class-name: com.microsoft.sqlserver.jdbc.SQLServerDriver
      jdbcurl: jdbc:sqlserver://****.**.**.**:****;databaseName=****;integratedSecurity=true
      username: pmikeladze
      password: dsad23
    secondary:
          Driver-class-name: com.microsoft.sqlserver.jdbc.SQLServerDriver
          jdbcurl: jdbc:sqlserver://****.**.**.**:****;databaseName=****;;integratedSecurity=true
          username: pmikeladze
          password: dsad23

  jpa:
    show-sql: true
server:
  port: 8081
##формат вывода логов
#logging.pattern.console: '%C{1.} [%-5level] %d{HH:mm:ss} - %msg%n'
/*Primary класс - он в приоритете*/
package dme.aero.corp.tariffEffective.project.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author miq 25.05.2022
 */
@Configuration /*помечает данный класс как конфигуратор*/
@EnableTransactionManagement /*включить управление транзакциями*/
//включаем JPARepository ведь мы работает со спринг дата jpa, нужен пакет
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactory"
        ,basePackages = "Референс путь до репозитория")
public class TotalReportConfig {
    /*С помощью указания префикса, он поймет, к какой бд относится*/
    @Primary
    @Bean(name="datasource")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource dataSource(){
        return DataSourceBuilder.create().build();
    }

    /*    Создаем контейнер для Entity*/

    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(
            EntityManagerFactoryBuilder builder
            ,@Qualifier("datasource") DataSource dataSource) {
        /*Укажем настройки для hibernate*/
        Map<String,Object> properties = new HashMap<>();
         properties.put("hibernate.hbm2ddl.auto","none");
        properties.put("hibernate.dialect","org.hibernate.dialect.SQLServerDialect");
        //необходимо указать путь к сущнсоти и имя сущности
        /*Указываем все сущности относящиеся к одной базе данных*/
        return builder
                .dataSource(dataSource)
                .properties(properties)
                .packages("Путь до энтити референс путь")
                .persistenceUnit("Имя класса энтити")
          //каждое энтити указываем
                .build();
    }

    /*для очистки и создания нового менеджера*/
    @Primary
    @Bean(name="transactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);

    }
}







/*Вторая база*/
package dme.aero.corp.tariffEffective.tariff.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author miq 25.05.2022
 */
@Configuration /*помечает данный класс как конфигуратор*/
@EnableTransactionManagement /*включить управление транзакциями*/
//включаем JPARepository ведь мы работает со спринг дата jpa, нужен пакет
@EnableJpaRepositories(
        entityManagerFactoryRef = "testEntityManagerFactory"
        , basePackages = "Путь репозитория"
        , transactionManagerRef = "bookTransactionManager" )//обязательное поле у других бд, которые не примари//
public class BaseA82Config {
    /*Настраиваем источники данных вручную*/



    /*мы используем  @Primary , чтобы отдавать предпочтение компоненту, когда имеется несколько компонентов одного типа.*/
    @Bean(name = "testDatasource")//имя бина понадобиться в контейнере//TODO:
    @ConfigurationProperties(prefix = "spring.datasource.secondary")//TODO:
    public DataSource dataSource() {
        /*специальный билдер, который позаботится о создании источника данных*/
        return DataSourceBuilder.create().build();
    }


    /*    Создаем контейнер для Entity*/



    @Bean(name = "testEntityManagerFactory")//TODO:
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(
            EntityManagerFactoryBuilder builder
            ,@Qualifier("testDatasource") DataSource dataSource) {//TODO:
        /*Укажем настройки для hibernate*/
        Map<String,Object> properties = new HashMap<>();
         properties.put("hibernate.hbm2ddl.auto","update");
        properties.put("hibernate.dialect","org.hibernate.dialect.SQLServerDialect");
        //необходимо указать путь к сущнсоти и имя сущности
        /*Указываем все сущности относящиеся к одной базе данных*/
        return builder
                .dataSource(dataSource)
                .properties(properties)
                .packages("референс путь энтити")//TODO:
                .persistenceUnit("Имя класса энтити")//TODO:
               //каждую энтити нужно указать
                //          .persistenceUnit("TutorEntity")
                .build();
    }



    @Bean(name="bookTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("testEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);

    }
}
