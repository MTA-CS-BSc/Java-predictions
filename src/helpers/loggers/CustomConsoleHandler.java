package helpers.loggers;

import java.util.logging.ConsoleHandler;

public class CustomConsoleHandler extends ConsoleHandler {
    public CustomConsoleHandler() {
        setFormatter(new CustomRecordFormatter());
    }
}
