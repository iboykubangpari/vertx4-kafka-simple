package iqbal.dev.kafka.learning_vertx4_kafka_consumer;

import io.vertx.core.Vertx;

public class DebugApp{
	public static void main( String[] args )
    {	
		System.out.println("run");
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MainVerticle());
        System.out.println("runxxxx");
    }
}
