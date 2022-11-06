package com.haulmont.ui.load.testing.jmeter;

import com.browserup.harreader.model.Har;
import com.browserup.harreader.model.HarEntry;
import com.browserup.harreader.model.HarPage;
import com.haulmont.ui.load.testing.UILoadTestingExtension;
import com.haulmont.ui.load.testing.util.HarHelper;
import com.haulmont.ui.load.testing.util.JMeterPropertiesBuilder;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.gui.LoopControlPanel;
import org.apache.jmeter.control.gui.TestPlanGui;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.http.control.CookieManager;
import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.protocol.http.control.gui.HttpTestSampleGui;
import org.apache.jmeter.protocol.http.gui.CookiePanel;
import org.apache.jmeter.protocol.http.gui.HeaderPanel;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.threads.gui.ThreadGroupGui;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.collections.ListedHashTree;
import org.gradle.api.Project;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestPlanBuilder {

    Project project;
    JMeterPropertiesBuilder propertiesBuilder = createJMeterPropertiesBuilder(project);

    public ListedHashTree getTestPlan(Har har) {

        File jmeterHome = new File(propertiesBuilder.getJmeterHome());

        if (!jmeterHome.exists()) {
            throw new IllegalStateException("JMeter home cannot be found");
        }

        File jmeterProperties = new File(jmeterHome.getPath() + propertiesBuilder.getPathDelimiter() + "bin"
                + propertiesBuilder.getPathDelimiter() + "jmeter.properties");

        StandardJMeterEngine jmeterEngine = new StandardJMeterEngine();

        JMeterUtils.setJMeterHome(jmeterHome.getPath());
        JMeterUtils.loadJMeterProperties(jmeterProperties.getPath());
        JMeterUtils.initLocale();

        ListedHashTree testPlanTree = new ListedHashTree();
        HTTPSamplerBuilder samplerBuilder = new HTTPSamplerBuilder();

        TestPlan testPlan = createTestPlan();
        testPlanTree.add(testPlan);

        HashTree threadGroupHashTree = testPlanTree.add(testPlan, createThreadGroup());
        threadGroupHashTree.add(createCookieManager());

        List<HarEntry> allEntries = har.getLog().getEntries();
        List<HarEntry> applicationEntries = getApplicationEntries(allEntries);

        List<HarPage> harPages = har.getLog().getPages();
        if (!harPages.isEmpty()) {
            harPages.forEach(harPage -> {

                String harPageId = harPage.getId();
                if (!HarHelper.isPageEmpty(applicationEntries, harPageId)) {
                    List<HarEntry> pageEntries = HarHelper.getEntriesByPageRef(applicationEntries, harPageId);
                    if (!pageEntries.isEmpty()) {

                        for (int i = 0; i < pageEntries.size(); i++) {
                            HTTPSampler httpSampler = samplerBuilder.getHTTPSampler(pageEntries.get(i).getRequest(),
                                    harPageId, i);
                            httpSampler.setProperty(TestElement.TEST_CLASS, HTTPSampler.class.getName());
                            httpSampler.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());

                            HeadersBuilder headersBuilder = new HeadersBuilder();
                            final HeaderManager headerManager = headersBuilder.getHeaderManager(pageEntries.get(i).
                                    getRequest());
                            headerManager.setProperty(TestElement.TEST_CLASS, HeaderManager.class.getName());
                            headerManager.setProperty(TestElement.GUI_CLASS, HeaderPanel.class.getName());
                            httpSampler.addTestElement(headerManager);

                            HashTree httpSamplerTree = samplerBuilder.getHTTPSamplerObject(httpSampler,
                                    pageEntries.get(i).getRequest());
                            threadGroupHashTree.add(httpSamplerTree);
                        }
                    }
                }
            });
        }
        return testPlanTree;
    }

    public void saveTestPlanToFile(ListedHashTree testPlanTree){
        try {
            SaveService.saveTree(testPlanTree, new FileOutputStream(propertiesBuilder.getJmeterHome() +
                    propertiesBuilder.getPathDelimiter() + propertiesBuilder.getTestPlanFileName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JMeterPropertiesBuilder createJMeterPropertiesBuilder(Project project) {
        UILoadTestingExtension extension = project.getExtensions().findByType(UILoadTestingExtension.class);
        return new JMeterPropertiesBuilder(project, extension);
    }

    private Integer getLoopsCount() {
        Integer loopsCount = propertiesBuilder.getLoopsCount();
        if (loopsCount == null) {
            throw new IllegalStateException("Property 'loopsCount' cannot be null");
        }
        return loopsCount;
    }

    private Boolean getIsLoopContinueForever() {
        return propertiesBuilder.getIsLoopContinueForever();
    }

    private Boolean getIsLoopFirst() {
        return propertiesBuilder.getIsLoopFirst();
    }

    private LoopController createLoopController() {
        LoopController loopController = new LoopController();
        if (!getIsLoopContinueForever()) {
            loopController.setLoops(propertiesBuilder.getLoopsCount().intValue());
        } else {
            loopController.setContinueForever(true);
        }
        if (getIsLoopFirst() != null) {
            loopController.setFirst(propertiesBuilder.getIsLoopFirst().booleanValue());
        }
        loopController.setProperty(TestElement.TEST_CLASS, LoopController.class.getName());
        loopController.setProperty(TestElement.GUI_CLASS, LoopControlPanel.class.getName());
        return loopController;
    }

    private ThreadGroup createThreadGroup() {
        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setNumThreads(propertiesBuilder.getNumThreads());
        threadGroup.setRampUp(propertiesBuilder.getRampUp());
        threadGroup.setIsSameUserOnNextIteration(propertiesBuilder.getIsSameUserOnNextIteration());
        threadGroup.setSamplerController(createLoopController());
        threadGroup.setProperty(TestElement.TEST_CLASS, ThreadGroup.class.getName());
        threadGroup.setProperty(TestElement.GUI_CLASS, ThreadGroupGui.class.getName());
        return threadGroup;
    }

    private TestPlan createTestPlan() {
        TestPlan testPlan = new TestPlan(propertiesBuilder.getTestPlanName());
        testPlan.setProperty(TestElement.TEST_CLASS, TestPlan.class.getName());
        testPlan.setProperty(TestElement.GUI_CLASS, TestPlanGui.class.getName());
        testPlan.setUserDefinedVariables((Arguments) new ArgumentsPanel().createTestElement());
        return testPlan;
    }

    private CookieManager createCookieManager() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setName(propertiesBuilder.getCookieManagerName());
        cookieManager.setClearEachIteration(propertiesBuilder.getIsClearEachIteration());
        cookieManager.setControlledByThread(propertiesBuilder.getIsControlledByThread());
        cookieManager.setEnabled(true);
        cookieManager.setProperty(TestElement.TEST_CLASS, CookieManager.class.getName());
        cookieManager.setProperty(TestElement.GUI_CLASS, CookiePanel.class.getName());
        return cookieManager;
    }

    private List<HarEntry> getApplicationEntries(List<HarEntry> allEntries) {
        List<HarEntry> appEntries = new ArrayList<HarEntry>();

        if (!allEntries.isEmpty()) {
            allEntries.forEach(harEntry -> {
                if (harEntry.getRequest() != null && harEntry.getRequest().getUrl()
                        .contains(propertiesBuilder.getHost())) {
                    appEntries.add(harEntry);
                }
            });
        }
        return appEntries;
    }
}
