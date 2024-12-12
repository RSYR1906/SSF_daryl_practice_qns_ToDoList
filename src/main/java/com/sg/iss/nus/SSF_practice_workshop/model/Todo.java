package com.sg.iss.nus.SSF_practice_workshop.model;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Todo {

    @Size(max = 50, message = "ID must not exceed 50 characters.")
    private String id;

    @NotBlank(message = "Name is required.")
    @Size(min = 10, max = 50, message = "Name must be between 10 and 50 characters.")
    private String name;

    @Size(max = 255, message = "Description must not exceed 255 characters.")
    private String description;

    @NotNull(message = "Due date is required.")
    @FutureOrPresent(message = "Due date must be today or in the future.")
    @DateTimeFormat(pattern = "MM-dd-yyyy") // Matches the format of HTML input
    private Date dueDate;

    private String priority;

    private String status;

    @DateTimeFormat(pattern = "MM-dd-yyyy") // Matches the format of HTML input
    private Date createdAt;

    @DateTimeFormat(pattern = "MM-dd-yyyy") // Matches the format of HTML input
    private Date updatedAt;

    public Todo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;

    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return id + "," + name + "," + description + "," + dueDate
                + "," + priority + "," + status + "," + createdAt + ","
                + updatedAt;
    }

    public JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();

        builder.add("id", this.id != null ? this.id : "")
                .add("name", this.name != null ? this.name : "")
                .add("description", this.description != null ? this.description : "")
                .add("due_date", this.dueDate != null ? this.dueDate.getTime() / 1000 : 0)
                .add("priority_level", this.priority != null ? this.priority : "")
                .add("status", this.status != null ? this.status : "")
                .add("created_at", this.createdAt != null ? this.createdAt.getTime() / 1000 : 0)
                .add("updated_at", this.updatedAt != null ? this.updatedAt.getTime() / 1000 : 0);

        return builder.build();
    }

    public static Todo toToDo(String json) {
        Todo todo = new Todo();

        // Convert the string to JSON
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject jsonObj = reader.readObject();

        todo.setId(jsonObj.getString("id"));
        todo.setName(jsonObj.getString("name"));
        todo.setDescription(jsonObj.getString("description"));
        todo.setPriority(jsonObj.getString("priority_level"));
        todo.setStatus(jsonObj.getString("status"));

        try {
            // Format used in the JSON input
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, MM/dd/yyyy");

            // Parse date strings into Date objects
            todo.setDueDate(sdf.parse(jsonObj.getString("due_date")));
            todo.setCreatedAt(sdf.parse(jsonObj.getString("created_at")));
            todo.setUpdatedAt(sdf.parse(jsonObj.getString("updated_at")));

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return todo;
    }
}