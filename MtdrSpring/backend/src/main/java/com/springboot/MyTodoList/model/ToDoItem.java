package com.springboot.MyTodoList.model;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder.In;

import java.time.OffsetDateTime;

/*
    representation of the TODOITEM table that exists already
    in the autonomous database
 */
@Entity
@Table(name = "TODOITEM")
public class ToDoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int ID;
    
    @Column(name = "DESCRIPTION")
    String description;
    
    @Column(name = "CREATION_TS")
    OffsetDateTime creation_ts;
    
    @Column(name = "done")
    boolean done;
    
    @Column(name = "STORYPOINTS")
    int storypoints;
    
    @Column(name = "RESPONSABLE")
    String responsable;

    public ToDoItem() {
    }

    public ToDoItem(int ID, String description, OffsetDateTime creation_ts, boolean done, int storypoints, String responsable) {

    @Column(name = "DESCRIPTION")
    String description;

    @Column(name = "CREATION_TS")
    OffsetDateTime creation_ts;
  
    @Column(name = "ESTADO")
    String estado;
  
    @Column(name = "DONE")
    Boolean done;

    @Column(name = "STORYPOINTS") 
    Integer storyPoints;

    @Column(name = "PRIORITY")
    String priority;

    @Column(name = "ASSIGNED")
    String assigned;
    
    @Column(name = "ESTIMATED_HOURS")
    Integer estimated_hours;

    @Column(name = "EXPIRATION_TS")
    OffsetDateTime expiration_ts;

    public ToDoItem(int ID, String description, OffsetDateTime creation_ts, Boolean done, Integer storyPoints, String priority, String assigned, Integer estimated_hours, OffsetDateTime expiration_ts, String estado) {
        this.ID = ID;
        this.description = description;
        this.creation_ts = creation_ts;
        this.done = done;
        this.storypoints = storypoints;
        this.responsable = responsable;
        this.estado = estado;
        this.storyPoints = storyPoints; 
        this.priority = priority;
        this.assigned = assigned;
        this.estimated_hours = estimated_hours;
        this.expiration_ts = expiration_ts;
    }

    public ToDoItem() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OffsetDateTime getCreation_ts() {
        return creation_ts;
    }

    public void setCreation_ts(OffsetDateTime creation_ts) {
        this.creation_ts = creation_ts;
    }

    public Boolean isDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getStoryPoints() {
        return storyPoints;
    }

    public void setStoryPoints(Integer storyPoints) {
        this.storyPoints = storyPoints;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getAssigned() {
        return assigned;
    }

    public void setAssigned(String assigned) {
        this.assigned = assigned;
    }

    public Integer getEstimated_Hours() {
        return estimated_hours;
    }

    public void setEstimated_Hours(Integer estimated_hours) {
        this.estimated_hours = estimated_hours;
    }

    public OffsetDateTime getExpiration_TS() {
        return expiration_ts;
    }

    public void setExpiration_TS(OffsetDateTime expiration_ts) {
        this.expiration_ts = expiration_ts;
    }

    @Override
    public String toString() {
        return "ToDoItem{" +
                "ID=" + ID +
                ", description='" + description + '\'' +
                ", creation_ts=" + creation_ts +
                ", done=" + done +
                ", storypoints=" + storypoints +
                ", responsable=" + responsable +
                ", estado=" + estado +
                ", storyPoints=" + storyPoints +
                ", priority='" + priority + '\'' +
                ", assigned='" + assigned + '\'' + 
                ", estimated_hours='" + estimated_hours +
                ", expiration_ts='" + expiration_ts +
                '}';
    }
}
