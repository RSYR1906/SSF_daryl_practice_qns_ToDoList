package com.sg.iss.nus.SSF_practice_workshop.controller;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sg.iss.nus.SSF_practice_workshop.model.Todo;
import com.sg.iss.nus.SSF_practice_workshop.model.User;
import com.sg.iss.nus.SSF_practice_workshop.service.TodoService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("")
public class ToDoController {

    @Autowired
    TodoService todoService;

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @GetMapping("/todos")
    public String showToDos(@RequestParam(value = "status", required = false) String currentStatus, HttpSession session,
            Model model) {
        // Check if session is valid
        if (session.getAttribute("fullName") == null || session.getAttribute("age") == null) {
            return "redirect:/refused"; // Redirect to error page if session is invalid
        }
        Map<String, Todo> todoList = todoService.getTodosByStatus(currentStatus == null ? "" : currentStatus);
        model.addAttribute("todos", todoList);
        model.addAttribute("selectedStatus", currentStatus);
        model.addAttribute("session", session);
        return "listing";
    }

    @GetMapping("/todos/add")
    public String showAddToDoPage(Model model) {
        model.addAttribute("todo", new Todo());
        return "add";
    }

    @GetMapping("/todos/{id}/edit")
    public String showEditPage(@PathVariable String id, Model model) {
        Todo todo = todoService.getTodoById(id);
        if (todo == null) {
            model.addAttribute("errorMessage", "Todo not found.");
            return "error";
        }
        model.addAttribute("todo", todo);
        return "edit";
    }

    @GetMapping("/todos/{id}/delete")
    public String deleteToDo(@PathVariable String id) {
        todoService.deleteTodoById(id);
        return "redirect:/todos";
    }

    @PostMapping("/todos/add")
    public String processToDo(@Valid @ModelAttribute Todo todo, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add"; // Show form again with errors
        }
        todo.setId(UUID.randomUUID().toString());
        todo.setCreatedAt(new Date());
        todo.setUpdatedAt(new Date());
        todoService.addTodo(todo);
        return "redirect:/todos";
    }

    @PostMapping("/todos/{id}/edit")
    public String processEdit(@PathVariable String id, @Valid @ModelAttribute Todo todo, BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("todo", todo);
            return "edit";
        }
        todo.setUpdatedAt(new Date()); // Update only the `updatedAt` field
        todoService.updateTodo(id, todo);
        return "redirect:/todos";
    }

    @PostMapping("/login")
    public String processLogin(@Valid @ModelAttribute User user, BindingResult result, HttpSession session) {
        if (result.hasErrors()) {
            return "login"; // Show the form with validation errors
        }
        if (user.getAge() < 10) {
            return "underage";
        }
        session.setAttribute("fullName", user.getFullName());
        session.setAttribute("age", user.getAge());
        return "redirect:/todos";
    }
}
