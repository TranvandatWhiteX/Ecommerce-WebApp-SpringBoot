package com.dattran.ecommerceapp.repository;

import com.dattran.ecommerceapp.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishListRepository extends JpaRepository<WishList, String> {
    boolean existsByUserIdAndProductId(String userId, String productId);
    List<WishList> findByUserId(String userId);
}
