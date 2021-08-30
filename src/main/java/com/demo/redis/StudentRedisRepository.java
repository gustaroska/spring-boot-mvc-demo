package com.demo.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.model.Student;

@Repository
public interface StudentRedisRepository extends CrudRepository<Student, String> {}
