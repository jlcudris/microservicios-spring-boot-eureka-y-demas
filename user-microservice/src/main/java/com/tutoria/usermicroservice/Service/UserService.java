package com.tutoria.usermicroservice.Service;

import com.tutoria.usermicroservice.Entity.User;
import com.tutoria.usermicroservice.ModelsOut.Bike;
import com.tutoria.usermicroservice.ModelsOut.Car;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

public interface UserService {

    public  List<User> getAll();

    public User getUserById(Long idUser);

    public User save(User user);

    public List<Car> getCars(long userId);
    public List<Bike> getBikes(long userId);

    public Car saveCar(Car car,long userId);

    public Bike saveBike(Bike bike,long userId);

    //retorna los carros de un usuario en epecifico mediante FeignClient
    public List<Car>getCarsAllByUserId(long userId);

    public List<Bike>getBikesAllByUserId(long userId);

    public Map<String,Object> getUserAndAllVehicles(long userId);


}
