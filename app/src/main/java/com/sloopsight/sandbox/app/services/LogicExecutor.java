package com.sloopsight.sandbox.app.services;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface LogicExecutor {

    boolean execute(HttpServletRequest request, HttpServletResponse response, String logic, Map<String, Object> bindings) throws IOException;

}
