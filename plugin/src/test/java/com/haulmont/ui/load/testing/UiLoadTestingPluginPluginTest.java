package com.haulmont.ui.load.testing;

import org.gradle.testfixtures.ProjectBuilder;
import org.gradle.api.Project;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UiLoadTestingPluginPluginTest {
    @Test void pluginRegistersATask() {
        Project project = ProjectBuilder.builder().build();
        //project.getPlugins().apply("com.haulmont.ui.load.testing");

        // Verify the result
        //assertNotNull(project.getTasks().findByName("convertHarFileToJmeterScript"));
    }
}
