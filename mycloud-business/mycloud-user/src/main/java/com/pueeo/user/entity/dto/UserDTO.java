package com.pueeo.user.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class UserDTO implements Serializable {

    private Long uid;
    private String name;
    private Byte gender; //1-男 2-女
}
