package com.sloopsight.sandbox.app.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sloopsight.sandbox.app.meta.Intellisense;
import com.sloopsight.sandbox.app.meta.MethodHint;
import com.sloopsight.sandbox.app.meta.ParamHint;

@Intellisense("http")
public class Rest {

    RestTemplate restTemplate = new RestTemplate();
    ObjectMapper mapper = new ObjectMapper();

    class Result {
        private HttpHeaders headers = new HttpHeaders();
        private Object object;
        private int code;

        public HttpHeaders getHeaders() {
            return headers;
        }

        public Object getObject() {
            return object;
        }

        public int getCode() {
            return code;
        }

    }

    public String nonData(String url, Object jo, HttpMethod httpMethod) throws JsonProcessingException {
        String json = mapper.writeValueAsString(jo);
        JsonNode jsonObject = mapper.readTree(json);
        HttpHeaders headers = new HttpHeaders();
        Map<String, Object> params = new HashMap<String, Object>();

        if (jsonObject.has("headers")) {
            jsonObject.get("headers").fields().forEachRemaining(e -> {
                headers.add(e.getKey(), e.getValue().asText(""));
            });
        }
        if (jsonObject.has("params")) {

            jsonObject.get("params").fields().forEachRemaining(e -> {
                params.put(e.getKey(), e.getValue().asText(""));
            });

        }
        HttpEntity<Object> entity = new HttpEntity<Object>(headers);
        Result result = new Result();

        try {
            ResponseEntity<Object> response = restTemplate.exchange(url, httpMethod, entity, Object.class, params);

            result.headers = response.getHeaders();
            result.object = response.getBody();
            result.code = response.getStatusCodeValue();

        } catch (RestClientException exception) {

            result.headers = headers;
            result.object = exception.getMessage();
            result.code = 500;
        }
        return mapper.writeValueAsString(result);

    }

    public String withData(String url, Object jo, HttpMethod httpMethod) throws JsonProcessingException {
        String json = mapper.writeValueAsString(jo);
        JsonNode jsonObject = mapper.readTree(json);
        HttpHeaders headers = new HttpHeaders();
        Map<String, Object> params = new HashMap<String, Object>();

        if (jsonObject.has("headers")) {
            jsonObject.get("headers").fields().forEachRemaining(e -> {
                headers.add(e.getKey(), e.getValue().asText(""));
            });
        }
        if (jsonObject.has("params")) {

            jsonObject.get("params").fields().forEachRemaining(e -> {
                params.put(e.getKey(), e.getValue().asText(""));
            });
        }
        Object object = new Object();
        if (jsonObject.has("body")) {
            object = jsonObject.get("body").asText();

        }
        HttpEntity<Object> entity = new HttpEntity<Object>(object, headers);
        Result result = new Result();

        try {
            ResponseEntity<Object> response = restTemplate.exchange(url, httpMethod, entity, Object.class, params);

            result.headers = response.getHeaders();
            result.object = response.getBody();
            result.code = response.getStatusCodeValue();

        } catch (RestClientException exception) {

            result.headers = headers;
            result.object = exception.getMessage();
            result.code = 500;
        }
        return mapper.writeValueAsString(result);

    }

    @MethodHint(name = "httpGet", comment = "Execute GET API response : ({headers:{},object:{},code:200})")
    public String get(@ParamHint(value = "endpoint") String url, @ParamHint("{params:{},headers:{}}") Object jo)
            throws JsonProcessingException {
        return nonData(url, jo, HttpMethod.GET);
    }

    @MethodHint(name = "httpDelete", comment = "Execute DELETE API response : ({headers:{},object:{},code:200})")
    public String delete(@ParamHint(value = "endpoint") String url, @ParamHint("{params:{},headers:{}}") Object jo)
            throws JsonProcessingException {
        return nonData(url, jo, HttpMethod.DELETE);
    }

    @MethodHint(name = "httpPut", comment = "Execute PUT API response : ({headers:{},object:{},code:200})")
    public String put(@ParamHint(value = "endpoint") String url, @ParamHint("{params:{},headers:{}}") Object jo)
            throws JsonProcessingException {
        return withData(url, jo, HttpMethod.PUT);
    }

    @MethodHint(name = "httpPost", comment = "Execute POST API response : ({headers:{},object:{},code:200})")
    public String post(@ParamHint(value = "endpoint") String url, @ParamHint("{params:{},headers:{},body:{}}") Object jo)
            throws JsonProcessingException {

        return withData(url, jo, HttpMethod.POST);
    }
}
