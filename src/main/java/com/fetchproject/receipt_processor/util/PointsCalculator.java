package com.fetchproject.receipt_processor.util;

import com.fetchproject.receipt_processor.model.Item;
import com.fetchproject.receipt_processor.model.Receipt;
import org.springframework.stereotype.Component;

@Component
public class PointsCalculator {

    private static final int POINTS_FOR_ROUND_DOLLAR = 50;
    private static final int POINTS_FOR_QUARTER_MULTIPLE = 25;
    private static final int POINTS_FOR_TWO_ITEMS = 5;
    private static final int POINTS_FOR_ODD_DAY = 6;
    private static final int POINTS_FOR_AFTERNOON_PURCHASE = 10;


    public int calculatePoints(Receipt receipt) {

        int points = 0;

        points += calculateRetailerNamePoints(receipt.getRetailer());

        if (isRoundDollarAmount(receipt.getTotal())) {
            points += POINTS_FOR_ROUND_DOLLAR;
        }

        if (isMultipleOfTwentyFiveCents(receipt.getTotal())) {
            points += POINTS_FOR_QUARTER_MULTIPLE;
        }

        points += calculatePointsPerTwoItems(receipt);

        points += calculateItemDescriptionPoints(receipt);

        if (isPurchaseDayOdd(receipt.getPurchaseDate())) {
            points += POINTS_FOR_ODD_DAY;
        }

        if (isPurchaseTimeAfternoon(receipt.getPurchaseTime())) {
            points += POINTS_FOR_AFTERNOON_PURCHASE;
        }

        return points;
    }

    private int calculateRetailerNamePoints(String retailer){
        if (retailer == null || retailer.isEmpty()) {
            return 0;
        }

        int points = 0;

        for (int i = 0; i < retailer.length(); i++) {
            char c = retailer.charAt(i);

            if(Character.isLetterOrDigit(c)){
                points++;
            }
        }
        return points;
    }

    private boolean isRoundDollarAmount(String total){
        if (total == null) {
            return false; // Treat null total as invalid
        }

        try{
            double amount = Double.parseDouble(total);
            return amount % 1.00 == 0;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    private boolean isMultipleOfTwentyFiveCents(String total) {
        if (total == null) {
            return false;
        }

        try{
            double amount = Double.parseDouble(total);
            return amount % 0.25 == 0;
        } catch(NumberFormatException e){
            return false;
        }
    }

    private int calculatePointsPerTwoItems(Receipt receipt){
        if (receipt.getItems() == null || receipt.getItems().isEmpty()) {
            return 0;
        }

        return (receipt.getItems().size()/2) * POINTS_FOR_TWO_ITEMS;
    }

    private int calculateItemDescriptionPoints(Receipt receipt) {
        if (receipt.getItems() == null || receipt.getItems().isEmpty()) {
            return 0;
        }

        int points = 0;
        for (Item item : receipt.getItems()) {
            if (item == null || item.getShortDescription() == null || item.getPrice() == null) {
                continue;
            }

            String description = item.getShortDescription().trim();
            if (description.length() % 3 == 0) {
                try {
                    double price = Double.parseDouble(item.getPrice());
                    points += (int) Math.ceil(price * 0.2);
                } catch (NumberFormatException e) {
                    continue;
                }
            }
        }
        return points;
    }

    private boolean isPurchaseDayOdd(String purchaseDate) {
        if (purchaseDate == null || !purchaseDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return false;
        }

        try {
            int day = Integer.parseInt(purchaseDate.split("-")[2]);
            return day % 2 != 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isPurchaseTimeAfternoon(String purchaseTime) {
        if (purchaseTime == null || !purchaseTime.matches("\\d{2}:\\d{2}")) {
            return false;
        }

        try{
            int hour = Integer.parseInt(purchaseTime.split(":")[0]);
            return (hour == 14 || hour == 15);
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
