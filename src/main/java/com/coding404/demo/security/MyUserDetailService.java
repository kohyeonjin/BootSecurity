package com.coding404.demo.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.coding404.demo.command.MemberVO;
import com.coding404.demo.member.MemberMapper;

@Service //빈으로 등록되어 있으면,
public class MyUserDetailService implements UserDetailsService {
	
	@Autowired
	private MemberMapper memberMapper;
	
	// 스프링이 UserDetailService 명을 찾아서, 사용자가 로그인 시 loadUserByUsername 실행시킴
	//.loginProcessingUrl 에 로그인 URL등록
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		System.out.println("사용자가 로그인을 시도함");
		System.out.println("사용자가 입력한 아이디 :" + username);
		
		//로그인시도 (비밀번호는 시큐리티가 알아서 비교후에 처리)
		//로그인을 해서 vo객체
		 MemberVO vo =memberMapper.login(username);
		 
		 System.out.println(vo);
		 
		 //회원정보가 있음 -> 비밀번호 비교를 하기위해서 UserDetail타입으로 리턴
		 if(vo != null) {
			 return new MyUserDetails(vo); //스프링 시큐리티가 비밀번호 비교, 롤 확인도 해서 로그인처리
			 //로그인 성공시
		 }
		 //스프링시큐리티에 설정한 형식대로, 권한까지 처리를 해줍니다.
		 //만약 아이디가 없거나, 비밀번호가 틀리면 login?error로 이동합니다(설정으로 변경가능)
		 //시큐리티는 특별한 세션 모형을 사용함
		 // 시큐리티세션(new Authentication( new MyUserDetails() ) ) 모형으로 저장시킵니다.
		
		 return null;
	}

}
