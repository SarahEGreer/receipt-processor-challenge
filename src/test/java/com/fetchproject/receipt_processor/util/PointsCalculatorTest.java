package com.fetchproject.receipt_processor.util;

import com.fetchproject.receipt_processor.model.Item;
import com.fetchproject.receipt_processor.model.Receipt;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PointsCalculatorTest {

    private final PointsCalculator pointsCalculator = new PointsCalculator();

    @Test
    void testCalculatePointsWithValidReceipt() {
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

        // Call the calculatePoints method
        int points = pointsCalculator.calculatePoints(receipt);

        // Verify the points
        assertEquals(103, points, "Expected points did not match the actual points");
    }

    @Test
    void testCalculatePointsWithEmptyValues() {
        // Create a receipt where everything is empty
        Receipt receipt = new Receipt();
        receipt.setRetailer(""); // Empty retailer
        receipt.setPurchaseDate(""); // Empty date
        receipt.setPurchaseTime(""); // Empty time
        receipt.setTotal(""); // Empty total
        receipt.setItems(List.of()); // Empty item list

        int points = pointsCalculator.calculatePoints(receipt);

        assertEquals(0, points, "Expected points did not match for receipt with empty values");
    }

    @Test
    void testCalculatePointsWithNoItems() {
        // Create a receipt with no items
        Receipt receipt = new Receipt();
        receipt.setRetailer(" gas station");
        receipt.setPurchaseDate("2022-03-20");
        receipt.setPurchaseTime("14:33");
        receipt.setTotal("9.00");
        receipt.setItems(List.of()); // Empty item list

        int points = pointsCalculator.calculatePoints(receipt);

        assertEquals(95, points, "Expected points did not match for receipt with no items");
    }

    @Test
    void testCalculatePointsWithInvalidDate() {
        // Create a receipt with an invalid date
        Receipt receipt = new Receipt();
        receipt.setRetailer(" gas station");
        receipt.setPurchaseDate("invalid-date"); // Invalid date
        receipt.setPurchaseTime("14:33");
        receipt.setTotal("9.00");
        receipt.setItems(List.of(
                new Item("candy", "5.00"),
                new Item("Emils Cheese Pizza", "12.25")
        ));

        int points = pointsCalculator.calculatePoints(receipt);

        assertEquals(103, points, "Expected points did not match for receipt with invalid date");
    }

    @Test
    void testCalculatePointsWithNullValues() {
        // Create a receipt with null values
        Receipt receipt = new Receipt();
        receipt.setRetailer(null);
        receipt.setPurchaseDate(null);
        receipt.setPurchaseTime(null);
        receipt.setTotal(null);
        receipt.setItems(null); // Null item list

        int points = pointsCalculator.calculatePoints(receipt);

        assertEquals(0, points, "Expected points did not match for receipt with null values");
    }
}

