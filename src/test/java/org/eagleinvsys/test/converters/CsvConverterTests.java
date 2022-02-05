package org.eagleinvsys.test.converters;

import org.eagleinvsys.test.converters.impl.CsvConverter;
import org.junit.jupiter.api.*;

import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("CsvConverterTests cases")
class CsvConverterTests {
    private static CsvConverter csvConverter;
    private static ConvertibleCollection convertibleCollection;
    private FileOutputStream fileOutputStream;
    private Exception exception;

    @BeforeAll
    public static void init() {
        csvConverter = TestDataService.getCsvConverter();
        convertibleCollection = TestDataService.getConvertibleCollection(10);
    }

    @BeforeEach
    public void createStream() {
        try {
            fileOutputStream = TestDataService.getFileOutputStream();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @AfterEach
    public void finishTest() {
        try {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("OutputStream is null")
    public void testForNullFileOutputStream() {
        fileOutputStream = null;
        exception = assertThrows(NullPointerException.class, () ->
                csvConverter.convert(convertibleCollection, fileOutputStream));
        assertEquals("OutputStream is null", exception.getMessage());
    }

    @Test
    @DisplayName("Collection is null case")
    public void testNullCollection() {
        try {
            convertibleCollection = null;
            exception = assertThrows(NullPointerException.class, () ->
                    csvConverter.convert(convertibleCollection, fileOutputStream));
            assertEquals("CollectionToConvert cant be null", exception.getMessage());
            fileOutputStream.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}