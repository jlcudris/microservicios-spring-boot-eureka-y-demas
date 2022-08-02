package com.tutoria.carmicroservice.Controller;

import com.tutoria.carmicroservice.Entity.Car;
import com.tutoria.carmicroservice.Service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/car")
public class CarController {

    @Autowired
    private CarService carService;

    @PostMapping
    public ResponseEntity<Car> save(@RequestBody Car car){
        return new ResponseEntity<>(carService.save(car), HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<Car>> getAllCar(){

        return carService.getAll().stream().count() >0
                ?ResponseEntity.ok(carService.getAll())
                :ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable("id")  Long id){

        if(carService.getCarById(id) != null){
           return ResponseEntity.ok(carService.getCarById(id));
        }
       return ResponseEntity.notFound().build();

    }
//retornar los carros por un usuario en especifico
    @GetMapping("/user/{id}")
    public ResponseEntity<List<Car>> getCarByUserId(@PathVariable("id")  Long id){
        if(carService.findAllCarByUserId(id).isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(carService.findAllCarByUserId(id));

    }
}
