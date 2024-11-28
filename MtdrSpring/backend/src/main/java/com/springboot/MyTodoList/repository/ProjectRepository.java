package com.springboot.MyTodoList.repository;

import com.springboot.MyTodoList.model.Project;  // Modelo Project
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository 
public interface ProjectRepository extends JpaRepository<Project, Integer> {
}