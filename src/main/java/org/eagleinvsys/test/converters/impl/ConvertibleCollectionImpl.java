package org.eagleinvsys.test.converters.impl;

import org.eagleinvsys.test.converters.ConvertibleCollection;
import org.eagleinvsys.test.converters.ConvertibleMessage;

import java.util.*;

public class ConvertibleCollectionImpl implements ConvertibleCollection {
    private final List<Map<String, String>> collectionToConvert;

    public ConvertibleCollectionImpl(List<Map<String, String>> collectionToConvert) {
        this.collectionToConvert = collectionToConvert;
    }

    @Override
    public Collection<String> getHeaders() {
        validateInputCollectionHeaders(collectionToConvert);
        return collectionToConvert.stream().findFirst().get().keySet();
    }

    @Override
    public Iterable<ConvertibleMessage> getRecords() {
        List<ConvertibleMessage> records = new ArrayList<>();

        for (Map<String, String> map : collectionToConvert) {
            records.add(new ConvertibleMessageImpl(map));
        }

        return records;
    }

    private void validateInputCollectionHeaders(List<Map<String, String>> collectionToConvert) {
        Set<String> headers;

        if (collectionToConvert != null && collectionToConvert.size() > 0) {
            headers = collectionToConvert.stream().findFirst().get().keySet();
        } else {
            throw new IllegalArgumentException("Collection is Empty");
        }

        for (Map<String, String> map : collectionToConvert) {
            Set<String> headersToValidate = map.keySet();

            if (!headersToValidate.equals(headers)) {
                throw new IllegalArgumentException("All maps must have the same set of keys");
            }
        }
    }
}