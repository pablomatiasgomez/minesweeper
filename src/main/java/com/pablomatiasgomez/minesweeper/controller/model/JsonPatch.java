package com.pablomatiasgomez.minesweeper.controller.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Basic implementation of the JsonPatch only for the purposes of this project,
 * which is to only allow modifying a single cell in the game.
 */
public class JsonPatch {

	private final JsonPatchOp op;
	private final String path;
	private final Object value;

	@JsonCreator
	public JsonPatch(
			@JsonProperty(value = "op", required = true) JsonPatchOp op,
			@JsonProperty(value = "path", required = true) String path,
			@JsonProperty(value = "value", required = true) Object value) {
		this.op = Objects.requireNonNull(op);
		this.path = Objects.requireNonNull(path);
		this.value = Objects.requireNonNull(value);
	}

	public JsonPatchOp getOp() {
		return op;
	}

	public String getPath() {
		return path;
	}

	public Object getValue() {
		return value;
	}
}
