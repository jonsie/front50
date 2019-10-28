/*
 * Copyright 2019 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.netflix.spectator.api.Registry
import com.netflix.spinnaker.front50.config.CommonStorageServiceDAOConfig
import com.netflix.spinnaker.front50.model.SqlStorageService
import com.netflix.spinnaker.front50.model.plugin.SqlPluginRepository
import com.netflix.spinnaker.kork.sql.config.DefaultSqlConfiguration
import com.netflix.spinnaker.kork.sql.config.SqlProperties
import org.jooq.DSLContext
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import java.time.Clock

@Configuration
@ConditionalOnProperty("sql.enabled")
@Import(DefaultSqlConfiguration::class)
class SqlConfiguration : CommonStorageServiceDAOConfig() {

  @Bean
  fun sqlStorageService(
    objectMapper: ObjectMapper,
    registry: Registry,
    jooq: DSLContext,
    sqlProperties: SqlProperties
  ): SqlStorageService =
    SqlStorageService(
      objectMapper,
      registry,
      jooq,
      Clock.systemDefaultZone(),
      sqlProperties.retries,
      1000
    )

  @Bean
  fun sqlPluginRepository(
    jooq: DSLContext
  ): SqlPluginRepository =
    SqlPluginRepository(
      jooq
    )
}
