package kg.alatoo.ecommerce.test;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class TextController {

    private final TextService textService;
    private final TextToSpeechService speechService;

    @PostMapping("/text")
    public TextResponse bye(MultipartFile mp3File) throws IOException {
        return textService.getSpeechToText(mp3File);
    }

    @GetMapping("/synthesize")
    public byte[] synthesize(@RequestParam String text) {
        return speechService.synthesizeText(text);
    }
}
