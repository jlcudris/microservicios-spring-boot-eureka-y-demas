package com.tutoria.carmicroservice.Repository;

import com.tutoria.carmicroservice.Entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car,Long> {

    List<Car> findByUserId(Long userId);
}
