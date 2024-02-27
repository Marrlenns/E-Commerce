package kg.alatoo.ecommerce.controllers;

import kg.alatoo.ecommerce.dto.product.ProductResponse;
import kg.alatoo.ecommerce.services.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/favorite")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/add")
    public String add(@RequestHeader("Authorization") String token, @RequestParam Long id){
        favoriteService.add(token, id);
        return "Product added to favorites!";
    }

    @GetMapping("/all")
    public List<ProductResponse> all(@RequestHeader("Authorization") String token){
        return favoriteService.all(token);
    }

    @DeleteMapping("/delete")
    public String delete(@RequestHeader("Authorization") String token, @RequestParam Long id){
        favoriteService.delete(token, id);
        return "Product deleted from your favorites!";
    }

}
