package com.haulmont.ui.load.testing.jmeter;

import com.browserup.harreader.model.HarPostData;
import com.browserup.harreader.model.HarPostDataParam;
import com.browserup.harreader.model.HarQueryParam;
import com.browserup.harreader.model.HarRequest;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.protocol.http.util.HTTPArgument;

import java.util.List;

public class ArgumentsBuilder {

    private static final String CONTENT_TYPE_PARAM = "Content-Type";
    private static final String PROTOCOL_MATCH_STRING = "http";
    private static final String CONTENT_ENCODING = "UTF-8";

    public Arguments getHttpSamplerArguments(HTTPSampler jmeterHttpSampler, HarRequest harRequest) {
        Arguments requestArguments = new Arguments();
        HTTPArgument httpArgument = new HTTPArgument();

        HarPostData harPostData = harRequest.getPostData();
        if (harPostData != null) {
            if (!harPostData.getText().isEmpty()) {
                jmeterHttpSampler.setPostBodyRaw(true);
                jmeterHttpSampler.setContentEncoding(CONTENT_ENCODING);
                httpArgument.setUseEquals(true);
                httpArgument.setContentType(harPostData.getMimeType());
                httpArgument.setValue(harPostData.getText());
                requestArguments.addArgument(httpArgument);
            } else {
                if ((harPostData.getParams() != null) && (!harPostData.getParams().isEmpty())) {
                    List<HarPostDataParam> harPostDataParameters = harPostData.getParams();
                    harPostDataParameters.forEach(harPostDataParameter -> {
                        requestArguments.addArgument(createPostDataParameter(harPostDataParameter));
                    });
                }
            }
        } else {
            List<HarQueryParam> harQueryParameters = harRequest.getQueryString();
            if (!harQueryParameters.isEmpty()) {
                harQueryParameters.forEach(harQueryParameter -> {
                    requestArguments.addArgument(createRequestParameter(harQueryParameter));
                });
            }
        }
        return requestArguments;
    }

    private HTTPArgument createRequestParameter(HarQueryParam harQueryParameter) {
        HTTPArgument httpRequestArgument = new HTTPArgument();
        if (harQueryParameter != null && harQueryParameter.getName() != null) {
            httpRequestArgument.setName(harQueryParameter.getName());
            if (harQueryParameter.getValue() != null && !harQueryParameter.getValue().isEmpty()) {
                httpRequestArgument.setValue(harQueryParameter.getValue());
                httpRequestArgument.setUseEquals(true);
            } else {
                httpRequestArgument.setValue("");
                httpRequestArgument.setUseEquals(false);
            }
            setAlwaysEncodeByParamData(harQueryParameter, httpRequestArgument);
        }
        return httpRequestArgument;
    }

    private HTTPArgument createPostDataParameter(HarPostDataParam harPostDataParameter) {
        HTTPArgument httpSamplerParameter = new HTTPArgument();
        if (harPostDataParameter != null && harPostDataParameter.getName() != null) {
            httpSamplerParameter.setName(harPostDataParameter.getName());
            if (harPostDataParameter.getValue() != null && !harPostDataParameter.getValue().isEmpty()) {
                httpSamplerParameter.setValue(harPostDataParameter.getValue());
                httpSamplerParameter.setUseEquals(true);
                httpSamplerParameter.setContentType(harPostDataParameter.getContentType());
            } else {
                httpSamplerParameter.setValue("");
                httpSamplerParameter.setUseEquals(true);
            }
            setAlwaysEncodedByPostDataParamData(harPostDataParameter, httpSamplerParameter);
        }
        return httpSamplerParameter;
    }

    private void setAlwaysEncodeByParamData(HarQueryParam harQueryParameter,
                                            HTTPArgument jmeterRequestArgument) {
        if (harQueryParameter != null) {
            if ((harQueryParameter.getName() != null &&
            harQueryParameter.getName().equalsIgnoreCase(CONTENT_TYPE_PARAM)) ||
            (harQueryParameter.getValue() != null &&
                    harQueryParameter.getValue().startsWith(PROTOCOL_MATCH_STRING))) {
                jmeterRequestArgument.setAlwaysEncoded(true);
            }
        }
    }

    private void setAlwaysEncodedByPostDataParamData(HarPostDataParam harPostDataParameter,
                                                     HTTPArgument httpSamplerParameter) {
        if (harPostDataParameter != null) {
            if ((harPostDataParameter.getName() != null &&
                    harPostDataParameter.getName().equalsIgnoreCase(CONTENT_TYPE_PARAM)) ||
                    (harPostDataParameter.getValue() != null &&
                            harPostDataParameter.getValue().startsWith(PROTOCOL_MATCH_STRING))) {
                httpSamplerParameter.setAlwaysEncoded(true);
            }
        }
    }
}
