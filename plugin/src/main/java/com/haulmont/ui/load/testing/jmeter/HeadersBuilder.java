package com.haulmont.ui.load.testing.jmeter;

import com.browserup.harreader.model.HarHeader;
import com.browserup.harreader.model.HarRequest;
import org.apache.jmeter.protocol.http.control.Header;
import org.apache.jmeter.protocol.http.control.HeaderManager;

import java.util.List;

public class HeadersBuilder {

    private static final String COOKIE_HEADER_STRING = "Cookie";

    public HeaderManager getHeaderManager(HarRequest harRequest) {
        HeaderManager headerManager = new HeaderManager();
        List<HarHeader> harHeaders = harRequest.getHeaders();

        if (!harHeaders.isEmpty()) {
            harHeaders.forEach(harHeader -> {
                if (!harHeader.getName().equalsIgnoreCase(COOKIE_HEADER_STRING)) {
                    headerManager.add(buildHeader(harHeader));
                }
            });
        }
        return headerManager;
    }

    public Header buildHeader(HarHeader harHeader) {
        Header requestHeader = new Header();

        setHeaderName(requestHeader, harHeader);
        setHeaderValue(requestHeader, harHeader);
        setHeaderComment(requestHeader, harHeader);

        return requestHeader;
    }

    private void setHeaderName(Header jmeterHeader, HarHeader harHeader) {
        String headerName = harHeader.getName();

        if (headerName != null && !headerName.isEmpty()) {
            jmeterHeader.setName(harHeader.getName());
        }
    }

    private void setHeaderValue(Header requestHeader, HarHeader harHeader) {
        String headerValue = harHeader.getValue();

        if (headerValue != null && !headerValue.isEmpty()) {
            requestHeader.setValue(harHeader.getValue());
        }
    }

    private void setHeaderComment(Header requestHeader, HarHeader harHeader) {
        String headerComment = harHeader.getComment();

        if (headerComment != null && !headerComment.isEmpty()) {
            requestHeader.setComment(harHeader.getComment());
        }
    }
}
