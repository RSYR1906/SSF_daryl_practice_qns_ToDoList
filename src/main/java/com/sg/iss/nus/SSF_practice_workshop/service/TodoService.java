package com.sg.iss.nus.SSF_practice_workshop.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.sg.iss.nus.SSF_practice_workshop.constant.constants;
import com.sg.iss.nus.SSF_practice_workshop.model.Todo;

@Service
public class TodoService {

    @Autowired
    @Qualifier(constants.template02)
    RedisTemplate<String, Object> template;

    // Retrieve all todos as a Map<String, Todo>
    public Map<String, Todo> getAllTodos() {
        // Fetch all entries from Redis hash
        Map<Object, Object> todoList = template.opsForHash().entries(constants.REDIS_KEY);

        // Create a map to store Todo objects
        Map<String, Todo> todos = new HashMap<>();

        for (Map.Entry<Object, Object> entry : todoList.entrySet()) {
            String key = entry.getKey().toString();
            Object value = entry.getValue();

            // Deserialize each value into a Todo object
            if (value instanceof String) { // Redis stores JSON as Strings
                Todo todo = Todo.toToDo((String) value); // Convert JSON string to Todo object
                todos.put(key, todo); // Add to the map
            }
        }

        return todos;
    }

    // Add a new Todo
    public void addTodo(Todo todo) {
        // Convert Todo object to JSON and save it in Redis
        String todoJson = todo.toJson().toString();
        template.opsForHash().put(constants.REDIS_KEY, todo.getId(), todoJson);
    }

    // Update an existing Todo
    public void updateTodo(String id, Todo updatedTodo) {
        // Convert updated Todo object to JSON and save it in Redis
        String updatedTodoJson = updatedTodo.toJson().toString();
        template.opsForHash().put(constants.REDIS_KEY, id, updatedTodoJson);
    }

    // Delete a Todo by ID
    public void deleteTodoById(String id) {
        template.opsForHash().delete(constants.REDIS_KEY, id);
    }

    // Get a single Todo by ID
    public Todo getTodoById(String id) {
        Object todoJson = template.opsForHash().get(constants.REDIS_KEY, id);
        if (todoJson instanceof String) {
            return Todo.toToDo((String) todoJson); // Convert JSON string to Todo object
        }
        return null; // Return null if not found
    }

    public Map<String, Todo> getTodosByStatus(String status) {
        Map<Object, Object> allTodos = template.opsForHash().entries(constants.REDIS_KEY);
        Map<String, Todo> filteredTodos = new HashMap<>();

        for (Map.Entry<Object, Object> entry : allTodos.entrySet()) {
            Todo todo = Todo.toToDo(entry.getValue().toString());
            if (status == null || status.isEmpty() || status.equalsIgnoreCase(todo.getStatus())) {
                filteredTodos.put(entry.getKey().toString(), todo);
            }
        }
        return filteredTodos;
    }

    public Boolean checkAgeAbove10(Integer age) {

        if (age < 10) {
            return false;
        }
        else {
            return true;
        }
    }
}