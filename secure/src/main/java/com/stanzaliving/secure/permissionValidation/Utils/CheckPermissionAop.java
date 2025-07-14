package com.stanzaliving.secure.permissionValidation.Utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stanzaliving.secure.error.StanzaSecurityException;
import com.stanzaliving.secure.permissionValidation.AttributeDto;
import com.stanzaliving.secure.permissionValidation.CheckPermission;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Aspect
@Component
@Log4j2
public class CheckPermissionAop {

    @Value("${service.acl.url}")
    private String aclBaseUrl;

    @Before(value = "@annotation(com.stanzaliving.secure.permissionValidation.CheckPermission)")
    public Object pcp(JoinPoint joinPoint) throws Exception {
        MethodSignature methodSignature=(MethodSignature) joinPoint.getSignature();
        String[] permissions=methodSignature.getMethod().getAnnotation(CheckPermission.class).permissions();
        String resource=methodSignature.getMethod().getAnnotation(CheckPermission.class).resource();
        EvaluationType evaluationType=methodSignature.getMethod().getAnnotation(CheckPermission.class).evaluationType();
        Class<? extends AttributeValueProvider> className=methodSignature.getMethod().getAnnotation(CheckPermission.class).attributeValueProvider();

        //get attribute value provider from abac resources.

        AttributeValueProvider attributeValueProvider= null;
        try {
            attributeValueProvider = className.getConstructor().newInstance();
        } catch (Exception e){
            log.error(e.getMessage(), e);
        }

        HttpServletRequest request=null;
        try {
            request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        }
        catch (Exception e){
            throw new ApiValidationException(RequestContextHolder.currentRequestAttributes().toString());
        }

        String token=extractTokenFromRequest(request);

        AttributeDto attributeDto=attributeValueProvider.fillAttributeValues(request);

        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        HashMap<String,Object> attributes= (HashMap<String, Object>) objectMapper.convertValue(attributeDto, Map.class);

        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String,String> conditionHeaders=new LinkedMultiValueMap<>();
        conditionHeaders.put("Accept", Collections.singletonList(MediaType.ALL_VALUE));

        HttpEntity<ConditionContextDto> conditionRequest = new HttpEntity<>(
                new ConditionContextDto(resource, Arrays.asList(permissions),attributes,evaluationType,token),
                conditionHeaders
        );

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);
        try {
            ResponseEntity<Boolean> conditionResponse = restTemplate.exchange(aclBaseUrl + "/internal/eval/permission",
                    HttpMethod.POST, conditionRequest, Boolean.class);
            if(!conditionResponse.getBody()){
                throw new StanzaSecurityException("User doesn't have the relevant role or permissions", HttpStatus.FORBIDDEN.value());
            }
            return joinPoint ;
        } catch (Throwable e) {
           throw new StanzaSecurityException(e.getMessage(), HttpStatus.FORBIDDEN.value());
        }

    }



    private String extractTokenFromRequest(HttpServletRequest request) {
        String token = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (SecurityConstants.TOKEN_HEADER_NAME.equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token == null) {

            token = request.getHeader(SecurityConstants.AUTHORIZATION_HEADER);
            if (token != null && token.startsWith(SecurityConstants.VENTA_TOKEN_PREFIX)) {		//only if it follows bearer schema, then we would consider valid token
                token = token.replace(SecurityConstants.VENTA_TOKEN_PREFIX, "");
            } else {
                token = null;
            }
        }
        return token;
    }
}
