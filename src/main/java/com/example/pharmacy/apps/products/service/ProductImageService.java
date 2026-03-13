package com.example.pharmacy.apps.products.service;

import com.example.pharmacy.apps.common.dto.response.CloudinaryResponse;
import com.example.pharmacy.apps.common.service.CloudinaryService;
import com.example.pharmacy.apps.products.dto.response.ProductImageDetailsResponse;
import com.example.pharmacy.apps.products.mapper.ProductMapper;
import com.example.pharmacy.apps.products.model.Product;
import com.example.pharmacy.apps.products.model.ProductImage;
import com.example.pharmacy.apps.products.repo.ProductImageRepo;
import com.example.pharmacy.apps.products.repo.ProductRepo;
import com.example.pharmacy.exception.custom.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductImageService {


    private final CloudinaryService cloudinaryService;
    private final ProductRepo productRepo;
    private final ProductImageRepo productImageRepo;
    private final ProductMapper productMapper;

    @Transactional
    public void uploadProductImage(MultipartFile file, UUID id){
        Product product = productRepo.findById(id)
                .orElseThrow(()-> new NotFoundException("Product Not Found"));

        ProductImage newProductImage = new ProductImage();

        CloudinaryResponse responseDto = cloudinaryService
                .upload(file, "product_images_folder");

        newProductImage.setProduct(product);
        newProductImage.setUrl(responseDto.url());
        newProductImage.setPublicId(responseDto.publicId());
        productImageRepo.save(newProductImage);

        product.getImages().add(newProductImage);
        productRepo.save(product);
    }

    public void deleteProductImage(String publicId, UUID id){
        Product product = productRepo.findById(id)
                .orElseThrow(()-> new NotFoundException("Product Not Found"));
        ProductImage productImage = productImageRepo.findByProduct(product)
                .orElseThrow(()-> new NotFoundException("Product Image Object Not Found"));

        cloudinaryService.delete(publicId);
        product.getImages().remove(productImage);
        productImageRepo.delete(productImage);
    }

    public ProductImageDetailsResponse getProductImage(UUID id){
        ProductImage productImage = productImageRepo.findById(id)
                .orElseThrow(()-> new NotFoundException("Product Image Object Not Found"));

        return productMapper.toImageDto(productImage);
    }
}
