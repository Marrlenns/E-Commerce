package kg.alatoo.ecommerce.controllers;

import io.swagger.annotations.Authorization;
import kg.alatoo.ecommerce.dto.product.CategoryRequest;
import kg.alatoo.ecommerce.dto.product.ProductRequest;
import kg.alatoo.ecommerce.dto.product.ProductResponse;
import kg.alatoo.ecommerce.services.ProductService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/add/category")
    public String addCategory(@RequestBody CategoryRequest request){
        productService.addCategory(request);
        return "Category: " + request.getTitle() + " - added successfully!";
    }

    @PostMapping("/add/product")
    public String addProduct(@RequestBody ProductRequest request, @RequestHeader("Authorization") String token){
        productService.addProduct(request, token);
        return "Product: " + request.getTitle() + " - added successfully!";
    }
    @GetMapping("/update/{id}")
    public void updateById(@PathVariable Long id,@RequestBody ProductRequest productRequest){
        productService.updateById(id,productRequest);
    }
    @GetMapping("/show/{id}")
    public ProductResponse showById(@PathVariable Long id){
        return productService.showById(id);
    }
}
