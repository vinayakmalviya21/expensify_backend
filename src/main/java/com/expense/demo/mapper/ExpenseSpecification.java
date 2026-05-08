package com.expense.demo.mapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import com.expense.demo.entity.Expense;
import jakarta.persistence.criteria.Predicate;

public class ExpenseSpecification {

    public static Specification<Expense> filter(
            String category, LocalDate start, LocalDate end, Long userId) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("user").get("id"), userId));

            if (category != null)
                predicates.add(cb.equal(root.get("category"), category));

            if (start != null && end != null)
                predicates.add(cb.between(root.get("date"), start, end));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}