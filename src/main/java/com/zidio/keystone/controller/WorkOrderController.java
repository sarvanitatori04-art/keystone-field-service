package com.zidio.keystone.controller;

import com.zidio.keystone.model.StatusHistory;
import com.zidio.keystone.model.WorkOrder;
import com.zidio.keystone.service.WorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/work-orders")
@CrossOrigin(origins = "*")
public class WorkOrderController {

    @Autowired
    private WorkOrderService service;

    @GetMapping
    public List<WorkOrder> listOrders() {
        return service.getAllWorkOrders();
    }

    @PostMapping
    public ResponseEntity<WorkOrder> createOrder(@RequestBody WorkOrder order, 
                                                 @RequestParam(defaultValue = "Dispatcher") String user) {
        return ResponseEntity.ok(service.createWorkOrder(order, user));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, 
                                          @RequestBody Map<String, String> body) {
        String targetStatus = body.get("status");
        String user = body.getOrDefault("user", "System");
        String note = body.getOrDefault("note", "Status updated");

        try {
            WorkOrder updated = service.transitionStatus(id, targetStatus, user, note);
            return ResponseEntity.ok(updated);
        } catch (IllegalStateException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/{id}/history")
    public List<StatusHistory> getOrderHistory(@PathVariable Long id) {
        return service.getHistory(id);
    }
}