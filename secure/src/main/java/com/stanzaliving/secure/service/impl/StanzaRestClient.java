/**
 *
 */
package com.stanzaliving.secure.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stanzaliving.secure.constants.StanzaConstants;
import com.stanzaliving.secure.error.StanzaHttpException;
import com.stanzaliving.secure.error.StanzaSecurityException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.MDC;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.RequestEntity.BodyBuilder;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.Map.Entry;

/**
 * @author naveen
 *
 * @date 17-Oct-2019
 */
@Log4j2
public class StanzaRestClient {

    private String basePath;

    private RestTemplate restTemplate;

    private ObjectMapper objectMapper;

    private HttpHeaders defaultHeaders = new HttpHeaders();

    private HttpMessageConverter messageConverter;

    public StanzaRestClient(String basePath) {
        this.basePath = basePath;
        this.restTemplate = buildRestTemplate();
        objectMapper = BaseMapperConfig.getDefaultMapper();
    }

    public StanzaRestClient(String basePath, int connectTimeOut, int readTimeOut) {
        this.basePath = basePath;
        this.restTemplate = buildRestTemplate(connectTimeOut, readTimeOut);
        objectMapper = BaseMapperConfig.getDefaultMapper();
    }

    public HttpMessageConverter getMessageConverter() {
        return messageConverter;
    }

    public void setMessageConverter(HttpMessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }

    public enum CollectionFormat {

        CSV(","),
        TSV("\t"),
        SSV(" "),
        PIPES("|"),
        MULTI(null);

        private final String separator;

        private CollectionFormat(String separator) {
            this.separator = separator;
        }

        private String collectionToString(Collection<? extends CharSequence> collection) {
            return StringUtils.collectionToDelimitedString(collection, separator);
        }
    }

    private RestTemplate buildRestTemplate() {
        RestTemplate template = new RestTemplate();

        configureRestTemplate(template);

        // This allows us to read the response more than once - Necessary for debugging.
        template.setRequestFactory(new BufferingClientHttpRequestFactory(template.getRequestFactory()));
        return template;
    }

    private RestTemplate buildRestTemplate(int connectTimeOut, int readTimeOut) {

        RestTemplate template = new RestTemplate(getClientHttpRequestFactory(connectTimeOut, readTimeOut));

        configureRestTemplate(template);

        // This allows us to read the response more than once - Necessary for debugging.
        template.setRequestFactory(new BufferingClientHttpRequestFactory(template.getRequestFactory()));
        return template;
    }

    private SimpleClientHttpRequestFactory getClientHttpRequestFactory(int connectTimeOut, int readTimeOut) {
        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        // Connect timeout
        clientHttpRequestFactory.setConnectTimeout(connectTimeOut);

        // Read timeout
        clientHttpRequestFactory.setReadTimeout(readTimeOut);
        return clientHttpRequestFactory;
    }

    @SuppressWarnings("rawtypes")
    public void configureRestTemplate(RestTemplate template) {
        for (HttpMessageConverter converter : template.getMessageConverters()) {

            if (converter instanceof AbstractJackson2HttpMessageConverter) {
                ObjectMapper mapper = ((AbstractJackson2HttpMessageConverter) converter).getObjectMapper();
                mapper = BaseMapperConfig.configureMapper(mapper);
            }
        }
    }

    public StanzaRestClient setUserAgent(String userAgent) {
        addDefaultHeader("User-Agent", userAgent);
        return this;
    }

    public StanzaRestClient addDefaultHeader(String name, String value) {

        if (defaultHeaders.containsKey(name)) {
            defaultHeaders.remove(name);
        }

        defaultHeaders.add(name, value);

        return this;
    }

    public List<MediaType> selectHeaderAccept(String[] accepts) {
        if (accepts.length == 0) {
            return new ArrayList<>();
        }

        for (String accept : accepts) {
            MediaType mediaType = MediaType.parseMediaType(accept);
            if (isJsonMime(mediaType)) {
                return Collections.singletonList(mediaType);
            }
        }
        return MediaType.parseMediaTypes(StringUtils.arrayToCommaDelimitedString(accepts));
    }

    private boolean isJsonMime(MediaType mediaType) {
        return mediaType != null && (MediaType.APPLICATION_JSON.isCompatibleWith(mediaType) || mediaType.getSubtype().matches("^.*\\+json[;]?\\s*$"));
    }

    public <T> T invokeAPI(
            String path,
            HttpMethod method,
            MultiValueMap<String, String> queryParams,
            Object body,
            HttpHeaders headerParams,
            List<MediaType> accept,
            ParameterizedTypeReference<T> returnType) {

        return invokeAPI(path, method, queryParams, body, headerParams, accept, returnType, MediaType.APPLICATION_JSON);
    }

    public <T> T invokeAPI(
            String path,
            HttpMethod method,
            MultiValueMap<String, String> queryParams,
            Object body,
            HttpHeaders headerParams,
            List<MediaType> accept,
            ParameterizedTypeReference<T> returnType,
            MediaType mediaType) {

        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(basePath).path(path);

        if (queryParams != null) {
            builder.queryParams(queryParams);
        }

        final BodyBuilder requestBuilder = RequestEntity.method(method, builder.build().toUri());

        if (accept != null) {
            requestBuilder.accept(accept.toArray(new MediaType[accept.size()]));
        }

        requestBuilder.contentType(mediaType);

        if(Objects.nonNull(headerParams)) {

            addHeadersToRequest(headerParams, requestBuilder);
        }

        log.info("Accessing API: {}", builder.toUriString());

        RequestEntity<Object> requestEntity = requestBuilder.body(body);

        return getResponse(requestEntity, returnType, builder);
    }

    private <T> T getResponse(RequestEntity<Object> requestEntity, ParameterizedTypeReference<T> returnType, final UriComponentsBuilder builder) {
        ResponseEntity<T> responseEntity = null;
        try {
            if (null != messageConverter) {
                List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
                messageConverters.add(messageConverter);
                restTemplate.setMessageConverters(messageConverters);
            }
            responseEntity = restTemplate.exchange(requestEntity, returnType);

        } catch (RestClientException e) {
            if (!StringUtils.isEmpty(e.getMessage()) && e.getMessage().contains("401")) {
                responseEntity = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            } else if (!StringUtils.isEmpty(e.getMessage()) && e.getMessage().contains("403")) {
                responseEntity = new ResponseEntity<>(HttpStatus.FORBIDDEN);
            } else {

                log.error("Exception caught while making rest call: ", e.getMessage());
                throw new StanzaHttpException(e.getMessage(), e);
            }
        }

        HttpStatus statusCode = responseEntity.getStatusCode();

        log.info("API: {}, Response: {}", builder.toUriString(), statusCode);

        if (responseEntity.getStatusCode() == HttpStatus.NO_CONTENT) {
            return null;
        } else if (responseEntity.getStatusCode().is2xxSuccessful()) {
            if (returnType == null) {
                return null;
            }
            return responseEntity.getBody();
        } else {
            // The error handler built into the RestTemplate should handle 400 and 500 series errors.
            throw new StanzaHttpException("API returned " + statusCode + " and it wasn't handled by the RestTemplate error handler", statusCode.value());
        }
    }

    public <T> T invokeAPI(
            String path,
            HttpMethod method,
            MultiValueMap<String, String> queryParams,
            Object body,
            HttpHeaders headerParams,
            List<MediaType> accept,
            Class<T> returnType) {

        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(basePath).path(path);

        if (queryParams != null) {
            builder.queryParams(queryParams);
        }

        final BodyBuilder requestBuilder = RequestEntity.method(method, builder.build().toUri());

        if (accept != null) {
            requestBuilder.accept(accept.toArray(new MediaType[accept.size()]));
        }

        requestBuilder.contentType(MediaType.APPLICATION_JSON);

        addHeadersToRequest(headerParams, requestBuilder);

        log.info("Accessing API: {}", builder.toUriString());

        RequestEntity<Object> requestEntity = requestBuilder.body(body);

        return getResponse(requestEntity, returnType, builder);
    }

    private <T> T getResponse(RequestEntity<Object> requestEntity, Class<T> returnType, final UriComponentsBuilder builder) {

        ResponseEntity<T> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(requestEntity, returnType);
        } catch (RestClientException e) {
            if (!StringUtils.isEmpty(e.getMessage()) && e.getMessage().contains("401")) {
                responseEntity = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            } else if (!StringUtils.isEmpty(e.getMessage()) && e.getMessage().contains("403")) {
                responseEntity = new ResponseEntity<>(HttpStatus.FORBIDDEN);
            } else {

                log.error("Exception caught while making rest call: ", e);
                throw new StanzaHttpException(e.getMessage(), e);
            }
        }

        HttpStatus statusCode = responseEntity.getStatusCode();

        log.info("API: {}, Response: {}", builder.toUriString(), statusCode);

        if (responseEntity.getStatusCode() == HttpStatus.NO_CONTENT) {
            return null;
        } else if (responseEntity.getStatusCode().is2xxSuccessful()) {
            if (returnType == null) {
                return null;
            }
            return responseEntity.getBody();

        } else if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            return null;

        } else {
            // The error handler built into the RestTemplate should handle 400 and 500 series errors.
            throw new StanzaHttpException("API returned " + statusCode + " and it wasn't handled by the RestTemplate error handler", statusCode.value());
        }
    }

    public <T> T invokeAPI(
            String path,
            HttpMethod method,
            MultiValueMap<String, String> queryParams,
            Object body,
            HttpHeaders headerParams,
            List<MediaType> accept,
            TypeReference<T> returnType) {

        return invokeAPI(path, method, queryParams, body, headerParams, accept, returnType, MediaType.APPLICATION_JSON);
    }

    public <T> T request(
            String path,
            HttpMethod method,
            MultiValueMap<String, String> queryParams,
            Object body,
            HttpHeaders headerParams,
            List<MediaType> accept,
            TypeReference<T> returnType,
            MediaType contentType) {

        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(basePath).path(path);

        if (MapUtils.isNotEmpty(queryParams)) {
            builder.queryParams(queryParams);
        }

        if (Objects.isNull(headerParams)) {
            headerParams = new HttpHeaders();
        }

        if (CollectionUtils.isEmpty(accept)) {
            accept = new ArrayList<>();
            accept.add(MediaType.ALL);
        }

        final BodyBuilder requestBuilder = RequestEntity.method(method, builder.build().toUri());


        if (CollectionUtils.isNotEmpty(accept)) {
            requestBuilder.accept(accept.toArray(new MediaType[0]));
        }

        requestBuilder.contentType(contentType);

        addHeadersToRequest(headerParams, requestBuilder);

        log.info("Accessing API: {}", builder.toUriString());

        RequestEntity<Object> requestEntity = requestBuilder.body(body);

        ResponseEntity<String> responseEntity = getResponse(requestEntity);

        if (returnType == null) {
            return null;
        }

        try {
            return objectMapper.readValue(responseEntity.getBody(), returnType);
        } catch (Exception e) {
            HttpStatus statusCode = responseEntity.getStatusCode();
            log.error("Error reading response: ", e);
            throw new StanzaHttpException("Error while reading response", statusCode.value(), e);
        }
    }

    public <T> T invokeAPI(
            String path,
            HttpMethod method,
            MultiValueMap<String, String> queryParams,
            Object body,
            HttpHeaders headerParams,
            List<MediaType> accept,
            TypeReference<T> returnType,
            MediaType mediaType) {

        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(basePath).path(path);

        if (queryParams != null) {
            builder.queryParams(queryParams);
        }

        final BodyBuilder requestBuilder = RequestEntity.method(method, builder.build().toUri());

        if (accept != null) {
            requestBuilder.accept(accept.toArray(new MediaType[accept.size()]));
        }

        requestBuilder.contentType(mediaType);

        addHeadersToRequest(headerParams, requestBuilder);

        log.info("Accessing API: {}", builder.toUriString());

        RequestEntity<Object> requestEntity = requestBuilder.body(body);

        ResponseEntity<String> responseEntity = getResponse(requestEntity);

        return processResponse(returnType, builder, responseEntity);
    }

    private ResponseEntity<String> getResponse(RequestEntity<Object> requestEntity) {

        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(requestEntity, String.class);

        } catch (RestClientException e) {

            if (!StringUtils.isEmpty(e.getMessage()) && e.getMessage().contains("401")) {
                responseEntity = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            } else if (!StringUtils.isEmpty(e.getMessage()) && e.getMessage().contains("403")) {
                responseEntity = new ResponseEntity<>(HttpStatus.FORBIDDEN);
            } else {

                log.error("Exception caught while making rest call: ", e);
                throw new StanzaHttpException(e.getMessage(), e);
            }
        }

        return responseEntity;
    }

    private <T> T processResponse(TypeReference<T> returnType, UriComponentsBuilder builder, ResponseEntity<String> responseEntity) {

        HttpStatus statusCode = responseEntity.getStatusCode();

        log.info("API: {}, Response: {}", builder.toUriString(), statusCode);

        if (isSuccessCode(statusCode)) {
            if (returnType == null) {
                return null;
            }

            try {
                return objectMapper.readValue(responseEntity.getBody(), returnType);
            } catch (Exception e) {
                log.error("Error reading response: ", e);
                throw new StanzaHttpException("Error while reading response", statusCode.value(), e);
            }
        }

        if (HttpStatus.NO_CONTENT == responseEntity.getStatusCode()) {
            return null;
        }

        if (isAccessDenied(statusCode)) {
            throw new StanzaSecurityException("Access Denied for User", statusCode.value());
        } else {
            // The error handler built into the RestTemplate should handle 400 and 500 series errors.
            throw new StanzaHttpException("API returned " + statusCode + " and it wasn't handled by the RestTemplate error handler", statusCode.value());
        }
    }

    private boolean isSuccessCode(HttpStatus httpStatus) {
        return HttpStatus.OK == httpStatus || HttpStatus.CREATED == httpStatus || HttpStatus.ACCEPTED == httpStatus;
    }

    private boolean isAccessDenied(HttpStatus httpStatus) {
        return HttpStatus.UNAUTHORIZED == httpStatus || HttpStatus.FORBIDDEN == httpStatus;
    }

    private void addHeadersToRequest(HttpHeaders headers, BodyBuilder requestBuilder) {

        for (Entry<String, List<String>> entry : headers.entrySet()) {

            List<String> values = entry.getValue();

            for (String value : values) {
                if (value != null) {
                    requestBuilder.header(entry.getKey(), value);
                }
            }
        }

        headers.add(StanzaConstants.GUID, MDC.get(StanzaConstants.GUID));
    }

    public MultiValueMap<String, String> parameterToMultiValueMap(CollectionFormat collectionFormat, String name, Object value) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        if (name == null || name.isEmpty() || value == null) {
            return params;
        }

        if (collectionFormat == null) {
            collectionFormat = CollectionFormat.CSV;
        }

        Collection<?> valueCollection = null;
        if (value instanceof Collection) {
            valueCollection = (Collection<?>) value;
        } else {
            params.add(name, parameterToString(value));
            return params;
        }

        if (valueCollection.isEmpty()) {
            return params;
        }

        if (collectionFormat.equals(CollectionFormat.MULTI)) {
            for (Object item : valueCollection) {
                params.add(name, parameterToString(item));
            }
            return params;
        }

        List<String> values = new ArrayList<>();
        for (Object o : valueCollection) {
            values.add(parameterToString(o));
        }
        params.add(name, collectionFormat.collectionToString(values));

        return params;
    }

    public String parameterToString(Object param) {
        if (param == null) {
            return "";
        } else if (param instanceof Collection) {
            StringBuilder b = new StringBuilder();
            for (Object o : (Collection<?>) param) {
                if (b.length() > 0) {
                    b.append(",");
                }
                b.append(String.valueOf(o));
            }
            return b.toString();
        } else {
            return String.valueOf(param);
        }
    }

    // This method is called to add GUID header in internal API calls
    public StanzaRestClient addGUIDHeader() {////////////////////////////

        if (defaultHeaders.containsKey(StanzaConstants.GUID)) {
            defaultHeaders.remove(StanzaConstants.GUID);
        }

        defaultHeaders.add(StanzaConstants.GUID, MDC.get(StanzaConstants.GUID));

        return this;
    }

    public <T> T get(String path,
                     MultiValueMap<String, String> queryParams,
                     HttpHeaders headerParams,
                     List<MediaType> accept,
                     TypeReference<T> returnType,
                     MediaType mediaType){
        return request(path, HttpMethod.GET, queryParams, null, headerParams, accept, returnType, mediaType);
    }

    public <T> T post(String path,
                      MultiValueMap<String, String> queryParams,
                      Object body,
                      HttpHeaders headerParams,
                      List<MediaType> accept,
                      TypeReference<T> returnType,
                      MediaType mediaType){
        return request(path, HttpMethod.POST, queryParams, body, headerParams, accept, returnType, mediaType);
    }

    public <T> T put(String path,
                     MultiValueMap<String, String> queryParams,
                     Object body,
                     HttpHeaders headerParams,
                     List<MediaType> accept,
                     TypeReference<T> returnType,
                     MediaType mediaType){
        return request(path, HttpMethod.PUT, queryParams, body, headerParams, accept, returnType, mediaType);
    }

    public <T> T head(String path,
                      MultiValueMap<String, String> queryParams,
                      Object body,
                      HttpHeaders headerParams,
                      List<MediaType> accept,
                      TypeReference<T> returnType,
                      MediaType mediaType){
        return request(path, HttpMethod.HEAD, queryParams, body, headerParams, accept, returnType, mediaType);
    }

    public <T> T patch(String path,
                       MultiValueMap<String, String> queryParams,
                       Object body,
                       HttpHeaders headerParams,
                       List<MediaType> accept,
                       TypeReference<T> returnType,
                       MediaType mediaType){
        return request(path, HttpMethod.PATCH, queryParams, body, headerParams, accept, returnType, mediaType);
    }

    public <T> T delete(String path,
                        MultiValueMap<String, String> queryParams,
                        Object body,
                        HttpHeaders headerParams,
                        List<MediaType> accept,
                        TypeReference<T> returnType,
                        MediaType mediaType){
        return request(path, HttpMethod.DELETE, queryParams, body, headerParams, accept, returnType, mediaType);
    }

    public <T> T options(String path,
                         MultiValueMap<String, String> queryParams,
                         Object body,
                         HttpHeaders headerParams,
                         List<MediaType> accept,
                         TypeReference<T> returnType,
                         MediaType mediaType){
        return request(path, HttpMethod.OPTIONS, queryParams, body, headerParams, accept, returnType, mediaType);
    }

}