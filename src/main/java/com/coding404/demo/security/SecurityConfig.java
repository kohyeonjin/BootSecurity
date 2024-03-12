package com.coding404.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration //설정파일
@EnableWebSecurity //이 설정파일을 시큐리티 필터에 등록을 시킴
public class SecurityConfig {
	
	
	//비밀번호 암호화 객체(시큐리티가 제공)
	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		//csrf토큰이 있는데, 사용안함x
		http.csrf().disable();
		
		//권한설정
		//모든요청에 대해 사용자 인증이 필요함
		//http.authorizeRequests( authorize -> authorize.anyRequest().authenticated() );
	
		//권한명 앞에는 default로 ROLE_ 가 붙는다.
		
		//특정요청
		//헬로페이지는 인증이 필요함
		//http.authorizeRequests( authorize -> authorize.antMatchers("/hello").authenticated());
		
		// user/모든경로는 인증이 필요함
		//http.authorizeRequests( authorize -> authorize.antMatchers("/user/**").authenticated());
		
		//유저페이지는 USER 권한이 필요함, 어드민페이지 ADMIN권한이 필요함(인증이 되더라도 권한이라는게 필요)
//		http.authorizeRequests(authorize -> authorize.antMatchers("/user/**").hasRole("USER")
//													 .antMatchers("/admin/**").hasRole("ADMIN"));
		
		//all은 인증필요, 유저페이지,어드민 페이지는 권한이 필요, 나머지 모든 요청은 허용
//		http.authorizeRequests(authorize -> authorize.antMatchers("/all").authenticated()
//													 .antMatchers("/user/**").hasRole("USER")
//													 .antMatchers("/admin/**").hasRole("ADMIN")
//													 .anyRequest().permitAll());
		
		//all은 인증필요, 유저페이지 USER,ADMIN,TESTER 중 1개의 권한 필요, 어드민페이지 ADMIN 권한 필요, 나머지 모든 요청은 허용
		http.authorizeRequests(authorize -> authorize.antMatchers("/all").authenticated()
												     .antMatchers("/user/**").hasAnyRole("USER","ADMIN","TESTER")
												     .antMatchers("/admin/**").hasRole("ADMIN")
												     .anyRequest().permitAll());
		//시큐리티 기반의 폼로그인을 사용하겠다.
		http.formLogin()
			.loginPage("/login") //우리가 만들어 놓은 커스터마이징 페이지의 경로를 로그인페이지로 사용함
			.loginProcessingUrl("/loginForm")// 로그인을 시도하는 경로
			.defaultSuccessUrl("/hello")//로그인 성공 후 이동하고 싶은 경로
		//	.usernameParameter("xxx") //로그인 username 파라미터 변경시
		//  .passwordParameter(null) //password 파라미터 변경시에
			.failureUrl("/login?err=true")//로그인 실패시 이동하고 싶은 경로
			.failureHandler(authenticationFailureHandler())
			.and()
			.exceptionHandling().accessDeniedPage("/deny") //권한이 없을 시에 처리
		//.exceptionHandling().accessDeniedHandler(핸들러)
			.and()
			.logout().logoutUrl("/myLogout").logoutSuccessUrl("/hello"); //로그아웃주소 /myLogout 로그아웃 후에는 /hello 로 이동
		
			return http.build();
	}
	
	//인증실패 핸들러
	@Bean
	public AuthenticationFailureHandler authenticationFailureHandler() {
		
		CustomAuthenicationFailure custom = new CustomAuthenicationFailure();
		custom.setRedirectURL("/login?err=true");
		
		return custom;
	}
}
