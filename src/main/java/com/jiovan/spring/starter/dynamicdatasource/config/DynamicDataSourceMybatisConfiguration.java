/*
 * Copyright (c) 2020 Jiovan Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jiovan.spring.starter.dynamicdatasource.config;


import javax.sql.DataSource;
import jakarta.annotation.Nonnull;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.jiovan.spring.starter.mybatisdelegator.composer.MybatisConfigurationDelegator;
import com.jiovan.spring.starter.mybatisdelegator.config.MybatisDelegatorAutoConfiguration;


/**
 * Configuration for mybatis of dynamic datasource
 *
 * @author David Hsing
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnBooleanProperty(prefix = DynamicDataSourceJdbcConfiguration.PROPERTIES_PREFIX, name = "enabled", matchIfMissing = true)
@ConditionalOnProperty(prefix = DynamicDataSourceJdbcConfiguration.PROPERTIES_PREFIX, name = "mybatis")
@ConditionalOnClass(value = {DataSource.class, SqlSession.class, MybatisConfigurationDelegator.class})
@ConditionalOnBean(name = DynamicDataSourceJdbcConfiguration.DATA_SOURCE, value = MybatisConfigurationDelegator.class)
@AutoConfigureAfter(value = {MybatisDelegatorAutoConfiguration.class, DynamicDataSourceJdbcConfiguration.class})
public class DynamicDataSourceMybatisConfiguration {
    public static final String MYBATIS_PROPERTIES = "dynamicMybatisProperties";    // $NON-NLS-1$
    public static final String SQL_SESSION_FACTORY = "dynamicSqlSessionFactory";    // $NON-NLS-1$
    public static final String SQL_SESSION_TEMPLATE = "dynamicSqlSessionTemplate";    // $NON-NLS-1$

    /**
     * Returns a new {@link org.mybatis.spring.boot.autoconfigure.MybatisProperties} bound to the dynamic datasource mybatis properties prefix
     *
     * @return a new {@link org.mybatis.spring.boot.autoconfigure.MybatisProperties}
     */
    @Bean(name = MYBATIS_PROPERTIES)
    @ConditionalOnMissingBean(name = MYBATIS_PROPERTIES)
    @ConfigurationProperties(prefix = DynamicDataSourceJdbcConfiguration.PROPERTIES_PREFIX + ".mybatis")
    public MybatisProperties mybatisProperties() {
        return new MybatisProperties();
    }

    /**
     * Returns a {@link org.apache.ibatis.session.SqlSessionFactory} built from the given data source and mybatis properties
     *
     * @param delegator the mybatis configuration delegator that builds the factory
     * @param dataSource the dynamic routing data source used by the factory
     * @param properties the mybatis properties used to configure the factory
     *
     * @return a {@link org.apache.ibatis.session.SqlSessionFactory} produced by the delegator
     *
     * @throws Exception if the delegator fails to build the SQL session factory
     */
    @Bean(name = SQL_SESSION_FACTORY)
    @ConditionalOnBean(name = MYBATIS_PROPERTIES)
    @ConditionalOnMissingBean(name = SQL_SESSION_FACTORY)
    public SqlSessionFactory sqlSessionFactory(@Nonnull MybatisConfigurationDelegator delegator, @Qualifier(value = DynamicDataSourceJdbcConfiguration.DATA_SOURCE) @Nonnull DataSource dataSource, @Qualifier(value = MYBATIS_PROPERTIES) @Nonnull MybatisProperties properties) throws Exception {
        return delegator.sqlSessionFactory(dataSource, properties);
    }

    /**
     * Returns a {@link org.mybatis.spring.SqlSessionTemplate} built from the given factory and mybatis properties
     *
     * @param delegator the mybatis configuration delegator that builds the template
     * @param factory the SQL session factory used by the template
     * @param properties the mybatis properties used to configure the template
     *
     * @return a {@link org.mybatis.spring.SqlSessionTemplate} produced by the delegator
     */
    @Bean(name = SQL_SESSION_TEMPLATE)
    @ConditionalOnBean(name = {SQL_SESSION_FACTORY, MYBATIS_PROPERTIES})
    @ConditionalOnMissingBean(name = SQL_SESSION_TEMPLATE)
    public SqlSessionTemplate sqlSessionTemplate(@Nonnull MybatisConfigurationDelegator delegator, @Qualifier(value = SQL_SESSION_FACTORY) @Nonnull SqlSessionFactory factory, @Qualifier(value = MYBATIS_PROPERTIES) @Nonnull MybatisProperties properties) {
        return delegator.sqlSessionTemplate(factory, properties);
    }
}
