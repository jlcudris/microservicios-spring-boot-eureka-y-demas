package com.tutoria.usermicroservice.Service;

import com.tutoria.usermicroservice.FeignClients.CarFeignClient;
import com.tutoria.usermicroservice.ModelsOut.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarServiceFeignClient {

    @Autowired
    private UserService userService;

    @Autowired
     CarFeignClient carFeignClient;

    //usando spring cloud

   public Car saveCar(Car car, long userId) {
        if(userService.getUserById(userId) == null){
            return null;
        }
        car.setUserId(userId);
        Car carNew =carFeignClient.save(car);
        return  carNew;
    }
}
