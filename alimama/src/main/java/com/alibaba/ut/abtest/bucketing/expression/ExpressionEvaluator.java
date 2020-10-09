package com.alibaba.ut.abtest.bucketing.expression;

import android.text.TextUtils;
import com.alibaba.ut.abtest.bucketing.feature.FeatureType;
import com.alibaba.ut.abtest.internal.ABContext;
import com.alibaba.ut.abtest.internal.util.LogUtils;
import com.alibaba.ut.abtest.internal.util.SystemInformation;
import com.taobao.weex.el.parse.Operators;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExpressionEvaluator {
    private static final String KEY_CHANNEL = "device.channel";
    public static final String KEY_CROWD = "service.crowd";
    private static final String KEY_PLATFORM = "mtop.platform";
    private static final String KEY_VERSION = "mtop.appVersion";
    private static final String LP_AND = "$and";
    private static final String LP_OR = "$or";
    private static final String TAG = "ExpressionEvaluator";
    private Map<String, BinaryOperator> operators = buildOperator();
    private Set<String> reservedKeywords = buildReservedKeywords();

    private Set<String> buildReservedKeywords() {
        HashSet hashSet = new HashSet();
        hashSet.add(KEY_PLATFORM);
        hashSet.add(KEY_VERSION);
        hashSet.add(KEY_CHANNEL);
        hashSet.add(KEY_CROWD);
        return hashSet;
    }

    private Map<String, BinaryOperator> buildOperator() {
        HashMap hashMap = new HashMap();
        EqualsOperator equalsOperator = new EqualsOperator();
        hashMap.put(equalsOperator.getOperatorSymbol(), equalsOperator);
        NotEqualsOperator notEqualsOperator = new NotEqualsOperator();
        hashMap.put(notEqualsOperator.getOperatorSymbol(), notEqualsOperator);
        GreaterThanOperator greaterThanOperator = new GreaterThanOperator();
        hashMap.put(greaterThanOperator.getOperatorSymbol(), greaterThanOperator);
        GreaterThanOrEqualsOperator greaterThanOrEqualsOperator = new GreaterThanOrEqualsOperator();
        hashMap.put(greaterThanOrEqualsOperator.getOperatorSymbol(), greaterThanOrEqualsOperator);
        LessThanOperator lessThanOperator = new LessThanOperator();
        hashMap.put(lessThanOperator.getOperatorSymbol(), lessThanOperator);
        LessThanOrEqualsOperator lessThanOrEqualsOperator = new LessThanOrEqualsOperator();
        hashMap.put(lessThanOrEqualsOperator.getOperatorSymbol(), lessThanOrEqualsOperator);
        ContainsOperator containsOperator = new ContainsOperator();
        hashMap.put(containsOperator.getOperatorSymbol(), containsOperator);
        RegularExpressionOperator regularExpressionOperator = new RegularExpressionOperator();
        hashMap.put(regularExpressionOperator.getOperatorSymbol(), regularExpressionOperator);
        return hashMap;
    }

    private boolean isLogicalOperator(String str) {
        return LP_AND.equals(str) || LP_OR.equals(str);
    }

    public boolean evaluate(Expression expression, Map<String, Object> map) {
        if (expression == null || expression.criterions == null || expression.criterions.isEmpty()) {
            return true;
        }
        if (TextUtils.isEmpty(expression.operator)) {
            expression.operator = LP_AND;
        }
        try {
            return logicalOperate(expression.operator, expression.criterions, map);
        } catch (Exception e) {
            LogUtils.logE(TAG, e.getMessage(), e);
            return false;
        }
    }

    private boolean logicalOperate(String str, List<ExpressionCriterion> list, Map<String, Object> map) {
        try {
            if (LP_AND.equals(str)) {
                for (ExpressionCriterion next : list) {
                    if (isLogicalOperator(next.operator)) {
                        return logicalOperate(next.operator, next.criterions, map);
                    }
                    if (!relationalOperate(next, map)) {
                        return false;
                    }
                }
                return true;
            }
            if (LP_OR.equals(str)) {
                for (ExpressionCriterion next2 : list) {
                    if (isLogicalOperator(next2.operator)) {
                        return logicalOperate(next2.operator, next2.criterions, map);
                    }
                    if (relationalOperate(next2, map)) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        } catch (Exception e) {
            LogUtils.logE(TAG, e.getMessage(), e);
        }
    }

    private boolean relationalOperate(ExpressionCriterion expressionCriterion, Map<String, Object> map) {
        if (TextUtils.isEmpty(expressionCriterion.name) || TextUtils.isEmpty(expressionCriterion.operator)) {
            return false;
        }
        if (TextUtils.equals(expressionCriterion.name, "mtop.appName")) {
            return true;
        }
        Object obj = null;
        if (this.reservedKeywords.contains(expressionCriterion.name)) {
            obj = getLocalFieldValue(expressionCriterion.name);
        } else if (map != null) {
            obj = map.get(expressionCriterion.name);
        }
        LogUtils.logD(TAG, "relationalOperate (" + expressionCriterion.name + "（" + obj + "）" + expressionCriterion.operator + Operators.SPACE_STR + expressionCriterion.value + Operators.BRACKET_END_STR);
        if (KEY_VERSION.equals(expressionCriterion.name)) {
            if ("$gt".equals(expressionCriterion.operator)) {
                return VersionUtils.greaterThan(obj, expressionCriterion.value);
            }
            if ("$gte".equals(expressionCriterion.operator)) {
                if (VersionUtils.equals(obj, expressionCriterion.value) || VersionUtils.greaterThan(obj, expressionCriterion.value)) {
                    return true;
                }
                return false;
            } else if ("$lt".equals(expressionCriterion.operator)) {
                return !VersionUtils.greaterThan(obj, expressionCriterion.value);
            } else {
                if ("$lte".equals(expressionCriterion.operator)) {
                    if (VersionUtils.equals(obj, expressionCriterion.value) || !VersionUtils.greaterThan(obj, expressionCriterion.value)) {
                        return true;
                    }
                    return false;
                }
            }
        } else if (KEY_CROWD.equals(expressionCriterion.name) && "$eq".equals(expressionCriterion.operator)) {
            return ABContext.getInstance().getFeatureService().isFeature(FeatureType.Crowd, expressionCriterion.value);
        }
        BinaryOperator binaryOperator = this.operators.get(expressionCriterion.operator);
        if (binaryOperator == null || !binaryOperator.apply(obj, expressionCriterion.value)) {
            return false;
        }
        return true;
    }

    private static Object getLocalFieldValue(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (KEY_PLATFORM.equals(str)) {
            return "android";
        }
        if (KEY_VERSION.equals(str)) {
            return SystemInformation.getInstance().getAppVersionName();
        }
        if (KEY_CHANNEL.equals(str)) {
            return SystemInformation.getInstance().getChannel();
        }
        return null;
    }
}
