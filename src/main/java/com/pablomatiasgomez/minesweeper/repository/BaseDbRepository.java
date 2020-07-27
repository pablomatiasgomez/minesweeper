package com.pablomatiasgomez.minesweeper.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.UpdateResult;
import com.pablomatiasgomez.minesweeper.domain.BaseDbEntity;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nullable;

public abstract class BaseDbRepository<T extends BaseDbEntity> {

	private static final Logger LOG = LoggerFactory.getLogger(BaseDbRepository.class);

	private final Class<T> entityClass;
	private final ObjectMapper objectMapper;
	private final MongoCollection<Document> collection;

	protected BaseDbRepository(Class<T> entityClass, ObjectMapper objectMapper, MongoCollection<Document> collection) {
		this.entityClass = entityClass;
		this.objectMapper = objectMapper;
		this.collection = collection;
	}

	protected Optional<T> findOne(Bson filter) {
		return Optional.ofNullable(collection.find(filter).first()).map(this::parseDocument);
	}

	@Nullable
	protected T create(T entity) {
		Document document = createDocument(entity);
		collection.insertOne(document);
		return parseDocument(document);
	}

	protected T replace(T entity) {
		Objects.requireNonNull(entity.getObjectId(), "entity should have objectId if going to replace!");
		Document document = createDocument(entity);
		UpdateResult result = collection.replaceOne(Filters.eq("_id", entity.getObjectId()), document, new ReplaceOptions().upsert(false));
		if (result.getMatchedCount() != 1) {
			throw new IllegalStateException("Entity with id " + entity.getId() + " was no longer present: " + entity);
		}
		return result.getModifiedCount() == 0 ? null : parseDocument(document);
	}

	protected String writeValue(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	private T parseDocument(Document document) {
		return readValue(document.toJson(), entityClass);
	}

	private Document createDocument(T entity) {
		Document document = Document.parse(writeValue(entity));
		if (entity.getObjectId() != null) {
			document.put("_id", entity.getObjectId());
			document.remove("id");
		}
		return document;
	}

	private <V> V readValue(String content, Class<V> valueType) {
		try {
			return objectMapper.readValue(content, valueType);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
