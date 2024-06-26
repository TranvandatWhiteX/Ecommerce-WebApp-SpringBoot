package com.dattran.ecommerceapp.repository;

import com.dattran.ecommerceapp.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, String> {
    boolean existsByName(String name);
    Optional<Category> findByName(String name);
    List<Category> findAllByIsDeleted(boolean isDeleted);
}
