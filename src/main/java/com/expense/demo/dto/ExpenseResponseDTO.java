package com.expense.demo.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExpenseResponseDTO {
    private Long id;
    private double amount;
    private String category;
    private LocalDate date;
    private String description;
}