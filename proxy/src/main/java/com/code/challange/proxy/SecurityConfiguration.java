package com.code.challange.proxy;

import com.code.challange.proxy.common.RoleConstants;
import com.code.challange.proxy.security.SecurityFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	private final String user_role = "USER";
	private final String userName = "user01";
	private final String userPassword = "secret01";
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		String endodedPassword = passwordEncoder().encode(userPassword);
		endodedPassword = endodedPassword == null ? "" : endodedPassword;
		auth.inMemoryAuthentication()
				.withUser(userName)
				.password(endodedPassword)
				.authorities(user_role);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic().and().authorizeRequests()
				.antMatchers( "/public/**").permitAll()
				.antMatchers("/private/**")
				.hasAuthority(RoleConstants.USER).and()
				.csrf().disable();
		http.addFilterBefore(getCustomSecurityFilter(), UsernamePasswordAuthenticationFilter.class);

	}

	@Bean
	SecurityFilter getCustomSecurityFilter(){
		return new SecurityFilter();
	}

}
