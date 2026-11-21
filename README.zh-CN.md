[English](README.md) | [简体中文](README.zh-CN.md) | [繁體中文](README.zh-TW.md)

# 动态数据源 Spring Boot Starter

让 Spring Boot 应用快速集成动态 `DataSource`。

## 快速开始

- 导入依赖

```xml
    <dependency>
        <groupId>com.jiovan.spring.starter</groupId>
        <artifactId>dynamic-datasource-spring-boot-starter</artifactId>
        <version>LATEST</version>
    </dependency>
```

> 默认情况下，此 starter 会自动生效，你可以通过 `jiovan.dynamic-datasource.enabled = false` 关闭它。

- 使用前缀 `jiovan.dynamic-datasource` 配置 Spring Boot 的 `application.yml`

```yml
jiovan:
    dynamic-datasource:
        datasource-route:
            testdb1:
                url: 'jdbc:mysql://127.0.0.1:3306/test_db1'
                driver-class-name: com.mysql.cj.jdbc.Driver
                type: com.zaxxer.hikari.HikariDataSource
            testdb2:
                url: 'jdbc:mysql://127.0.0.1:3306/test_db2'
                driver-class-name: com.mysql.cj.jdbc.Driver
                type: com.mchange.v2.c3p0.ComboPooledDataSource
            testdb3:
                url: 'jdbc:mysql://127.0.0.1:3306/test_db3'
                driver-class-name: com.mysql.cj.jdbc.Driver
                type: org.apache.commons.dbcp2.BasicDataSource
```

> 这些标识符（`testdb1`、`testdb2`、`testdb3`）用作切换多个 `DataSource` 的键，你可以随意命名，但需注意名称必须由字母和数字组成。

- 在（非静态）方法上标注 `@DataSourceRouting` 注解即可！
以上述数据库 `testdb1` 为例，注解写法如下
```
    @DataSourceRouting(key = "testdb1")
```

> 注意其中的 `key` 部分，它是上一步 `datasource-route` 节点下的数据库标识符。

- **可选功能**：你也可以在代码中定义数据库（而不仅限于 `application.properties`），只需使用 `@DataSourceDefinition` 注解声明 `DataSource` Bean 即可。

- 此 starter 支持全球主流数据源连接池，包括
  - c3p0
  - dbcp2
  - druid
  - hikari
  - oracle ucp
  - tomcat
  - vibur

## 文档

- GitHub：https://github.com/jiovan-labs/dynamic-datasource-spring-boot-starter

## 环境要求

- jdk 17+

## 许可证

本项目采用 [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0) 许可证。

请参阅 `NOTICE.txt` 文件了解所需的声明与归属信息。

## 捐赠

喜欢这个工具包吗？欢迎[向我们捐赠](https://jiovan.com/donation)以支持开发。

## 版权

北京玖漫科技有限公司

## 网站

- 玖漫：https://jiovan.com
