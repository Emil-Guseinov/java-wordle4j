package ru.yandex.practicum;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Wordle {
    public static final String WORDS_FILE_NAME = "words_ru.txt";
    private static final String LOG_FILE_NAME = "log.txt";

    public static void main(String[] args) {

        try (FileOutputStream fos = new FileOutputStream(LOG_FILE_NAME);
             Writer writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
             PrintWriter logger = new PrintWriter(writer, true);
             Scanner scanner = new Scanner(System.in)) {

            WordleDictionaryLoader loader = new WordleDictionaryLoader(logger);
            WordleDictionary dictionary = loader.loadFromFile(WORDS_FILE_NAME);

            boolean isRunning = true;
            printMenu();

            while (isRunning) {
                System.out.println("Введите команду: 1 Играть  0 Выход");
                String input = scanner.nextLine();
                switch (input) {
                    case ("1"):
                        WordleGame game = new WordleGame(dictionary, logger);
                        game.start(scanner);
                        break;

                    case ("0"):
                        isRunning = false;
                        break;

                    default:
                        System.out.println("Вы ввели неверно");
                        break;
                }
            }

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

    }

    public static void printMenu() {
        System.out.println("Добро пожаловать в Wordle");
        System.out.println("Слово будет загадано автоматически из 5 букв");
        System.out.println("Будет доступно 6 попыток");
    }
}
