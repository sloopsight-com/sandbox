package com.sloopsight.sandbox.app.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriTemplate;

import com.sloopsight.sandbox.app.entity.Endpoint;
import com.sloopsight.sandbox.app.repo.EndpointRepository;

@Component
public class ApiExecutor extends RouteBuilder {

    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    EndpointService endpointService;

    @Autowired
    private EndpointRepository epRepository;

    @Autowired
    private LogicExecutor executor;

    @Autowired
    private ApplicationContext context;

    @Value("${server.servlet.context-path:/}")
    private String contextPath;

    @Override
    public void configure() throws Exception {
        // TODO Auto-generated method stub
        from("servlet:///exec?matchOnUriPrefix=true").process(new Processor() {

            @Override
            public void process(Exchange exchange) throws Exception {
                // TODO Auto-generated method stub
                HttpServletRequest request = exchange.getIn().getBody(HttpServletRequest.class);
                HttpServletResponse response = exchange.getMessage(HttpServletResponse.class);
                UriTemplate template = new UriTemplate(contextPath + "/camel/exec/{projectId}/");
                Map<String, String> parameters = template.match(request.getRequestURI());
                Long projectId = NumberUtils.toLong(parameters.get("projectId"));
                String method = StringUtils.lowerCase(request.getMethod());
                List<Endpoint> endpoints = epRepository.findAllByProjectAndMethod(projectId, method);

                for (Endpoint endpoint : endpoints) {
                    try {
                        String ep = endpoint.getPath();
                        if (!endpoint.getPath().startsWith("/")) {
                            ep = "/" + endpoint.getPath();
                        }
                        String path = contextPath + "/camel/exec/{projectId}" + ep;
                        UriTemplate projectTemplate = new UriTemplate(path);
                        if (projectTemplate.matches(request.getRequestURI())) {
                            Map<String, String> projectParam = projectTemplate.match(request.getRequestURI());
                            Map<String, Object> bindings = new HashMap<String, Object>();
                            bindings.put("req", request);
                            bindings.put("res", response);
                            bindings.put("web", new LogicHelper(exchange, projectParam, context));
                            bindings.put("localDb", new LocalDb(projectId));
                            bindings.put("http", new Rest());
                            executor.execute(request, response, endpoint.getLogic(), bindings);
                            exchange.getMessage().removeHeaders(".*");
                            response.getHeaderNames().forEach(h -> {
                                exchange.getMessage().setHeader(h, response.getHeader(h));
                            });
                            return;
                        }
                    } catch (IOException e) {
                        logger.error(e.getMessage(), e);
                        tryFillError(e.getMessage(), response);
                    }
                }

            }
        });
    }

    private void tryFillError(String error, HttpServletResponse response) {
        try {
            response.setStatus(500);
            response.getWriter().write(error);
        } catch (Exception e) {
            // TODO: handle exception
        }

    }
}
