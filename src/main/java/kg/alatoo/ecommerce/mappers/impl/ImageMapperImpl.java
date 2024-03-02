package kg.alatoo.ecommerce.mappers.impl;

import kg.alatoo.ecommerce.dto.image.ImageResponse;
import kg.alatoo.ecommerce.entities.Image;
import kg.alatoo.ecommerce.mappers.ImageMapper;
import org.springframework.stereotype.Component;

@Component
public class ImageMapperImpl implements ImageMapper {
    @Override
    public ImageResponse toDetailDto(Image image) {
        ImageResponse response = new ImageResponse();
        response.setId(image.getId());
        response.setPath(image.getPath());
        response.setName(image.getName());
        response.setProductId(image.getProduct().getId());
        return response;
    }
}
