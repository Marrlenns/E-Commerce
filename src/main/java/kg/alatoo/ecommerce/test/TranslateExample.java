//package kg.alatoo.ecommerce.test;
//
//import com.google.cloud.translate.*;
//import com.google.cloud.translate.Translate.TranslateOption;
//
//public class TranslateExample {
//    public static void main(String[] args) {
//        // Инициализируйте клиент API
////        Translate translate = TranslateOptions.getDefaultInstance().getService();
//        Translate translate = TranslateOptions.newBuilder().setApiKey("AIzaSyCwejMMJCXhfTpQRX-733ZiS6zBpGjsz9I").build().getService();
//
//
//        // Текст для перевода и язык перевода
//        String text = "Эрдан кыргыз тилин мыкты билет!!!";
//        Translation translation = translate.translate(text, TranslateOption.targetLanguage("ru"));
//        Translation translation1 = translate.translate(text, TranslateOption.targetLanguage("fr"));
//
//        // Выведите переведенный текст
//        System.out.println(text);
//
//        System.out.println("Перевод: ru\n" + translation.getTranslatedText());
//        System.out.println("Перевод: fr\n" + translation1.getTranslatedText());
//    }
//}
