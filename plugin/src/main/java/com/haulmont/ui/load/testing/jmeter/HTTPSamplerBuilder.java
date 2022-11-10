package com.haulmont.ui.load.testing.jmeter;

import com.browserup.harreader.model.HarRequest;
import com.haulmont.ui.load.testing.util.JMeterPropertiesBuilder;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.protocol.http.gui.HeaderPanel;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.collections.HashTree;

public class HTTPSamplerBuilder {

    JMeterPropertiesBuilder jMeterPropertiesBuilder;

    public HTTPSamplerBuilder(JMeterPropertiesBuilder jMeterPropertiesBuilder) {
        this.jMeterPropertiesBuilder = jMeterPropertiesBuilder;
    }

    public HTTPSampler getHTTPSampler(HarRequest harRequest, String caseName, int index) {
        HTTPSampler httpSampler = new HTTPSampler();
        httpSampler.setAutoRedirects(false);
        httpSampler.setFollowRedirects(true);
        httpSampler.setUseKeepAlive(true);
        httpSampler.setProtocol(getStandardProtocol());
        httpSampler.setDomain(getHost());
        httpSampler.setPort(Integer.parseInt(getPort()));

        setSamplerName(httpSampler, harRequest, caseName, index);
        setSamplerMethod(httpSampler, harRequest);
        setSamplerPath(httpSampler, harRequest);
        setSamplerComment(httpSampler, harRequest);
        addRequestParameters(httpSampler, harRequest);

        return httpSampler;
    }

    public HashTree getHTTPSamplerObject(HTTPSampler httpSampler, HarRequest harRequest) {
        HashTree httpSamplerHashTree = new HashTree();
        if (!harRequest.getHeaders().isEmpty()) {
            httpSamplerHashTree.add(httpSampler, addHeaders(harRequest));
        }
        return httpSamplerHashTree;
    }

    private String getStandardProtocol() {
        String standardProtocol = jMeterPropertiesBuilder.getProtocol();
        if (standardProtocol == null) {
            throw new IllegalStateException("Property 'protocol' cannot be null");
        }
        return standardProtocol;
    }

    private String getWsProtocol() {
        String wsProtocol = jMeterPropertiesBuilder.getWsProtocol();
        if (wsProtocol == null) {
            throw new IllegalStateException("Property 'ws Protocol' is null");
        }
        return wsProtocol;
    }

    private String getHost() {
        String host = jMeterPropertiesBuilder.getHost();
        if (host == null) {
            throw new IllegalStateException("Property 'host' is null");
        }
        return host;
    }

    private String getPort() {
        String port = jMeterPropertiesBuilder.getPort();
        if (port == null) {
            throw new IllegalStateException("Property 'port' is null");
        }
        return port;
    }


    private void setSamplerMethod(HTTPSampler httpSampler, HarRequest harRequest) {
        if (harRequest.getMethod() != null) {
            httpSampler.setMethod(harRequest.getMethod().name());
        }
    }

    private void setSamplerPath(HTTPSampler httpSampler, HarRequest harRequest) {
        if (harRequest.getUrl() != null) {
            String fullUrlInfo = getFullUrlInfo(harRequest);
            httpSampler.setPath(harRequest.getUrl().substring(fullUrlInfo.length() + 1));
        }
    }

    private void setSamplerName(HTTPSampler httpSampler, HarRequest harRequest, String caseName, int index) {
        if (harRequest.getUrl() != null && harRequest.getMethod() != null) {
            String fullURLInfo = getFullUrlInfo(harRequest);
            String samplerFullName = "(" + harRequest.getMethod().name() + ") " + caseName + " - "
                    + harRequest.getUrl().substring(fullURLInfo.length() + 1);
            if (samplerFullName.length() > 55) {
                httpSampler.setName(samplerFullName.substring(0, 55)  + "...-" + index) ;
            } else {
                httpSampler.setName(samplerFullName  + "-" + index) ;
            }
        }
    }

    private String getFullUrlInfo(HarRequest harRequest) {
        String fullUrlInfo = "";
        if (harRequest.getUrl().startsWith("http")) {
            fullUrlInfo = getStandardProtocol() + "/:" + getHost() + ":" + getPort();
        }
        if (harRequest.getUrl().startsWith("ws")) {
            fullUrlInfo = getWsProtocol() + "/:" + getHost() + ":" + getPort();
        }
        return fullUrlInfo;
    }

    private void setSamplerComment(HTTPSampler httpSampler, HarRequest harRequest) {
        if (harRequest.getComment() != null) {
            httpSampler.setComment(harRequest.getComment());
        }
    }

    private HeaderManager addHeaders(HarRequest harRequest) {
        HeadersBuilder headersBuilder = new HeadersBuilder();
        final HeaderManager headerManager = headersBuilder.getHeaderManager(harRequest);

        if (!harRequest.getHeaders().isEmpty()) {
            headerManager.setProperty(TestElement.TEST_CLASS, HeaderManager.class.getName());
            headerManager.setProperty(TestElement.GUI_CLASS, HeaderPanel.class.getName());
            headerManager.setEnabled(true);
            headerManager.setName("Request Headers");
        }
        return headerManager;
    }

    private void addRequestParameters(HTTPSampler httpSampler, HarRequest harRequest) {
        ArgumentsBuilder jmeterSamplerArgumentsBuilder = new ArgumentsBuilder();
        Arguments jmeterRequestArguments = jmeterSamplerArgumentsBuilder.getHttpSamplerArguments(httpSampler, harRequest);

        jmeterRequestArguments.setProperty(TestElement.TEST_CLASS, Arguments.class.getName());
        jmeterRequestArguments.setProperty(TestElement.GUI_CLASS, ArgumentsPanel.class.getName());
        jmeterRequestArguments.setEnabled(true);

        httpSampler.setArguments(jmeterRequestArguments);
    }
}
