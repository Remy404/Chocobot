package com.springboot.MyTodoList.controller;

import com.springboot.MyTodoList.model.User;  // Cambiado de Users a User
import com.springboot.MyTodoList.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users") // Base path para todos los endpoints
public class UserController {

    @Autowired
    private UserService userService;

    // Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        try {
            User user = userService.getUserById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Método para obtener todos los nombres de los usuarios
    @GetMapping("/users/names")
    public List<String> getAllUserNames() {
        return userService.getAllUserNames();
    }

    // Crear un nuevo usuario
    @PostMapping
    public ResponseEntity<Void> addUser(@RequestBody User user) {  
        try {
            User newUser = userService.addUser(user); 
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("location", "/users/" + newUser.getId());
            responseHeaders.set("Access-Control-Expose-Headers", "location");
            return ResponseEntity.ok().headers(responseHeaders).build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Eliminar un usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Calcular productividad de un usuario
    @GetMapping("/{id}/productivity")
    public ResponseEntity<Double> getUserProductivity(@PathVariable int id) {
        try {
            User user = userService.getUserById(id);  
            double productivity = userService.calculateProductivity(user);
            return new ResponseEntity<>(productivity, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
