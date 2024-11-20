package com.pranav.repositorty;

import com.pranav.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,String> {


    // This method is used to retrieve the user from the email
    public User findByEmail(String email);

    public User findByEmailAndPassword(String email,String password);

    public List<User> findByNameContaining(String keywords);

}
