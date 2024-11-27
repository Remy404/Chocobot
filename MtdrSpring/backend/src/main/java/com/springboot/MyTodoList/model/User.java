package com.springboot.MyTodoList.model;

import javax.persistence.*;

@Entity // Marca esta clase como una entidad JPA.
@Table(name = "USERS") // Vincula esta clase con la tabla "users".
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;  // Cambié el tipo a Integer y usé minúscula

    @Column(name = "NAME") // El nombre no puede ser nulo.
    private String name;

    @Column(name = "EMAIL") // El email debe ser único y no nulo.
    private String email;

    @Column(name = "ASSIGNED_TASKS") // Asocia a la columna "assigned_tasks".
    private Integer assignedTasks;

    @Column(name = "DONE_TASKS") // Asocia a la columna "done_tasks".
    private Integer doneTasks;

    @Column(name = "COMPLETED_SP") // Asocia a la columna "completed_sp".
    private Integer completedSP;

    @Column(name = "PRODUCTIVITY") // Asocia a la columna "productivity".
    private Double productivity;

    // Constructor sin argumentos (requerido por JPA)
    public User() {}

    // Constructor con todos los campos
    public User(String name, String email, Integer assignedTasks, Integer doneTasks, Integer completedSP, Double productivity) {
        this.name = name;
        this.email = email;
        this.assignedTasks = assignedTasks;
        this.doneTasks = doneTasks;
        this.completedSP = completedSP;
        this.productivity = productivity;
    }

    // Getters y Setters
     public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAssignedTasks() {
        return assignedTasks;
    }

    public void setAssignedTasks(Integer assignedTasks) {
        this.assignedTasks = assignedTasks;
    }

    public Integer getDoneTasks() {
        return doneTasks;
    }

    public void setDoneTasks(Integer doneTasks) {
        this.doneTasks = doneTasks;
    }

    public Integer getCompletedSP() {
        return completedSP;
    }

    public void setCompletedSP(Integer completedSP) {
        this.completedSP = completedSP;
    }

    public Double getProductivity() {
        return productivity;
    }

    public void setProductivity(Double productivity) {
        this.productivity = productivity;
    }

    // Método toString para depuración
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", assignedTasks=" + assignedTasks +
                ", doneTasks=" + doneTasks +
                ", completedSP=" + completedSP +
                ", productivity=" + productivity +
                '}';
    }
}