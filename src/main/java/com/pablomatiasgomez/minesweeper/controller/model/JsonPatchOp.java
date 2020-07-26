package com.pablomatiasgomez.minesweeper.controller.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum JsonPatchOp {
	ADD,
	REMOVE,
	REPLACE,
	MOVE,
	COPY,
	TEST,
	;

	@JsonCreator
	public JsonPatchOp fromString(String value) {
		return JsonPatchOp.valueOf(value.toUpperCase());
	}
}
