package com.fetchproject.receipt_processor.service;

import com.fetchproject.receipt_processor.exception.InvalidReceiptException;
import com.fetchproject.receipt_processor.exception.ReceiptNotFoundException;
import com.fetchproject.receipt_processor.model.Receipt;
import com.fetchproject.receipt_processor.util.PointsCalculator;
import com.fetchproject.receipt_processor.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptServiceTest {

    private ReceiptService receiptService;

    @BeforeEach
    void setUp() {
        PointsCalculator pointsCalculator = new PointsCalculator();
        receiptService = new ReceiptService(pointsCalculator);
    }

    @Test
    void testProcessReceiptWithNoItemsThrowsException() {
        // Create a receipt with no items
        Receipt receipt = new Receipt();
        receipt.setRetailer("gas station");
        receipt.setPurchaseDate("2022-03-20");
        receipt.setPurchaseTime("14:33");
        receipt.setTotal("9.00");
        receipt.setItems(List.of()); // Empty item list

        // Verify that an InvalidReceiptException is thrown
        InvalidReceiptException exception = assertThrows(InvalidReceiptException.class, () -> {
            receiptService.processReceipt(receipt);
        });

        assertEquals("The receipt is invalid.", exception.getMessage());
    }

    @Test
    void testGetPointsForNonExistentReceiptThrowsException() {
        String nonExistentId = "non-existent-id";

        // Verify that a ReceiptNotFoundException is thrown
        ReceiptNotFoundException exception = assertThrows(ReceiptNotFoundException.class, () -> {
            receiptService.getPoints(nonExistentId);
        });

        assertEquals("No receipt found for that id", exception.getMessage());
    }

    @Test
    void testProcessReceiptWithValidData() {
        // Create a valid receipt
        Receipt receipt = new Receipt();
        receipt.setRetailer("Target");
        receipt.setPurchaseDate("2022-03-20");
        receipt.setPurchaseTime("14:33");
        receipt.setTotal("9.00");
        receipt.setItems(List.of(new Item("item1", "5.00")));

        String id = receiptService.processReceipt(receipt);

        assertNotNull(id, "Expected receipt ID to be generated");
    }
}
