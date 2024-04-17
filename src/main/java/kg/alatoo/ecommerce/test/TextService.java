package kg.alatoo.ecommerce.test;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface TextService {
    TextResponse getSpeechToText(MultipartFile mp3File) throws IOException;
}
