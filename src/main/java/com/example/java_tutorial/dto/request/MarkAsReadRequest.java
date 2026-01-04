package com.example.java_tutorial.dto.request;

import lombok.Getter;

@Getter
public class MarkAsReadRequest {
    private String notificationId;
    private boolean read;
}
