package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.*;

public class WordleGame {

    private final String secretWord;
    private int attempts = 0;
    private static final int MAX_ATTEMPTS = 6;
    private final WordleDictionary dictionary;
    private final PrintWriter logger;
    private final Set<String> usedWords = new HashSet<>();
    private final Set<Character> excluded = new HashSet<>();
    private final Set<Character> required = new HashSet<>();
    private final Map<Integer, Character> fixed = new HashMap<>();

    public WordleGame(WordleDictionary dictionary, PrintWriter logger) {
        this.dictionary = dictionary;
        this.logger = logger;
        this.secretWord = dictionary.getRandomWord();
        logger.println("Начало игры,загадано слово " + secretWord);
    }

    public int getAttempts() {
        return attempts;
    }

    public String processGuess(String input) {

        String guess = input.trim().toLowerCase().replace("ё", "е");

        if (guess.isEmpty()) {
            String hint = findAutoHint();
            if (hint == null) {
                attempts = MAX_ATTEMPTS;
                return "Нету возможных слов";

            }
            guess = hint;
            logger.println("Компьютер начал ходить ");

        }

        if (!usedWords.add(guess)) {
            return "Слово уже использовано";
        }
        try {
            dictionary.userGuess(guess);
            String result = attemptWords(guess);
            updateState(guess, result);

            if (guess.equals(secretWord)) {
                logger.println("Победа на попытке " + (attempts + 1));
                logger.println("работа завершена");
                return "Победа " + secretWord;
            }

            attempts++;
            return result;

        } catch (WordleException e) {

            logger.println("[Ошибка] Сообщение: " + e.getMessage());
            return "[Ошибка] " + e.getMessage();
        }

    }

    public boolean isGameOver() {
        return attempts >= MAX_ATTEMPTS;

    }

    public String getSecretWord() {
        return secretWord;
    }

    public String attemptWords(String guess) {

        Map<Character, Integer> remaining = new HashMap<>();

        for (char c : secretWord.toCharArray()) {
            remaining.put(c, remaining.getOrDefault(c, 0) + 1);
        }
        char[] result = new char[5];
        Arrays.fill(result, '-');

        for (int i = 0; i < 5; i++) {
            if (guess.charAt(i) == secretWord.charAt(i)) {
                result[i] = '+';
                char c = guess.charAt(i);
                remaining.put(c, remaining.get(c) - 1);
            }
        }
        for (int i = 0; i < 5; i++) {
            if (result[i] == '+') continue;
            char g = guess.charAt(i);

            if (remaining.getOrDefault(g, 0) > 0) {
                result[i] = '^';
                remaining.put(g, remaining.get(g) - 1);

            }
        }
        return new String(result);
    }

    private void updateState(String guess, String result) {

        for (int i = 0; i < guess.length(); i++) {
            char g = guess.charAt(i);
            char r = result.charAt(i);

            if (r == '+') {
                fixed.put(i, g);
            } else if (r == '^') {
                required.add(g);
            } else if (r == '-') {
                excluded.add(g);
                if (!required.contains(g) && !fixed.containsValue(g)) {
                    excluded.add(g);
                }
            }
        }
    }

    private Boolean isValid(String word) {

        if (word.length() != 5) return false;

        for (char c : excluded) {
            if (word.indexOf(c) != -1) return false;
        }

        for (char c : required) {
            if (!word.contains(String.valueOf(c))) return false;
        }

        for (Map.Entry<Integer, Character> e : fixed.entrySet()) {
            if (word.charAt(e.getKey()) != e.getValue()) return false;
        }

        return true;
    }

    public String findAutoHint() {
        List<String> allWords = dictionary.getAllWords();
        Collections.shuffle(allWords);
        for (String word : allWords) {
            if (!usedWords.contains(word) && isValid(word)) {
                logger.println("Нашли подходящее слово " + word);
                return word;
            }
        }
        logger.println("Подходящих слов нет");
        return null;
    }
}
