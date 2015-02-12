/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.tinkerpop.gremlin.console.commands

import com.tinkerpop.gremlin.console.Mediator
import org.codehaus.groovy.tools.shell.CommandSupport
import org.codehaus.groovy.tools.shell.Groovysh

/**
 * Uninstall a maven dependency from the Console's path.
 *
 * @author Stephen Mallette (http://stephen.genoprime.com)
 */
class UninstallCommand extends CommandSupport {

    private final Mediator mediator

    public UninstallCommand(final Groovysh shell, final Mediator mediator) {
        super(shell, ":uninstall", ":-")
        this.mediator = mediator
    }

    @Override
    def Object execute(final List<String> arguments) {
        final String module = arguments.size() >= 1 ? arguments.get(0) : null
        if (module == null || module.isEmpty()) return "Specify the name of the module containing plugins to uninstall"

        final String extClassPath = getPathFromDependency(module)

        final File f = new File(extClassPath)
        if (!f.exists())
            return "There is no module with the name $module to remove - $extClassPath"
        else {
            f.deleteDir()
            return "Uninstalled $module - restart the console for removal to take effect"
        }
    }

    private static String getPathFromDependency(final String module) {
        def fileSep = System.getProperty("file.separator")
        def extClassPath = System.getProperty("user.dir") + fileSep + "ext" + fileSep + (String) module
        return extClassPath
    }
}