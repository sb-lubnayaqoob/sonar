/*
 * Copyright 2014-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.liquigraph.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.liquigraph.core.api.Liquigraph;
import org.liquigraph.core.configuration.Configuration;
import org.liquigraph.core.configuration.ConfigurationBuilder;

abstract class ChangelogExecutionMojoBase extends JdbcConnectionMojoBase {

    /**
     * Classpath location of the main change log file
     */
    @Parameter(property = "changelog", required = true)
    String changelog;

    /**
     * Comma-separated execution context list. If no context is specified, all Liquigraph change sets contexts will
     * match. If contexts are defined, the Liquigraph change sets without any contexts or with least 1 matching declared
     * context will match.
     */
    @Parameter(property = "executionContexts", defaultValue = "")
    String executionContexts = "";

    private final Liquigraph liquigraph = new Liquigraph();

    @Override
    public final void execute() throws MojoExecutionException {
        try {
            liquigraph.runMigrations(withExecutionMode(new ConfigurationBuilder()
                .withChangelogLoader(ChangeLogLoaders.changeLogLoader(project))
                .withExecutionContexts(ExecutionContexts.executionContexts(executionContexts))
                .withMasterChangelogLocation(changelog)
                .withDatabase(database)
                .withUsername(username)
                .withPassword(password)
                .withUri(jdbcUri))
                .build());
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    protected abstract ConfigurationBuilder withExecutionMode(ConfigurationBuilder configurationBuilder);

}
