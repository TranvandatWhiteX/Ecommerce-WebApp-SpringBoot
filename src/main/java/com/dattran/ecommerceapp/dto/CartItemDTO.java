package com.dattran.ecommerceapp.dto;
import com.dattran.ecommerceapp.entity.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemDTO {
    @JsonProperty("product")
    Product product;
    @JsonProperty("quantity")
    Integer quantity;
    @JsonProperty("flavorName")
    String flavorName;
}
