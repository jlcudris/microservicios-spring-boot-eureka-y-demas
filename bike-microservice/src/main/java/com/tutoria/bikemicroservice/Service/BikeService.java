package com.tutoria.bikemicroservice.Service;



import com.tutoria.bikemicroservice.Entity.Bike;

import java.util.List;

public interface BikeService {

    public  List<Bike> getAll();

    public Bike getBikeById(Long carId);

    public Bike save(Bike bike);

    public List<Bike> findAllBikeByUserId(Long userId);
}
