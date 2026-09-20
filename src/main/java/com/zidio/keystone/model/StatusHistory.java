package com.zidio.keystone.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "work_order_status_history")
@Data
@NoArgsConstructor
public class StatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long workOrderId;
    private String fromStatus;
    private String toStatus;
    private String changedBy;
    private LocalDateTime changedAt = LocalDateTime.now();
    private String note;

    public StatusHistory(Long workOrderId, String fromStatus, String toStatus, String changedBy, String note) {
        this.workOrderId = workOrderId;
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
        this.changedBy = changedBy;
        this.note = note;
    }
}