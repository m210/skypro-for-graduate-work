package ru.skypro.homework;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class WebSecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/webjars/**",
            "/login", "/register"
    };

    @Bean
    public JdbcUserDetailsManager userDetailsService(AuthenticationManagerBuilder auth, DataSource datasource) throws Exception {
        JdbcUserDetailsManager jdbcUserDetailsManager = auth.jdbcAuthentication()
                .passwordEncoder(new BCryptPasswordEncoder()).dataSource(datasource)
                .usersByUsernameQuery("select email, password, enabled from users where email = ?")
                .authoritiesByUsernameQuery("select username, role from authorities where username = ?")
                .getUserDetailsService();

        jdbcUserDetailsManager.setUserExistsSql("select email from users where email = ?");
        jdbcUserDetailsManager.setCreateUserSql("insert into users (email, password, enabled) values (?,?,?)");
        jdbcUserDetailsManager.setUpdateUserSql("update users set password = ?, enabled = ? where email = ?");
        jdbcUserDetailsManager.setCreateAuthoritySql("insert into authorities (username, role) values (?,?)");
        jdbcUserDetailsManager.setChangePasswordSql("update users set password = ? where email = ?");

        return jdbcUserDetailsManager;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests((authz) ->
                        authz
                                .mvcMatchers(AUTH_WHITELIST).permitAll()
                                .mvcMatchers("/ads/**", "/users/**").authenticated()

                )
                .cors().disable()
                .httpBasic(withDefaults());
        return http.build();
    }


}

