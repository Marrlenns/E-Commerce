package kg.alatoo.ecommerce.test;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TextServiceImpl implements TextService{

    private final RestTemplate restTemplate;

    @Override
    public TextResponse getSpeechToText(MultipartFile mp3File) throws IOException {
        // Подготовка URL и заголовков для запроса
        String url = "https://asr.ulut.kg/api/receive_data";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        // Добавить другие заголовки, если это необходимо, например, ключ API
        headers.set("Authorization", "Bearer 87f1467af3fb14548d74cc0e806434f338a277350177669156c3ea0e8b0ed11c6e90630c98ec6a978a699f59ae47abaaa9c51b337812ef46473a31e2e4545b82");

        HttpEntity<MultiValueMap<String, Object>> requestEntity = getMultiValueMapHttpEntity(mp3File, headers);

        // Отправка POST запроса
        ResponseEntity<TextResponse> response = restTemplate.postForEntity(url, requestEntity, TextResponse.class);

        // Возврат полученного текста
        return Objects.requireNonNull(response.getBody());

        // Подготовка файла

    }

    private static HttpEntity<MultiValueMap<String, Object>> getMultiValueMapHttpEntity(MultipartFile mp3File, HttpHeaders headers) throws IOException {
        Resource resource = new ByteArrayResource(mp3File.getBytes()) {
            @Override
            public String getFilename() {
                return mp3File.getOriginalFilename();
            }
        };

        // Создание MultiValueMap для отправки файла
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("audio", resource);

        // Создание HttpEntity
        return new HttpEntity<>(body, headers);
    }
}
