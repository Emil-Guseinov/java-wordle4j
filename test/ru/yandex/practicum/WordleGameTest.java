package ru.yandex.practicum;

import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleGameTest {

    @Test
    void CorrectSymbols() {

        WordleDictionary dic = new WordleDictionary();
        PrintWriter logger = new PrintWriter(System.out, true);
        dic.addAll(List.of("герой"));
        WordleGame game = new WordleGame(dic, logger);

        String result = game.attemptWords("глина");

        assertEquals("+----", result, "Символы подсказки рассчитаны не верно");
    }

    @Test
    void WrongLength() {
        WordleDictionary dic = new WordleDictionary();
        dic.addAll(List.of("пианист", "груз", "герой"));

        assertEquals(1, dic.getAllWords().size());

    }

    @Test
    void EmptyDictionary() {
        WordleDictionary dic = new WordleDictionary();

        try {
            dic.getRandomWord();
            fail("ошибка IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Словарь пуст!", e.getMessage());
        }
    }

}