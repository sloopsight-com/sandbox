package com.sloopsight.sandbox.app.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class MemberShipKey implements Serializable {

    private static final long serialVersionUID = 7653985735057705787L;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public static MemberShipKey of(User user, Project project) {
        MemberShipKey key = new MemberShipKey();
        key.user = user;
        key.project = project;
        return key;
    }
}
