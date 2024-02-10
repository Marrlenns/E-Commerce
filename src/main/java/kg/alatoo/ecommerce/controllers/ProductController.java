package kg.alatoo.ecommerce.controllers;

import io.swagger.annotations.Authorization;
import kg.alatoo.ecommerce.dto.product.*;
import kg.alatoo.ecommerce.services.ProductService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/add/category")
    public String addCategory(@RequestBody CategoryRequest request){
        productService.addCategory(request);
        return "Category: " + request.getTitle() + " - added successfully!";
    }

    @PostMapping("/add")
    public String addProduct(@RequestBody ProductRequest request, @RequestHeader("Authorization") String token){
        productService.addProduct(request, token);
        return "Product: " + request.getTitle() + " - added successfully!";
    }
    @PutMapping("/update/{id}")
    public String updateById(@PathVariable Long id,@RequestBody ProductRequest productRequest, @RequestHeader("Authorization") String token){
        productService.updateById(id,productRequest, token);
        return "Product with id: " + id + " - updated successfully!";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id, @RequestHeader("Authorization") String token){
        productService.deleteById(id, token);
        return "Product with id: " + id + " - deleted successfully!";
    }

    @GetMapping("/{id}")
    public ProductDetailResponse showById(@PathVariable Long id){
        return productService.showById(id);
    }

    @GetMapping("/all")
    public List<ProductResponse> all(){
        return productService.all();
    }

    @GetMapping("/all/{id}")
    public List<ProductResponse> allByOwner(@PathVariable Long id){
        return productService.allByOwner(id);
    }

    @PostMapping("/{id}/add/review")
    public String addReview(@RequestBody ReviewRequest request, @PathVariable Long id, @RequestHeader("Authorization") String token){
        productService.addReview(id, token, request);
        return "Review added successfully!";
    }
}
