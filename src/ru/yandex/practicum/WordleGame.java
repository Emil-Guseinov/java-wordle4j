package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.*;

public class WordleGame {

    private final String secretWord;
    private int attempts = 0;
    private static final int MAX_ATTEMPTS = 6;
    private final WordleDictionary dictionary;
    private final PrintWriter logger;
    private final Map<Integer, Character> positions = new HashMap<>();
    private final Set<Character> mustHave = new HashSet<>();
    private final Set<Character> excluded = new HashSet<>();

    public WordleGame(WordleDictionary dictionary, PrintWriter logger) {
        this.dictionary = dictionary;
        this.logger = logger;
        this.secretWord = dictionary.getRandomWord();
        logger.println("Начало игры,загадано слово " + secretWord);
    }

    public void start(Scanner scanner) {

        while (attempts < MAX_ATTEMPTS) {
            System.out.println("Попытка: " + (attempts + 1));
            String guess = scanner.nextLine().trim().toLowerCase();
            String hint;

            if (guess.isEmpty()) {
                hint = findAutoHint();
                if (hint.equals("Подходящих слов нет")) {
                    continue;
                }
                System.out.println("Компьютер делает ход " + hint);
                logger.println("Компьютер начал ходить " + hint);

            } else {
                hint = guess;
            }

            try {
                dictionary.userGuess(hint);

                String input = attemptWords(hint);
                System.out.println("> " + input);

                if (hint.equals(secretWord)) {
                    System.out.println("Поздравляем вы отгадали слово " + secretWord);
                    logger.println("Победа на попытке " + (attempts + 1));
                    logger.println("работа завершена");
                    return;
                }

                attempts++;
            } catch (WordleException e) {

                System.out.println(e.getMessage());
                logger.println("[Ошибка] Сообщение: " + e.getMessage());
                e.printStackTrace(logger);
                logger.flush();


            }
        }
        System.out.println("Попытки кончились, загаданное слово " + secretWord);
        logger.println("Попытки кончились");
        logger.close();
    }

    public String attemptWords(String guess) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            char g = guess.charAt(i);
            char c = secretWord.charAt(i);

            if (g == c) {
                b.append("+");
                positions.put(i, g);

            } else if (secretWord.contains(String.valueOf(g))) {
                b.append("^");
                mustHave.add(g);
            } else {
                b.append("-");
                if (!secretWord.contains(String.valueOf(g))) {
                    excluded.add(g);
                }
            }

        }
        return b.toString();
    }

    private boolean isValidHint(String word) {
        if (word.length() != 5) {
            return false;
        }
        for (Map.Entry<Integer, Character> entry : positions.entrySet()) {
            if (word.charAt(entry.getKey()) != entry.getValue()) {
                return false;
            }
        }
        for (char c : mustHave) {
            if (word.indexOf(c) == -1) {
                return false;
            }
        }
        for (char c : excluded) {
            if (word.indexOf(c) != -1 && !mustHave.contains(c) && !positions.containsValue(c)) {
                return false;
            }
        }
        return true;
    }

    public String findAutoHint() {
        List<String> allWords = dictionary.getAllWords();
        Collections.shuffle(allWords);
        for (String word : allWords) {
            if (isValidHint(word)) {
                logger.println("Нашли подходящее слово " + word);
                return word;
            }
        }
        logger.println("Подходящих слов нет");
        return "Подходящих слов нет";
    }
}
