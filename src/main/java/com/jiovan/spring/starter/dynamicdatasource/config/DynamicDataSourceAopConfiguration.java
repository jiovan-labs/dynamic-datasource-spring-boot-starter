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


import java.util.Optional;
import javax.sql.DataSource;
import jakarta.annotation.Nonnull;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.jdbc.core.JdbcOperations;
import com.jiovan.plexus.spring.condition.annotation.ConditionalOnAllBooleanProperties;
import com.jiovan.spring.starter.dynamicdatasource.advisor.DataSourceDefinitionAdvisor;
import com.jiovan.spring.starter.dynamicdatasource.advisor.DataSourceRoutingAdvisor;
import com.jiovan.spring.starter.dynamicdatasource.composer.DynamicDataSourceHolder;
import com.jiovan.spring.starter.dynamicdatasource.interceptor.DataSourceDefinitionInterceptor;
import com.jiovan.spring.starter.dynamicdatasource.interceptor.DataSourceRoutingInterceptor;
import com.jiovan.spring.starter.dynamicdatasource.property.DynamicDataSourceProperties;


/**
 * Configuration for AOP of dynamic datasource
 *
 * @author David Hsing
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnAllBooleanProperties(value = {
    @ConditionalOnBooleanProperty(prefix = DynamicDataSourceJdbcConfiguration.PROPERTIES_PREFIX, name = "enabled", matchIfMissing = true),
    @ConditionalOnBooleanProperty(prefix = "spring.aop", name = "auto", matchIfMissing = true)
})
@ConditionalOnClass(value = {DataSource.class, JdbcOperations.class, Advice.class, Advisor.class})
@AutoConfigureAfter(value = {AopAutoConfiguration.class, DynamicDataSourceJdbcConfiguration.class})
@EnableConfigurationProperties(value = DynamicDataSourceProperties.class)
public class DynamicDataSourceAopConfiguration {
    public static final String DEFINITION_INTERCEPTOR = "dynamicDataSourceDefinitionInterceptor";    // $NON-NLS-1$
    public static final String DEFINITION_ADVISOR = "dynamicDataSourceDefinitionAdvisor";    // $NON-NLS-1$
    public static final String ROUTING_INTERCEPTOR = "dynamicDataSourceRoutingInterceptor";    // $NON-NLS-1$
    public static final String ROUTING_ADVISOR = "dynamicDataSourceRoutingAdvisor";    // $NON-NLS-1$

    /**
     * Returns a {@link com.jiovan.spring.starter.dynamicdatasource.interceptor.DataSourceDefinitionInterceptor} that resolves data source definitions
     *
     * @param helper the data source holder used to register and look up target data sources
     *
     * @return a {@link com.jiovan.spring.starter.dynamicdatasource.interceptor.DataSourceDefinitionInterceptor} backed by the given helper
     */
    @Bean(name = DEFINITION_INTERCEPTOR)
    @Role(value = BeanDefinition.ROLE_INFRASTRUCTURE)
    @ConditionalOnMissingBean(name = DEFINITION_INTERCEPTOR)
    public DataSourceDefinitionInterceptor definitionInterceptor(@Qualifier(value = DynamicDataSourceJdbcConfiguration.DATA_SOURCE_HELPER) @Nonnull DynamicDataSourceHolder helper) {
        return new DataSourceDefinitionInterceptor(helper);
    }

    /**
     * Returns a {@link com.jiovan.spring.starter.dynamicdatasource.advisor.DataSourceDefinitionAdvisor} that applies the definition interceptor
     *
     * @param interceptor the method interceptor that resolves data source definitions
     * @param properties the dynamic datasource properties providing the advisor order
     *
     * @return a {@link com.jiovan.spring.starter.dynamicdatasource.advisor.DataSourceDefinitionAdvisor} with the configured order
     */
    @Bean(name = DEFINITION_ADVISOR)
    @Role(value = BeanDefinition.ROLE_INFRASTRUCTURE)
    @ConditionalOnMissingBean(name = DEFINITION_ADVISOR)
    public DataSourceDefinitionAdvisor definitionAdvisor(@Qualifier(value = DEFINITION_INTERCEPTOR) @Nonnull MethodInterceptor interceptor, @Nonnull DynamicDataSourceProperties properties) {
        DataSourceDefinitionAdvisor result = new DataSourceDefinitionAdvisor(interceptor);
        Optional.ofNullable(properties.getDefinitionAdvisorOrder()).ifPresent(result::setOrder);
        return result;
    }

    /**
     * Returns a {@link com.jiovan.spring.starter.dynamicdatasource.interceptor.DataSourceRoutingInterceptor} that switches the active routing key
     *
     * @return a new {@link com.jiovan.spring.starter.dynamicdatasource.interceptor.DataSourceRoutingInterceptor}
     */
    @Bean(name = ROUTING_INTERCEPTOR)
    @Role(value = BeanDefinition.ROLE_INFRASTRUCTURE)
    @ConditionalOnMissingBean(name = ROUTING_INTERCEPTOR)
    public DataSourceRoutingInterceptor routingInterceptor() {
        return new DataSourceRoutingInterceptor();
    }

    /**
     * Returns a {@link com.jiovan.spring.starter.dynamicdatasource.advisor.DataSourceRoutingAdvisor} that applies the routing interceptor
     *
     * @param interceptor the method interceptor that switches the routing key
     * @param properties the dynamic datasource properties providing the advisor order
     *
     * @return a {@link com.jiovan.spring.starter.dynamicdatasource.advisor.DataSourceRoutingAdvisor} with the configured order
     */
    @Bean(name = ROUTING_ADVISOR)
    @Role(value = BeanDefinition.ROLE_INFRASTRUCTURE)
    @ConditionalOnMissingBean(name = ROUTING_ADVISOR)
    public DataSourceRoutingAdvisor routingAdvisor(@Qualifier(value = ROUTING_INTERCEPTOR) @Nonnull MethodInterceptor interceptor, @Nonnull DynamicDataSourceProperties properties) {
        DataSourceRoutingAdvisor result = new DataSourceRoutingAdvisor(interceptor);
        Optional.ofNullable(properties.getRoutingAdvisorOrder()).ifPresent(result::setOrder);
        return result;
    }
}
