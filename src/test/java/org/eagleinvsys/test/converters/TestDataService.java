package org.eagleinvsys.test.converters;

import org.eagleinvsys.test.converters.impl.ConvertibleCollectionImpl;
import org.eagleinvsys.test.converters.impl.CsvConverter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.*;

public class TestDataService {
    public static final String OUTPUT_FILE_PATH = "D:\\test.csv";

    public static FileOutputStream getFileOutputStream() throws FileNotFoundException {
        return new FileOutputStream(OUTPUT_FILE_PATH);
    }

    public static CsvConverter getCsvConverter() {
        return new CsvConverter();
    }

    public static ConvertibleCollection getConvertibleCollection(int number) {
        return new ConvertibleCollectionImpl(getTestDataCollection(number));
    }

    public static List<Map<String, String>> getTestDataCollection(Integer numberOfRecords) {
        List<Map<String, String>> testDataCollection = new ArrayList<>();

        for (int i = 0; i < numberOfRecords; i++) {
            testDataCollection.add(generateRecord());
        }

        return testDataCollection;
    }

    public static BufferedReader readCSVFile() throws FileNotFoundException {
        return new BufferedReader(new FileReader(OUTPUT_FILE_PATH));
    }

    private static Map<String, String> generateRecord() {
        String[] nameArrays = new String[]{"Андрей", "Сергей", "Александр", "Афанасий", "Робинзон", "Карл", "Роберт", "Кирилл", "Филарет"};
        String[] surnameArrays = new String[]{"Старый", "Добрый", "Мирный", "Честный", "Злой", "Великий", "Бедный", "Робкий", "Храбрый", "Могучий"};
        String[] cityArrays = new String[]{"Москва", "Санкт-Петербург", "Омск", "Самара", "Казань", "Югра", "Тула", "Белгород"};

        Map<String, String> record = new HashMap<>();
        record.put("Имя", getRandomValueFromCollection(Arrays.asList(nameArrays)));
        record.put("Фамилия", getRandomValueFromCollection(Arrays.asList(surnameArrays)));
        record.put("Дата рождения", getRandomDateOfBirthDay());
        record.put("Город рождения", getRandomValueFromCollection(Arrays.asList(cityArrays)));

        return record;
    }

    private static String getRandomValueFromCollection(List<String> valueCollection) {
        return valueCollection.stream()
                .skip((int) (valueCollection.size() * Math.random()))
                .findFirst()
                .orElse("");
    }

    private static String getRandomDateOfBirthDay() {
        GregorianCalendar gc = new GregorianCalendar();
        int year = randBetween(1950, 2016);
        int dayOfYear = randBetween(1, gc.getActualMaximum(gc.DAY_OF_YEAR));

        gc.set(gc.YEAR, year);
        gc.set(gc.DAY_OF_YEAR, dayOfYear);

        return gc.get(gc.DAY_OF_MONTH) + "." + (gc.get(gc.MONTH) + 1) + "." + gc.get(gc.YEAR);
    }

    private static int randBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }
}