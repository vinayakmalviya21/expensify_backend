package com.expense.demo.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Expense {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;
    private String category;
    private LocalDate date;
    private String description;

    @ManyToOne
    private User user;
}