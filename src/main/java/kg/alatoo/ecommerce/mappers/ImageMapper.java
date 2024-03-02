package kg.alatoo.ecommerce.mappers;

import kg.alatoo.ecommerce.dto.image.ImageResponse;
import kg.alatoo.ecommerce.entities.Image;

public interface ImageMapper {
    ImageResponse toDetailDto(Image image);
}
