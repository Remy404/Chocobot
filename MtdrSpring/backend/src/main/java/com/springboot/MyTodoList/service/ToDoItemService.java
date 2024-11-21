package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.repository.ToDoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Arrays;
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
        return todoData.map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                       .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ToDoItem addToDoItem(ToDoItem toDoItem) {
        if (toDoItem.getEstado() == null || toDoItem.getEstado().isEmpty()) {
            toDoItem.setEstado("To Do");  // Valor por defecto si no se establece un estado
        }
        validateEstado(toDoItem.getEstado());

        return toDoItemRepository.save(toDoItem);
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
            // No se necesita establecer ID manualmente ya que es autogenerado
            toDoItem.setCreation_ts(td.getCreation_ts());
            toDoItem.setDescription(td.getDescription());
            toDoItem.setDone(td.isDone());
            toDoItem.setStoryPoints(td.getStoryPoints());
            toDoItem.setPriority(td.getPriority());
            toDoItem.setAssigned(td.getAssigned());
            toDoItem.setEstimated_Hours(td.getEstimated_Hours());
            toDoItem.setEstado(td.getEstado());
            toDoItem.setExpiration_TS(td.getExpiration_TS());

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

    public ToDoItem updateDoneStatus(int id, boolean isDone) {
        Optional<ToDoItem> toDoItemData = toDoItemRepository.findById(id);
        if (toDoItemData.isPresent()) {
            ToDoItem toDoItem = toDoItemData.get();
            toDoItem.setDone(isDone);

            if (isDone) {
                // Set 'finished_ts' to the current timestamp if done is true
                toDoItem.setFinished_TS(OffsetDateTime.now());
            } else {
                // Reset 'finished_ts' to null if done is false
                toDoItem.setFinished_TS(null);
            }

            return toDoItemRepository.save(toDoItem); // Save the updated item
        } else {
            return null; // Return null if the item is not found
        }
    }

    // Nuevo método para buscar tareas por nombre asignado
    public List<ToDoItem> findByAssignedName(String assignedName) {
        return toDoItemRepository.findByAssigned(assignedName);
    }

    private void validateEstado(String estado) {
        List<String> validEstados = Arrays.asList("To Do", "In Progress", "Completed");
        if (!validEstados.contains(estado)) {
            throw new IllegalArgumentException("Estado inválido: " + estado);
        }
    }
}