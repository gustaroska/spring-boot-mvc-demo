package com.demo.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StudentMapper {
	
	@Delete("DELETE FROM students WHERE id=#{id}")
	public void deleteById(@Param("id") String id);


}
