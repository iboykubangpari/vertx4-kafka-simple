package iqbal.dev.kafka.learning_vertx4_kafka_consumer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.kafka.client.consumer.KafkaConsumer;

public class KafkaConsumerVerticle extends AbstractVerticle{
	private final Logger log = LoggerFactory.getLogger(KafkaConsumerVerticle.class);
	
	@Override
	public void start(Promise<Void> startPromise) throws Exception {
		System.out.println("kafka consumer deployed");
		try {
			Map<String, String> kafkaConfig = config().getJsonObject(ConfigKey.KAFKA_CONSUMER_CONFIG).getMap()
					.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,  v->(String) v.getValue()));
			KafkaConsumer<String, String> consumer = KafkaConsumer.create(vertx, kafkaConfig);
			List<String> topic = new ArrayList<>();
			topic.add(config().getString(ConfigKey.KAFKA_TOPIC));
			
			consumer.subscribe(new HashSet<>(topic)).onFailure(err -> {
				log.error("failed to subscribe", err.getCause());
				System.out.println("failed to subscribe"+ err.getCause());
			});
			consumer.handler(msg -> {
				log.debug("msg coming, {}", msg.value());
				System.out.println("msg coming, {}"+ msg.value());
			});
		}catch(Exception e) {
			log.error("error create kafka consumer", e);
		}
	}
}
