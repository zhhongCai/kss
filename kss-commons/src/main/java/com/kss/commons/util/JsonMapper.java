package com.kss.commons.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Map;

public class JsonMapper
{
	private static Logger logger = LoggerFactory.getLogger(JsonMapper.class);
	private final ThreadLocal<ObjectMapper> mapper;

	public JsonMapper()
	{
		this(null);
	}

	public JsonMapper(JsonInclude.Include include)
	{
		this.mapper = new ThreadLocal<ObjectMapper>() {
			@Override
			protected ObjectMapper initialValue() {
				return new ObjectMapper();
			}
		};
		if (include != null) {
			this.mapper.get().setSerializationInclusion(include);
		}
		this.mapper.get().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		this.mapper.get().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.mapper.get().setDateFormat(df);

		SimpleModule module = new SimpleModule();
		module.addSerializer(Long.class, new CustomLongSerialize());

		this.mapper.get().registerModule(module);
	}

	public static JsonMapper nonEmptyMapper()
	{
		return new JsonMapper(JsonInclude.Include.NON_EMPTY);
	}

	public static JsonMapper nonDefaultMapper()
	{
		return new JsonMapper(JsonInclude.Include.NON_DEFAULT);
	}

	public String toJson(Object object)
	{
		try
		{
			return this.mapper.get().writeValueAsString(object);
		}
		catch (IOException e)
		{
			logger.warn("write to json string error:" + object.getClass(), e);
		}
		return null;
	}

	public <T> T fromJson(String jsonString, Class<T> clazz)
	{
		if (StringUtils.isEmpty(jsonString)) {
			return null;
		}
		try
		{
			return (T) this.mapper.get().readValue(jsonString, clazz);
		}
		catch (IOException e)
		{
			logger.warn("parse json string error:" + jsonString, e);
		}
		return null;
	}

	public <T> T fromJson(String jsonString, JavaType javaType)
	{
		if (StringUtils.isEmpty(jsonString)) {
			return null;
		}
		try
		{
			return (T) this.mapper.get().readValue(jsonString, javaType);
		}
		catch (IOException e)
		{
			logger.warn("parse json string error:" + jsonString, e);
		}
		return null;
	}

	public JavaType contructCollectionType(Class<? extends Collection> collectionClass, Class<?> elementClass)
	{
		return this.mapper.get().getTypeFactory().constructCollectionType(collectionClass, elementClass);
	}

	public JavaType contructMapType(Class<? extends Map> mapClass, Class<?> keyClass, Class<?> valueClass)
	{
		return this.mapper.get().getTypeFactory().constructMapType(mapClass, keyClass, valueClass);
	}

	public void update(String jsonString, Object object)
	{
		try
		{
			this.mapper.get().readerForUpdating(object).readValue(jsonString);
		}
		catch (JsonProcessingException e)
		{
			logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
		}
		catch (IOException e)
		{
			logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
		}
	}

	public String toJsonP(String functionName, Object object)
	{
		return toJson(new JSONPObject(functionName, object));
	}

	public void enableEnumUseToString()
	{
		this.mapper.get().enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
		this.mapper.get().enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
	}

	public void enableJaxbAnnotation()
	{
//		JaxbAnnotationModule module = new JaxbAnnotationModule();
//		this.mapper.get().registerModule(module);
	}

	public void setDateFormat(String pattern)
	{
		if (StringUtils.isNotBlank(pattern))
		{
			DateFormat df = new SimpleDateFormat(pattern);
			this.mapper.get().setDateFormat(df);
		}
	}

	public ObjectMapper getMapper()
	{
		return this.mapper.get();
	}
}
