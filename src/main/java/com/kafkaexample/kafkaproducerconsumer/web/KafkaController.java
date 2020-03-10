package com.kafkaexample.kafkaproducerconsumer.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.kafkaexample.kafkaproducerconsumer.models.AnotherSimpleModel;
import com.kafkaexample.kafkaproducerconsumer.models.SimpleModel;

@RestController
@RequestMapping(("/api/kafka"))
public class KafkaController {

	//for kafka serialization/deserialization
	/*private KafkaTemplate<String, SimpleModel> kafkaTemplate;
	
	@Autowired
	public KafkaController(KafkaTemplate<String, SimpleModel> kafkaTemplate) {
		this.kafkaTemplate=kafkaTemplate;
	}*/
	
	//for independent serialization/deserialization
	private KafkaTemplate<String, String> kafkaTemplate;
	
	private Gson jsonConverter;
	
	@Autowired
	public KafkaController(KafkaTemplate<String, String> kafkaTemplate,Gson jsonConverter) {
		this.kafkaTemplate=kafkaTemplate;
		this.jsonConverter=jsonConverter;
	}
	
	@PostMapping("/publish")
	public ResponseEntity<SimpleModel> publish(@RequestBody SimpleModel simpleModel){
		try {
			//kafkaTemplate.send("Topic1",simpleModel);
			kafkaTemplate.send("Topic1",jsonConverter.toJson(simpleModel));
			return new ResponseEntity<SimpleModel>(simpleModel, HttpStatus.OK);	
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);	
		}
	}
	
	@PostMapping("/publish/v2")
	public ResponseEntity<AnotherSimpleModel> publish(@RequestBody AnotherSimpleModel simpleModel){
		try {
			//kafkaTemplate.send("Topic1",simpleModel);
			kafkaTemplate.send("Topic2",jsonConverter.toJson(simpleModel));
			return new ResponseEntity<AnotherSimpleModel>(simpleModel, HttpStatus.OK);	
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);	
		}
	}
	
	@KafkaListener(topics = "Topic1")
	public void getFromKafka(/*SimpleModel*/ String simpleModel) {
		//System.out.println(simpleModel.toString());
		System.out.println(simpleModel);
		
		SimpleModel simpleModelconverted = (SimpleModel) jsonConverter.fromJson(simpleModel, SimpleModel.class);
		System.out.println(simpleModelconverted.toString());
	}
	
	@KafkaListener(topics = "Topic2")
	public void getFromKafka2(/*SimpleModel*/ String anothersimpleModel) {
		//System.out.println(simpleModel.toString());
		System.out.println(anothersimpleModel);
		
		AnotherSimpleModel anothersimpleModelconverted = (AnotherSimpleModel) jsonConverter.fromJson(anothersimpleModel, AnotherSimpleModel.class);
		System.out.println(anothersimpleModelconverted.toString());
	}
	
	
	
}
