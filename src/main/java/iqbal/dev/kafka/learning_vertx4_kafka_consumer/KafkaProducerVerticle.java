package iqbal.dev.kafka.learning_vertx4_kafka_consumer;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.kafka.common.config.SaslConfigs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.util.internal.StringUtil;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;

public class KafkaProducerVerticle extends AbstractVerticle{
	private final Logger log = LoggerFactory.getLogger(KafkaProducerVerticle.class);
	private Map<String, String> kafkaConfig = new HashMap<>();
	private KafkaProducer<String, String> producer;
	
	@Override
	public void start(Promise<Void> startPromise) throws Exception {
		System.out.println("kafka producer deployed");
		final String evbKafkaPublisher =  config().getString(ConfigKey.KAFKA_TOPIC);
		
		this.kafkaConfig = config().getJsonObject(ConfigKey.KAFKA_PRODUCER_CONFIG).getMap().entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, v -> (String) v.getValue()));
		
		String username = config().getJsonObject(ConfigKey.KAFKA_PRODUCER_CONFIG).getString("username");
	    String password = config().getJsonObject(ConfigKey.KAFKA_PRODUCER_CONFIG).getString("password");
	    if (!StringUtil.isNullOrEmpty(username) && !StringUtil.isNullOrEmpty(password)) {
	      String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username={} password={};";
	      String jaasCfg = String.format(jaasTemplate, username, password);
	      this.kafkaConfig.put("security.protocol", "SASL_PLAINTEXT");
	      this.kafkaConfig.put(SaslConfigs.SASL_MECHANISM, "SCRAM-SHA-256");
	      this.kafkaConfig.put(SaslConfigs.SASL_JAAS_CONFIG, jaasCfg);
	    }
	    
	    try {
	    	this.producer = KafkaProducer.create(vertx, kafkaConfig);
	    	for(int i = 1; i<11;i++) {
	    		String message = "ini adalah data yang ke "+ (i);
	    		System.out.println(message);
	    		this.producer.write(KafkaProducerRecord.create(config().getString(ConfigKey.KAFKA_TOPIC), message));
	    	}
	    }catch (Exception e) {
			log.error("failed to send to kafka", e);
			System.out.println("failed to send to kafka "+ e);
		}
		
	}
}
