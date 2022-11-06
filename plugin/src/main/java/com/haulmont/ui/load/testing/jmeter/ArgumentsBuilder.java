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

    public Arguments getAllRequestParameters(HarRequest harRequest) {
        Arguments arguments = new Arguments();
        if (harRequest != null) {
            List<HarQueryParam> harQueryParams = harRequest.getQueryString();
            if (!harQueryParams.isEmpty()) {
                harQueryParams.forEach(harQueryParam -> {
                    HTTPArgument jmeterRequestArgument = createRequestParameter(harQueryParam);
                    arguments.addArgument(jmeterRequestArgument);
                });
            }
        }
        return arguments;
    }

    public HTTPArgument getBodyData(HTTPSampler jmeterHttpSampler, HarPostData harPostData) {
        HTTPArgument httpRequestArgument = new HTTPArgument();
        if (harPostData != null) {
            jmeterHttpSampler.setPostBodyRaw(true);
            httpRequestArgument.setUseEquals(true);
            httpRequestArgument.setContentType(harPostData.getMimeType());

            httpRequestArgument.setValue(harPostData.getText());
        }
        return httpRequestArgument;
    }

    public Arguments getHttpSamplerArguments(HTTPSampler jmeterHttpSampler, HarRequest harRequest) {
        Arguments requestArguments = new Arguments();
        HTTPArgument httpArgument = new HTTPArgument();
        HarPostData harPostData = harRequest.getPostData();

        if (harPostData != null) {
            if (!harPostData.getText().isEmpty()) {
                jmeterHttpSampler.setPostBodyRaw(true);
                jmeterHttpSampler.setContentEncoding("UTF-8");
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
            harQueryParameter.getName().equalsIgnoreCase("Content-Type")) ||
            (harQueryParameter.getValue() != null &&
                    harQueryParameter.getValue().startsWith("http"))) {
                jmeterRequestArgument.setAlwaysEncoded(true);
            }
        }
    }

    private void setAlwaysEncodedByPostDataParamData(HarPostDataParam harPostDataParameter,
                                                     HTTPArgument httpSamplerParameter) {
        if (harPostDataParameter != null) {
            if ((harPostDataParameter.getName() != null &&
                    harPostDataParameter.getName().equalsIgnoreCase("Content-Type")) ||
                    (harPostDataParameter.getValue() != null &&
                            harPostDataParameter.getValue().startsWith("http"))) {
                httpSamplerParameter.setAlwaysEncoded(true);
            }
        }
    }
}
