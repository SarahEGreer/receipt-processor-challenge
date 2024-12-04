package com.fetchproject.receipt_processor.controller;

import com.fetchproject.receipt_processor.model.Receipt;
import com.fetchproject.receipt_processor.service.ReceiptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {
    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService){
        this.receiptService = receiptService;
    }

//    @PostMapping("/process")
//    public ResponseEntity<Map<String, String>> processReceipt(@RequestBody Receipt receipt){
//        String id = receiptService.processReceipt(receipt);
//        return ResponseEntity.ok(Map.of("id", id));
//    }

    @PostMapping("/process")
    public ResponseEntity<Map<String, String>> processReceipt(@RequestBody Receipt receipt){
        String id = receiptService.processReceipt(receipt);
        return ResponseEntity.ok(Map.of("id", id));
    }

    @GetMapping("/{id}/points")
    public ResponseEntity<Map<String, Integer>> getPoints(@PathVariable String id) {
        int points = receiptService.getPoints(id);
        return ResponseEntity.ok(Map.of("points", points));
    }
}
