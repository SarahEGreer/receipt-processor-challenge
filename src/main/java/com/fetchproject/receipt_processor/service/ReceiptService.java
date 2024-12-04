package com.fetchproject.receipt_processor.service;


import com.fetchproject.receipt_processor.exception.InvalidReceiptException;
import com.fetchproject.receipt_processor.exception.ReceiptNotFoundException;
import com.fetchproject.receipt_processor.model.Receipt;
import com.fetchproject.receipt_processor.util.PointsCalculator;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ReceiptService {

    private final Map<String, Receipt> receiptData = new HashMap<>();
    private final PointsCalculator pointsCalculator;

    public ReceiptService(PointsCalculator pointsCalculator){
        this.pointsCalculator = pointsCalculator;
    }

    public String processReceipt(Receipt receipt){
        if(receipt == null || receipt.getRetailer() == null || receipt.getPurchaseDate() == null || receipt.getPurchaseTime() == null || receipt.getItems() == null || receipt.getTotal() == null || receipt.getItems().isEmpty()){
            throw new InvalidReceiptException("The receipt is invalid.");
        }
        String id = UUID.randomUUID().toString();
        receiptData.put(id, receipt);
        return id;
    }

    public int getPoints(String id) {
        Receipt receipt = receiptData.get(id);
        if (receipt == null) {
            throw new ReceiptNotFoundException("No receipt found for that id");
        }
        return pointsCalculator.calculatePoints(receipt);
    }
}
