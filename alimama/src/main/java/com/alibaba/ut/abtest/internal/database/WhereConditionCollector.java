package com.alibaba.ut.abtest.internal.database;

import com.taobao.weex.el.parse.Operators;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class WhereConditionCollector {
    private final ArrayList<WhereCondition> whereConditions = new ArrayList<>();

    public WhereConditionCollector whereAnd(WhereCondition whereCondition, WhereCondition... whereConditionArr) {
        this.whereConditions.add(whereCondition);
        if (whereConditionArr != null && whereConditionArr.length > 0) {
            Collections.addAll(this.whereConditions, whereConditionArr);
        }
        return this;
    }

    public WhereConditionCollector whereAnd(List<WhereCondition> list) {
        this.whereConditions.addAll(list);
        return this;
    }

    public WhereConditionCollector whereOr(WhereCondition whereCondition, WhereCondition whereCondition2, WhereCondition... whereConditionArr) {
        this.whereConditions.add(combineOrWhereConditions(whereCondition, whereCondition2, whereConditionArr));
        return this;
    }

    public WhereConditionCollector whereOr(List<WhereCondition> list) {
        this.whereConditions.add(combineWhereConditions(" OR ", list));
        return this;
    }

    public boolean isEmpty() {
        return this.whereConditions.isEmpty();
    }

    public WhereCondition combine() {
        StringBuilder sb = new StringBuilder();
        ArrayList arrayList = new ArrayList();
        ListIterator<WhereCondition> listIterator = this.whereConditions.listIterator();
        while (listIterator.hasNext()) {
            if (listIterator.hasPrevious()) {
                sb.append(" AND ");
            }
            addCondition(sb, arrayList, listIterator.next());
        }
        return new WhereCondition(sb.toString(), arrayList.toArray());
    }

    public static WhereCondition combineOrWhereConditions(WhereCondition whereCondition, WhereCondition whereCondition2, WhereCondition... whereConditionArr) {
        return combineWhereConditions(" OR ", whereCondition, whereCondition2, whereConditionArr);
    }

    public static WhereCondition combineAndWhereConditions(WhereCondition whereCondition, WhereCondition whereCondition2, WhereCondition... whereConditionArr) {
        return combineWhereConditions(" AND ", whereCondition, whereCondition2, whereConditionArr);
    }

    public static WhereCondition combineWhereConditions(String str, WhereCondition whereCondition, WhereCondition whereCondition2, WhereCondition... whereConditionArr) {
        StringBuilder sb = new StringBuilder(Operators.BRACKET_START_STR);
        ArrayList arrayList = new ArrayList();
        addCondition(sb, arrayList, whereCondition);
        sb.append(str);
        addCondition(sb, arrayList, whereCondition2);
        for (WhereCondition addCondition : whereConditionArr) {
            sb.append(str);
            addCondition(sb, arrayList, addCondition);
        }
        sb.append(')');
        return new WhereCondition(sb.toString(), arrayList.toArray());
    }

    public static WhereCondition combineWhereConditions(String str, List<WhereCondition> list) {
        StringBuilder sb = new StringBuilder(Operators.BRACKET_START_STR);
        ArrayList arrayList = new ArrayList();
        ListIterator<WhereCondition> listIterator = list.listIterator();
        while (listIterator.hasNext()) {
            if (listIterator.hasPrevious()) {
                sb.append(str);
            }
            addCondition(sb, arrayList, listIterator.next());
        }
        sb.append(')');
        return new WhereCondition(sb.toString(), arrayList.toArray());
    }

    static void addCondition(StringBuilder sb, List<Object> list, WhereCondition whereCondition) {
        whereCondition.appendTo(sb);
        whereCondition.appendValuesTo(list);
    }
}
