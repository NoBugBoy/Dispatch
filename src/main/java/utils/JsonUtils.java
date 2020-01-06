package utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.ZoneId;
import java.util.Objects;
import java.util.TimeZone;

public class JsonUtils {
    private JsonUtils(){};
    protected static  ObjectMapper OBJECT_MAPPER;
    public static ObjectMapper get(){
        if(Objects.isNull(OBJECT_MAPPER)){
            synchronized (ObjectMapper.class){
                if(Objects.isNull(OBJECT_MAPPER)){
                    OBJECT_MAPPER = new ObjectMapper();
                    OBJECT_MAPPER.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Shanghai")));
                }
            }
        }
        return OBJECT_MAPPER;
    }
}
