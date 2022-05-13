package com.pueeo.oauth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AbstractUserDetailsService extends UserDetailsService {

    UserDetails loadByPhone(String phone);
}
