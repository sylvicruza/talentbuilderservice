package com.talentbuilder.talentbuilder.utilities;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateDeserializer implements JsonDeserializer<Date> {

	  @Override
	  public Date deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
	      String date = element.getAsString();
	      long d = Long.parseLong(date);
	      Date date2 = new Date(d);
	      
	      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
	      format.setTimeZone(TimeZone.getTimeZone("UTC"));                               
	      return date2;
	   }
	}