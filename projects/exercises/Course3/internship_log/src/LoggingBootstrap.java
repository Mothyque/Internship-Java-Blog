import java.net.URL;

final class LoggingBootstrap {

    private LoggingBootstrap() {
    }

    static void configureDev() {
        configure("/logback-dev.xml");
    }

    static void configureMain() {
        configure("/logback-main.xml");
    }

    static void configureProd() {
        configure("/logback-prod.xml");
    }

    private static void configure(String resourcePath) {
        URL resource = LoggingBootstrap.class.getResource(resourcePath);
        if (resource == null) {
            throw new IllegalStateException("Missing logging configuration: " + resourcePath);
        }

        System.setProperty("logback.configurationFile", resource.toExternalForm());
    }
}
