package com.tutoria.carmicroservice.Service;


import com.tutoria.carmicroservice.Entity.Car;
import com.tutoria.carmicroservice.Repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarServiceImp implements CarService {
    @Autowired
    private CarRepository carRepository;


    @Override
    public List<Car> getAll() {
        return carRepository.findAll();
    }

    @Override
    public Car getCarById(Long idCard) {
        return carRepository.findById(idCard).orElse(null);
    }

    @Override
    public Car save(Car car) {
        return carRepository.save(car);
    }

    @Override
    public List<Car> findAllCarByUserId(Long userId) {
        return  carRepository.findByUserId(userId);
    }
}
