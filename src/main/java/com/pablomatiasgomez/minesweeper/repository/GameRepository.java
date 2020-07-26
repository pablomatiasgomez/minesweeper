package com.pablomatiasgomez.minesweeper.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.pablomatiasgomez.minesweeper.domain.Game;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class GameRepository extends BaseDbRepository<Game> {

	private static final Logger LOG = LoggerFactory.getLogger(Game.class);

	public GameRepository(ObjectMapper objectMapper, MongoDatabase database) {
		super(Game.class, objectMapper, database.getCollection("games"));
	}

	public Game createGame(Game game) {
		return create(game);
	}

	public Optional<Game> getGame(String id) {
		LOG.info("Getting game with id {}", id);
		return findOne(Filters.eq("_id", new ObjectId(id)));
	}

}
