package com.example.java_tutorial.models;

import java.sql.Date;

import com.example.java_tutorial.enums.PackageStatusEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "packages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PackageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotNull
    public String mineralType;

    @NotNull
    public double quantity;

    @NotNull
    public Date mineDate;

    @NotNull
    public String location;

    @NotNull
    public String grade;

    @NotNull
    public String minerId;

    @NotNull
    public String minerName;

    @NotNull
    public Date createdAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    public PackageStatusEnum status;

    public String notes;
}
