package com.example.java_tutorial.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.java_tutorial.models.UserModel;
import com.example.java_tutorial.models.NotificationModel;
import java.util.List;

public interface NotificationResipository extends JpaRepository<NotificationModel, String> {
    List<NotificationModel> getAllByUser(UserModel user);
}
