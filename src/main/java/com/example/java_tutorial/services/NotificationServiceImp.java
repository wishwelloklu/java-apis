package com.example.java_tutorial.services;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.server.ResponseStatusException;

import com.example.java_tutorial.dto.request.MarkAsReadRequest;
import com.example.java_tutorial.models.NotificationModel;
import com.example.java_tutorial.models.UserModel;
import com.example.java_tutorial.repository.NotificationResipository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotificationServiceImp implements NotificationService {
    private final AuthService authService;
    private final NotificationResipository notificationResipository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Async
    public void sendNotification(String token, String title, String body, @Null Object data) {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message.Builder messageBuilder = Message.builder()
                .setToken(token)
                .setNotification(notification);

        if (data != null) {
            try {
                String jsonString = objectMapper.writeValueAsString(data);
                messageBuilder.putData("data", jsonString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            FirebaseMessaging.getInstance()
                    .send(messageBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<NotificationModel> fetchNotification(String email) {
        UserModel user = authService.getUserByEmail(email);
        ArrayList<NotificationModel> notifications = notificationResipository.getAllByUser(user.getId().toString())
                .stream()
                .collect(Collectors.toCollection(ArrayList::new));
        return notifications;
    }

    @Override
    public boolean deleteNotification(String notificationId) {
        NotificationModel notification = notificationResipository.findById(notificationId).orElse(null);
        if (notification == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found");
        }
        notificationResipository.delete(notification);
        return true;
    }

    @Override
    public boolean markNotificationAsRead(MarkAsReadRequest request) {
        NotificationModel notification = notificationResipository.findById(request.getNotificationId()).orElse(null);
        if (notification == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found");
        }
        notification.setRead(request.isRead());
        notificationResipository.save(notification);
        return true;
    }

}
