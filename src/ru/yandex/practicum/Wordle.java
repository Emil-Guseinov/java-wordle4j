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
        PrintWriter logger = new PrintWriter(System.out);
        try (FileOutputStream fos = new FileOutputStream(LOG_FILE_NAME);
             Writer writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
             PrintWriter log = new PrintWriter(writer, true);
             Scanner scanner = new Scanner(System.in)) {

            logger = log;

            WordleDictionaryLoader loader = new WordleDictionaryLoader(logger);
            WordleDictionary dictionary = loader.loadFromFile(WORDS_FILE_NAME);

            printMenu();

            boolean isRunning = true;
            while (isRunning) {
                System.out.println("Введите команду: 1 Играть  0 Выход");
                String input = scanner.nextLine();

                switch (input) {
                    case "1":
                        System.out.println("Игра началась!");
                        WordleGame game = new WordleGame(dictionary, logger);
                        boolean isWin = false;

                        while (!game.isGameOver()) {
                            System.out.println("Попытка " + (game.getAttempts() + 1) + ": Введите слово");
                            String guess = scanner.nextLine();

                            String result = game.processGuess(guess);

                            if (result.startsWith("Победа")) {
                                System.out.println("Победа, слово " + game.getSecretWord());
                                isWin = true;
                                break;

                            } else if (result.startsWith("[Ошибка]")) {
                                System.out.println(result.replace("[Ошибка] ", ""));

                            } else {
                                System.out.println("--> " + result);
                            }
                        }
                        if (!isWin && game.isGameOver()) {
                            System.out.println("Вы проиграли. Слово: " + game.getSecretWord());
                        }
                        break;

                    case "0":
                        logger.println("Пользователь завершил программу");
                        isRunning = false;
                        break;

                    default:
                        System.out.println("Вы ввели неверно");
                        logger.println("Пользователь ввел неверную команду");
                }
            }
        } catch (Exception exception) {

            logger.println("[Ошибка] Сообщение: " + exception.getMessage());
            exception.printStackTrace(logger);
            logger.flush();

        }

    }

    public static void printMenu() {
        System.out.println("Добро пожаловать в Wordle");
        System.out.println("Слово будет загадано автоматически из 5 букв");
        System.out.println("Будет доступно 6 попыток");
    }
}
