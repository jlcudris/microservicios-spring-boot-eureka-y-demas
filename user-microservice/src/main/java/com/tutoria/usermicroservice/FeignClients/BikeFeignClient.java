package com.tutoria.usermicroservice.FeignClients;

import com.tutoria.usermicroservice.ModelsOut.Bike;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "bike-service")
public interface BikeFeignClient {

    @PostMapping("/bike")
    Bike save(@RequestBody Bike bike);

    @GetMapping("/bike/user/{id}")
    List<Bike>getAllBikeByUserId(@PathVariable("id") long id);

}
