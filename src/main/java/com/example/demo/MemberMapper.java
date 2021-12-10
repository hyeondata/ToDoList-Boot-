package com.example.demo;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface MemberMapper{


    @Insert("INSERT INTO member(_id,_pw,_name) VALUES (#{member.Id},#{member.pw},#{member.name})")
    void memberInsert(@Param("member") Member member);

    @Select("select id from member where _id = #{member.Id} and _pw = #{member.pw} ")
    Integer Login(@Param("member") Member member);

    @Select("select _name from member where _id = #{member.Id} and _pw = #{member.pw} ")
    String Name(@Param("member") Member member);
}
