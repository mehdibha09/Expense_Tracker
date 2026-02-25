package com.training.expenseTracker.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.training.expenseTracker.model.Expense;
import com.training.expenseTracker.service.ExpenseService;

public class ExpenseControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ExpenseService expenseService;

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @InjectMocks
    private ExpenseController expenseController;

    private Expense expense;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(expenseController).build();
        expense = new Expense(1, "lassi", 100.0, "Food");
    }

    @Test
    void testGetAllExpense() throws Exception {
        List<Expense> expenses = Arrays.asList(expense);
        when(expenseService.getAllExpense()).thenReturn(expenses);

        mockMvc.perform(get("/api/v1/expense/show"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("lassi"))
                .andExpect(jsonPath("$[0].amount").value(100.0));
    }

    @Test
    void testAddExpense() throws Exception {
        Expense newExpense = new Expense(2, "shirt", 2000.0, "Clothing");
        when(expenseService.addExpense(newExpense)).thenReturn(newExpense);

        mockMvc.perform(post("/api/v1/expense/add")
                        .contentType("application/json")
                        .content("{\"id\":2, \"title\":\"shirt\", \"amount\":2000.0, \"category\":\"Clothing\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.title").value("shirt"))
            .andExpect(jsonPath("$.amount").value(2000.0));

        verify(expenseService, times(1)).addExpense(newExpense);
    }

    @Test
    void testDeleteExpenseById() throws Exception {
        doNothing().when(expenseService).deleteExpenseById(expense.getId());

        mockMvc.perform(delete("/api/v1/expense/delete")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Expense deleted successfully"));

        verify(expenseService, times(1)).deleteExpenseById(expense.getId());
    }

    @Test
    void testGetDatabaseStatus() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.isClosed()).thenReturn(false);

        mockMvc.perform(get("/api/v1/expense/db-status"))
                .andExpect(status().isOk())
                .andExpect(content().string("Connected to PostgreSQL pod: postgres-0"));
    }
}
