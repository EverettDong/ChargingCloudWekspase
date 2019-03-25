package com.cpit.common;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;


public class DeserializeDateTime extends JsonDeserializer<Date> {

    @Override  
    public Date deserialize(JsonParser jp, DeserializationContext ctxt)  
            throws IOException, JsonProcessingException {  
       	return TimeConvertor.stringTime2Date(jp.getText(),TimeConvertor.FORMAT_MINUS_24HOUR);  
    }
	



}
