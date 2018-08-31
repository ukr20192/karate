/*
 * The MIT License
 *
 * Copyright 2018 Intuit Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.intuit.karate.core;

import com.intuit.karate.CallContext;
import com.intuit.karate.FileLogAppender;
import com.intuit.karate.LogAppender;
import com.intuit.karate.ScriptEnv;
import java.io.File;
import java.util.function.Consumer;

/**
 *
 * @author pthomas3
 */
public class ExecutionContext {

    public final Feature feature;
    public final ScriptEnv env;
    public final CallContext callContext;
    public final FeatureResult result;
    public final LogAppender appender;
    public final Consumer<Runnable> system;

    private static final Consumer<Runnable> SYNC_EXECUTOR = r -> r.run();

    public ExecutionContext(Feature feature, ScriptEnv env, CallContext callContext, Consumer<Runnable> system) {
        this.feature = feature;
        result = new FeatureResult(feature);
        this.env = env;
        this.callContext = callContext;
        if (system == null) {
            this.system = SYNC_EXECUTOR;
        } else {
            this.system = system;
        }
        if (callContext.useLogAppenderFile) {
            File logFileDir = new File(Engine.getBuildDir() + "/surefire-reports/");
            if (!logFileDir.exists()) {
                logFileDir.mkdirs();
            }
            String basePath = feature.getPackageQualifiedName();
            appender = new FileLogAppender(logFileDir.getPath() + "/" + basePath + ".log", env.logger);
        } else {
            appender = LogAppender.NO_OP;
        }
    }

}