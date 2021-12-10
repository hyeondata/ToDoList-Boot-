package com.example.demo;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;



@Mapper
public interface BoardMapper {
    
    @Select("select _Desc, _Date, _id, status from description WHERE MemberID = #{member.Id}")
    List<Board> boards(@Param("member") Member member);




    @Select("select status from description where _id = #{id}")
    int SelectId (@Param("id") String id); 

    //Button

    @Update("UPDATE description SET status = #{status} where _id = #{id}")
    void Button(@Param("status") int status ,@Param("id") String id);


    //Delete

    @Delete("DELETE FROM todolist.description where _id = #{id}")
    void CancelBoard(@Param("id") String id);

    //CreateBoard

    @Insert("INSERT INTO description(_Desc,_Date,_Desc_ID,MemberID) VALUES (#{Desc},#{Date},#{member.name},#{member.Num})")
    void CreateWork(@Param("Desc") String Desc, @Param("Date") String Date, @Param("member") Member member);

}
