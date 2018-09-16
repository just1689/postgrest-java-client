package za.co.captain.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import za.co.captain.exception.CaptainException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {

    public static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX"));
        //TODO: other settings (dates etc)
    }

    public static String objectToJson(Object o) throws CaptainException {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (Exception ex) {
            throw new CaptainException(ex, "ObjectToJson could not convert object to json");
        }
    }

    public static Object jsonToObject(String json, Class clazz) throws CaptainException {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new CaptainException(e, "jsonToObjects could not turn json into Object.");
        }
    }

    public static List<Object> jsonToObjects(String json, Class clazz) throws CaptainException {
        try {
            CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
            return objectMapper.readValue(json, listType);
        } catch (IOException e) {
            throw new CaptainException(e, "jsonToObjects could not turn json into list.");
        }
    }

}
