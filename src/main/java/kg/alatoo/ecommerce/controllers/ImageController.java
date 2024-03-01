package kg.alatoo.ecommerce.controllers;

import kg.alatoo.ecommerce.entities.Image;
import kg.alatoo.ecommerce.repositories.ImageRepository;
import kg.alatoo.ecommerce.services.ImageService;
import kg.alatoo.ecommerce.services.ProductService;
import kg.alatoo.ecommerce.services.impl.ImageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class ImageController {

    @Autowired
    private ImageServiceImpl imageServiceImpl;
    private final ImageRepository imageRepository;
    private final ProductService productService;
    private final ImageService imageService;

    @PostMapping("/upload/{productId}")
    public String uploadFile(@RequestHeader("Authorization") String token, @RequestParam(value = "file") MultipartFile file, @PathVariable Long productId) {
        productService.uploadFile(token, file, productId);
//        imageService.uploadFile(file);
        return "Image uploaded successfully!";
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName) {
        byte[] data = imageService.downloadFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @DeleteMapping("/delete/{fileName}")
    public String deleteFile(@PathVariable String fileName) {
        imageService.deleteFile(fileName);
        return "Image deleted successfully!";
    }

    @GetMapping("{id}")
    public Image getById(@PathVariable Long id){
        return imageRepository.findById(id).get();
    }

}
