package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class ResourceLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceLoader.class);

    public static InputStream getResources(String path) throws IOException {
        LOGGER.info("reading resources from path: {}", path);
        InputStream inputStream = ResourceLoader.class.getClassLoader().getResourceAsStream(path);
        if(Objects.nonNull(inputStream)){
            return inputStream;
        }
        return Files.newInputStream(Path.of(path));
    }
}
