package com.demo;

import java.security.interfaces.RSAPublicKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@EnableWebSecurity
public class OAuth2ResourceConfig extends WebSecurityConfigurerAdapter {

	@Value("${spring.security.oauth2.resourceserver.jwt.public-key-location}")
	RSAPublicKey key;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
        
			.oauth2ResourceServer((resourceServer) -> resourceServer
					.jwt((jwt) -> jwt
							.decoder(jwtDecoder())
					)
			)
			
			.authorizeRequests()
            .antMatchers("/api/**").authenticated()
            .antMatchers("/**").permitAll()
            
            ;
		
		
		
	}

	@Bean
	JwtDecoder jwtDecoder() {
		return NimbusJwtDecoder.withPublicKey(this.key).build();
	}

}