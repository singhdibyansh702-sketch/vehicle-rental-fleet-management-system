package com.vehiclerental.repository;

import com.vehiclerental.model.Maintenance;
import com.vehiclerental.model.MaintenanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {

    List<Maintenance> findByVehicleIdOrderByMaintenanceDateDesc(Long vehicleId);

    List<Maintenance> findByStatus(MaintenanceStatus status);

    List<Maintenance> findAllByOrderByMaintenanceDateDesc();

    @Query("SELECT COALESCE(SUM(m.cost), 0.0) FROM Maintenance m")
    Double getTotalMaintenanceCost();
}
