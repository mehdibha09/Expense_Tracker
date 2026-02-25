package com.training.expenseTracker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.training.expenseTracker.exceptions.ExpenseNotFoundException;
import com.training.expenseTracker.model.Expense;
import com.training.expenseTracker.repository.ExpenseRepository;

@Service
public class ExpenseService {

    @Autowired ExpenseRepository expenseRepository;

    public List<Expense> getAllExpense() {
        return expenseRepository.findAll();
    }

    public void addExpense(Expense expense) {
        if(expense != null) expenseRepository.save(expense);
        else throw new IllegalArgumentException();
    }

    public void deleteExpenseById(Integer id) {
        Optional<Expense> expense = expenseRepository.findById(id);

        if(expense.isPresent()) expenseRepository.delete(expense.get());
        else throw new ExpenseNotFoundException(id);
    }
}
