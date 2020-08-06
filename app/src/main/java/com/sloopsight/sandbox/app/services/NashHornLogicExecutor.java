package com.sloopsight.sandbox.app.services;

import java.io.IOException;
import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

@Component
public class NashHornLogicExecutor implements LogicExecutor {

    @Override
    public boolean execute(HttpServletRequest req, HttpServletResponse res, String logic, Map<String, Object> context) throws IOException {
        try {
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
            Bindings bindings = engine.createBindings();
            context.forEach((k, v) -> {

                bindings.put(k, v);
            });
            engine.eval(logic, bindings);
        } catch (Exception e) {
            res.setStatus(500);
            res.setContentType("text/html");
            IOUtils.write(e.getMessage(), res.getOutputStream(), "UTF-8");
        }
        return false;
    }

    public static void main(String[] args) {

    }

}
