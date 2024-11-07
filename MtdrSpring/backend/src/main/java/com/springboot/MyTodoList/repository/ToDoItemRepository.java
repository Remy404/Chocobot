package com.springboot.MyTodoList.repository;
import java.util.List;


import com.springboot.MyTodoList.model.ToDoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ToDoItemRepository extends JpaRepository<ToDoItem, Integer> {
    List<ToDoItem> findByAssigned(String assignedName);
}