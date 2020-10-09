package com.taobao.android.ultron.expr;

import android.util.Pair;
import com.taobao.android.dinamic.DinamicConstant;
import com.taobao.weex.el.parse.Operators;
import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionExt {
    public static final Set<String> OPS = new HashSet();
    private static Pattern sPattern = Pattern.compile("\\$\\{[a-zA-Z()\\. _\\-\\[\\]0-9]*(?!\\$\\{[a-zA-Z()\\. \\_\\-\\[\\]0-9]*\\})[a-zA-Z()\\. \\_\\-\\[\\]0-9]*\\}");

    static {
        OPS.add("empty");
        OPS.add(DinamicConstant.NOT_PREFIX);
        OPS.add("deleteifnull");
    }

    public static Object evaluate(Object obj, String str) {
        Object obj2;
        String str2;
        if (obj == null || str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(str);
        for (Matcher matcher = sPattern.matcher(sb); matcher.find(); matcher = sPattern.matcher(sb)) {
            int start = matcher.start();
            int end = matcher.end();
            int length = sb.length();
            String group = matcher.group();
            Pair<String, String> extendOperator = getExtendOperator(group);
            if (extendOperator != null) {
                OpsResult evaluateOps = evaluateOps((String) extendOperator.first, extendOperator.second, obj);
                obj2 = evaluateOps.result;
                if (evaluateOps.interrupt) {
                    return obj2;
                }
            } else {
                obj2 = Expression.getValue(obj, group);
            }
            if (start == 0 && end == length) {
                return obj2;
            }
            if (obj2 == null) {
                str2 = "";
            } else {
                str2 = obj2.toString();
            }
            sb.replace(start, end, str2);
        }
        return sb.toString();
    }

    private static OpsResult evaluateOps(String str, Object obj, Object obj2) {
        OpsResult opsResult = new OpsResult();
        if ("empty".equalsIgnoreCase(str)) {
            opsResult.result = Integer.valueOf(doEmpty(obj2, obj));
            return opsResult;
        } else if (DinamicConstant.NOT_PREFIX.equalsIgnoreCase(str)) {
            opsResult.result = Integer.valueOf(doNot(obj2, obj));
            return opsResult;
        } else if ("deleteIfNull".equalsIgnoreCase(str)) {
            doDeleteIfNull(obj2, obj, opsResult);
            return opsResult;
        } else {
            opsResult.result = obj;
            return opsResult;
        }
    }

    private static int doNot(Object obj, Object obj2) {
        if (obj2 instanceof String) {
            String str = "${" + obj2 + "}";
            Object evaluate = evaluate(obj, str);
            if (!(evaluate instanceof String)) {
                return doNot(obj, evaluate);
            }
            String str2 = (String) evaluate;
            if ("true".equalsIgnoreCase(str2)) {
                return 0;
            }
            if ("false".equalsIgnoreCase(str2)) {
                return 1;
            }
            try {
                if (Integer.parseInt(str2) == 0) {
                    return 1;
                }
                return 0;
            } catch (Exception unused) {
                if (str.length() > 0) {
                    return 0;
                }
                return 1;
            }
        } else if (obj2 instanceof Number) {
            if (((Number) obj2).intValue() != 0) {
                return 0;
            }
            return 1;
        } else if (obj2 instanceof Boolean) {
            return ((Boolean) obj2).booleanValue() ^ true ? 1 : 0;
        } else {
            return 0;
        }
    }

    private static void doDeleteIfNull(Object obj, Object obj2, OpsResult opsResult) {
        opsResult.result = evaluate(obj, "${" + obj2 + "}");
        opsResult.interrupt = doEmpty(obj, obj2) > 0;
    }

    private static int doEmpty(Object obj, Object obj2) {
        if (obj2 instanceof String) {
            Object evaluate = evaluate(obj, "${" + obj2 + "}");
            if (!(evaluate instanceof String)) {
                return doEmpty(obj, evaluate);
            }
            String trim = ((String) evaluate).trim();
            if ("true".equalsIgnoreCase(trim)) {
                return 0;
            }
            if (!"false".equalsIgnoreCase(trim) && trim.length() > 0) {
                return 0;
            }
            return 1;
        } else if (obj2 == null) {
            return 1;
        } else {
            if (obj2 instanceof Number) {
                if (((Number) obj2).intValue() != 0) {
                    return 0;
                }
                return 1;
            } else if (obj2 instanceof Boolean) {
                return ((Boolean) obj2).booleanValue() ^ true ? 1 : 0;
            } else {
                if (obj2 instanceof List) {
                    return ((List) obj2).isEmpty() ? 1 : 0;
                }
                if (obj2.getClass().isArray()) {
                    try {
                        if (Array.getLength(obj2) > 0) {
                            return 0;
                        }
                        return 1;
                    } catch (Exception unused) {
                        return 0;
                    }
                } else if (obj2 instanceof Map) {
                    return ((Map) obj2).isEmpty() ? 1 : 0;
                } else {
                    return 0;
                }
            }
        }
    }

    private static Pair<String, String> getExtendOperator(String str) {
        String trim = str.substring(str.indexOf(Operators.BLOCK_START_STR) + 1, str.length() - 1).trim();
        String[] split = trim.split(Operators.SPACE_STR);
        if (split.length <= 0) {
            return null;
        }
        String str2 = split[0];
        if (isOps(str2)) {
            return new Pair<>(str2, trim.substring(str2.length()));
        }
        return null;
    }

    private static boolean isOps(String str) {
        return OPS.contains(str.toLowerCase());
    }

    private static class OpsResult {
        public boolean interrupt;
        public Object result;

        private OpsResult() {
        }
    }
}
