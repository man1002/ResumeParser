package resume.parser.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;
import java.util.TimeZone;

import org.springframework.core.io.ClassPathResource;

/**
 * Класс содержит значения файла parser.properties для доступа к ним в разных частях приложения.
 */
public class ParserProperties {
    private static Logger logger = LoggerFactory.getLogger(ParserProperties.class);

    private static Properties properties;

    static {
        try {
            properties = new Properties();
            properties.load(new ClassPathResource("parser.properties").getInputStream());
        } catch (IOException ioException) {
            logger.error("Ошибка чтения parser.properties", ioException);
        }
    }

    public static final int RESUMES_ON_PAGE = Integer.parseInt(properties.getProperty("resumesOnPage"), 10);
    public static final String PARSE_URL = properties.getProperty("parseUrl") + RESUMES_ON_PAGE + "&offset=";
    public static final TimeZone TIME_ZONE = TimeZone.getTimeZone(properties.getProperty("timeZone"));
    public static final int ADVERTISING_ON_FIRST_PAGE = Integer.parseInt(properties.getProperty("advertisingOnFirstPage"), 10);
    public static final int OFFSET_LIMIT = Integer.parseInt(properties.getProperty("parsePagesLimit"), 10) * RESUMES_ON_PAGE;
}
