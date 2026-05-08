package com.expense.demo.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ExpenseRequestDTO {
    private double amount;
    private String category;
    private LocalDate date;
    private String description;
}
