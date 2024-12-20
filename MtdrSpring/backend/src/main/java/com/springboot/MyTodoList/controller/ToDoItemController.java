package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.service.ToDoItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.springboot.MyTodoList.repository.ToDoItemRepository;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
public class ToDoItemController {

    private static final Logger logger = LoggerFactory.getLogger(ToDoItemBotController.class);

    private ToDoItemRepository toDoItemRepository;
    
    @Autowired
    private ToDoItemService toDoItemService;
    //@CrossOrigin
    @GetMapping(value = "/todolist")
    public List<ToDoItem> getAllToDoItems(){
        return toDoItemService.findAll();
    }
    //@CrossOrigin
    @GetMapping(value = "/todolist/{id}")
    public ResponseEntity<ToDoItem> getToDoItemById(@PathVariable int id){
        try{
            ResponseEntity<ToDoItem> responseEntity = toDoItemService.getItemById(id);
            return new ResponseEntity<ToDoItem>(responseEntity.getBody(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    //@CrossOrigin
    @PostMapping(value = "/todolist/add")
    public ResponseEntity addToDoItem(@RequestBody ToDoItem todoItem) throws Exception{
        logger.info("addToDoItem ");
        try {
            ToDoItem td = toDoItemService.addToDoItem(todoItem);
            logger.info("addToDoItem 1");
            
            return new ResponseEntity<>(td, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            // Capturar estado inválido
            return new ResponseEntity<>("Estado inválido: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //@CrossOrigin
    @PutMapping(value = "todolist/{id}")
    public ResponseEntity updateToDoItem(@RequestBody ToDoItem toDoItem, @PathVariable int id) {
        try {
            ToDoItem toDoItem1 = toDoItemService.updateToDoItem(id, toDoItem);
            System.out.println(toDoItem1.toString());
            return new ResponseEntity<>(toDoItem1, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            // Capturar estado inválido
            return new ResponseEntity<>("Estado inválido: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    //@CrossOrigin
    @DeleteMapping(value = "todolist/{id}")
    public ResponseEntity<Boolean> deleteToDoItem(@PathVariable("id") int id){
        Boolean flag = false;
        try{
            flag = toDoItemService.deleteToDoItem(id);
            return new ResponseEntity<>(flag, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(flag,HttpStatus.NOT_FOUND);
        }
    }
    
    @PatchMapping(value = "/todolist/{id}/description")
    public ResponseEntity updateToDoItemDescription(@PathVariable int id, @RequestBody String newDescription) {
    try {
        ToDoItem updatedItem = toDoItemService.updateToDoItemText(id, newDescription); // Aquí llamas al servicio
        if (updatedItem != null) {
            return new ResponseEntity<>(updatedItem, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    } catch (Exception e) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/todolist/{id}/done")
    public ResponseEntity<ToDoItem> updateToDoItemDoneStatus(@PathVariable int id, @RequestBody Map<String, Boolean> request) {
        try {
            if (!request.containsKey("done")) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            boolean isDone = request.get("done");
            ToDoItem updatedItem = toDoItemService.updateDoneStatus(id, isDone);

            if (updatedItem != null) {
                return new ResponseEntity<>(updatedItem, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Error updating 'done' status for ToDoItem with id " + id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/todolist/assigned/{assigned}")
    public ResponseEntity<List<ToDoItem>> getItemsByAssigned(@PathVariable String assigned) {
    List<ToDoItem> items = toDoItemRepository.findByAssigned(assigned);
        if (items.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
}
