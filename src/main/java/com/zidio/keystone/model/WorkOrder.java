package com.zidio.keystone.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "work_orders")
@Data
@NoArgsConstructor
public class WorkOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String title;

    private String priority; // LOW, MEDIUM, HIGH, CRITICAL
    private String status;   // NEW, ASSIGNED, IN_PROGRESS, ON_HOLD, COMPLETED, CLOSED, CANCELLED

    private LocalDateTime slaDueAt;
    private Long customerId;
    private Long siteId;
    private Long assignedToUserId;
    private LocalDateTime createdAt = LocalDateTime.now();

    public WorkOrder(String code, String title, String priority, String status, LocalDateTime slaDueAt) {
        this.code = code;
        this.title = title;
        this.priority = priority;
        this.status = status;
        this.slaDueAt = slaDueAt;
    }
}