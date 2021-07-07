package com.sloopsight.sandbox.app.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.Exchange;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;

import com.sloopsight.sandbox.app.meta.Intellisense;
import com.sloopsight.sandbox.app.meta.MethodHint;
import com.sloopsight.sandbox.app.meta.ParamHint;
import com.sloopsight.sandbox.app.util.Mapper;

import delight.fileupload.FileUpload;

@Intellisense("web")
public class LogicHelper {

    private HttpServletRequest httpServletRequest;

    private HttpServletResponse httpServletResponse;
    private Map<String, String> path;
    private String body;
    private ApplicationContext context;
    private Exchange exchange;
    Map<String, String> fields = new HashMap<String, String>();

    public LogicHelper(Exchange exchange, Map<String, String> path, ApplicationContext context) throws IOException {
        super();

        this.httpServletRequest = exchange.getIn().getBody(HttpServletRequest.class);

        this.httpServletResponse = exchange.getMessage(HttpServletResponse.class);
        this.context = context;
        this.path = path;
        this.exchange = exchange;
        FileItemIterator iterator = null;
        String ct = exchange.getIn().getHeader("Content-Type", "", String.class).trim();

        if (ct.startsWith("multipart/form-data")) {
            try {
                iterator = FileUpload.parse(exchange.getIn().getBody(byte[].class), (ct));
                while (iterator.hasNext()) {
                    FileItemStream item = iterator.next();

                    if (item.isFormField()) {
                        fields.put(item.getFieldName(), IOUtils.toString(item.openStream(), "UTF-8"));
                    } else {
                        byte[] data = IOUtils.toByteArray(item.openStream());

                        fields.put(item.getFieldName(), "file:" + item.getName() + ":data:" + item.getContentType()
                                + ";base64, " + Base64.getEncoder().encodeToString(data));
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.body = exchange.getIn().getBody(String.class);
        }

    }

    @MethodHint(name = "getRequestParam", comment = "Get request parameter")
    public String param(@ParamHint("Request Param Name") String param) {

        String defaultParam = fields.getOrDefault(param, this.httpServletRequest.getParameter(param));
        return exchange.getIn().getHeader(param, defaultParam, String.class);
    }

    @MethodHint(name = "getBody", comment = "Get request body")
    public String body() throws IOException {
        return body;
    }

    @MethodHint(name = "sendReply", comment = "Send reply to, should be called once")
    public void reply(@ParamHint("HTTP Response Code") int code, @ParamHint("HTTP Response body") String body)
            throws IOException {

        if (body.contains(";base64")) {
            String contentType = StringUtils.substringBetween(body, "data:", ";base64").trim();
            String data = StringUtils.substringAfter(body, ";base64,").trim();
            String file = StringUtils.substringBetween(body, "file:", ":data").trim();

            httpServletResponse.setContentType(contentType);
            if (contentType.startsWith("image")) {
                httpServletResponse.setHeader("Content-Disposition", "inline");
            } else {
                httpServletResponse.setHeader("Content-Disposition", "attachment; filename=" + file);
            }
            httpServletResponse.setStatus(code);
            IOUtils.copyLarge(new ByteArrayInputStream(Base64.getDecoder().decode(data)),
                    httpServletResponse.getOutputStream());
        } else {
            httpServletResponse.setStatus(code);
            IOUtils.write(body, httpServletResponse.getOutputStream(), "UTF-8");
        }
    }

    @MethodHint(name = "replyWithContentType", comment = "Send reply to, should be called once")
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

    @MethodHint(name = "setHeader", comment = "Set Repsonse Header")
    public void header(@ParamHint("Header name") String param, @ParamHint("Header value") String value) throws IOException {

        httpServletResponse.setHeader(param, value);
    }

    @MethodHint(name = "getHeader", comment = "Get Request Header")
    public String header(@ParamHint("Header name") String param) throws IOException {
        return httpServletRequest.getHeader(param);
    }

    @MethodHint(name = "getPath", comment = "Get Value of path variable")
    public String path(@ParamHint("Path Variable Name") String param) {
        return path.getOrDefault(param, StringUtils.EMPTY);
    }

    @MethodHint(name = "getOauthBearerToken", comment = "Get Bearer Value")
    public String bearer() {
        String auth = httpServletRequest.getHeader("Authorization");
        if (StringUtils.isNotBlank(auth) && auth.contains(" ")) {
            return auth.split(" ")[1];
        }
        return StringUtils.EMPTY;
    }

    @MethodHint(name = "getBasicBearerToken", comment = "Get Basic Auth Map [user:<username>,password:<password>]")
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
        }

        return new HashMap<String, String>();
    }
}
