package alimama.com.unwetaologger.base;

public class EmptyLogger extends UNWLogger {
    public void debug(LogContent logContent) {
    }

    public void debug(String str, String str2) {
    }

    public void error(ErrorContent errorContent) {
    }

    public void info(LogContent logContent) {
    }

    public EmptyLogger(String str) {
        super(str);
    }
}
