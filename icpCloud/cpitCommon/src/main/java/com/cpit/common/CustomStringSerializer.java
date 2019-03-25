package com.cpit.common;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;


public class CustomStringSerializer extends JsonSerializer<String> {
	
	@Override
	public void serialize(String value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		String newValue = StringUtils.filter(value);
		newValue = newValue.replaceAll("\r\n","<br>");
		newValue = newValue.replaceAll("\n","<br>");
		jgen.writeString(newValue);
	}


}
