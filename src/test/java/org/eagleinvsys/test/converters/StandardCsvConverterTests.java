package org.eagleinvsys.test.converters;

import org.eagleinvsys.test.converters.impl.CsvConverter;
import org.eagleinvsys.test.converters.impl.StandardCsvConverter;
import org.junit.jupiter.api.*;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("StandardCsvConverterTests cases")
class StandardCsvConverterTests {
    private List<Map<String, String>> testDataCollection;
    private static StandardConverter standardConverter;
    private static String filePath;
    private FileOutputStream fileOutputStream;
    private Exception exception;

    @BeforeAll
    public static void init() {
        filePath = "D:\\test.csv";
        standardConverter = new StandardCsvConverter(new CsvConverter());
    }

    @BeforeEach
    public void initCollectionAndCreateStream() {
        testDataCollection = getTestDataCollection(10);
        try {
            fileOutputStream = new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void finishTest() {
        testDataCollection = null;
        try {
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Headers does`t match case")
    public void testHeaders() {
        Map<String, String> mapTest3 = new HashMap<>();
        mapTest3.put("Имя", "Мифодий");
        testDataCollection.add(mapTest3);

        try {
            exception = assertThrows(IllegalArgumentException.class, () ->
                    standardConverter.convert(testDataCollection, fileOutputStream));
            assertEquals("All maps must have the same set of keys", exception.getMessage());
            fileOutputStream.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    @DisplayName("Empty collection case")
    public void testEmptyCollection() {
        try {
            List<Map<String, String>> emptyCollection = new ArrayList<>();
            exception = assertThrows(IllegalArgumentException.class, () ->
                    standardConverter.convert(emptyCollection, fileOutputStream));
            assertEquals("Collection is Empty", exception.getMessage());
            fileOutputStream.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    @DisplayName("Compare headers in resultFile and originalCollection")
    public void testFileHeaders() {
        try {
            standardConverter.convert(testDataCollection, fileOutputStream);

            BufferedReader bufferedReader = readCSVFile();
            String headersInFile = bufferedReader.readLine();
            bufferedReader.close();

            List<String> headersListInFile = new ArrayList<>(Arrays.asList(headersInFile.split(",")));
            List<String> headersInOriginalCollection = new ArrayList<>(testDataCollection.stream().findFirst().get().keySet());

            assertEquals(headersListInFile, headersInOriginalCollection);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    @DisplayName("Compare records count in resultFile and originalCollection")
    public void testRowCountInFile() {
        try {
            standardConverter.convert(testDataCollection, fileOutputStream);
            BufferedReader bufferedReader = readCSVFile();
            int countInFile = 0;
            int headers = 1;

            while (bufferedReader.readLine() != null) {
                countInFile++;
            }
            bufferedReader.close();

            assertEquals(countInFile - headers, testDataCollection.size());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private BufferedReader readCSVFile() throws FileNotFoundException {
        return new BufferedReader(new FileReader(filePath));
    }

    private List<Map<String, String>> getTestDataCollection(Integer numberOfRecords) {
        List<Map<String, String>> testDataCollection = new ArrayList<>();

        for (int i = 0; i < numberOfRecords; i++) {
            testDataCollection.add(generateRecord());
        }

        return testDataCollection;
    }

    private Map<String, String> generateRecord() {
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

    private String getRandomValueFromCollection(List<String> valueCollection) {
        return valueCollection.stream()
                .skip((int) (valueCollection.size() * Math.random()))
                .findFirst()
                .orElse("");
    }

    private String getRandomDateOfBirthDay() {
        GregorianCalendar gc = new GregorianCalendar();
        int year = randBetween(1950, 2016);
        int dayOfYear = randBetween(1, gc.getActualMaximum(gc.DAY_OF_YEAR));

        gc.set(gc.YEAR, year);
        gc.set(gc.DAY_OF_YEAR, dayOfYear);

        return gc.get(gc.DAY_OF_MONTH) + "." + (gc.get(gc.MONTH) + 1) + "." + gc.get(gc.YEAR);
    }

    public int randBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }
}