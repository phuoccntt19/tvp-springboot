package com.springboot.tvpspringbootheroku.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebServiceConfig extends WebSecurityConfigurerAdapter {
 
    @Autowired
    private DataSource dataSource;
 
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
			.usersByUsernameQuery("select username, password, enabled from user where username = ?")
			.authoritiesByUsernameQuery("select username, role from user where username = ?");
	}
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
 
        http.csrf().disable();
 
        http.authorizeRequests().antMatchers("/", "/login", "/logout").permitAll();
 
        http.authorizeRequests().antMatchers("/user").access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')");
 
        http.authorizeRequests().antMatchers("/admin").access("hasRole('ROLE_ADMIN')");
 
        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");
 
        http.authorizeRequests().and().formLogin()
                .loginProcessingUrl("/j_spring_security_check")
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error=true")
                .usernameParameter("username")
				.passwordParameter("password");
	}
}
