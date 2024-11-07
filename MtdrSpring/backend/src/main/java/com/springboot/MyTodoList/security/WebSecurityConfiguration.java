package com.springboot.MyTodoList.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
/*         http
            .authorizeRequests()
                .anyRequest().authenticated()   // Cualquier solicitud debe estar autenticada
                .and()
            .formLogin()
                // .loginPage("/login")           // Página personalizada de login
                .defaultSuccessUrl("/", true) // Redirige a /home después de un login exitoso
                .permitAll()
                .and()
            .logout()
                .permitAll(); */
                http.cors().and().csrf().disable();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user =
             User.withDefaultPasswordEncoder()
                .username("ADMIN")
                .password("None10001000")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user);
    }
}
