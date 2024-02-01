package kg.alatoo.ecommerce.services.impl;

import kg.alatoo.ecommerce.dto.product.CategoryRequest;
import kg.alatoo.ecommerce.dto.product.ProductRequest;
import kg.alatoo.ecommerce.entities.Category;
import kg.alatoo.ecommerce.entities.Product;
import kg.alatoo.ecommerce.exceptions.BadRequestException;
import kg.alatoo.ecommerce.repositories.CategoryRepository;
import kg.alatoo.ecommerce.repositories.ProductRepository;
import kg.alatoo.ecommerce.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public void addCategory(CategoryRequest request) {
        Optional<Category> isCategory = categoryRepository.findByTitle(request.getTitle());
        if(isCategory.isPresent())
            throw new BadRequestException("This category already exists!");
        Category category = new Category();
        category.setTitle(request.getTitle());
        categoryRepository.save(category);
    }

    @Override
    public void addProduct(ProductRequest request) {
        Optional<Product> isProduct = productRepository.findBySku(request.getSku());
        if(isProduct.isPresent())
            throw new BadRequestException("Product with this SKU already exists!");
        Product product = new Product();
        product.setTitle(request.getTitle());
        product.setPrice(request.getPrice());
        product.setSku(request.getSku());
        product.setDescription(request.getDescription());
        Optional<Category> isCategory = categoryRepository.findByTitle(request.getCategory());
        if(isCategory.isEmpty())
            throw new BadRequestException("This category doesn't exist!");
        product.setCategory(isCategory.get());
        product.setSizes(request.getSizes());
        product.setColors(request.getColors());
        product.setTags(request.getTags());
        productRepository.save(product);

        ArrayList<Product> products = new ArrayList<>();
        if(!isCategory.get().getProducts().isEmpty())
            products = (ArrayList<Product>) isCategory.get().getProducts();
        products.add(product);
        isCategory.get().setProducts(products);
        categoryRepository.save(isCategory.get());
    }
}
