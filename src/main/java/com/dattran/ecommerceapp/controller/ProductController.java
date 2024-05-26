package com.dattran.ecommerceapp.controller;

import com.dattran.ecommerceapp.dto.ProductDTO;
import com.dattran.ecommerceapp.dto.response.HttpResponse;
import com.dattran.ecommerceapp.entity.Product;
import com.dattran.ecommerceapp.entity.ProductImage;
import com.dattran.ecommerceapp.enumeration.ResponseStatus;
import com.dattran.ecommerceapp.service.IProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    IProductService productService;

    @PostMapping("/create-product")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public HttpResponse createProduct(@RequestBody @Valid ProductDTO productDTO, HttpServletRequest httpServletRequest) {
        Product product = productService.createProduct(productDTO);
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.CREATED)
                .statusCode(ResponseStatus.PRODUCT_CREATED.getCode())
                .message(ResponseStatus.PRODUCT_CREATED.getMessage())
                .data(Map.of("product", product))
                .build();
        return httpResponse;
    }

    @PostMapping(value = "/upload-images/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public HttpResponse uploadImages(@PathVariable("id") String productId, @ModelAttribute("files") List<MultipartFile> files, HttpServletRequest httpServletRequest) {
        List<ProductImage> productImages = productService.uploadImages(productId, files);
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.CREATED)
                .statusCode(ResponseStatus.UPLOAD_IMAGES_SUCCESSFULLY.getCode())
                .message(ResponseStatus.UPLOAD_IMAGES_SUCCESSFULLY.getMessage())
                .data(Map.of("files", productImages))
                .build();
        return httpResponse;
    }

    @GetMapping("")
    public HttpResponse getAllProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "9") int limit, HttpServletRequest httpServletRequest
    ) {
        PageRequest pageRequest = PageRequest.of(
                page-1, limit,
                Sort.by("createdAt").descending()
        );
        Page<Product> products = productService.getAllProducts(pageRequest);
        List<Product> productList = products.stream().toList();
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.OK)
                .statusCode(ResponseStatus.GET_ALL_PRODUCTS_SUCCESSFULLY.getCode())
                .message(ResponseStatus.GET_ALL_PRODUCTS_SUCCESSFULLY.getMessage())
                .data(Map.of("products", productList, "totalPages", products.getTotalPages()))
                .build();
        return httpResponse;
    }
}
