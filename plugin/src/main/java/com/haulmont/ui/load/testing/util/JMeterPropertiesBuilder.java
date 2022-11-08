package com.haulmont.ui.load.testing.util;

import com.haulmont.ui.load.testing.UILoadTestingExtension;
import org.gradle.api.Project;

import java.util.Map;

@SuppressWarnings("Constant conditions & exceptions")
public class JMeterPropertiesBuilder {

    private static final String PROTOCOL = "protocol";
    private static final String WS_PROTOCOL = "wsProtocol";
    private static final String HOST = "host";
    private static final String PORT = "port";
    private static final String LOOPS_COUNT = "loopsCount";
    private static final String IS_LOOP_CONTINUE_FOREVER = "isLoopContinueForever";
    private static final String IS_LOOP_FIRST = "isLoopFirst";
    private static final String JMETER_HOME = "jmeterHome";
    private static final String PATH_DELIMITER = "pathDelimiter";
    private static final String TEST_PLAN_FILE_NAME = "testPlanFileName";
    private static final String TEST_PLAN_PATH = "testPlanPath";
    private static final String HAR_PATH = "harPath";
    private static final String HAR_FILE_NAME = "harFileName";
    private static final String THREAD_GROUP_NAME = "threadGroupName";
    private static final String NUM_THREADS = "numThreads";
    private static final String RAMP_UP = "rampUp";
    private static final String IS_SAME_USER_ON_NEXT_ITERATION = "isSameUserOnNextIteration";
    private static final String TEST_PLAN_NAME = "testPlanName";
    private static final String COOKIE_MANAGER_NAME = "cookieManagerName";
    private static final String IS_CLEAR_CACHE_AFTER_EACH_ITERATION = "isClearEachIteration";
    private static final String IS_CONTROLLED_BY_THREAD = "isControlledByThread";

    private Project project;
    private UILoadTestingExtension extension;
    private Map<String, ?> properties;

    public JMeterPropertiesBuilder(Project project, UILoadTestingExtension extension) {
        this.project = project;
        this.extension = extension;
        this.properties = project.getProperties();
    }

    public String getProtocol() {
        String protocol = (String) properties.get(PROTOCOL);
        if (protocol != null) {
            extension.setProtocol(protocol);
        }
        return extension.getProtocol();
    }

    public String getWsProtocol() {
        String wsProtocol = (String) properties.get(WS_PROTOCOL);
        if (wsProtocol != null) {
            extension.setWsProtocol(wsProtocol);
        }
        return extension.getWsProtocol();
    }

    public String getHost() {
        String host = (String) properties.get(HOST);
        if (host != null) {
            extension.setHost(host);
        }
        return extension.getHost();
    }

    public String getPort() {
        String port = (String) properties.get(PORT);
        if (port != null) {
            extension.setPort(port);
        }
        return extension.getPort();
    }

    public Integer getLoopsCount() {
        String loopsCount = (String) properties.get(LOOPS_COUNT);
        if (loopsCount != null) {
            extension.setLoopsCount(Integer.parseInt(loopsCount));
        }
        return extension.getLoopsCount();
    }

    public Boolean getIsLoopContinueForever() {
        String isLoopContinueForever = (String) properties.get(IS_LOOP_CONTINUE_FOREVER);
        if (isLoopContinueForever != null) {
            extension.setIsLoopContinueForever("true".equals(isLoopContinueForever));
        }
        return extension.getIsLoopContinueForever();
    }

    public Boolean getIsLoopFirst() {
        String isLoopFirst = (String) properties.get(IS_LOOP_FIRST);
        if (isLoopFirst != null) {
            extension.setIsLoopFirst("true".equals(isLoopFirst));
        }
        return extension.getIsLoopFirst();
    }

    public String getJmeterHome() {
        String jmeterHome = (String) properties.get(JMETER_HOME);
        if (jmeterHome != null) {
            extension.setJmeterHome(jmeterHome);
        }
        return extension.getJmeterHome();
    }

    public String getPathDelimiter() {
        String pathDelimiter = (String) properties.get(PATH_DELIMITER);
        if (pathDelimiter != null) {
            extension.setPathDelimiter(pathDelimiter);
        }
        return extension.getPathDelimiter();
    }

    public String getTestPlanFileName() {
        String testPlanFileName = (String) properties.get(TEST_PLAN_FILE_NAME);
        if (testPlanFileName != null) {
            extension.setTestPlanFileName(testPlanFileName);
        }
        return extension.getTestPlanFileName();
    }

    public String getTestPlanPath() {
        String testPlanPath = (String) properties.get(TEST_PLAN_PATH);
        if (testPlanPath != null) {
            extension.setTestPlanPath(testPlanPath);
        }
        return extension.getTestPlanPath();
    }

    public String getHarPath() {
        String harFilePath = (String) properties.get(HAR_PATH);
        if (harFilePath != null) {
            extension.setHarPath(harFilePath);
        }
        return extension.getHarPath();
    }

    public String getHarFileName() {
        String harFileName = (String) properties.get(HAR_FILE_NAME);
        if (harFileName != null) {
            extension.setHarFileName(harFileName);
        }
        return extension.getHarFileName();
    }

    public String getThreadGroupName() {
        String threadGroupName = (String) properties.get(THREAD_GROUP_NAME);
        if (threadGroupName != null) {
            extension.setThreadGroupName(threadGroupName);
        }
        return extension.getThreadGroupName();
    }

    public Integer getNumThreads() {
        String numThreads = (String) properties.get(NUM_THREADS);
        if (numThreads != null) {
            extension.setNumThreads(Integer.parseInt(numThreads));
        }
        return extension.getNumThreads();
    }

    public Integer getRampUp() {
        String rampUp = (String) properties.get(RAMP_UP);
        if (rampUp != null) {
            extension.setRampUp(Integer.parseInt(rampUp));
        }
        return extension.getRampUp();
    }

    public Boolean getIsSameUserOnNextIteration() {
        String isSameUserOnNextIteration = (String) properties.get(IS_SAME_USER_ON_NEXT_ITERATION);
        if (isSameUserOnNextIteration != null) {
            extension.setSameUserOnNextIteration("true".equals(isSameUserOnNextIteration));
        }
        return extension.getSameUserOnNextIteration();
    }

    public String getTestPlanName() {
        String testPlanName = (String) properties.get(TEST_PLAN_NAME);
        if (testPlanName != null) {
            extension.setTestPlanName(testPlanName);
        }
        return extension.getTestPlanName();
    }

    public String getCookieManagerName() {
        String cookieManagerName = (String) properties.get(COOKIE_MANAGER_NAME);
        if (cookieManagerName != null) {
            extension.setCookieManagerName(cookieManagerName);
        }
        return extension.getCookieManagerName();
    }

    public Boolean getIsClearEachIteration() {
        String isClearEachIteration = (String) properties.get(IS_CLEAR_CACHE_AFTER_EACH_ITERATION);
        if (isClearEachIteration != null) {
            extension.setClearEachIteration("true".equals(isClearEachIteration));
        }
        return extension.getClearEachIteration();
    }

    public Boolean getIsControlledByThread() {
        String isControlledByThread = (String) properties.get(IS_CONTROLLED_BY_THREAD);
        if (isControlledByThread != null) {
            extension.setControlledByThread("true".equals(isControlledByThread));
        }
        return extension.getControlledByThread();
    }
}
