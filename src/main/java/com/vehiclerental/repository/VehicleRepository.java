package com.vehiclerental.repository;

import com.vehiclerental.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByRegistrationNumber(String registrationNumber);

    boolean existsByRegistrationNumber(String registrationNumber);

    List<Vehicle> findByStatus(VehicleStatus status);

    List<Vehicle> findByVehicleType(VehicleType vehicleType);

    long countByStatus(VehicleStatus status);

    @Query("SELECT v FROM Vehicle v WHERE " +
           "(:type IS NULL OR v.vehicleType = :type) AND " +
           "(:fuel IS NULL OR v.fuelType = :fuel) AND " +
           "(:transmission IS NULL OR v.transmission = :transmission) AND " +
           "(:status IS NULL OR v.status = :status) AND " +
           "(:maxPrice IS NULL OR v.pricePerDay <= :maxPrice) AND " +
           "(:keyword IS NULL OR LOWER(v.brand) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(v.model) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(v.registrationNumber) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Vehicle> filterVehicles(
            @Param("type") VehicleType type,
            @Param("fuel") FuelType fuel,
            @Param("transmission") Transmission transmission,
            @Param("status") VehicleStatus status,
            @Param("maxPrice") Double maxPrice,
            @Param("keyword") String keyword
    );
}
