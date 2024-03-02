package kg.alatoo.ecommerce.services.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import jakarta.transaction.Transactional;
import kg.alatoo.ecommerce.dto.image.ImageResponse;
import kg.alatoo.ecommerce.entities.CartItem;
import kg.alatoo.ecommerce.entities.Image;
import kg.alatoo.ecommerce.entities.Order;
import kg.alatoo.ecommerce.exceptions.NotFoundException;
import kg.alatoo.ecommerce.mappers.ImageMapper;
import kg.alatoo.ecommerce.repositories.CartItemRepository;
import kg.alatoo.ecommerce.repositories.ImageRepository;
import kg.alatoo.ecommerce.repositories.OrderRepository;
import kg.alatoo.ecommerce.services.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    @Value("${application.bucket.name}")
    private String bucketName;

    @Value("${location.path}")
    private String PATH;

    @Autowired
    private AmazonS3 s3Client;

    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public Image uploadFile(MultipartFile file, Image oldDocument)  {
        if (oldDocument != null) {
            deleteFile(oldDocument.getId());
        }
        return save(file);
    }

    @Override
    public Image uploadFile(MultipartFile file)  {
        return save(file);
    }

    private Image save( MultipartFile file) {
        Image image = new Image();

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        // Normalize file name
        if (file.getOriginalFilename()!=null){
            fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        }

        // Check if the file's name contains invalid characters
        if (fileName.contains("..")) {
            throw new NotFoundException("Sorry! Filename contains invalid path sequence " + fileName, HttpStatus.NOT_FOUND);
        }

        fileName = validateFileName(fileName);

        image.setName(fileName);

//        uploadFileToS3Bucket(file);
        log.info("File with name = {} has successfully uploaded", image.getName());
        Image image1 = imageRepository.saveAndFlush(image);
        String url = PATH+image1.getId();

        image1.setPath(url);
        return imageRepository.saveAndFlush(image1);
    }

    private String validateFileName(String fileName) {
        // Get file name without extension
        String fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf("."));
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));

        LocalDateTime time = LocalDateTime.now(Clock.systemUTC());
        Random random = new Random();

        if (fileNameWithoutExtension.contains("_")) {
            String fileNameWithoutDate = fileName.substring(0, fileName.indexOf("_")) + fileExtension;
            fileExtension = fileName.substring(fileName.lastIndexOf("."));
            fileNameWithoutExtension = fileNameWithoutDate.substring(0, fileNameWithoutDate.lastIndexOf("."));
            fileName = fileNameWithoutExtension + "_" + LocalDate.now() + "_" + time.getHour() +
                    "-" + time.getMinute() + "-" + random.nextInt(10) + fileExtension;

            return fileName;
        }

        fileName = fileNameWithoutExtension + "**_" + LocalDate.now() + "_" + time.getHour() +
                "-" + time.getMinute() + "-" + random.nextInt(10) + fileExtension;

        return fileName;
    }


    @Override
    public void uploadFileToS3Bucket(MultipartFile file){
        File fileObj = convertMultiPartFileToFile(file);
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
        fileObj.delete();
    }

    @Override
    public byte[] downloadFile(String fileName) {
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteFile(Long id) {
        Optional<Image> image = imageRepository.findById(id);
//        System.out.println(image);
        if(image.isEmpty())
            throw new NotFoundException("This image not found!", HttpStatus.NOT_FOUND);
        if(image.get().getProduct() != null){
            image.get().getProduct().setImage(null);
            image.get().setProduct(null);
        }
        if(image.get().getItems() != null){
            for(CartItem item: image.get().getItems()){
                item.setImage(null);
                cartItemRepository.save(item);
            }
            image.get().setItems(null);
        }
        if(image.get().getOrders() != null){
            for(Order order : image.get().getOrders()) {
                order.setImage(null);
                orderRepository.save(order);
            }
            image.get().setOrders(null);
        }
        imageRepository.delete(image.get());
//        s3Client.deleteObject(bucketName, fileName);
//        return fileName + " removed ...";
    }

    @Override
    public ImageResponse showById(Long id) {
        Optional<Image> image = imageRepository.findById(id);
        if(image.isEmpty())
            throw new NotFoundException("Image not found!", HttpStatus.NOT_FOUND);
        return imageMapper.toDetailDto(image.get());
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }
}
