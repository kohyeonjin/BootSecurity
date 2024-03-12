package com.coding404.demo.member;

import org.apache.ibatis.annotations.Mapper;

import com.coding404.demo.command.MemberVO;

@Mapper
public interface MemberMapper {

	public void join(MemberVO vo);
	public MemberVO login(String username);
}
