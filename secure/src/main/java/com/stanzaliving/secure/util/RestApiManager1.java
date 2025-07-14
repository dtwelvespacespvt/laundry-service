package com.stanzaliving.secure.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stanzaliving.secure.error.CustomRuntimeException;
import com.stanzaliving.secure.error.UnauthorisedException;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Service
public abstract class RestApiManager1 {
    private final String REST_RESP_TOKEN = "RestAPI response received with token: {} and body: {}";
    private  RestTemplate restTemplate = getRestTemplate();
    private  static ObjectMapper objectMapper = new ObjectMapper();

    @Getter
    protected String baseUrl;
    @Getter
    protected String serviceName;

    private RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
        return restTemplate;
    }


    protected <T> T get(String url, Map<String, Object> queryParams, Map<String, String> overrideHeaders, boolean skipDefaultHeaders, Type responseClassType) throws CustomRuntimeException, UnauthorisedException, HttpClientErrorException, HttpServerErrorException {
        HttpMethod method = HttpMethod.GET;
        String fullUrl = getFinalUrl(getBaseUrl(), url, queryParams);
        HttpHeaders headers = generateHeaders(overrideHeaders, skipDefaultHeaders);
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        try {
            logBeforeCall(fullUrl, method, headers, null);
            ResponseEntity<T> responseEntity = restTemplate.exchange(fullUrl, method, httpEntity, new ParameterizedTypeReference<T>() {
                @Override
                public Type getType() {
                    return responseClassType;
                }
            });

            if (responseEntity.getStatusCode() == null || !responseEntity.getStatusCode().is2xxSuccessful()) {
                logNotOkResponse(fullUrl, method, headers, null, responseEntity);
                return null;
            }

            logOkResponse(fullUrl, method, headers, null, responseEntity);
            return responseEntity.getBody();
        } catch (ResourceAccessException ex) {
            return handleResourceAccessException(fullUrl, method, headers, null, ex);
        } catch (HttpStatusCodeException ex) {
            return handleHttpStatusCodeException(fullUrl, method, headers, null, ex);
        } catch (RestClientException ex) {
            return handleRestClientException(fullUrl, method, headers, null, ex);
        }
    }

    public <T> T post(String url, Map<String, Object> queryParams, Object body, Map<String, String> overrideHeaders, boolean skipDefaultHeaders, Type responseClassType) {
        HttpMethod method = HttpMethod.POST;
        ResponseEntity<T> responseEntity = null;
        String fullUrl = getFinalUrl(getBaseUrl(), url, queryParams);

        HttpHeaders headers = generateHeaders(overrideHeaders, skipDefaultHeaders);
        HttpEntity<?> httpEntity = new HttpEntity<>(body, headers);

        ParameterizedTypeReference<T> parameterizedTypeReference = new ParameterizedTypeReference<T>() {
            @Override
            public Type getType() {
                return responseClassType;
            }
        };
        return _execute(fullUrl, method, headers, body, parameterizedTypeReference, httpEntity, responseEntity);
    }

    public <T> T put(String url, Map<String, Object> queryParams, Object body, Map<String, String> overrideHeaders, boolean skipDefaultHeaders, Type responseClassType) {
        HttpMethod method = HttpMethod.PUT;
        ResponseEntity<T> responseEntity = null;
        String fullUrl = getFinalUrl(getBaseUrl(), url, queryParams);

        HttpHeaders headers = generateHeaders(overrideHeaders, skipDefaultHeaders);
        HttpEntity<?> httpEntity = new HttpEntity<>(body, headers);

        ParameterizedTypeReference<T> parameterizedTypeReference = new ParameterizedTypeReference<T>() {
            @Override
            public Type getType() {
                return responseClassType;
            }
        };
        return _execute(fullUrl, method, headers, body, parameterizedTypeReference, httpEntity, responseEntity);
    }

    protected <T> T _execute(String fullUrl, HttpMethod method, HttpHeaders headers, Object body, ParameterizedTypeReference<T> parameterizedTypeReference, HttpEntity<?> httpEntity, ResponseEntity<T> responseEntity) {
        try {
            logBeforeCall(fullUrl, method, headers, body);
            responseEntity = this.restTemplate.exchange(fullUrl, method, httpEntity, parameterizedTypeReference);

            if (responseEntity.getStatusCode() == null || !responseEntity.getStatusCode().is2xxSuccessful()) {
                logNotOkResponse(fullUrl, method, headers, body, responseEntity);
                return null;
            }
            logOkResponse(fullUrl, method, headers, body, responseEntity);
            return responseEntity.getBody();
        } catch (ResourceAccessException ex) {
            return handleResourceAccessException(fullUrl, method, headers, body, ex);
        } catch (HttpStatusCodeException ex) {
            return handleHttpStatusCodeException(fullUrl, method, headers, body, ex);
        } catch (RestClientException ex) {
            return handleRestClientException(fullUrl, method, headers, body, ex);
        }
    }

    protected <T> T handleHttpStatusCodeException(String fullUrl, HttpMethod method, HttpHeaders headers, Object body, HttpStatusCodeException ex) throws CustomRuntimeException, UnauthorisedException {
        log.error(toJson(
                new HashMap<String, Object>() {{
                    put("message", "API Call Failed");
                    put("url", fullUrl);
                    put("method", method);
                    put("headers", headers);
                    put("body", body);
                    put("status", ex.getStatusCode());
                    put("response", ex.getResponseBodyAsString());
                }}
        ));

        if (ex.getStatusCode() == HttpStatus.GATEWAY_TIMEOUT) {
            log.warn("Error in conversion to responseClassType. Formatting into parsed json instead. Error not found");
            log.info("Error with message :" + ex.getMessage());
            throw new CustomRuntimeException(String.format("System error [GATEWAY_TIMEOUT]. Please contact tr tech with error as Service: %s something went wrong please check with this code : ", getServiceName()), ex.getStatusCode());
        }

        if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            log.info("Unauthorized Exception with message :" + ex.getMessage());
            throw new UnauthorisedException(String.format("Service: %s System error : ", getServiceName()));
        }

        else {
            log.warn("ErrorResponse model change required");
            log.info("Error with message :" + ex.getMessage());
            throw new CustomRuntimeException("Service: " + getServiceName() + " " + "System error" + ex.getMessage() + ex.getResponseBodyAsString(), ex.getStatusCode());
        }
    }

    protected <T> T handleRestClientException(String fullUrl, HttpMethod method, HttpHeaders headers, Object body, RestClientException ex) throws CustomRuntimeException {
        log.error(toJson(
                new HashMap<String, Object>() {{
                    put("message", "API Call Failed");
                    put("url", fullUrl);
                    put("method", method);
                    put("headers", headers);
                    put("body", body);
                }}
        ));
        String errorCode = "";
        log.warn("RestClientException in RestApiManager with errorCode: {}", errorCode);
        log.info("Server error: Message {}", ex.getMessage());
        throw new CustomRuntimeException(String.format("Service: %s System error %s ", getServiceName(), ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    protected <T> T handleResourceAccessException(String fullUrl, HttpMethod method, HttpHeaders headers, Object body, ResourceAccessException ex) throws HttpClientErrorException, HttpServerErrorException {
        if (ex.getCause() instanceof SocketTimeoutException || ex.getCause() instanceof ConnectException) {
            log.error(" Request Timed Out - " + ex.toString());
            throw new HttpClientErrorException(HttpStatus.REQUEST_TIMEOUT, String.format("%s I/O error on get request %s %s", getServiceName(), ex.toString(), HttpStatus.REQUEST_TIMEOUT));
        } else {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, String.format("%s I/O error on get request %s %s", getServiceName(), ex.toString(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    protected <T> void logNotOkResponse(String fullUrl, HttpMethod method, HttpHeaders headers, Object body, ResponseEntity<T> responseEntity) {
        log.error("response received is not valid http code: {} and entity is : {}", responseEntity.getStatusCode(), responseEntity.toString());
        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    protected <T> void logOkResponse(String fullUrl, HttpMethod method, HttpHeaders headers, Object body, ResponseEntity<T> responseEntity) {
        log.info("RestAPI response received with body: {}", responseEntity);
    }

    protected void logBeforeCall(String fullUrl, HttpMethod method, HttpHeaders headers, Object body) {
        log.info(toJson(new HashMap<String, Object>() {{
            put("message", String.format("%s called", getServiceName()));
            put("category", "logBeforeCall");
            put("fullUrl", fullUrl);
            put("method", method);
            put("headers", headers);
            put("body", body);
        }}));
    }

    protected HttpHeaders generateHeaders(Map<String, String> overrideHeaders, boolean skipDefaultHeaders) {
        Map<String, String> finalHeaders = new HashMap<>();
        if (!skipDefaultHeaders) {
            finalHeaders.putAll(getDefaultRequestHeaders());
        }
        if (overrideHeaders != null) {
            finalHeaders.putAll(overrideHeaders);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        finalHeaders.forEach(httpHeaders::add);
        return httpHeaders;
    }

    protected String toJson(Object obj) {
        try {
            if (obj != null) {
                return objectMapper.writeValueAsString(obj);
            }
        } catch (JsonProcessingException e) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }

    private String getFinalUrl(String baseUrl, String endpoint, Map<String, Object> queryParams) {
        StringBuilder finalUrl = new StringBuilder();
        finalUrl.append(baseUrl != null ? baseUrl : "");
        finalUrl.append(endpoint != null ? endpoint : "");
        if (queryParams != null) {
            finalUrl.append("?");
            queryParams.forEach((key, value) -> finalUrl.append(key).append("=").append(value == null ? "" : value).append("&"));

            finalUrl.deleteCharAt(finalUrl.length() - 1);
        }
        return finalUrl.toString();
    }

    protected Map<String, String> getDefaultRequestHeaders() {
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put(HttpHeaders.CONTENT_TYPE, "application/json");
        return requestHeaders;
    }

    public <T> T delete(String url, Map<String, Object> queryParams, Object body, Map<String, String> overrideHeaders, boolean skipDefaultHeaders, Type responseClassType) {
        HttpMethod method = HttpMethod.DELETE;
        ResponseEntity<T> responseEntity = null;
        String fullUrl = getFinalUrl(getBaseUrl(), url, queryParams);

        HttpHeaders headers = generateHeaders(overrideHeaders, skipDefaultHeaders);
        HttpEntity<?> httpEntity = new HttpEntity<>(body, headers);

        ParameterizedTypeReference<T> parameterizedTypeReference = new ParameterizedTypeReference<T>() {
            @Override
            public Type getType() {
                return responseClassType;
            }
        };
        return _execute(fullUrl, method, headers, body, parameterizedTypeReference, httpEntity, responseEntity);
    }

}
