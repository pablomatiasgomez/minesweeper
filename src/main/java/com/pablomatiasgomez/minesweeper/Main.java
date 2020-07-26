package com.pablomatiasgomez.minesweeper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.pablomatiasgomez.minesweeper.controller.GameController;
import com.pablomatiasgomez.minesweeper.controller.JsonTransformer;
import com.pablomatiasgomez.minesweeper.repository.GameRepository;
import com.pablomatiasgomez.minesweeper.service.GameService;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.Properties;

public class Main {

	private static final Logger LOG = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "10");
		LOG.info("Starting app..");

		MongoClient mongoClient = getMongoClient(getDatabaseProperties());
		MongoDatabase database = mongoClient.getDatabase("minesweeper");
		ObjectMapper objectMapper = createObjectMapper();
		JsonTransformer jsonTransformer = new JsonTransformer(objectMapper);

		GameRepository gameRepository = new GameRepository(objectMapper, database);
		GameService gameService = new GameService(gameRepository);

		Spark.port(8080);
		Spark.exception(Exception.class, (e, request, response) -> {
			LOG.error(e.getMessage(), e);
			response.status(500);
			response.body(e.getMessage());
		});
		Spark.exception(IllegalArgumentException.class, (e, request, response) -> {
			LOG.error(e.getMessage(), e);
			response.status(400);
			response.body(e.getMessage());
		});
		Spark.exception(IllegalStateException.class, (e, request, response) -> {
			LOG.error(e.getMessage(), e);
			response.status(400);
			response.body(e.getMessage());
		});

		new GameController(jsonTransformer, gameService);
	}

	private static ObjectMapper createObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
		objectMapper.registerModule(new JavaTimeModule());
		return objectMapper;
	}

	private static MongoClient getMongoClient(Properties databaseProperties) {
		String connectionString = (String) databaseProperties.get("mongodb.connectionString");

		MongoClientSettings settings = MongoClientSettings.builder()
				.codecRegistry(CodecRegistries.fromRegistries(
						MongoClientSettings.getDefaultCodecRegistry(),
						CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())))
				.applyConnectionString(new ConnectionString(connectionString))
				.build();
		return MongoClients.create(settings);
	}

	private static Properties getDatabaseProperties() {
		try {
			Properties properties = new Properties();
			properties.load(Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("database.properties")));
			return properties;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
