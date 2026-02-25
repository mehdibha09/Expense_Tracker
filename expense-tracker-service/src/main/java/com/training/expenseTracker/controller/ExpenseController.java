package com.training.expenseTracker.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.training.expenseTracker.model.Expense;
import com.training.expenseTracker.service.ExpenseService;

import jakarta.annotation.PostConstruct;

@RestController
@RequestMapping("/api/v1/expense")
@CrossOrigin(origins = "http://localhost:4200")
public class ExpenseController {

    @Autowired ExpenseService expenseService;
    @Autowired(required = false) DataSource dataSource;

    @Value("${db.pod.name:postgres-0}")
    private String dbPodName = "postgres-0";

    @PostConstruct
    public void init() {
        if (!expenseService.getAllExpense().isEmpty()) {
            return;
        }

        Expense expense1 = new Expense();
        expense1.setId(1);
        expense1.setTitle("lassi");
        expense1.setAmount(100.0);
        expense1.setCategory("Food");

        Expense expense2 = new Expense();
        expense2.setId(2);
        expense2.setTitle("shirt");
        expense2.setAmount(2000.0);
        expense2.setCategory("Clothing");

        expenseService.addExpense(expense1);
        expenseService.addExpense(expense2);
    }

    @GetMapping("/show")
    public List<Expense> getAllExpense() {
        return expenseService.getAllExpense();
    }

    @PostMapping("/add")
    public ResponseEntity<Expense> addExpense(@RequestBody Expense expense) {
        Expense addedExpense = expenseService.addExpense(expense);
        return new ResponseEntity<>(addedExpense, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteExpenseById(@RequestParam Integer id) {
        expenseService.deleteExpenseById(id);
        return new ResponseEntity<>("Expense deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/db-status")
    public ResponseEntity<String> getDatabaseStatus() {
        if (dataSource == null) {
            return new ResponseEntity<>("Database connection not configured", HttpStatus.SERVICE_UNAVAILABLE);
        }

        try (Connection connection = dataSource.getConnection()) {
            if (connection != null && !connection.isClosed()) {
                return new ResponseEntity<>("Connected to PostgreSQL pod: " + dbPodName, HttpStatus.OK);
            }
        } catch (SQLException exception) {
            return new ResponseEntity<>("Failed to connect to PostgreSQL", HttpStatus.SERVICE_UNAVAILABLE);
        }

        return new ResponseEntity<>("Failed to connect to PostgreSQL", HttpStatus.SERVICE_UNAVAILABLE);
    }
}
