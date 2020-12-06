package com.sloopsight.sandbox.app.dto.response;

public class Hint {
    private String keyword;
    private String snippet;
    private String description;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Hint(String keyword, String snippet, String description) {
        super();
        this.keyword = keyword;
        this.snippet = snippet;
        this.description = description;
    }

    public Hint() {
        super();
        // TODO Auto-generated constructor stub
    }

}
