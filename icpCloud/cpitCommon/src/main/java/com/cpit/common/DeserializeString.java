package com.cpit.common;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;


public class DeserializeString extends JsonDeserializer<String> {

    @Override  
    public String deserialize(JsonParser jp, DeserializationContext ctxt)  
            throws IOException, JsonProcessingException {  
    	    String newValue = jp.getText();
    		newValue = newValue.replaceAll("<br>","\n");
    	    return newValue;
    }
	



}
