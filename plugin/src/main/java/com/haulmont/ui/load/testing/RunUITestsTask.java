package com.haulmont.ui.load.testing;

import com.haulmont.ui.load.testing.uitests.listeners.CustomProxyTestListener;
import com.haulmont.ui.load.testing.util.JMeterPropertiesBuilder;
import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.logging.LogLevel;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.testing.TestFrameworkOptions;
import org.gradle.api.tasks.testing.junitplatform.JUnitPlatformOptions;
import org.gradle.api.tasks.testing.testng.TestNGOptions;
import org.gradle.api.tasks.testing.Test;
import org.gradle.util.internal.ConfigureUtil;
import org.testng.IExecutionListener;
import org.testng.TestNG;

import java.io.File;
import java.util.*;

public class RunUITestsTask extends Test {

    @TaskAction
    public void runUITests() {

        Project project = getProject();
        UILoadTestingExtension extensions = project.getExtensions().findByType(UILoadTestingExtension.class);
        JMeterPropertiesBuilder propertiesBuilder = new JMeterPropertiesBuilder(project, extensions);

        File suitesFile = new File("src/test/suites.xml");
        if (!suitesFile.exists()) {
            throw new IllegalStateException("File: " + suitesFile.getName() + " doesn't exist");
        }

        List<File> suites = new ArrayList<>();
        suites.add(suitesFile);

        Test testTask = (Test) project.getTasks().findByName("runUITests");
        testTask.getLogging().captureStandardOutput(LogLevel.INFO);
        testTask.useTestNG();

        // TestNG Options
        TestNGOptions testTaskOptions = (TestNGOptions) testTask.getOptions();
        testTaskOptions.setSuiteXmlFiles(suites);
        testTask.setScanForTestClasses(false);
        testTask.executeTests();
    }
}
