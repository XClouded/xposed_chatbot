package com.taobao.weex.el.parse;

import com.alibaba.fastjson.JSONArray;
import java.util.List;

class Block extends Token {
    private List<Token> tokens;

    public Block(List<Token> list, int i) {
        super("", i);
        this.tokens = list;
    }

    public Object execute(Object obj) {
        if (getType() == 7) {
            if (this.tokens == null || this.tokens.size() == 0) {
                return new JSONArray(4);
            }
            JSONArray jSONArray = new JSONArray(this.tokens.size());
            for (int i = 0; i < this.tokens.size(); i++) {
                Token token = this.tokens.get(i);
                if (token == null) {
                    jSONArray.add((Object) null);
                } else {
                    jSONArray.add(token.execute(obj));
                }
            }
            return jSONArray;
        } else if (this.tokens == null || this.tokens.size() == 0) {
            return null;
        } else {
            return this.tokens.get(0).execute(obj);
        }
    }

    public String toString() {
        if (getType() == 7) {
            return "" + this.tokens + "";
        } else if (this.tokens == null || this.tokens.size() != 1) {
            return Operators.BLOCK_START_STR + this.tokens + '}';
        } else {
            return Operators.BLOCK_START_STR + this.tokens.get(0) + '}';
        }
    }
}
