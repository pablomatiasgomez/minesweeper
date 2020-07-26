package com.pablomatiasgomez.minesweeper.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.ResponseTransformer;

import java.io.IOException;

public class JsonTransformer implements ResponseTransformer {

	private final ObjectMapper objectMapper;

	public JsonTransformer(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public String render(Object object) {
		return writeValue(object);
	}

	public <T> T readValue(String content, Class<T> valueType) {
		try {
			return objectMapper.readValue(content, valueType);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String writeValue(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
