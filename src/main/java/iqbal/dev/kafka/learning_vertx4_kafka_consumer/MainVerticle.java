package iqbal.dev.kafka.learning_vertx4_kafka_consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;

public class MainVerticle extends AbstractVerticle{

	private final Logger log = LoggerFactory.getLogger(MainVerticle.class);
	
	@Override
	public void start() throws Exception {
		System.out.println("running... ");
		log.info("running... ");
		ConfigRetriever retriever = ConfigRetriever.create(vertx); 
		retriever.getConfig(config -> {
			System.out.println(Json.encode(config)+config.failed());
			if(config.failed()) {
				log.error("failed read properties file", config.cause());
			}else {
				System.out.println("producer mode "+config.result().getBoolean(ConfigKey.PRODUCER_MODE));
				System.out.println("consumer mode "+config.result().getBoolean(ConfigKey.CONSUMER_MODE));
				
				if(Boolean.TRUE.equals(config.result().getBoolean(ConfigKey.CONSUMER_MODE))) {
					System.out.println("deploying consumer");
					vertx.deployVerticle(KafkaConsumerVerticle.class.getName(),
							new DeploymentOptions().setConfig(config.result()));
				}
				if(Boolean.TRUE.equals(config.result().getBoolean(ConfigKey.PRODUCER_MODE))) {
					System.out.println("deploying producer");
					vertx.deployVerticle(KafkaProducerVerticle.class.getName(),
							new DeploymentOptions().setConfig(config.result()));
				}
				
			}
		});
		//startPromise.complete();
	}
}
