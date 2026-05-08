package com.expense.demo.mapper;

import org.mapstruct.Mapper;
import com.expense.demo.entity.Expense;
import com.expense.demo.dto.*;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {

    Expense toEntity(ExpenseRequestDTO dto);

    ExpenseResponseDTO toDTO(Expense expense);
}