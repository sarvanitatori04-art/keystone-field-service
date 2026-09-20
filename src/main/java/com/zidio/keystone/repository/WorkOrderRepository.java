package com.zidio.keystone.repository;

import com.zidio.keystone.model.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {
    List<WorkOrder> findByAssignedToUserId(Long userId);
    List<WorkOrder> findByCustomerId(Long customerId);
    List<WorkOrder> findByStatus(String status);
}