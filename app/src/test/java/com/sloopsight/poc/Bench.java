package com.sloopsight.poc;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;

public class Bench {

    public static void main(String[] args) throws IOException {
        String spec=IOUtils.toString(new ClassPathResource("swager.yaml").getInputStream());
        OpenAPI openAPI = new OpenAPIV3Parser().readContents(spec).getOpenAPI();
        System.out.println(new ObjectMapper().writeValueAsString(openAPI.getPaths().get("/pet/{petId}")));
        System.out.println( new OpenAPIV3Parser().readContents(spec));
    }

}
