package com.zidio.keystone.service;

import com.zidio.keystone.model.StatusHistory;
import com.zidio.keystone.model.WorkOrder;
import com.zidio.keystone.repository.StatusHistoryRepository;
import com.zidio.keystone.repository.WorkOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class WorkOrderService {

    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Autowired
    private StatusHistoryRepository historyRepository;

    private static final Map<String, Set<String>> VALID_TRANSITIONS = Map.of(
        "NEW", Set.of("ASSIGNED", "CANCELLED"),
        "ASSIGNED", Set.of("IN_PROGRESS", "CANCELLED"),
        "IN_PROGRESS", Set.of("ON_HOLD", "COMPLETED"),
        "ON_HOLD", Set.of("IN_PROGRESS"),
        "COMPLETED", Set.of("CLOSED"),
        "CLOSED", Set.of(),
        "CANCELLED", Set.of()
    );

    public List<WorkOrder> getAllWorkOrders() {
        return workOrderRepository.findAll();
    }

    public WorkOrder createWorkOrder(WorkOrder order, String user) {
        order.setStatus("NEW");
        if (order.getCode() == null || order.getCode().isEmpty()) {
            order.setCode("WO-" + (System.currentTimeMillis() % 100000));
        }
        if (order.getSlaDueAt() == null) {
            order.setSlaDueAt(LocalDateTime.now().plusHours(4));
        }
        WorkOrder saved = workOrderRepository.save(order);
        historyRepository.save(new StatusHistory(saved.getId(), null, "NEW", user, "Work order registered"));
        return saved;
    }

    @Transactional
    public WorkOrder transitionStatus(Long id, String targetStatus, String user, String note) {
        WorkOrder order = workOrderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Work order not found: " + id));

        String currentStatus = order.getStatus();
        Set<String> allowedNext = VALID_TRANSITIONS.getOrDefault(currentStatus, Set.of());

        if (!allowedNext.contains(targetStatus)) {
            throw new IllegalStateException("Illegal Transition: Cannot move from " + currentStatus + " to " + targetStatus);
        }

        order.setStatus(targetStatus);
        WorkOrder updated = workOrderRepository.save(order);
        historyRepository.save(new StatusHistory(id, currentStatus, targetStatus, user, note));
        return updated;
    }

    public List<StatusHistory> getHistory(Long id) {
        return historyRepository.findByWorkOrderIdOrderByChangedAtDesc(id);
    }
}