package com.google.gson.internal.bind;

import java.io.IOException;
import java.lang.reflect.Method;

import com.enumeration.example.IEnum;

import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class EnumTypeAdapter<T> extends TypeAdapter<T>{
    @Override
    public T read(JsonReader in) throws IOException {
            if(in.peek() == JsonToken.NULL){
                    in.nextNull();
                    return null;
            }
            return null;
    }
            
    public Object read(JsonReader in,TypeToken<T> type) throws IOException{
            boolean isEnum = type.getRawType().isEnum();
            if(isEnum){
                    int value = in.nextInt();
                    try {
                            Method valuesMethod = type.getRawType().getMethod("values", null);
                            IEnum[] enumArr = (IEnum[])valuesMethod.invoke(type.getClass(), null);
                            for (IEnum iEnum : enumArr) {
//                                    System.out.println( iEnum );
                                    if(iEnum.getValue() == value){
//                                            Log.d("This is a enum ", "value is=====>"+value);
                                          System.out.println("This is a enum "+"  value is=====>"+value);
                                            return iEnum;
                                    }
                            }
                            } catch (Exception e) {
                                    e.printStackTrace();
                    }
            }        
            return null;
    }
    @Override
    public void write(JsonWriter out, T value) throws IOException {
            if(value == null){
                    out.nullValue();
                    return;
            }
                    
            if(value instanceof IEnum){
                    out.value(((IEnum)value).getValue());
            }
    }                
}

