package kg.alatoo.ecommerce.controllers;


import kg.alatoo.ecommerce.dto.cart.AddToCartRequest;
import kg.alatoo.ecommerce.repositories.ProductRepository;
import kg.alatoo.ecommerce.services.CartService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {

    private final ProductRepository productRepository;
    private final CartService cartService;

    @PostMapping("/add")
    public String addToCart(@RequestBody AddToCartRequest request, @RequestHeader("Authorization") String token){
        cartService.add(request, token);
        return productRepository.findById(request.getProductId()).get().getTitle() + " added to your cart!";
    }
}
