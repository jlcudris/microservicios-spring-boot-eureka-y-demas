package com.tutoria.usermicroservice.Controller;

import com.tutoria.usermicroservice.Entity.User;
import com.tutoria.usermicroservice.ModelsOut.Bike;
import com.tutoria.usermicroservice.ModelsOut.Car;
import com.tutoria.usermicroservice.Repository.UserRepository;
import com.tutoria.usermicroservice.Service.CarServiceFeignClient;
import com.tutoria.usermicroservice.Service.UserService;
import com.tutoria.usermicroservice.Service.UserServiceImp;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
     CarServiceFeignClient carServiceFeignClient;
    @Autowired
    UserRepository userRepository;

    @PostMapping
    public ResponseEntity<User> save(@RequestBody User user){
        return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){

        return userService.getAll().stream().count() >0
                ?ResponseEntity.ok(userService.getAll())
                :ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id")  Long id){

        if(userService.getUserById(id) != null){
           return ResponseEntity.ok(userService.getUserById(id));
        }
       return ResponseEntity.notFound().build();

    }

    @CircuitBreaker(name = "carsCB", fallbackMethod = "fallBackGetCarsByUser")
    @GetMapping("car/byUser/{id}")
    public ResponseEntity<List<Car>> getCarsByUser(@PathVariable(name = "id") long id){

        if(userService.getUserById(id) == null)
            return ResponseEntity.notFound().build();

        return userService.getCars(id) != null
                ?ResponseEntity.ok(userService.getCars(id))
                :ResponseEntity.noContent().build();
    }
    @CircuitBreaker(name = "carsCB", fallbackMethod = "fallBackSaveCar")
    @PostMapping("saveCar/user/{userId}")
    public ResponseEntity<Car> saveCar(@PathVariable long userId, @RequestBody  Car car ){

       /* Car car1 = carServiceFeignClient.saveCar(car,userId);

        if (car1 == null){
            return ResponseEntity.noContent().build();
        }*/

        Car car1 = userService.saveCar(car,userId);
        if(car1 ==null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(car1);

    }
    @CircuitBreaker(name = "bikesCB", fallbackMethod = "fallBackGetBikesByUser")
    @GetMapping("bike/byUser/{id}")
    public ResponseEntity<List<Bike>> getBikesByUser(@PathVariable(name = "id") long id){
        if(userService.getUserById(id) == null)
            return ResponseEntity.notFound().build();

        return userService.getBikes(id) !=null
                ?ResponseEntity.ok(userService.getBikes(id))
                :ResponseEntity.noContent().build();

    }


    @CircuitBreaker(name = "bikesCB", fallbackMethod = "fallBackSaveBike")
    @PostMapping("saveBike/user/{userId}")
    public ResponseEntity<Bike> saveBike(@PathVariable long userId, @RequestBody  Bike bike ){

        Bike bike1 = userService.saveBike(bike,userId);
        if(bike1 ==null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bike1);

    }
//este retorna los carros por id de usuarios mediante feignClient
    @CircuitBreaker(name = "carsCB", fallbackMethod = "fallBackGetCarsByUserId")
    @GetMapping("carAll/byUserFeignClient/{id}")
    public ResponseEntity<?> getCarsByUserId(@PathVariable(name = "id") long id){
        User user =userService.getUserById(id);
        Map<String,Object> ressu=new HashMap<>();
        ressu.put("user",user);
        ressu.put("bikes",userService.getCarsAllByUserId(id));
        //return ResponseEntity.ok(ressu);

        if(userService.getCarsAllByUserId(id) != null){
            return userService.getCarsAllByUserId(id).stream().count() >0
                    ?ResponseEntity.ok(userService.getCarsAllByUserId(id))
                    :ResponseEntity.noContent().build();

        }
        return new ResponseEntity<>(ressu,HttpStatus.NOT_FOUND);

       /* if(userService.getCarsAllByUserId(id) == null)
            return ResponseEntity.notFound().build();

        return userService.getCarsAllByUserId(id).stream().count() >0
                ?ResponseEntity.ok(userService.getCarsAllByUserId(id))
                :ResponseEntity.noContent().build();*/

    }

    //este retorna los bikes por id de usuarios mediante feignClient
    @CircuitBreaker(name = "bikesCB", fallbackMethod = "fallBackGetAllBikeByUserId")
    @GetMapping("bikeAll/byUserFeignClient/{id}")
    public ResponseEntity<?> getAllBikeByUserId(@PathVariable("id") Long id){
        User user =userService.getUserById(id);
        Map<String,Object> ressu=new HashMap<>();
        ressu.put("user",user);
        ressu.put("bikes",userService.getBikesAllByUserId(id));
        //return ResponseEntity.ok(ressu);


       if(userService.getBikesAllByUserId(id) != null){
            return userService.getBikesAllByUserId(id).stream().count() >0
                    ?ResponseEntity.ok(userService.getBikesAllByUserId(id))
                    :ResponseEntity.noContent().build();

        }
       return new ResponseEntity<>(ressu,HttpStatus.NOT_FOUND);


    }
    //retorna todo los vehiculos de un usuario en epecifico con feignClient
    @CircuitBreaker(name = "allCB", fallbackMethod = "fallBackGetAllVehiclesByUserId")
    @GetMapping("allVehicles/byUserFeignClient/{userId}")
    public ResponseEntity<Map<String,Object>> getAllVehiclesByUserId(@PathVariable long userId){

        if (userService.getUserAndAllVehicles(userId)==null){
            Map<String,Object> resul=new HashMap<>();
            resul.put("error","usuario no encontrado");
            return new ResponseEntity<>(resul,HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(userService.getUserAndAllVehicles(userId));
    }

    private ResponseEntity<List<Car>> fallBackGetCarsByUser(@PathVariable(name = "id") long id,RuntimeException e){
        return new ResponseEntity("El usuario "+ id + "tiene los coches en el taller",HttpStatus.OK);
    }

    private ResponseEntity<Car> fallBackSaveCar(@PathVariable long userId, @RequestBody  Car car,RuntimeException e){
        return new ResponseEntity("El usuario "+ userId + "no tiene dinero para coches",HttpStatus.OK);
    }

    private ResponseEntity<List<Bike>> fallBackGetBikesByUser(@PathVariable(name = "id") long id,RuntimeException e){
        return new ResponseEntity("El usuario "+ id + "tienes las motos en el taller",HttpStatus.OK);
    }

    private ResponseEntity<Bike> fallBackSaveBike(@PathVariable long userId, @RequestBody  Bike bike,RuntimeException e){
        return new ResponseEntity("El usuario "+ userId + "no tiene dinero para las motos",HttpStatus.OK);
    }

    private ResponseEntity<?> fallBackGetCarsByUserId(@PathVariable(name = "id") long id,RuntimeException e){
        return new ResponseEntity("El usuario desde feignClient "+ id + "tiene los coches en el taller",HttpStatus.OK);
    }

    private ResponseEntity<?> fallBackGetAllBikeByUserId(@PathVariable("id") Long id,RuntimeException e){
        return new ResponseEntity("El usuario desde feignClient get bikeAll "+ id + "tienes las motos en el taller",HttpStatus.OK);
    }

    private ResponseEntity<Map<String,Object>> fallBackGetAllVehiclesByUserId(@PathVariable long userId,RuntimeException e){
        return new ResponseEntity("El usuario desde feignClient get vehiclesALl "+ userId + "tienes los  vehiculos en el taller",HttpStatus.OK);
    }


}
