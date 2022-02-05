package org.eagleinvsys.test.converters.impl;

import org.eagleinvsys.test.converters.Converter;
import org.eagleinvsys.test.converters.ConvertibleCollection;
import org.eagleinvsys.test.converters.ConvertibleMessage;

import java.io.*;
import java.util.Collection;


public class CsvConverter implements Converter {
    private static final String DELIMITER = ",";
    private PrintWriter writer;

    /**
     * Converts given {@link ConvertibleCollection} to CSV and outputs result as a text to the provided {@link OutputStream}
     *
     * @param collectionToConvert collection to convert to CSV format
     * @param outputStream        output stream to write CSV conversion result as text to
     */
    @Override
    public void convert(ConvertibleCollection collectionToConvert, OutputStream outputStream) {
        if (outputStream == null) {
            throw new NullPointerException("OutputStream is null");
        }

        if (collectionToConvert == null) {
            throw new NullPointerException("CollectionToConvert cant be null");
        }

        writer = new PrintWriter(outputStream);

        Collection<String> headers = collectionToConvert.getHeaders();
        Iterable<ConvertibleMessage> records = collectionToConvert.getRecords();

        printHeaders(headers);
        printRows(headers, records);
        writer.close();
    }

    private void printHeaders(Collection<String> headers) {
        StringBuilder headerRow = new StringBuilder();

        for (String header : headers) {
            if (isEmptyKeyOrValue(header)) {
                headerRow.append("emptyHeader" + DELIMITER);
            } else {
                headerRow.append(header + DELIMITER);
            }
        }

        writeRow(headerRow);
    }

    private void printRows(Collection<String> headers, Iterable<ConvertibleMessage> records) {
        for (ConvertibleMessage record : records) {
            StringBuilder row = new StringBuilder();

            for (String header : headers) {
                String keyValue = record.getElement(header);

                if (isEmptyKeyOrValue(keyValue)) {
                    row.append("emptyValue" + DELIMITER);
                } else {
                    row.append(keyValue + DELIMITER);
                }
            }

            writeRow(row);
        }
    }

    private void writeRow(StringBuilder row) {
        String resultRow = row.substring(0, row.length() - 1) + "\n";
        writer.write(resultRow);
    }

    private boolean isEmptyKeyOrValue(String value) {
        return value == null || "".equals(value);
    }
}