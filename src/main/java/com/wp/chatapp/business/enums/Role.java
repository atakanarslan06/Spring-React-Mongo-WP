package com.wp.chatapp.business.enums;

import lombok.Getter;

@Getter
public enum Role {
    USER("ROLE_ADMIN"),
    ADMIN("ROLE_USER");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    }
