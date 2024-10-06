package com.example.todo.controller;

import com.example.todo.model.Todo;
import com.example.todo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/todos")  // Base URL for all API endpoints
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;

    // Retrieve all todos
    @GetMapping
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    // Retrieve a single todo by id
    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        Optional<Todo> todo = todoRepository.findById(id);
        return todo.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new todo
    @PostMapping
    public Todo createTodo(@RequestBody Todo todo) {
        return todoRepository.save(todo);
    }

    // Update an existing todo by id
    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo updatedTodo) {
        Optional<Todo> existingTodo = todoRepository.findById(id);

        if (existingTodo.isPresent()) {
            Todo todo = existingTodo.get();
            todo.setTitle(updatedTodo.getTitle());
            todo.setCompleted(updatedTodo.isCompleted());
            Todo savedTodo = todoRepository.save(todo);
            return ResponseEntity.ok(savedTodo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a todo by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        Optional<Todo> todo = todoRepository.findById(id);

        if (todo.isPresent()) {
            todoRepository.delete(todo.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
