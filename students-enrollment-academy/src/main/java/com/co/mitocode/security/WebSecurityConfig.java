package com.co.mitocode.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Slf4j
public class WebSecurityConfig {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityContextRepository securityContextRepository;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
        return http
                .exceptionHandling()
                .authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() -> {
                    swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                })).accessDeniedHandler((swe, e) -> Mono.fromRunnable(() -> {
                    swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                }))
                .and().csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange()
                //SWAGGER PARA SPRING SECURITY
                .pathMatchers("/swagger-resources/**").permitAll()
                .pathMatchers("/swagger-ui.html").permitAll()
                .pathMatchers("/webjars/**").permitAll()
                //SWAGGER PARA SPRING SECURITY
                .pathMatchers("/login").permitAll()
                .pathMatchers("/v2/login").permitAll()
                .pathMatchers("/user").permitAll()
                .pathMatchers("/v2/**").authenticated()
                //.pathMatchers("/v2/**").hasAnyAuthority("ADMIN")
                //.pathMatchers("/V2/**")
                /*.access((mono, context) -> mono
                        .map(auth -> auth.getAuthorities()
                                .stream()
                                .filter(e -> e.getAuthority().equals("ADMIN"))
                                .count() > 0)
                        .map(AuthorizationDecision::new)
                )*/
                .pathMatchers("/enrollment/**").authenticated()
                .pathMatchers("/courses/**").authenticated()
                .pathMatchers("/students/**").authenticated()
                .anyExchange().authenticated()
                .and().build();
    }

}
