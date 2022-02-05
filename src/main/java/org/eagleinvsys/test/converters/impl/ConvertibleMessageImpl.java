package org.eagleinvsys.test.converters.impl;

import org.eagleinvsys.test.converters.ConvertibleMessage;

import java.util.Map;

public class ConvertibleMessageImpl implements ConvertibleMessage {

    private final Map<String, String> record;

    public ConvertibleMessageImpl(Map<String, String> record) {
        this.record = record;
    }

    @Override
    public String getElement(String elementId) {
        return record.get(elementId);
    }
}