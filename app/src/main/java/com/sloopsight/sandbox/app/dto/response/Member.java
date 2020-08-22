package com.sloopsight.sandbox.app.dto.response;

public class Member {

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Member of(Long id, String name) {
        Member member = new Member();
        member.id = id;
        member.name = name;
        return member;
    }

}
