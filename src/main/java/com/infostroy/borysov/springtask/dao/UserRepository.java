package com.infostroy.borysov.springtask.dao;

import com.infostroy.borysov.springtask.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findUserByEmailAndPhone(String email, String phone);
}
