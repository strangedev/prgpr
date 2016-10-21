import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by strange on 10/21/16.
 */
public class ErrorLoggingExample {

    private static final Logger log = LogManager.getFormatterLogger(ErrorLoggingExample.class);

    public static void main(String[] args) {

        log.error("Hello this is an error message");

    }

}
