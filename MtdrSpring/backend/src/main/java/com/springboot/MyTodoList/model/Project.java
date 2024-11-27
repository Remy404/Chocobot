package com.springboot.MyTodoList.model;

import javax.persistence.*;

@Entity // Marca esta clase como una entidad JPA.
@Table(name = "PROJECTS") // Vincula esta clase con la tabla "projects".
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;  // ID del proyecto.

    @Column(name = "ALL_TASKS") // Número de tareas pendientes.
    private Integer allTasks;

    @Column(name = "PENDING_TASKS") // Número de tareas pendientes.
    private Integer pendingTasks;

    @Column(name = "COMPLETED_TASKS") // Número de tareas completadas.
    private Integer completedTasks;

    @Column(name = "PROGRESS") // El progreso del proyecto (porcentaje).
    private Double progress;

    @Column(name = "TOTAL_PRODUCTIVITY") // Productividad total del proyecto.
    private Double totalProductivity;

    // Constructor sin argumentos (requerido por JPA)
    public Project() {}

    // Constructor con todos los campos
    public Project(Integer pendingTasks,Integer allTasks, Integer completedTasks, Double progress, Double totalProductivity) {
        this.pendingTasks = pendingTasks;
        this.allTasks = allTasks;
        this.completedTasks = completedTasks;
        this.progress = progress;
        this.totalProductivity = totalProductivity;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAllTasks(){
        return allTasks;
    }

    public void setAllTasks(){
        this.allTasks = allTasks;
    }

    public Integer getPendingTasks() {
        return pendingTasks;
    }

    public void setPendingTasks(Integer pendingTasks) {
        this.pendingTasks = pendingTasks;
    }

    public Integer getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(Integer completedTasks) {
        this.completedTasks = completedTasks;
    }

    public Double getProgress() {
        return progress;
    }

    public void setProgress(Double progress) {
        this.progress = progress;
    }

    public Double getTotalProductivity() {
        return totalProductivity;
    }

    public void setTotalProductivity(Double totalProductivity) {
        this.totalProductivity = totalProductivity;
    }

    // Método toString para depuración
    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", allTasks=" + allTasks +
                ", pendingTasks=" + pendingTasks +
                ", completedTasks=" + completedTasks +
                ", progress=" + progress +
                ", totalProductivity=" + totalProductivity +
                '}';
    }
}
