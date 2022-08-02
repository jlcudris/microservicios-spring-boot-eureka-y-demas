package com.tutoria.usermicroservice.Repository;

import com.tutoria.usermicroservice.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
