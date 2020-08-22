package com.sloopsight.sandbox.app.dto.request;

import java.util.Map;

public class AuthConfigRequest {

    private Boolean enabled;

    private Map<String, Object> config;

    public Map<String, Object> getConfig() {
        return config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

}
