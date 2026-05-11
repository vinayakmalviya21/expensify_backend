package com.expense.demo.controller;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.expense.demo.dto.*;
import com.expense.demo.entity.User;
import com.expense.demo.repository.UserRepository;
import com.expense.demo.service.ExpenseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService service;
    private final UserRepository userRepository;

    // =========================
    // GET ALL EXPENSES
    // =========================

    @GetMapping(
            produces = "application/json"
    )
    public ResponseEntity<Page<ExpenseResponseDTO>>
    getAll(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(required = false)
            String category,

            @RequestParam(required = false)
            LocalDate start,

            @RequestParam(required = false)
            LocalDate end,

            Authentication authentication
    ) {

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return ResponseEntity.ok(

                service.getAll(
                        page,
                        size,
                        category,
                        start,
                        end,
                        user.getId()
                )

        );
    }

    // =========================
    // ADD EXPENSE
    // =========================

    @PostMapping
    public ExpenseResponseDTO addExpense(

            @RequestBody
            ExpenseRequestDTO dto,

            Authentication authentication
    ) {

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return service.addExpense(
                dto,
                user.getId()
        );
    }

    // =========================
    // UPDATE
    // =========================

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO>
    updateExpense(

            @PathVariable Long id,

            @RequestBody ExpenseRequestDTO dto
    ) {

        return ResponseEntity.ok(

                service.updateExpense(id, dto)

        );
    }

    // =========================
    // DELETE
    // =========================

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>>
    deleteExpense(
            @PathVariable Long id
    ) {

        service.deleteExpense(id);

        return ResponseEntity.ok(

                Map.of(
                        "message",
                        "Expense deleted successfully"
                )

        );
    }
}