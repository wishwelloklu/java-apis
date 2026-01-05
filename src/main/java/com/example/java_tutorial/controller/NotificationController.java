package com.example.java_tutorial.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.java_tutorial.dto.request.MarkAsReadRequest;
import com.example.java_tutorial.dto.responses.ApiResponseDto;
import com.example.java_tutorial.models.NotificationModel;
import com.example.java_tutorial.services.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/")
    public ResponseEntity<ApiResponseDto<List<NotificationModel>>> getNotifications() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<NotificationModel> notifications = notificationService.fetchNotification(email);
        return ResponseEntity.ok(new ApiResponseDto<>(true, "Notifications fetched successfully", notifications));
    }

    @PatchMapping("/mark-as-read")
    public ResponseEntity<ApiResponseDto<NotificationModel>> markNotificationAsRead(@RequestBody MarkAsReadRequest request) {
        notificationService.markNotificationAsRead(request);
        return ResponseEntity.ok(new ApiResponseDto<>(true, "Notification marked as read successfully", null));
    }

    @DeleteMapping("/delete/{notificationId}")
    public ResponseEntity<ApiResponseDto<NotificationModel>> deleteNotification(@PathVariable String notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok(new ApiResponseDto<>(true, "Notification deleted successfully", null));
    }
}
