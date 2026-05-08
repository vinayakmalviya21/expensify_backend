package com.expense.demo.repository;

import com.expense.demo.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends
        JpaRepository<Expense, Long>,
        JpaSpecificationExecutor<Expense> {

    // 🔹 Get all expenses of a user (pagination)
    Page<Expense> findByUserId(Long userId, Pageable pageable);

    // 🔹 Filter by category
    Page<Expense> findByUserIdAndCategory(
            Long userId,
            String category,
            Pageable pageable
    );

    // 🔹 Filter by date range
    Page<Expense> findByUserIdAndDateBetween(
            Long userId,
            LocalDate start,
            LocalDate end,
            Pageable pageable
    );

    // 🔹 Combined filter (category + date)
    Page<Expense> findByUserIdAndCategoryAndDateBetween(
            Long userId,
            String category,
            LocalDate start,
            LocalDate end,
            Pageable pageable
    );

    // 🔥 Category-wise analytics (for charts)
    @Query("""
        SELECT e.category, SUM(e.amount)
        FROM Expense e
        WHERE e.user.id = :userId
        GROUP BY e.category
    """)
    List<Object[]> getCategorySummary(Long userId);

    // 🔥 Monthly summary
    @Query("""
        SELECT MONTH(e.date), SUM(e.amount)
        FROM Expense e
        WHERE e.user.id = :userId
        GROUP BY MONTH(e.date)
        ORDER BY MONTH(e.date)
    """)
    List<Object[]> getMonthlySummary(Long userId);

    // 🔥 Yearly summary
    @Query("""
        SELECT YEAR(e.date), SUM(e.amount)
        FROM Expense e
        WHERE e.user.id = :userId
        GROUP BY YEAR(e.date)
        ORDER BY YEAR(e.date)
    """)
    List<Object[]> getYearlySummary(Long userId);
}