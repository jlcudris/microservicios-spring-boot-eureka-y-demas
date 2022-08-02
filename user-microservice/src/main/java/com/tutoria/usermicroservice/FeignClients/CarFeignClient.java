package com.tutoria.usermicroservice.FeignClients;

import com.tutoria.usermicroservice.ModelsOut.Car;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "car-service",url = "http://localhost:8002/car")

public interface CarFeignClient {
    @PostMapping()
    Car save(@RequestBody Car car);

    @GetMapping("/user/{userId}")
    List<Car> getAllCarsByUser(@PathVariable long userId);

}
