package com.haulmont.ui.load.testing.uitests.listeners;

import com.browserup.bup.BrowserUpProxy;
import com.browserup.bup.BrowserUpProxyServer;
import com.browserup.bup.client.ClientUtil;
import com.browserup.bup.proxy.CaptureType;
import com.browserup.harreader.model.Har;
import com.browserup.harreader.model.HarPage;
import com.codeborne.selenide.Selenide;
import com.haulmont.ui.load.testing.util.JMeterPropertiesBuilder;
import org.openqa.selenium.Proxy;
import org.testng.IExecutionListener;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class CustomProxyTestListener implements IExecutionListener, ITestListener {

    JMeterPropertiesBuilder propertiesBuilder;
    private Har har;
    private HarPage harPage;
    private List<HarPage> harPageList = new ArrayList<>();
    private BrowserUpProxy browserUpProxy = new BrowserUpProxyServer();
    // ToDo: set a correct full path to HAR file instead of current example
    private File harFile = new File ("C:\\PluginTest\\HarFile\\" + propertiesBuilder.getHarFileName());

    public CustomProxyTestListener(JMeterPropertiesBuilder propertiesBuilder) {
        this.propertiesBuilder = propertiesBuilder;
    }

    @Override
    public void onExecutionStart() {
        System.out.println("Method onExecutionStart: ");
        browserUpProxy.start();

        Proxy seleniumProxy = new Proxy();
        try {
            seleniumProxy = ClientUtil.createSeleniumProxy(browserUpProxy);
            //seleniumProxy.setSslProxy(getCurrentHostAddress() + ":" + browserUpProxy.getPort());
            seleniumProxy.setHttpProxy(getCurrentHostAddress() + ":" + browserUpProxy.getPort());
        } catch (IllegalStateException ex) {
            throw new IllegalStateException("Unable to start a Proxy on Host: " + getCurrentHostAddress()
                    + "and Port: " + browserUpProxy.getPort());
        }
        har = browserUpProxy.newHar("Load Tests");
        browserUpProxy.setHarCaptureTypes(CaptureType.getAllContentCaptureTypes());
        browserUpProxy.enableHarCaptureTypes(CaptureType.getAllContentCaptureTypes());
    }

    @Override
    public void onTestStart(ITestResult testResult) {
        System.out.println("On Test Start");
        String currentTestMethodName = testResult.getMethod().getMethodName();
        String currentTestName = "";
        try {
            currentTestName = testResult.getTestClass().getRealClass()
                    .getMethod(currentTestMethodName).getAnnotation(Test.class).testName();
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }
        if (!currentTestName.isEmpty()) {
            har = browserUpProxy.newPage(currentTestName);
        } else {
            har = browserUpProxy.newPage(propertiesBuilder.getProtocol() + "://" + propertiesBuilder.getHost()
                    + ":" + propertiesBuilder.getPort());
        }
    }

    @Override
    public void onExecutionFinish() {
       System.out.println("Method onExecutionFinish: ");
       try {
            browserUpProxy.getHar().writeTo(harFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //proxy.endHar();
        //proxy.stop();
        Selenide.closeWebDriver();
    }

    private File getHarFile() {
        return new File(propertiesBuilder.getHarPath() + propertiesBuilder.getPathDelimiter()
                + propertiesBuilder.getHarFileName());
    }

    public String getCurrentHostAddress() {
        try {
            return Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            throw new IllegalStateException("Unable to get a current Host Address");
        }
    }

    public String getFullAddress() {
        return propertiesBuilder.getProtocol() + "://" + propertiesBuilder.getHost()
                + ":" + propertiesBuilder.getPort();
    }
}
