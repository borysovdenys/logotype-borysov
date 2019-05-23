package com.infostroy.borysov.springtask.dao;

import com.infostroy.borysov.springtask.entity.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {

    List<Response> findAllByOrderById();
}