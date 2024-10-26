package oop;

import java.util.Scanner;

public class EncryptionDecryption {
    public static void runEncryptionDecryption(Scanner scanner) {
        while (true) {
            // Şifreleme/Çözme menüsü
            System.out.println("\nMetin Şifreleme/Çözme");
            System.out.println("1. Şifreleme");
            System.out.println("2. Şifre Çözme");
            System.out.println("3. Ana Menüye Dön");
            System.out.print("Seçiminizi yapın: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Enter tuşunu temizlemek için

            // Ana menüye dön
            if (choice == 3) {
                System.out.println("Ana menüye dönülüyor...");
                break;
            }

            // Kaydırma değerini al
            System.out.print("Lütfen kaydırma değerini girin (-26 ile 26 arasında): ");
            int shift = scanner.nextInt();
            scanner.nextLine(); // Enter tuşunu temizlemek için

            // Kaydırma değerinin geçerliliğini kontrol et
            if (shift < -26 || shift > 26) {
                System.out.println("Geçersiz kaydırma değeri. Lütfen -26 ile 26 arasında bir değer girin.");
                continue;
            }

            // Metni al
            System.out.print("Metni girin: ");
            String text = scanner.nextLine();

            // Metni şifrele veya çöz
            String result = processText(choice, shift, text);

            // Sonucu göster
            if (choice == 1) {
                System.out.println("Şifrelenmiş metin: " + result);
            } else {
                System.out.println("Çözülmüş metin: " + result);
            }
        }
    }

    private static String processText(int choice, int shift, String text) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char character = text.charAt(i);

            if (Character.isLetter(character)) {
                char mainLetter = Character.isUpperCase(character) ? 'A' : 'a';
                int diff = character - mainLetter;
                int newDiff = (choice == 1) ? (diff + shift + 26) % 26 : (diff - shift + 26) % 26;
                result.append((char) (mainLetter + newDiff));
            } else {
                result.append(character);
            }
        }
        return result.toString();
    }
}
