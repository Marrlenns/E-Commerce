package kg.alatoo.ecommerce.controllers;

import kg.alatoo.ecommerce.dto.product.CategoryRequest;
import kg.alatoo.ecommerce.dto.product.ProductRequest;
import kg.alatoo.ecommerce.services.ProductService;
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
    public String addProduct(@RequestBody ProductRequest request){
        productService.addProduct(request);
        return "Product: " + request.getTitle() + " - added successfully!";
    }
    @GetMapping("/update/{id}")
    public void updateById(@PathVariable Long id,@RequestBody ProductRequest productRequest){
        productService.updateById(id,productRequest);
    }
}
