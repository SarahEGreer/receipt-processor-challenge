package com.fetchproject.receipt_processor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fetchproject.receipt_processor.model.Item;
import com.fetchproject.receipt_processor.model.Receipt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ReceiptControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testProcessReceipt() throws Exception {
        // Create a sample receipt
        Receipt receipt = new Receipt();
        receipt.setRetailer(" gas station");
        receipt.setPurchaseDate("2022-03-20");
        receipt.setPurchaseTime("14:33");
        receipt.setTotal("9.00");
        receipt.setItems(List.of(
                new Item("candy", "5.00"),
                new Item("Emils Cheese Pizza", "12.25")
        ));

        // Convert receipt to JSON
        String receiptJson = objectMapper.writeValueAsString(receipt);

        // Perform POST request to /receipts/process
        mockMvc.perform(post("/receipts/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(receiptJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists()); // Ensure the response contains an ID
    }

    @Test
    void testGetPointsForValidReceipt() throws Exception {
        // Process a receipt and extract its ID
        Receipt receipt = new Receipt();
        receipt.setRetailer(" gas station");
        receipt.setPurchaseDate("2022-03-20");
        receipt.setPurchaseTime("14:33");
        receipt.setTotal("9.00");
        receipt.setItems(List.of(
                new Item("candy", "5.00"),
                new Item("Emils Cheese Pizza", "12.25")
        ));

        String receiptJson = objectMapper.writeValueAsString(receipt);

        // Simulate processing the receipt
        String receiptId = mockMvc.perform(post("/receipts/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(receiptJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extract ID from response
        String id = objectMapper.readTree(receiptId).get("id").asText();

        // Perform GET request to /receipts/{id}/points
        mockMvc.perform(get("/receipts/" + id + "/points"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.points").value(103)); // Ensure the points match expected value
    }

    @Test
    void testGetPointsForInvalidReceipt() throws Exception {
        // Perform GET request to a non-existent ID
        mockMvc.perform(get("/receipts/invalid-id/points"))
                .andExpect(status().isNotFound()); // Ensure a 404 is returned for invalid receipt IDs
    }
}
