package kg.alatoo.ecommerce.services.impl;

import kg.alatoo.ecommerce.dto.product.*;
import kg.alatoo.ecommerce.entities.Category;
import kg.alatoo.ecommerce.entities.Product;
import kg.alatoo.ecommerce.entities.Review;
import kg.alatoo.ecommerce.entities.User;
import kg.alatoo.ecommerce.enums.Color;
import kg.alatoo.ecommerce.enums.Size;
import kg.alatoo.ecommerce.enums.Tag;
import kg.alatoo.ecommerce.exceptions.BadCredentialsException;
import kg.alatoo.ecommerce.exceptions.BadRequestException;
import kg.alatoo.ecommerce.exceptions.NotFoundException;
import kg.alatoo.ecommerce.mappers.ProductMapper;
import kg.alatoo.ecommerce.repositories.CategoryRepository;
import kg.alatoo.ecommerce.repositories.ProductRepository;
import kg.alatoo.ecommerce.repositories.ReviewRepository;
import kg.alatoo.ecommerce.repositories.UserRepository;
import kg.alatoo.ecommerce.services.AuthService;
import kg.alatoo.ecommerce.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final ProductMapper productMapper;
    private final ReviewRepository reviewRepository;


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
    public void addProduct(ProductRequest request, String token) {
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

        User user = authService.getUserFromToken(token);

        product.setCategory(isCategory.get());
        product.setSizes(request.getSizes());
        product.setColors(request.getColors());
        product.setTags(request.getTags());
        Product product1 = productRepository.saveAndFlush(product);
        connectUserProduct(product1, user);
        List<Product> products = new ArrayList<>();
        if(!isCategory.get().getProducts().isEmpty())
            products = isCategory.get().getProducts();
        products.add(product);
        isCategory.get().setProducts(products);
        categoryRepository.save(isCategory.get());
    }

    private void connectUserProduct(Product product, User user) {
        product.setUser(user);
        productRepository.save(product);

        List<Product> products = new ArrayList<>();
        if(!user.getProducts().isEmpty())
            products = user.getProducts();
        products.add(product);
        user.setProducts(products);
        userRepository.save(user);
    }

    @Override
    public void updateById(Long id, ProductRequest request, String token){
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty())
            throw new NotFoundException("Product with id: " + id + " - doesn't exist!", HttpStatus.BAD_REQUEST);
        User user = authService.getUserFromToken(token);
        if(product.get().getUser() != user)
            throw new BadCredentialsException("U can't update this product!");
        if(request.getTitle() != null)
            product.get().setTitle(request.getTitle());
        if(request.getPrice() != null)
            product.get().setPrice(request.getPrice());
        if(request.getDescription() != null)
            product.get().setDescription(request.getDescription());
        if(request.getColors() != null)
            product.get().setColors(request.getColors());
        if(request.getTags() != null)
            product.get().setTags(request.getTags());
        if(request.getSizes() != null)
            product.get().setSizes(request.getSizes());
        Optional<Category> isCategory = categoryRepository.findByTitle(request.getCategory());
        if(isCategory.isEmpty())
            throw new BadRequestException("This category doesn't exist!");
        product.get().setCategory(isCategory.get());
        Optional<Product> product1 = productRepository.findBySku(request.getSku());
        if(request.getSku() != null && (product1.isEmpty() || product1.get() == product.get()))
            product.get().setSku(request.getSku());
        else
            throw new BadRequestException("Product with this sku already exist!");
        productRepository.save(product.get());
    }
    @Override
    public void deleteById(Long id, String token) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty())
            throw new BadRequestException("Product with id: " + id + " - doesn't exist!");
        User user = authService.getUserFromToken(token);
        if(product.get().getUser() != user)
            throw new BadCredentialsException("U can't delete this product!");
        List<Product> products = user.getProducts();
        List<Product> newProducts = new ArrayList<>();
        for(Product product1: products)
            if(product1 != product.get())
                newProducts.add(product1);
        user.setProducts(newProducts);
        product.get().setUser(null);
        products = product.get().getCategory().getProducts();
        newProducts = new ArrayList<>();
        Category category = product.get().getCategory();
        for(Product product1: products)
            if(product1 != product.get()) {
                newProducts.add(product1);
            }
        category.setProducts(newProducts);
        product.get().setCategory(null);

        userRepository.save(user);
        productRepository.delete(product.get());
        categoryRepository.save(category);

    }

    @Override
    public List<ProductResponse> all() {
        List<Product> products = productRepository.findAll();
        return productMapper.toDtos(products);
    }

    @Override
    public List<ProductResponse> allByOwner(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty())
            throw new BadRequestException("This user doesn't exist!");
        List<Product> products = productRepository.findAllByUser(user.get());
        return productMapper.toDtos(products);
    }

    @Override
    public ProductDetailResponse showById(Long id){
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty())
            throw new NotFoundException("Product with id: "+id+" - doesn't found!", HttpStatus.BAD_REQUEST);
        return productMapper.toDetailDto(product.get());
    }

    @Override
    public ProductComparisonResponse compare(Long id, Long idd) {
        Optional<Product> product1 = productRepository.findById(id);
        Optional<Product> product2 = productRepository.findById(idd);
        if(product1.isEmpty())
            throw new BadRequestException("Product with id: " + id + " - doesn't exist!");
        if(product2.isEmpty())
            throw new BadRequestException("Product with id: " + idd + " - doesn't exist!");
        return productMapper.toCompareDtos(product1.get(), product2.get());
    }

}

