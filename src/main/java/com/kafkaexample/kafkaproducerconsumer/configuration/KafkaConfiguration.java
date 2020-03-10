package com.kafkaexample.kafkaproducerconsumer.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.google.gson.Gson;
import com.kafkaexample.kafkaproducerconsumer.models.SimpleModel;


@Configuration
@EnableKafka //to enable kafka listeners 
public class KafkaConfiguration {

	@Bean
	public /*ProducerFactory<String, SimpleModel>*/ ProducerFactory<String, String> producerFactory(){
		Map<String, Object> config = new HashMap<>();
		
		//specify kafka server address : here we are running kafka on localhost, 
		//default port 9092 
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		//key serializer
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		//value serializer
		//for objects kafka serialization
		//config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		//for independent serialization to convert to any object later
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		//return a default kafka producer with our config
		return new DefaultKafkaProducerFactory<>(config);
		
	}
	
	@Bean
	public /*KafkaTemplate<String, SimpleModel>*/ KafkaTemplate<String, String> kafkaTemplate(){
		return new KafkaTemplate<>(producerFactory());
	}
	
	@Bean
	public /*ConsumerFactory<String, SimpleModel>*/ ConsumerFactory<String, String> consumerFactory(){
		Map<String, Object> config = new HashMap<>();
		
		//specify kafka server address : here we are running kafka on localhost, 
		//defa	ult port 9092 
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		//key deserializer
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		//value deserializer
		//for objects kafka deserialization
		//config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		//for independent deserialization to convert to any object later
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		//needed to differentiate listener when there is more then one config 
		config.put(ConsumerConfig.GROUP_ID_CONFIG, "MyGroupID");
		
		//return a default kafka consumer with our config
		//for objects kafka deserialization
		//return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(),new JsonDeserializer<>(SimpleModel.class));
		//for independent deserialization
		return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(),new StringDeserializer());
	
	}
	
	//naming this bean kafkaListenerContainerFactory will override the default kafka listener
	@Bean
	public /*ConcurrentKafkaListenerContainerFactory<String, SimpleModel>*/ ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(){
		//for objects kafka deserialization
		//ConcurrentKafkaListenerContainerFactory<String, SimpleModel> concurrentKafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();
		//for independent deserialization		
		ConcurrentKafkaListenerContainerFactory<String, String> concurrentKafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();
		
		//the consumer factory not set by deafault we have to set it
		concurrentKafkaListenerContainerFactory.setConsumerFactory(consumerFactory());
		
		return concurrentKafkaListenerContainerFactory;
	}
	
	//it's better to create an independant serializer/deserializer
	//independent to kafka serialization for versionn issues we can encounter
	@Bean
	public Gson jsonConverter() {
		return new Gson();
	}
	
}
