package com.tutoria.bikemicroservice.Controller;
import com.tutoria.bikemicroservice.Entity.Bike;
import com.tutoria.bikemicroservice.Service.BikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bike")
public class BikeController {

    @Autowired
    private BikeService bikeService;

    @PostMapping
    public ResponseEntity<Bike> save(@RequestBody Bike bike){
        return new ResponseEntity<>(bikeService.save(bike), HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<Bike>> getAllBike(){

        return bikeService.getAll().stream().count() >0
                ?ResponseEntity.ok(bikeService.getAll())
                :ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Bike> getCarById(@PathVariable("id")  Long id){

        if(bikeService.getBikeById(id) != null){
           return ResponseEntity.ok(bikeService.getBikeById(id));
        }
       return ResponseEntity.notFound().build();

    }
//retornar los bikes por un usuario en especifico
    @GetMapping("/user/{id}")
    public ResponseEntity<List<Bike>> getCarByUserId(@PathVariable("id")  Long id){
        if(bikeService.findAllBikeByUserId(id).isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bikeService.findAllBikeByUserId(id));

    }
}
