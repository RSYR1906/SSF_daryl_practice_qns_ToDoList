package com.sg.iss.nus.SSF_practice_workshop;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;

import com.sg.iss.nus.SSF_practice_workshop.constant.constants;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;

@SpringBootApplication
public class SsfPracticeWorkshopApplication implements CommandLineRunner {

    @Autowired
    @Qualifier(constants.template02)
    RedisTemplate<String, Object> template;

    public static void main(String[] args) {
        SpringApplication.run(SsfPracticeWorkshopApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Map<String, Object> toDoMap = new HashMap<>();
        FileInputStream fis = new FileInputStream(constants.filePath);
        JsonReader jReader = Json.createReader(fis);
        JsonArray jArray = jReader.readArray();

        for (JsonValue jsonValue : jArray) {
            JsonObject jsonObject = jsonValue.asJsonObject();
            toDoMap.put(jsonObject.getString("id"), jsonObject.toString());
            // System.out.println(jsonObject.toString());
        }
        template.opsForHash().putAll(constants.REDIS_KEY, toDoMap);

    }
}