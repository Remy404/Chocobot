package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.repository.ToDoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ToDoItemService {

    @Autowired
    private ToDoItemRepository toDoItemRepository;

    public List<ToDoItem> findAll() {
        return toDoItemRepository.findAll();
    }

    public ResponseEntity<ToDoItem> getItemById(int id) {
        Optional<ToDoItem> todoData = toDoItemRepository.findById(id);
        return todoData.map(toDoItem -> new ResponseEntity<>(toDoItem, HttpStatus.OK))
                       .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ToDoItem addToDoItem(ToDoItem toDoItem) {
        
        ToDoItem savedItem = toDoItemRepository.save(toDoItem);
        
        return savedItem;
    }
    

    public boolean deleteToDoItem(int id) {
        try {
            toDoItemRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ToDoItem updateToDoItem(int id, ToDoItem td) {
        Optional<ToDoItem> toDoItemData = toDoItemRepository.findById(id);
        if (toDoItemData.isPresent()) {
            ToDoItem toDoItem = toDoItemData.get();
            toDoItem.setID(id);
            toDoItem.setCreation_ts(td.getCreation_ts());
            toDoItem.setDescription(td.getDescription());
            toDoItem.setDone(td.isDone());
            toDoItem.setStorypoints(td.getStorypoints());
            toDoItem.setResponsable(td.getResponsable());
            return toDoItemRepository.save(toDoItem);
        } else {
            return null;
        }
    }

    public ToDoItem updateToDoItemText(int id, String newText) {
        Optional<ToDoItem> toDoItemData = toDoItemRepository.findById(id);
        if (toDoItemData.isPresent()) {
            ToDoItem toDoItem = toDoItemData.get();
            toDoItem.setDescription(newText); // Actualiza solo la descripción
            return toDoItemRepository.save(toDoItem); // Guarda los cambios
        } else {
            return null; // Si no se encuentra el item, regresa null
        }
    }
}
