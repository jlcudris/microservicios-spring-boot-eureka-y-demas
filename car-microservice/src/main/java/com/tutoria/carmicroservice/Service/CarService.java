package com.tutoria.carmicroservice.Service;



import com.tutoria.carmicroservice.Entity.Car;

import java.util.List;

public interface CarService {

    public  List<Car> getAll();

    public Car getCarById(Long carId);

    public Car save(Car car);

    public List<Car> findAllCarByUserId(Long userId);
}
