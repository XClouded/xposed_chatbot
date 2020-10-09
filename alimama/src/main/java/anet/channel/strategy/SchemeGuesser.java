package anet.channel.strategy;

import java.util.concurrent.ConcurrentHashMap;

public class SchemeGuesser {
    private boolean enabled = true;
    private final ConcurrentHashMap<String, String> guessSchemeMap = new ConcurrentHashMap<>();

    public static SchemeGuesser getInstance() {
        return holder.instance;
    }

    private static class holder {
        static SchemeGuesser instance = new SchemeGuesser();

        private holder() {
        }
    }

    public void setEnabled(boolean z) {
        this.enabled = z;
    }

    public String guessScheme(String str) {
        if (!this.enabled) {
            return null;
        }
        String str2 = this.guessSchemeMap.get(str);
        if (str2 != null) {
            return str2;
        }
        this.guessSchemeMap.put(str, "https");
        return "https";
    }

    public void onSslFail(String str) {
        this.guessSchemeMap.put(str, "http");
    }
}
