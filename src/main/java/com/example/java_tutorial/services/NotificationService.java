package com.example.java_tutorial.services;

import java.util.ArrayList;

import com.example.java_tutorial.dto.request.MarkAsReadRequest;
import com.example.java_tutorial.models.NotificationModel;

import jakarta.validation.constraints.Null;

public interface NotificationService {
    void sendNotification(String token, String title, String body, @Null Object data);
    
    ArrayList<NotificationModel> fetchNotification(String email);
   
    boolean deleteNotification(String notificationId);

    boolean markNotificationAsRead(MarkAsReadRequest request);
}
