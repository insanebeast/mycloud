package com.pueeo.common.support;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class LoginUser{
    private Long userId;
    private String username;
    private String jti;
    private Long expiresIn;
    private List<String> authorities;
}