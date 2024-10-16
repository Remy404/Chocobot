package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.repository.ToDoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;


@Service
public class ToDoItemService {

    @Autowired
    private ToDoItemRepository toDoItemRepository;
    public List<ToDoItem> findAll(){
        List<ToDoItem> todoItems = toDoItemRepository.findAll();
        return todoItems;
    }
    public ResponseEntity<ToDoItem> getItemById(int id){
        Optional<ToDoItem> todoData = toDoItemRepository.findById(id);
        if (todoData.isPresent()){
            return new ResponseEntity<>(todoData.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    public ToDoItem addToDoItem(ToDoItem toDoItem){
        if (toDoItem.getEstado() == null || toDoItem.getEstado().isEmpty()) {
            toDoItem.setEstado("To Do");  // Valor por defecto si no se establece un estado
        }
        validateEstado(toDoItem.getEstado());
        return toDoItemRepository.save(toDoItem);
    }

    public boolean deleteToDoItem(int id){
        try{
            toDoItemRepository.deleteById(id);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    public ToDoItem updateToDoItem(int id, ToDoItem td) {
        Optional<ToDoItem> toDoItemData = toDoItemRepository.findById(id);
        if(toDoItemData.isPresent()){
            ToDoItem toDoItem = toDoItemData.get();
            toDoItem.setID(id);
            toDoItem.setCreation_ts(td.getCreation_ts());
            toDoItem.setDescription(td.getDescription());
            toDoItem.setDone(td.isDone());
            
            if (td.getEstado() == null || td.getEstado().isEmpty()) {
                toDoItem.setEstado("To Do");  // Valor por defecto si no se establece un estado
            } else {
                validateEstado(td.getEstado());
                toDoItem.setEstado(td.getEstado());  // Actualiza el campo estado
            }
    
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

    private void validateEstado(String estado) {
        List<String> validEstados = Arrays.asList("To Do", "In Progress", "Completed");
        if (!validEstados.contains(estado)) {
            throw new IllegalArgumentException("Estado inválido: " + estado);
        }
    }
    
    
    

}
