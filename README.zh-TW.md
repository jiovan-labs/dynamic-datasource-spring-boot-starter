[English](README.md) | [簡體中文](README.zh-CN.md) | [繁體中文](README.zh-TW.md)

# 動態資料來源 Spring Boot Starter

讓 Spring Boot 應用快速整合動態 `DataSource`。

## 快速開始

- 匯入相依性

```xml
    <dependency>
        <groupId>com.jiovan.spring.starter</groupId>
        <artifactId>dynamic-datasource-spring-boot-starter</artifactId>
        <version>LATEST</version>
    </dependency>
```

> 預設情況下，此 starter 會自動生效，你可以透過 `jiovan.dynamic-datasource.enabled = false` 關閉它。

- 使用字首 `jiovan.dynamic-datasource` 設定 Spring Boot 的 `application.yml`

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

> 這些識別符號（`testdb1`、`testdb2`、`testdb3`）用作切換多個 `DataSource` 的鍵，你可以隨意命名，但需注意名稱必須由字母和數字組成。

- 在（非靜態）方法上標註 `@DataSourceRouting` 註解即可！
以上述資料庫 `testdb1` 為例，註解寫法如下
```
    @DataSourceRouting(key = "testdb1")
```

> 注意其中的 `key` 部分，它是上一步 `datasource-route` 節點下的資料庫識別符號。

- **可選功能**：你也可以在程式碼中定義資料庫（而不僅限於 `application.properties`），只需使用 `@DataSourceDefinition` 註解宣告 `DataSource` Bean 即可。

- 此 starter 支援全球主流資料來源連線池，包括
  - c3p0
  - dbcp2
  - druid
  - hikari
  - oracle ucp
  - tomcat
  - vibur

## 文件

- GitHub：https://github.com/jiovan-labs/dynamic-datasource-spring-boot-starter

## 環境要求

- jdk 17+

## 許可證

本專案採用 [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0) 許可證。

請參閱 `NOTICE.txt` 檔案瞭解所需的宣告與歸屬資訊。

## 捐贈

喜歡這個工具包嗎？歡迎[向我們捐贈](https://jiovan.com/donation)以支援開發。

## 版權

北京玖漫科技有限公司

## 網站

- 玖漫：https://jiovan.com
