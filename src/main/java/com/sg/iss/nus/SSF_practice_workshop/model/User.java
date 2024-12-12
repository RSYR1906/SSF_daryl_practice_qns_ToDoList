package com.sg.iss.nus.SSF_practice_workshop.model;

import jakarta.validation.constraints.NotBlank;

public class User {

    @NotBlank(message = "Name is required.")
    private String fullName;

    private Integer age;

    public User() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

}
