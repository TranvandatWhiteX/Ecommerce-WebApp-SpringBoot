package com.dattran.ecommerceapp.repository;

import com.dattran.ecommerceapp.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {
    boolean existsByName(String name);
    List<Product> findTop4ByOrderByCreatedAtDesc();
    List<Product> findByCategoryId(String categoryId);
//    @Query("SELECT p FROM Product p WHERE " +
//            "(:categoryId IS NULL OR p.category.id = :categoryId) " +
//            "AND (:keyword IS NULL OR :keyword = '' OR p.name LIKE %:keyword%)")
//    Page<Product> searchProducts
//            (@Param("categoryId") String categoryId,
//             @Param("keyword") String keyword, Pageable pageable);
    Long countByCategoryId(String categoryId);
    List<Product> findAllByIsDeleted(Boolean isDeleted);

    @Query(value = "SELECT * FROM products p WHERE YEAR(p.created_at) = :year AND p.solved = (SELECT MAX(p2.solved) FROM products p2 WHERE YEAR(p2.created_at) = :year)", nativeQuery = true)
    List<Product> findProductWithMaxSolvedByYear(@Param("year") int year);

    @Query(value = "SELECT * FROM products p WHERE " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))", nativeQuery = true)
    List<Product> search(@Param("keyword") String keyword);
}
