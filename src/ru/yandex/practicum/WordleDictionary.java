package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class WordleDictionary {

    private final List<String> words = new ArrayList<>();
    private final Random random = new Random();

    public void addAll(Collection<String> addAllWords) {
        for (String word : addAllWords) {
            if (word == null) continue;
            String treason = word.trim().toLowerCase().replace("ё", "е");
            if (treason.length() == 5) {
                this.words.add(treason);
            }
        }
    }

    public void userGuess(String guess) throws WordleException {
        if (guess == null) {
            throw new WordleException("Ввод не может быть пустым");
        }
        String normalGuess = guess.trim().toLowerCase().replace("ё", "е");

        if (!normalGuess.matches("[а-я]+")) {
            throw new WordleException("Слово должно состоять из РуССких букв");
        }
        if (normalGuess.length() != 5) {
            throw new InvalidWordLengthException("Слово должно быть из 5 букв!");
        }
        if (!this.words.contains(normalGuess)) {

            throw new WordNotFoundException("Нету такого слова");
        }
    }

    public List<String> getAllWords() {
        return new ArrayList<>(words);

    }

    public String getRandomWord() {
        if (words.isEmpty()) {
            throw new IllegalStateException("Словарь пуст!");
        }

        int index = random.nextInt(words.size());
        return words.get(index);

    }

}
