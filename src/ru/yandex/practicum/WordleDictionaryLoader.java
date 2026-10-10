package ru.yandex.practicum;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {
    private final PrintWriter logger;

    public WordleDictionaryLoader(PrintWriter logger) {
        this.logger = logger;
    }

    public WordleDictionary loadFromFile(String fileName) throws IOException {
        logger.println("Начало чтения файла для загрузки словаря");
        WordleDictionary dictionary = new WordleDictionary();

        File dictionaryFile = getDictionaryFile(fileName);
        List<String> list = readFile(dictionaryFile);
        dictionary.addAll(list);

        logger.println("Словарь из файла " + fileName + " загружен");
        return dictionary;
    }

    private File getDictionaryFile(String fileName) throws FileNotFoundException {
        logger.println("Получаем файл по имени " + fileName);
        Path pathFile = Paths.get(fileName);
        File file = pathFile.toFile();

        if (!file.exists()) {
            throw new FileNotFoundException(String.format("Не существует файл %s ", fileName));
        }
        logger.println("Файл по имени " + fileName + " получен");
        return file;
    }

    private List<String> readFile(File dictionaryFile) throws IOException {
        List<String> result = new ArrayList<>();

        try (FileReader fileReader = new FileReader(dictionaryFile, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(fileReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                result.add(line);
            }
            logger.println("Из файла прочитано " + result.size() + " слов");

        } catch (IOException exception) {
            logger.println("Не удалось прочитать из файла " + dictionaryFile);
            throw exception;
        }

        return result;
    }

}
