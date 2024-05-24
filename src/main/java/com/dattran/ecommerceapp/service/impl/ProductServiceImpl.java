package com.dattran.ecommerceapp.service.impl;

import com.dattran.ecommerceapp.aws.S3Service;
import com.dattran.ecommerceapp.dto.ProductDTO;
import com.dattran.ecommerceapp.entity.*;
import com.dattran.ecommerceapp.enumeration.ResponseStatus;
import com.dattran.ecommerceapp.exception.AppException;
import com.dattran.ecommerceapp.mapper.EntityMapper;
import com.dattran.ecommerceapp.repository.*;
import com.dattran.ecommerceapp.service.IProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements IProductService {
    @NonFinal
    private static final int MAX_SIZE_OF_IMAGE_10MB = 10 * 1024 * 1024;
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    FlavorRepository flavorRepository;
    EntityMapper entityMapper;
    ProductImageRepository productImageRepository;
    S3Service s3Service;
    @Transactional
    @Override
    public Product createProduct(ProductDTO productDTO) {
        if (productRepository.existsByName(productDTO.getName().trim())) {
            throw new AppException(ResponseStatus.PRODUCT_EXISTED);
        }
        Product product = entityMapper.toProduct(productDTO);
        Optional<Category> optionalCategory = categoryRepository.findByName(productDTO.getCategoryName());
        Category category = optionalCategory.orElseGet(() -> categoryRepository.save(Category.builder().name(productDTO.getCategoryName().trim()).build()));
        product.setCategory(category);
        String flavors = productDTO.getFlavors();
        Ingredient ingredient = entityMapper.toIngredient(productDTO);
        StringTokenizer stringTokenizer = new StringTokenizer(flavors, ",");
        Set<Flavor> flavorSet = new HashSet<>();
        while (stringTokenizer.hasMoreTokens()) {
            String currentFlavor = stringTokenizer.nextToken().trim();
            Optional<Flavor> optionalFlavor = flavorRepository.findByName(currentFlavor);
            Flavor flavor = optionalFlavor.orElseGet(() -> flavorRepository.save(Flavor.builder().name(currentFlavor).build()));
            flavorSet.add(flavor);
        }
        ingredient.setFlavors(flavorSet);
        product.setIngredient(ingredient);
        ProductDetail productDetail = entityMapper.toProductDetail(productDTO);
        product.setProductDetail(productDetail);
        return productRepository.save(product);
    }

    @Override
    public List<ProductImage> uploadImages(String productId, List<MultipartFile> files) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ResponseStatus.PRODUCT_NOT_FOUND));
        if (files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
            throw new AppException(ResponseStatus.PRODUCT_IMAGES_OVERLOAD);
        }
        List<ProductImage> productImages = new ArrayList<>();
        for (MultipartFile file : files) {
            if(file.getSize() == 0) {
                continue;
            }
            if (file.getSize() > MAX_SIZE_OF_IMAGE_10MB) {
                throw new AppException(ResponseStatus.IMAGE_SIZE_TOO_LARGE);
            }
            String contentType = file.getContentType();
            if(contentType == null || !contentType.startsWith("image/")) {
                throw new AppException(ResponseStatus.UNSUPPORTED_FILE);
            }
            String path = s3Service.uploadFile(file, "product-images/");
            ProductImage productImage = ProductImage.builder().product(product).imageUrl(path).build();
            productImages.add(productImageRepository.save(productImage));
        }
        return productImages;
    }
}