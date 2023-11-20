package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

//this class responsible for converting inputStream from the json file into Java object
public class JsonUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T getTestData(String path, Class<T> type) throws IOException {
        try(InputStream stream = ResourceLoader.getResources(path)){
            String content = IOUtils.toString(stream, StandardCharsets.UTF_8);
            //System.out.println(content);
            return mapper.readValue(content, type); //beret input i class, v ob`ekt kotorogo etot input nado convertirovat.
        }catch (IOException e){
            LOGGER.error("unable to read test data from the json {}", path, e);
        }
        return null;
    }

}
