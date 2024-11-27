package com.springboot.MyTodoList.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

import com.springboot.MyTodoList.model.User;  // Cambiado a User en vez de Users
import com.springboot.MyTodoList.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Servicio para encontrar todos los usuarios
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<String> getAllUserNames() {
        List<User> users = userRepository.findAll();
        return users.stream().map(User::getName).collect(Collectors.toList());  // Extrae solo los nombres
    }

    // Encontrar un usuario por su ID
    public User getUserById(int id) {
        return userRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("User not found with id " + id)
        );
    }

    public String getUsernameById(int id) {
        Optional<User> userData = userRepository.findById(id);  // Cambiado de Users a User
        return userData.map(User::getName).orElse("User not found");  // Cambiado de Users a User
    }

    // Añadir un usuario
    public User addUser(User user) {  // Cambiado de Users a User
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error adding user", e);
        }
    }

    // Borrar un usuario
    public boolean deleteUser(int id) {
        try {
            userRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting user", e);
        }
    }

    // Actualizar un usuario
    public User updateUser(int id, User user) {  // Cambiado de Users a User
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setName(user.getName());  // Cambiado de setUsername() a setName()
            existingUser.setEmail(user.getEmail());  // Cambiado de setFullName() a setEmail()
            existingUser.setAssignedTasks(user.getAssignedTasks());  // Actualización correcta
            existingUser.setDoneTasks(user.getDoneTasks());  // Actualización correcta
            existingUser.setCompletedSP(user.getCompletedSP());  // Actualización correcta
            existingUser.setProductivity(user.getProductivity());  // Actualización correcta
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    public double calculateProductivity(User user) {
    // Lógica para calcular la productividad del usuario
    return (user.getAssignedTasks() - user.getDoneTasks()) * user.getProductivity();
}
}
