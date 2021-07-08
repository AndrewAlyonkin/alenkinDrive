package net.alenkin.alenkindrive.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * @author Alenkin Andrew
 * oxqq@ya.ru
 */
@Component
public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final JwtTokenFilter filter;

    @Autowired
    public JwtConfigurer(JwtTokenFilter filter) {
        this.filter = filter;
    }

    @Override
    public void configure(HttpSecurity builder) {
        builder.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
