package com.Utilities;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * User: cwatts
 * <p/>
 * Date: 9/8/11
 * <p/>
 * CommandArguments is used to handle generic json objects.
 */
@SuppressWarnings("unchecked")
public class CommandArguments
{
	private static final Logger logger = Logger.getLogger(CommandArguments.class);

	private Map<String, Object> objectMap = new HashMap<String, Object>();

	private String originalJsonString;

	/**
	 * Will create a map of key value pairs from the specified json object.
	 *
	 * @param jsonAsString the specified json object as a string.
	 */
	public CommandArguments(String jsonAsString)
	{
		this.originalJsonString = jsonAsString;
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Object.class, new NaturalDeserializer());
		Gson gson = gsonBuilder.create();
		Object natural = gson.fromJson(jsonAsString, Object.class);
		objectMap = (HashMap<String, Object>) natural;
	}

	/**
	 * This will get a argument from the deserialized json object. Example: to get a normal value just give the key, ie "method" if you want to get a property from a nested object give the key "params.location"
	 *
	 * @param key the specified key.
	 *
	 * @return the object value from the given key.
	 */
	public Object getArgument(String key)
	{
		Object result;
		try
		{
			result = this.getArgument(key, this.objectMap);
		}
		catch(Exception ex)
		{
			logger.error("Could not find key from get property. Key : " + key + "\n" + ex);
			return null;
		}

		// If it is still null then we couldn't find the value associated with the key.
		if(result == null)
		{
			logger.error("Could not find key from get property. Key : " + key);
		}

		return result;
	}

	/**
	 * Gets the "Command" argument from the arguments.
	 * @return the command argument value.
	 */
	public String getCommandName()
	{
		String commandName = "";
		try
		{
			commandName = this.getArgument("Command").toString();
		}
		catch (Exception ex)
		{
			logger.error("Could not convert 'Command' to a string");
		}

		return commandName;
	}

	@Override public String toString()
	{
		return this.originalJsonString;
	}

	public boolean doesKeyExist(String key)
	{
		return this.getArgument(key, this.objectMap) != null;
	}

	private Object getArgument(String key, Map<String, Object> subMap)
	{
		Object result;

		// If the key is empty or null then we just want to return the root object or whatever object is given.
		if (key == null ||
		    key.isEmpty())
		{
			return subMap;
		}

		// Try to find the array accessor first
		result = findArray(key, subMap);

		// If we dont find one then check to see if there is a .
		if (result == null)
		{
			result = findSubKey(key, subMap);
		}

		// If we still cant find anything then it has to just a key so get it from the submap.
		if (result == null)
		{
			result = subMap.get(key);
		}

		return result;
	}

	private Object findArray(String key, Map<String, Object> subMap)
	{
		// Look for an array accessor in the key string.
		// If there isnt one then return a null object meaning that it is usually just a property accessor.
		Object result = null;
		if (key.contains("["))
		{
			// Get the index of the brackets and get the actual key
			// ie. actual key = ltmpools
			// full key = ltmpools[0]
			int indexOfArrayChar = key.indexOf("[") + 1;
			String actualKey = key.substring(0, indexOfArrayChar - 1);
			int indexOfEndOfArrayChar = key.indexOf("]") + 1;

			// Get the actual value of the array index
			int arrayIndex = Integer.parseInt(key.substring(indexOfArrayChar, indexOfEndOfArrayChar - 1));

			// Find the array in the mapped object using the key.
			Object[] objectList = (Object[]) subMap.get(actualKey);

			// Once we get the array get map that is associated with the given index.
			subMap = (Map<String, Object>) objectList[arrayIndex];

			// If we are at the end of the key, ie there is not a . after the []
			// then pass back "" to the recursive call to get property.
			String subKey = "";
			if (indexOfEndOfArrayChar != key.length())
			{
				// Set the key to the next part of the string
				// ie ltmpools[0].members[0]
				// subkey would be members.
				subKey = key.substring(indexOfEndOfArrayChar + 1);
			}

			// Call a recursive call on the sub key and using the map that we got from the index array from before.
			result = this.getArgument(subKey, subMap);
		}

		return result;
	}

	private Object findSubKey(String key, Map<String, Object> subMap)
	{
		// Look for a dot notation denoting that there is more than one property accessors.
		// If there isn't one then return null.
		Object result = null;
		if (key.contains("."))
		{
			// Find the period and separate the first part of the property from the second.
			// ie. member.address
			// first = member
			// subkey = address
			int indexOfSubKey = key.indexOf(".") + 1;
			String firstKey = key.substring(0, indexOfSubKey - 1);
			String subKey = key.substring(indexOfSubKey);

			// Get the map associated with first property accessor
			Map<String, Object> map = (Map<String, Object>) subMap.get(firstKey);

			// Call get property recursively to find the second part of the key
			result = this.getArgument(subKey, map);
		}

		return result;
	}

	private class NaturalDeserializer implements JsonDeserializer<Object>
	{
		public Object deserialize(JsonElement json, Type typeOfT,
		                          JsonDeserializationContext context)
		{
			if (json.isJsonNull())
				return null;
			else if (json.isJsonPrimitive())
				return handlePrimitive(json.getAsJsonPrimitive());
			else if (json.isJsonArray())
				return handleArray(json.getAsJsonArray(), context);
			else
				return handleObject(json.getAsJsonObject(), context);
		}

		private Object handlePrimitive(JsonPrimitive json)
		{
			if (json.isBoolean())
				return json.getAsBoolean();
			else if (json.isString())
				return json.getAsString();
			else
			{
				BigDecimal bigDec = json.getAsBigDecimal();
				// Find out if it is an int type
				try
				{
					BigInteger dec = bigDec.toBigIntegerExact();
					try
					{
						return bigDec.intValueExact();
					}
					catch (ArithmeticException ignored)
					{
					}
					return bigDec.longValue();
				}
				catch (ArithmeticException ignored)
				{
				}
				// Just return it as a double
				return bigDec.doubleValue();
			}
		}

		private Object handleArray(JsonArray json, JsonDeserializationContext context)
		{
			Object[] array = new Object[json.size()];
			for (int i = 0; i < array.length; i++)
				array[i] = context.deserialize(json.get(i), Object.class);
			return array;
		}

		private Object handleObject(JsonObject json, JsonDeserializationContext context)
		{
			Map<String, Object> map = new HashMap<String, Object>();
			for (Map.Entry<String, JsonElement> entry : json.entrySet())
				map.put(entry.getKey(), context.deserialize(entry.getValue(), Object.class));
			return map;
		}
	}
}