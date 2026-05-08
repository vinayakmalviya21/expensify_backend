package com.expense.demo.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import com.expense.demo.entity.Expense;
import com.expense.demo.entity.User;
import com.expense.demo.repository.ExpenseRepository;
import com.expense.demo.repository.UserRepository;
import com.expense.demo.mapper.ExpenseMapper;
import com.expense.demo.mapper.ExpenseSpecification;
import com.expense.demo.dto.ExpenseRequestDTO;
import com.expense.demo.dto.ExpenseResponseDTO;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository repo;
    private final ExpenseMapper mapper;
    private final UserRepository userRepository;

    public Page<ExpenseResponseDTO> getAll(
            int page, int size,
            String category,
            LocalDate start,
            LocalDate end,
            Long userId) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());

        Specification<Expense> spec = ExpenseSpecification.filter(category, start, end, userId);

        return repo.findAll(spec, pageable)
                .map(mapper::toDTO);
    }

    public ExpenseResponseDTO addExpense(
            ExpenseRequestDTO dto,
            Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow();

        Expense expense = mapper.toEntity(dto);

        expense.setUser(user);

        Expense saved = repo.save(expense);

        return mapper.toDTO(saved);
    }

    public ExpenseResponseDTO updateExpense(
            Long id,
            ExpenseRequestDTO dto) {

        Expense expense = repo.findById(id)
                .orElseThrow();

        expense.setAmount(dto.getAmount());

        expense.setCategory(dto.getCategory());

        expense.setDate(dto.getDate());

        expense.setDescription(dto.getDescription());

        Expense updated = repo.save(expense);

        return mapper.toDTO(updated);
    }

    public void deleteExpense(Long id) {

        if (!repo.existsById(id)) {
            throw new RuntimeException("Expense not found");
        }

        repo.deleteById(id);
    }
}