package com.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.demo.model.Tutorial;

@Mapper
public interface TutorialMapper {
	
	@Select("SELECT * FROM tutorials WHERE id = #{id}")
	Tutorial findById(@Param("id") long id);
	
	List<Tutorial> findAll();
}
