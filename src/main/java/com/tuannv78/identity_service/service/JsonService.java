package com.tuannv78.identity_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JsonService {
    ObjectMapper object = new ObjectMapper();

    public String toString(Object object) throws JsonProcessingException {
        return this.object.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }
}
