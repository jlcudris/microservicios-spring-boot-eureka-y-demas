package com.tutoria.bikemicroservice.Service;


import com.tutoria.bikemicroservice.Entity.Bike;
import com.tutoria.bikemicroservice.Repository.BikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BikeServiceImp implements BikeService {
    @Autowired
    private BikeRepository bikeRepository;


    @Override
    public List<Bike> getAll() {
        return bikeRepository.findAll();
    }

    @Override
    public Bike getBikeById(Long idBike) {
        return bikeRepository.findById(idBike).orElse(null);
    }

    @Override
    public Bike save(Bike bike) {
        return bikeRepository.save(bike);
    }

    @Override
    public List<Bike> findAllBikeByUserId(Long userId) {
        return  bikeRepository.findByUserId(userId);
    }
}
