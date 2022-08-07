package com.tutoria.usermicroservice.Service;

import com.tutoria.usermicroservice.Config.RestTemplateConfig;
import com.tutoria.usermicroservice.Entity.User;
import com.tutoria.usermicroservice.FeignClients.BikeFeignClient;
import com.tutoria.usermicroservice.FeignClients.CarFeignClient;
import com.tutoria.usermicroservice.ModelsOut.Bike;
import com.tutoria.usermicroservice.ModelsOut.Car;
import com.tutoria.usermicroservice.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImp implements   UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CarFeignClient carFeignClient;

    @Autowired
    private BikeFeignClient bikeFeignClient;


    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long idUser) {
        return userRepository.findById(idUser).orElse(null);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<Car> getCars(long userId) {
        List<Car> cars =restTemplate.getForObject("http://car-service/car/user/"+userId,List.class);
        return cars;
    }

    @Override
    public List<Bike> getBikes(long userId) {
        List<Bike> bikes =restTemplate.getForObject("http://bike-service/bike/user/"+userId,List.class);
        return bikes;
    }

    @Override
    public Car saveCar(Car car, long userId) {
        if(getUserById(userId) == null){
            return null;
        }
        car.setUserId(userId);
        //Car carNew =carFeignClient.save(car);
        return  carFeignClient.save(car);
    }

    @Override
    public Bike saveBike(Bike bike, long userId) {
        if(getUserById(userId) == null){
            return null;
        }
        bike.setUserId(userId);
        return bikeFeignClient.save(bike);
    }

    @Override
    public List<Car> getCarsAllByUserId(long userId) {
        if(getUserById(userId) == null){
            return null;
        }
        return carFeignClient.getAllCarsByUser(userId);
    }

    @Override
    public List<Bike> getBikesAllByUserId(long userId) {
        User user =userRepository.findById(userId).orElse(null);
        if(user == null){
            return null;
        }
        return bikeFeignClient.getAllBikeByUserId(userId);
    }

    @Override
    public Map<String, Object> getUserAndAllVehicles(long userId) {
       User user =getUserById(userId);
        Map<String,Object> response = new HashMap<>();
        Map<String,Object> responsecar = new HashMap<>();
       if (user == null){
            // response.put("mensaje","usuario no encontrado con id: "+userId);
             return null;
       }
       response.put("user",user);

       List<Car> cars =carFeignClient.getAllCarsByUser(user.getId());
       if (cars != null){
           response.put("Cars",cars);
       }else {
           responsecar.put("mensaje","este usuario no tiene carros");
           responsecar.put("total",0);
           response.put("Cars",responsecar);
       }

       List<Bike> bikes =bikeFeignClient.getAllBikeByUserId(user.getId());
       if (bikes !=null){
           response.put("Bikes",bikes);
       }else {
           response.put("Bikes","este usuario no tiene bikes asignados");
       }

        return response;

    }

}
