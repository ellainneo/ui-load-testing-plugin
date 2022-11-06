package com.haulmont.ui.load.testing;

import org.gradle.api.Project;
import org.gradle.api.Plugin;

public class UILoadTestingPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getTasks().create("convertHarFileToJmeterScript", HarFileConverterTask.class);
        project.getExtensions().create("jmeterExtensions", UILoadTestingExtension.class);
    }
}
