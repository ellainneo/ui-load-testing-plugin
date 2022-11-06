package com.haulmont.ui.load.testing;

import com.browserup.harreader.model.Har;
import com.haulmont.ui.load.testing.jmeter.TestPlanBuilder;
import com.haulmont.ui.load.testing.util.HarHelper;
import com.haulmont.ui.load.testing.util.JMeterPropertiesBuilder;
import org.apache.jorphan.collections.ListedHashTree;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;

public class HarFileConverterTask extends DefaultTask {

    @TaskAction
    public void convertHarFileToJmeterScript() {

        Project project = getProject();
        UILoadTestingExtension jmeterExtension = project.getExtensions().findByType(UILoadTestingExtension.class);
        JMeterPropertiesBuilder jMeterPropertiesBuilder = new JMeterPropertiesBuilder(project, jmeterExtension);

        //ToDo Add har file path property
        Har har = HarHelper.readHarLogFromFile("");

        TestPlanBuilder testPlanBuilder = new TestPlanBuilder();
        ListedHashTree testPlan = testPlanBuilder.getTestPlan(har);
        testPlanBuilder.saveTestPlanToFile(testPlan);


    }
}
