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

package com.jiovan.spring.starter.dynamicdatasource.util;


import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import lombok.extern.slf4j.Slf4j;


/**
 * Utilities for dynamic datasource routing
 *
 * @author David Hsing
 */
@Slf4j
@SuppressWarnings("unused")
public abstract class DynamicRoutingKeyHolder {
    private static final ThreadLocal<String> keyHolder = new ThreadLocal<>();

    /**
     * Returns the routing key bound to the current thread
     *
     * @return the current thread's routing key, or {@code null} if none is bound
     */
    @Nullable
    public static String getRoutingKey() {
        return keyHolder.get();
    }

    /**
     * Binds the given routing key to the current thread, clearing any existing key when it is blank
     *
     * @param key the routing key to bind, may be {@code null} or blank to clear the current binding
     */
    public static synchronized void setRoutingKey(@Nullable String key) {
        if (StringUtils.isNotBlank(key)) {
            if (log.isDebugEnabled()) {
                log.debug("Routing to dynamic data source with key '{}'", key);
            }
            keyHolder.set(key);
        } else {
            clearRoutingKey();
        }
    }

    /**
     * Removes the routing key bound to the current thread
     */
    public static synchronized void clearRoutingKey() {
        if (log.isDebugEnabled()) {
            log.debug("Cleaning up the dynamic data source routing key");
        }
        keyHolder.remove();
    }
}
