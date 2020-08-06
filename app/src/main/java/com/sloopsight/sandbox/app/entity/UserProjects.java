package com.sloopsight.sandbox.app.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user_projects")
public class UserProjects {
    @EmbeddedId
    private MemberShipKey key;

    public MemberShipKey getKey() {
        return key;
    }

    public void setKey(MemberShipKey key) {
        this.key = key;
    }

}
