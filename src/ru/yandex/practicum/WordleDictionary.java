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
            if (word.trim().length() == 5) {
                this.words.add(word.toLowerCase());
            }
        }
    }

    public boolean contains(String word) {
        if (word == null) {
            return false;
        }
        return words.contains(word.toLowerCase());
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
