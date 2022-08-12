package com.tutoria.usermicroservice.FeignClients;

import com.tutoria.usermicroservice.ModelsOut.Car;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "car-service")

public interface CarFeignClient {
    @PostMapping("/car")
    Car save(@RequestBody Car car);

    @GetMapping("/car/user/{userId}")
    List<Car> getAllCarsByUser(@PathVariable(name = "userId") long userId);

}
