package kg.alatoo.ecommerce.services;

import jakarta.transaction.Transactional;
import kg.alatoo.ecommerce.dto.image.ImageResponse;
import kg.alatoo.ecommerce.entities.Image;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    @Transactional
    Image uploadFile(MultipartFile file, Image oldDocument);

    Image uploadFile(MultipartFile file);

    void uploadFileToS3Bucket(MultipartFile file);

    byte[] downloadFile(String fileName);

    void deleteFile(Long id);

    ImageResponse showById(Long id);
}
