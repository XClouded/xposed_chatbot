package com.alibaba.android.enhance.svg.parser;

import androidx.annotation.NonNull;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LLFunctionParser<K, V> {
    private static final char SPACE = ' ';
    private Lexer lexer;
    private Mapper<K, V> mapper;

    public interface Mapper<K, V> {
        Map<K, V> map(String str, List<String> list);
    }

    private enum Token {
        FUNC_NAME,
        PARAM_VALUE,
        LEFT_PARENT,
        RIGHT_PARENT,
        COMMA
    }

    public LLFunctionParser(@NonNull String str, @NonNull Mapper<K, V> mapper2) {
        this.lexer = new Lexer(str);
        this.mapper = mapper2;
    }

    public LinkedList<Map<K, V>> parse() {
        boolean unused = this.lexer.moveOn();
        return definition();
    }

    private LinkedList<Map<K, V>> definition() {
        LinkedList<Map<K, V>> linkedList = new LinkedList<>();
        do {
            linkedList.add(function());
        } while (this.lexer.getCurrentToken() == Token.FUNC_NAME);
        return linkedList;
    }

    private Map<K, V> function() {
        LinkedList linkedList = new LinkedList();
        String match = match(Token.FUNC_NAME);
        match(Token.LEFT_PARENT);
        linkedList.add(match(Token.PARAM_VALUE));
        while (true) {
            if (this.lexer.getCurrentToken() != Token.COMMA && this.lexer.getCurrentToken() != Token.PARAM_VALUE) {
                match(Token.RIGHT_PARENT);
                return this.mapper.map(match, linkedList);
            } else if (this.lexer.getCurrentToken() == Token.COMMA) {
                match(Token.COMMA);
                linkedList.add(match(Token.PARAM_VALUE));
            } else {
                linkedList.add(match(Token.PARAM_VALUE));
            }
        }
    }

    private String match(Token token) {
        if (token == this.lexer.getCurrentToken()) {
            String access$300 = this.lexer.getCurrentTokenValue();
            boolean unused = this.lexer.moveOn();
            return access$300;
        }
        throw new InterpretationException(token + " Token doesn't match " + this.lexer.source);
    }

    private static class InterpretationException extends RuntimeException {
        private InterpretationException(String str) {
            super(str);
        }
    }

    private static class Lexer {
        private static final char A_LOWER = 'a';
        private static final char A_UPPER = 'A';
        private static final String COMMA = ",";
        private static final char DOT = '.';
        private static final String LEFT_PARENT = "(";
        private static final char MINUS = '-';
        private static final char NINE = '9';
        private static final char PLUS = '+';
        private static final String RIGHT_PARENT = ")";
        private static final char ZERO = '0';
        private static final char Z_LOWER = 'z';
        private static final char Z_UPPER = 'Z';
        private Token current;
        private int pointer;
        /* access modifiers changed from: private */
        public String source;
        private String value;

        private boolean isCharacterOrDigit(char c) {
            return ('0' <= c && c <= '9') || ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z');
        }

        private Lexer(String str) {
            this.pointer = 0;
            this.source = str;
        }

        /* access modifiers changed from: private */
        public Token getCurrentToken() {
            return this.current;
        }

        /* access modifiers changed from: private */
        public String getCurrentTokenValue() {
            return this.value;
        }

        /* access modifiers changed from: private */
        public boolean moveOn() {
            int i = this.pointer;
            while (true) {
                if (this.pointer >= this.source.length()) {
                    break;
                }
                char charAt = this.source.charAt(this.pointer);
                if (charAt == ' ') {
                    int i2 = this.pointer;
                    this.pointer = i2 + 1;
                    if (i != i2) {
                        break;
                    }
                    i++;
                } else if (isCharacterOrDigit(charAt) || charAt == '.' || charAt == '%' || charAt == '-' || charAt == '+') {
                    this.pointer++;
                } else if (i == this.pointer) {
                    this.pointer++;
                }
            }
            if (i != this.pointer) {
                moveOn(this.source.substring(i, this.pointer).trim());
                return true;
            }
            this.current = null;
            this.value = null;
            return false;
        }

        private void moveOn(String str) {
            if ("(".equals(str)) {
                this.current = Token.LEFT_PARENT;
                this.value = "(";
            } else if (")".equals(str)) {
                this.current = Token.RIGHT_PARENT;
                this.value = ")";
            } else if (",".equals(str)) {
                this.current = Token.COMMA;
                this.value = ",";
            } else if (isFuncName(str)) {
                this.current = Token.FUNC_NAME;
                this.value = str;
            } else {
                this.current = Token.PARAM_VALUE;
                this.value = str;
            }
        }

        private boolean isFuncName(CharSequence charSequence) {
            for (int i = 0; i < charSequence.length(); i++) {
                char charAt = charSequence.charAt(i);
                if (('a' > charAt || charAt > 'z') && (('A' > charAt || charAt > 'Z') && charAt != '-')) {
                    return false;
                }
            }
            return true;
        }
    }
}
