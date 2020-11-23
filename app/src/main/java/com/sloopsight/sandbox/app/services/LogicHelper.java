package com.sloopsight.sandbox.app.services;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.Exchange;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;

import com.sloopsight.sandbox.app.meta.Intellisense;
import com.sloopsight.sandbox.app.meta.MethodHint;
import com.sloopsight.sandbox.app.meta.ParamHint;
import com.sloopsight.sandbox.app.util.Mapper;

@Intellisense("web")
public class LogicHelper {

    private HttpServletRequest httpServletRequest;
    private HttpServletResponse httpServletResponse;
    private Map<String, String> path;
    private String body;
    private ApplicationContext context;
    private Exchange exchange;

    public LogicHelper(Exchange exchange, Map<String, String> path, ApplicationContext context) throws IOException {
        super();

        this.httpServletRequest = exchange.getIn().getBody(HttpServletRequest.class);
        this.httpServletResponse = exchange.getMessage(HttpServletResponse.class);
        this.body = exchange.getIn().getBody(String.class);
        this.context = context;
        this.path = path;
        this.exchange = exchange;
    }

    @MethodHint(name = "param", comment = "Get request parameter")
    public String param(@ParamHint("Request Param Name") String param) {
        String defaultParam = this.httpServletRequest.getParameter(param);
        return exchange.getIn().getHeader(param, defaultParam, String.class);
    }

    @MethodHint(name = "body", comment = "Get request body")
    public String body() throws IOException {
        return body;
    }

    @MethodHint(name = "reply", comment = "Send reply to, should be called once")
    public void reply(@ParamHint("HTTP Response Code") int code, @ParamHint("HTTP Response body") String body)
            throws IOException {
        httpServletResponse.setStatus(code);
        IOUtils.write(body, httpServletResponse.getOutputStream(), "UTF-8");
    }

    @MethodHint(name = "reply", comment = "Send reply to, should be called once")
    public void reply(@ParamHint("HTTP Response Code") int code, @ParamHint("body") Object body,
            @ParamHint("Content Type") String mediaType) throws IOException {

        if (MediaType.parseMediaType(mediaType).equalsTypeAndSubtype(MediaType.APPLICATION_JSON)) {
            Mapper mapper = context.getBean(Mapper.class);
            IOUtils.write(mapper.toJson(body), httpServletResponse.getOutputStream(), "UTF-8");
        } else {
            IOUtils.write(String.valueOf(body), httpServletResponse.getOutputStream(), "UTF-8");
        }

        httpServletResponse.setStatus(code);
        httpServletResponse.setContentType(mediaType);
    }

    @MethodHint(name = "header", comment = "Set Repsonse Header")
    public void header(@ParamHint("Header name") String param, @ParamHint("Header value") String value) throws IOException {

        httpServletResponse.setHeader(param, value);
    }

    @MethodHint(name = "header", comment = "Get Request Header")
    public String header(@ParamHint("Header name") String param) throws IOException {
        return httpServletRequest.getHeader(param);
    }

    @MethodHint(name = "path", comment = "Get Value of path variable")
    public String path(@ParamHint("Path Variable Name") String param) {
        return path.getOrDefault(param, StringUtils.EMPTY);
    }

    @MethodHint(name = "bearer", comment = "Get Bearer Value")
    public String bearer() {
        String auth = httpServletRequest.getHeader("Authorization");
        if (StringUtils.isNotBlank(auth) && auth.contains(" ")) {
            return auth.split(" ")[1];
        }
        return StringUtils.EMPTY;
    }

    @MethodHint(name = "bearer", comment = "Get Basic Auth Map [user:<username>,password:<password>]")
    public Map<String, String> basic() {
        try {
            String auth = httpServletRequest.getHeader("Authorization");
            if (StringUtils.isNotBlank(auth) && auth.contains(" ")) {
                String decodedAuth = new String(Base64.getDecoder().decode(auth.split(" ")[1]));
                Map<String, String> cred = new HashMap<String, String>();
                cred.put("user", decodedAuth.split(",")[0]);
                cred.put("password", decodedAuth.split(",")[1]);
                return cred;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return new HashMap<String, String>();
    }
}
