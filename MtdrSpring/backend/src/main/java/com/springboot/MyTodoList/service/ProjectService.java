package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.model.Project;  // Modelo Project
import com.springboot.MyTodoList.repository.ProjectRepository;  // Repositorio ProjectRepository

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    // Servicio para encontrar todos los proyectos
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    // Encontrar un proyecto por su ID
    public Project getProjectById(int id) {
        return projectRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("Project not found with id " + id)
        );
    }

    // Añadir un proyecto
    public Project addProject(Project project) {
        try {
            return projectRepository.save(project);  // Guardar el proyecto
        } catch (Exception e) {
            throw new RuntimeException("Error adding project", e);
        }
    }

    // Borrar un proyecto
    public boolean deleteProject(int id) {
        try {
            projectRepository.deleteById(id);  // Eliminar el proyecto por su ID
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting project", e);
        }
    }

    // Actualizar un proyecto
    public Project updateProject(int id, Project project) {
        return projectRepository.findById(id).map(existingProject -> {
            existingProject.setPendingTasks(project.getPendingTasks());  // Actualización correcta
            existingProject.setCompletedTasks(project.getCompletedTasks());  // Actualización correcta
            existingProject.setProgress(project.getProgress());  // Actualización correcta
            existingProject.setTotalProductivity(project.getTotalProductivity());  // Actualización correcta
            return projectRepository.save(existingProject);  // Guardar proyecto actualizado
        }).orElseThrow(() -> new RuntimeException("Project not found with id " + id));
    }

    /* // Calcular la productividad total de un proyecto
    public double calculateProductivity(Project project) {
        // Lógica para calcular la productividad del proyecto
        // Ejemplo de cálculo: (Total de tareas completadas / Total de tareas) * Productividad total
        double totalTasks = project.getPendingTasks() + project.getCompletedTasks();
        if (totalTasks == 0) {
            return 0;  // Evitar división por cero
        }
        return (project.getCompletedTasks() / totalTasks) * project.getTotalProductivity();
    } */
}
