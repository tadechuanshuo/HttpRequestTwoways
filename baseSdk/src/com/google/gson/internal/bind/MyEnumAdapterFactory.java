package com.google.gson.internal.bind;

import java.lang.reflect.Type;

import com.enumeration.example.IEnum;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

public class MyEnumAdapterFactory implements TypeAdapterFactory {

	// @Override
	// public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
	// Class<? super T> rawType = type.getRawType();
	// if(rawType.isEnum()){
	// Type[] types = rawType.getGenericInterfaces();
	// for(Type item:types){
	// if(item == IEnum.class){
	// return new EnumTypeAdapter<T>();
	// }
	// }
	// }
	// return null;
	// }

	@Override
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
		Class<? super T> rawType = type.getRawType();
		if (rawType.isEnum()) {
			Type[] types = rawType.getGenericInterfaces();
			for (Type item : types) {
				if (item == IEnum.class) {
					return new EnumTypeAdapter<T>();
				}
			}
		}
		return null;
	}
}
