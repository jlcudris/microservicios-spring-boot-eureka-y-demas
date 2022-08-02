package com.tutoria.bikemicroservice.Repository;

import com.tutoria.bikemicroservice.Entity.Bike;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BikeRepository extends JpaRepository<Bike,Long> {

    List<Bike> findByUserId(Long userId);
}
