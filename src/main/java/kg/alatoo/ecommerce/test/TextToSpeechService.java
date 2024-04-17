package kg.alatoo.ecommerce.test;

import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;

@Service
public class TextToSpeechService {
    public byte[] synthesizeText(String text) {
        System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", "/home/marlen/Desktop/hiroka-5effde0199dc.json");
        System.out.println("AAAAAAAAAAAAAAAAAAA" + System.getenv("GOOGLE_APPLICATION_CREDENTIALS"));
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
            SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();
            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode("en-US")
                    .setSsmlGender(SsmlVoiceGender.NEUTRAL)
                    .build();
            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.MP3)
                    .build();

            SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);
            ByteString audioContents = response.getAudioContent();

            return audioContents.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create speech output", e);
        }
    }
}
