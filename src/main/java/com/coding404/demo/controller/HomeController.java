package com.coding404.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.coding404.demo.command.MemberVO;
import com.coding404.demo.member.MemberMapper;
import com.coding404.demo.security.MyUserDetails;

@Controller
public class HomeController {
	
	//비밀번호 암호화 객체
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private MemberMapper memberMapper;
	
	//시큐리티 세션에 저장된 인증객체 사용하는 방법
	@GetMapping("/hello")
	public String hello(Authentication auth) {
		
		if(auth != null) { //null이 아니면 인증객체가 존재한다.
			MyUserDetails details = (MyUserDetails)auth.getPrincipal();
			String username = details.getUsername();
			String password = details.getPassword();
			String role = details.getRole();
			
			System.out.println("컨트롤러 시큐리티인증객체정보:"+ username + ", " + password +  ", " + role);
		}
		
		return "hello";
	}
	
	@GetMapping("/all")
	public String all() {
		return "all";
	}
	@GetMapping("/join")
	public String join() {
		return "join";
	}
	
	@GetMapping("/user/mypage")
	@ResponseBody
	public String mypage() {
		return "REST 방식의 요청페이지";
	}
	
	@GetMapping("/admin/mypage")
	@ResponseBody
	public String admin() {
		return "REST 방식의 어드민페이지";
	}
	
	@PostMapping("/joinForm")
	public String joinForm(MemberVO vo) {
		
		String pw = bCryptPasswordEncoder.encode(vo.getPassword());
		vo.setPassword(pw); //암호화된 비밀번호 처리
		
		//서비스영역 생략...
		memberMapper.join(vo);
		
		
		
		return "redirect:/login";
	}
	
	//시큐리티가 처음 적용되면, 모든 페이지는 시큐리티가 요청을 가로채서 login페이지로 연결을 합니다.
	
	//login 페이지는 시큐리티가 제공해주는 기본페이지로 사용하기 때문에, 별도의 설정으로 지정을 해야합니다.
	@GetMapping("/login")
	public String login(@RequestParam(value = "err", required=false) String err , Model model) {
		
		if(err != null) {
			model.addAttribute("msg","아이디,비밀번호를 확인하세요");
		}
		
		return "login";
	}
	//접근 권한 없음 핸들러
	@GetMapping("/deny")
	@ResponseBody
	public String deny() {
		
		return "너 권한 없어";
	}
}
