package kg.alatoo.ecommerce.services.impl;

import kg.alatoo.ecommerce.dto.product.*;
import kg.alatoo.ecommerce.entities.*;
import kg.alatoo.ecommerce.enums.Color;
import kg.alatoo.ecommerce.enums.Size;
import kg.alatoo.ecommerce.enums.Tag;
import kg.alatoo.ecommerce.exceptions.BadCredentialsException;
import kg.alatoo.ecommerce.exceptions.BadRequestException;
import kg.alatoo.ecommerce.exceptions.NotFoundException;
import kg.alatoo.ecommerce.mappers.ProductMapper;
import kg.alatoo.ecommerce.repositories.*;
import kg.alatoo.ecommerce.services.AuthService;
import kg.alatoo.ecommerce.services.ImageService;
import kg.alatoo.ecommerce.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    private final ImageService imageService;
    private final ImageRepository imageRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;


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

        user.getProducts().remove(product.get());
        product.get().setUser(null);
        Category category = product.get().getCategory();
        category.getProducts().remove(product.get());
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

    @Override
    public void uploadFile(String token, MultipartFile file, Long id) {
        User user = authService.getUserFromToken(token);
        Product product = productRepository.findById(id).orElseThrow(() ->
                new NotFoundException("product not found!" + id, HttpStatus.NOT_FOUND));
        if(user != product.getUser())
            throw new BadRequestException("You can't edit this product!");
        Image save;
        List<Order> orders = null;
        List<CartItem> items = null;
        if(product.getImage() != null){
            Image image = product.getImage();
            items = image.getItems();
            orders = image.getOrders();
            save = imageService.uploadFile(file, image);
            if(items != null){
                List<CartItem> itemList = new ArrayList<>();
                for(CartItem item: items){
                    item.setImage(save);
                    itemList.add(item);
                    cartItemRepository.save(item);
                }
                save.setItems(itemList);
            }
            if(orders != null){
                List<Order> orderList = new ArrayList<>();
                for(Order order: orders){
                    order.setImage(save);
                    orderList.add(order);
                    orderRepository.save(order);
                }
                save.setOrders(orderList);
            }
        } else save = imageService.uploadFile(file);

        product.setImage(save);
        productRepository.save(product);
        save.setProduct(product);
        imageRepository.save(save);
    }

}

