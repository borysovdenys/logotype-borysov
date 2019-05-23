package com.infostroy.borysov.springtask.dao;

import com.infostroy.borysov.springtask.entity.Field;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FieldRepository extends JpaRepository<Field, Long> {

    @Query("select f from Field f where (f.isDeleted = null or  f.isDeleted = false) and f.isActive = true order by f.id")
    List<Field> findAllDisplayed();

    List<Field> findAllByOrderById();

    Page<Field> findAllByIsDeletedFalseOrIsDeletedIsNull(Pageable pageable);

    @Query("select f from Field f where (f.isDeleted = null or  f.isDeleted = false) and f.id = :id")
    Optional<Field> findByIdAndIsDeletedFalseOrIsDeletedIsNull(@Param("id") Long id);
}