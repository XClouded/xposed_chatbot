package com.taobao.orange.candidate;

import android.os.RemoteException;
import android.text.TextUtils;
import com.taobao.orange.aidl.OrangeCandidateCompareStub;
import com.taobao.orange.aidl.ParcelableCandidateCompare;
import com.taobao.orange.util.OLog;
import com.taobao.orange.util.OrangeUtils;
import com.taobao.weex.el.parse.Operators;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mtopsdk.common.util.SymbolExpUtil;

public class UnitAnalyze {
    private static final Pattern BASE_FORMAT;
    private static final String CANDIDATE_HASH_BUCKET = "did_hash";
    private static final Map<String, OPERATOR> OPERATOR_SYMBOL_MAP = new HashMap();
    private static final String TAG = "UnitAnalyze";
    public String key;
    private OPERATOR opr;
    private String val;

    enum OPERATOR {
        EQUALS(SymbolExpUtil.SYMBOL_EQUAL),
        GREATER_EQUALS(Operators.GE),
        LESS_EQUALS(Operators.LE),
        GREATER(Operators.G),
        LESS(Operators.L),
        NOT_EQUALS(Operators.NOT_EQUAL2),
        FUZZY("~="),
        NOT_FUZZY("!~");
        
        private String symbol;

        public String getSymbol() {
            return this.symbol;
        }

        private OPERATOR(String str) {
            this.symbol = str;
        }
    }

    static {
        ArrayList arrayList = new ArrayList();
        for (OPERATOR operator : OPERATOR.values()) {
            OPERATOR_SYMBOL_MAP.put(operator.getSymbol(), operator);
            arrayList.add(operator.getSymbol());
        }
        BASE_FORMAT = Pattern.compile(String.format("^\\s*(\\w+)\\s*(%s)\\s*(.+)\\s*$", new Object[]{OrangeUtils.formatOperateSymbols(arrayList)}));
    }

    static UnitAnalyze complie(String str) {
        return new UnitAnalyze(str);
    }

    private UnitAnalyze(String str) {
        Matcher matcher = BASE_FORMAT.matcher(str);
        if (matcher.find()) {
            this.key = matcher.group(1);
            this.opr = OPERATOR_SYMBOL_MAP.get(matcher.group(2));
            this.val = matcher.group(3);
            if (this.key.equals("did_hash") && this.opr != OPERATOR.EQUALS) {
                throw new IllegalArgumentException(String.format("invalid hash candidate:%s", new Object[]{str}));
            }
            return;
        }
        throw new IllegalArgumentException(String.format("fail parse candidate:%s", new Object[]{str}));
    }

    /* access modifiers changed from: package-private */
    public boolean match(String str, ParcelableCandidateCompare parcelableCandidateCompare) throws RemoteException {
        boolean z;
        if (TextUtils.isEmpty(str)) {
            if (OLog.isPrintLog(1)) {
                OLog.d(TAG, "match no clientVal", "key", this.key);
            }
            return false;
        } else if (parcelableCandidateCompare == null) {
            if (OLog.isPrintLog(1)) {
                OLog.d(TAG, "match no compare", "key", this.key);
            }
            return false;
        } else {
            if (OLog.isPrintLog(0)) {
                String str2 = null;
                if (parcelableCandidateCompare instanceof OrangeCandidateCompareStub) {
                    str2 = ((OrangeCandidateCompareStub) parcelableCandidateCompare).getName();
                }
                OLog.v(TAG, "match start", "key", this.key, "clientVal", str, "opr", this.opr.getSymbol(), "serverVal", this.val, "compare", str2);
            }
            switch (this.opr) {
                case EQUALS:
                    z = parcelableCandidateCompare.equals(str, this.val);
                    break;
                case NOT_EQUALS:
                    z = parcelableCandidateCompare.equalsNot(str, this.val);
                    break;
                case GREATER:
                    z = parcelableCandidateCompare.greater(str, this.val);
                    break;
                case GREATER_EQUALS:
                    z = parcelableCandidateCompare.greaterEquals(str, this.val);
                    break;
                case LESS:
                    z = parcelableCandidateCompare.less(str, this.val);
                    break;
                case LESS_EQUALS:
                    z = parcelableCandidateCompare.lessEquals(str, this.val);
                    break;
                case FUZZY:
                    z = parcelableCandidateCompare.fuzzy(str, this.val);
                    break;
                case NOT_FUZZY:
                    z = parcelableCandidateCompare.fuzzyNot(str, this.val);
                    break;
                default:
                    z = false;
                    break;
            }
            if (!z && OLog.isPrintLog(1)) {
                OLog.d(TAG, "match fail", "key", this.key);
            }
            return z;
        }
    }

    public String toString() {
        return String.format("%s%s%s", new Object[]{this.key, this.opr.getSymbol(), this.val});
    }
}
