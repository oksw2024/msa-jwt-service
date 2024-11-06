package osj.javat.security;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import lombok.RequiredArgsConstructor;
import osj.javat.filter.JwtTokenFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
	private final JwtTokenFilter jwtTokenFilter;
	
	/*
	 * CorsConfigurationSource corsConfigurationSource() { return request -> {
	 * CorsConfiguration config = new CorsConfiguration();
	 * config.setAllowedHeaders(Collections.singletonList("*"));
	 * config.setAllowedMethods(Collections.singletonList("*"));
	 * config.setAllowedOriginPatterns(Collections.singletonList(
	 * "http://localhost:5173")); config.setAllowCredentials(true); return config;
	 * }; }
	 */
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
		//.cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
		.httpBasic(HttpBasicConfigurer::disable)// UI를 사용하는 것을 기본값으로 가진 시큐리티 설정을 비활성
        .csrf(CsrfConfigurer::disable) // CSRF 보안설정 비활성
        .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // JWT 토큰인증 방식의 사용으로 세션은 사용하지 않음
        .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/v2/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v2/auth/**").permitAll()
                .requestMatchers("/api/v1/user/check/**").permitAll()
                .requestMatchers("/api/v2/user/check/**").permitAll()
                .requestMatchers("/api/v1/user/**").authenticated()
                .requestMatchers("/api/v2/user/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/v1/attachment/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v2/attachment/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/comment/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v2/comment/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/post/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v2/post/**").permitAll()
                .requestMatchers("/api/v1/**").authenticated()
                .requestMatchers("/api/v2/**").authenticated()
        )
        .addFilterBefore(this.jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
            // 현재 필터에서 인증이 정상처리되면 UsernamePasswordAuthenticationFilter 는 자동으로 통과
		return httpSecurity.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
