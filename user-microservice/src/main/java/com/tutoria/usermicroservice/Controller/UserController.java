package com.tutoria.usermicroservice.Controller;

import com.tutoria.usermicroservice.Entity.User;
import com.tutoria.usermicroservice.ModelsOut.Bike;
import com.tutoria.usermicroservice.ModelsOut.Car;
import com.tutoria.usermicroservice.Repository.UserRepository;
import com.tutoria.usermicroservice.Service.CarServiceFeignClient;
import com.tutoria.usermicroservice.Service.UserService;
import com.tutoria.usermicroservice.Service.UserServiceImp;
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

    @GetMapping("car/byUser/{id}")
    public ResponseEntity<List<Car>> getCarsByUser(@PathVariable(name = "id") long id){

        if(userService.getUserById(id) == null)
            return ResponseEntity.notFound().build();

        return userService.getCars(id) != null
                ?ResponseEntity.ok(userService.getCars(id))
                :ResponseEntity.noContent().build();

    }

    @GetMapping("bike/byUser/{id}")
    public ResponseEntity<List<Bike>> getBikesByUser(@PathVariable(name = "id") long id){
        if(userService.getUserById(id) == null)
            return ResponseEntity.notFound().build();

        return userService.getBikes(id) !=null
                ?ResponseEntity.ok(userService.getBikes(id))
                :ResponseEntity.noContent().build();

    }

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

    @PostMapping("saveBike/user/{userId}")
    public ResponseEntity<Bike> saveBike(@PathVariable long userId, @RequestBody  Bike bike ){

        Bike bike1 = userService.saveBike(bike,userId);
        if(bike1 ==null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bike1);

    }
//este retorna los carros por id de usuarios mediante feignClient
    @GetMapping("carAll/byUserFeignClient/{id}")
    public ResponseEntity<List<Car>> getCarsByUserId(@PathVariable(name = "id") long id){

        if(userService.getCarsAllByUserId(id) == null)
            return ResponseEntity.notFound().build();

        return userService.getCarsAllByUserId(id).stream().count() >0
                ?ResponseEntity.ok(userService.getCarsAllByUserId(id))
                :ResponseEntity.noContent().build();

    }

    //este retorna los bikes por id de usuarios mediante feignClient
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
    @GetMapping("allVehicles/byUserFeignClient/{userId}")
    public ResponseEntity<Map<String,Object>> getAllVehiclesByUserId(@PathVariable long userId){

        if (userService.getUserAndAllVehicles(userId)==null){
            Map<String,Object> resul=new HashMap<>();
            resul.put("error","usuario no encontrado");
            return new ResponseEntity<>(resul,HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(userService.getUserAndAllVehicles(userId));
    }
}
