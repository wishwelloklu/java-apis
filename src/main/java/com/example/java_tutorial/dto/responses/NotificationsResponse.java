package com.example.java_tutorial.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationsResponse {
    private String id;
    private String title;
    private String body;
    private String type;
    private String status;
    private String createdAt;
    private String updatedAt;
}
