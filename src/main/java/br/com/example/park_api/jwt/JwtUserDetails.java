package br.com.example.park_api.jwt;

import br.com.example.park_api.entity.User;
import org.springframework.security.core.authority.AuthorityUtils;

public class JwtUserDetails extends org.springframework.security.core.userdetails.User { // User from Security
    private User user; // User from entity

    public JwtUserDetails(User user) {
        super(user.getUsername(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().name()));
        this.user = user;
    }

    public Long getId() {
        return this.user.getId();
    }

    public String role() {
        return this.user.getRole().name();
    }
}
