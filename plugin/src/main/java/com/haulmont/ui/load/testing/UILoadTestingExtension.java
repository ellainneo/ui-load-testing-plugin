package com.haulmont.ui.load.testing;

@SuppressWarnings("ExtensionProperties")
public class UILoadTestingExtension {

    // Sampler Properties
    private String protocol = "http";
    private String wsProtocol = "ws";
    private String host = "127.0.0.1";
    private String port = "8080";

    // Loop Properties
    private Integer loopsCount = 1;
    private boolean isLoopContinueForever = false;
    private boolean isLoopFirst = true;

    // General Properties
    private String jmeterHome = "";
    private String pathDelimiter = System.getProperty("file.separator");
    private String testPlanFileName = "Project_Jmeter_Test_Plan.jmx";

    // Test Plan Properties
    private String threadGroupName = "Main Thread Group";
    private Integer numThreads = 2;
    private Integer rampUp = 0;
    private Boolean isSameUserOnNextIteration = false;
    private String testPlanName = "Project Test Plan";

    // Cookie Manager Properties
    private String cookieManagerName = "Cookie Manager";
    private Boolean isClearEachIteration = true;
    private Boolean isControlledByThread = false;

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setWsProtocol(String wsProtocol) {
        this.wsProtocol = wsProtocol;
    }

    public String getWsProtocol() {
        return wsProtocol;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPort() {
        return port;
    }

    public void setNumThreads(int numThreads) {
        this.numThreads = numThreads;
    }

    public void setLoopsCount(Integer loopsCount) {
        this.loopsCount = loopsCount;
    }

    public Integer getLoopsCount() {
        return loopsCount;
    }

    public void setIsLoopContinueForever(Boolean isLoopContinueForever) {
        this.isLoopContinueForever = isLoopContinueForever;
    }

    public Boolean getIsLoopContinueForever() {
        return isLoopContinueForever;
    }

    public void setIsLoopFirst(Boolean isLoopFirst) {
        this.isLoopFirst = isLoopFirst;
    }

    public Boolean getIsLoopFirst() {
        return isLoopFirst;
    }

    public void setJmeterHome(String jmeterHome) {
        this.jmeterHome = jmeterHome;
    }

    public String getJmeterHome() {
        return jmeterHome;
    }

    public void setPathDelimiter(String pathDelimiter) {
        this.pathDelimiter = pathDelimiter;

    }

    public String getPathDelimiter() {
        return pathDelimiter;
    }

    public void setTestPlanFileName(String testPlanFileName) {
        this.testPlanFileName = testPlanFileName;
    }

    public String getTestPlanFileName() {
        return testPlanFileName;
    }

    public void setThreadGroupName(String threadGroupName) {
        this.threadGroupName = threadGroupName;
    }

    public String getThreadGroupName() {
        return threadGroupName;
    }

    public void setNumThreads(Integer numThreads) {
        this.numThreads = numThreads;
    }

    public Integer getNumThreads() {
        return numThreads;
    }

    public void setRampUp(Integer rampUp) {
        this.rampUp = rampUp;
    }

    public Integer getRampUp() {
        return rampUp;
    }

    public void setSameUserOnNextIteration(Boolean sameUserOnNextIteration) {
        isSameUserOnNextIteration = sameUserOnNextIteration;
    }

    public Boolean getSameUserOnNextIteration() {
        return isSameUserOnNextIteration;
    }

    public void setTestPlanName(String testPlanName) {
        this.testPlanName = testPlanName;
    }

    public String getTestPlanName() {
        return testPlanName;
    }

    public void setCookieManagerName(String cookieManagerName) {
        this.cookieManagerName = cookieManagerName;
    }

    public String getCookieManagerName() {
        return cookieManagerName;
    }

    public void setClearEachIteration(Boolean clearEachIteration) {
        isClearEachIteration = clearEachIteration;
    }

    public Boolean getClearEachIteration() {
        return isClearEachIteration;
    }

    public void setControlledByThread(Boolean controlledByThread) {
        isControlledByThread = controlledByThread;
    }

    public Boolean getControlledByThread() {
        return isControlledByThread;
    }
}

