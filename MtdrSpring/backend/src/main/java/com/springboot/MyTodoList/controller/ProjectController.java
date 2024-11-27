package com.springboot.MyTodoList.controller;

import com.springboot.MyTodoList.model.Project;  // Modelo Project
import com.springboot.MyTodoList.service.ProjectService;  // Servicio ProjectService

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects") // Base path para todos los endpoints
public class ProjectController {

    @Autowired
    private ProjectService projectService;  // Inyección de dependencias del servicio de proyectos

    // Obtener todos los proyectos
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    // Obtener proyecto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable int id) {
        try {
            Project project = projectService.getProjectById(id);
            return new ResponseEntity<>(project, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Crear un nuevo proyecto
    @PostMapping
    public ResponseEntity<Void> addProject(@RequestBody Project project) {
        try {
            Project newProject = projectService.addProject(project);  // Llamada al servicio para agregar el proyecto
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("location", "/projects/" + newProject.getId());
            responseHeaders.set("Access-Control-Expose-Headers", "location");
            return ResponseEntity.ok().headers(responseHeaders).build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Eliminar un proyecto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable int id) {
        try {
            projectService.deleteProject(id);  // Llamada al servicio para eliminar el proyecto
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
