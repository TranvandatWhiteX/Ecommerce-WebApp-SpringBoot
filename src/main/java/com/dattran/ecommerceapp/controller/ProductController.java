package com.dattran.ecommerceapp.controller;

import com.dattran.ecommerceapp.dto.CommentDTO;
import com.dattran.ecommerceapp.dto.ProductDTO;
import com.dattran.ecommerceapp.dto.response.HttpResponse;
import com.dattran.ecommerceapp.entity.*;
import com.dattran.ecommerceapp.enumeration.ResponseStatus;
import com.dattran.ecommerceapp.exception.AppException;
import com.dattran.ecommerceapp.service.ICommentService;
import com.dattran.ecommerceapp.service.IProductRedisService;
import com.dattran.ecommerceapp.service.IProductService;
import com.dattran.ecommerceapp.util.SecurityUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    IProductService productService;
    IProductRedisService productRedisService;
    ICommentService commentService;
    SecurityUtil securityUtil;

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

    @PostMapping(value = "/upload-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public HttpResponse uploadImages(@RequestParam String productId, @ModelAttribute("files") List<MultipartFile> files, HttpServletRequest httpServletRequest) {
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
//    @GetMapping("")
//    public HttpResponse getAllProducts(HttpServletRequest httpServletRequest) throws JsonProcessingException {
//        List<Product> products;
//        List<Product> productsFromRedis = productRedisService.getAllProducts();
//        if (productsFromRedis != null && !productsFromRedis.isEmpty()) {
//            products = productsFromRedis;
//        } else {
//            products = productService.getAllProducts();
//            productRedisService.saveAll(products);
//        }
//        HttpResponse httpResponse = HttpResponse.builder()
//                .timeStamp(LocalDateTime.now().toString())
//                .path(httpServletRequest.getRequestURI())
//                .requestMethod(httpServletRequest.getMethod())
//                .status(HttpStatus.OK)
//                .statusCode(ResponseStatus.GET_ALL_PRODUCTS_SUCCESSFULLY.getCode()).message(ResponseStatus.GET_ALL_PRODUCTS_SUCCESSFULLY.getMessage())
//                .data(Map.of("products", products))
//                .build();
//        return httpResponse;
//    }
    @GetMapping("")
    public HttpResponse getAllProducts(HttpServletRequest httpServletRequest) {
        List<Product> products = productService.getAllProducts();
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.OK)
                .statusCode(ResponseStatus.GET_ALL_PRODUCTS_SUCCESSFULLY.getCode())
                .message(ResponseStatus.GET_ALL_PRODUCTS_SUCCESSFULLY.getMessage())
                .data(Map.of("products", products))
                .build();
        return httpResponse;
    }
    @GetMapping("/product-detail/{productId}")
    public HttpResponse getProductById(@PathVariable String productId,HttpServletRequest httpServletRequest) {
        Product product = productService.getProductById(productId);
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.OK)
                .statusCode(ResponseStatus.GET_ALL_PRODUCTS_SUCCESSFULLY.getCode())
                .message(ResponseStatus.GET_ALL_PRODUCTS_SUCCESSFULLY.getMessage())
                .data(Map.of("product", product))
                .build();
        return httpResponse;
    }
    @PostMapping("/product-detail")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public HttpResponse createComment(@RequestBody @Valid CommentDTO commentDTO, HttpServletRequest httpServletRequest) {
        User loginUser = securityUtil.getLoggedInUserInfor();
        if(!Objects.equals(loginUser.getId(), commentDTO.getUserId())) {
            throw new AppException(ResponseStatus.USER_NOT_FOUND);
        }
        Comment comment = commentService.createComment(commentDTO);
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.CREATED)
                .statusCode(ResponseStatus.USER_CREATED.getCode())
                .message(ResponseStatus.USER_CREATED.getMessage())
                .data(Map.of("comment", comment))
                .build();
        return httpResponse;
    }

    @GetMapping("/get-top-4")
    public HttpResponse getTop4(HttpServletRequest httpServletRequest) {
        List<Product> products = productService.findTop4();
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.OK)
                .statusCode(ResponseStatus.GET_ALL_PRODUCTS_SUCCESSFULLY.getCode())
                .message(ResponseStatus.GET_ALL_PRODUCTS_SUCCESSFULLY.getMessage())
                .data(Map.of("products", products))
                .build();
        return httpResponse;
    }

    @PostMapping("/add-to-wish-list/{productId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public HttpResponse addToWishList(@PathVariable String productId, HttpServletRequest httpServletRequest) throws Exception {
        User loggedUser = securityUtil.getLoggedInUserInfor();
        WishList wishList = productService.addToWishList(loggedUser.getId(), productId);
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.OK)
                .statusCode(ResponseStatus.WISH_LIST_CREATED.getCode())
                .message(ResponseStatus.WISH_LIST_CREATED.getMessage())
                .data(Map.of("favorites", wishList))
                .build();
        return httpResponse;
    }
}
