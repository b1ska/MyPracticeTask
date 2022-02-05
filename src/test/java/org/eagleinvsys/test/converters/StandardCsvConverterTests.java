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
    private FileOutputStream fileOutputStream;
    private Exception exception;
    private static BufferedReader bufferedReader;

    @BeforeAll
    public static void init() {
        standardConverter = new StandardCsvConverter(new CsvConverter());
    }

    @BeforeEach
    public void initCollectionAndCreateStream() {
        testDataCollection = TestDataService.getTestDataCollection(10);
        try {
            fileOutputStream = TestDataService.getFileOutputStream();
            bufferedReader = TestDataService.readCSVFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void finishTest() {
        testDataCollection = null;
        try {
            fileOutputStream.close();
            bufferedReader.close();
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
            String headersInFile = bufferedReader.readLine();

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
}