package com.dattran.ecommerceapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class DetailResponse{
    String productId;
    String productThumbnail;
    String productName;
    double price;
    int numberOfProducts;
    double totalMoney;
}