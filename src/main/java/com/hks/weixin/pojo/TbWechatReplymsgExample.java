package com.hks.weixin.pojo;

import java.util.ArrayList;
import java.util.List;

public class TbWechatReplymsgExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TbWechatReplymsgExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andReplyKeyIsNull() {
            addCriterion("reply_key is null");
            return (Criteria) this;
        }

        public Criteria andReplyKeyIsNotNull() {
            addCriterion("reply_key is not null");
            return (Criteria) this;
        }

        public Criteria andReplyKeyEqualTo(String value) {
            addCriterion("reply_key =", value, "replyKey");
            return (Criteria) this;
        }

        public Criteria andReplyKeyNotEqualTo(String value) {
            addCriterion("reply_key <>", value, "replyKey");
            return (Criteria) this;
        }

        public Criteria andReplyKeyGreaterThan(String value) {
            addCriterion("reply_key >", value, "replyKey");
            return (Criteria) this;
        }

        public Criteria andReplyKeyGreaterThanOrEqualTo(String value) {
            addCriterion("reply_key >=", value, "replyKey");
            return (Criteria) this;
        }

        public Criteria andReplyKeyLessThan(String value) {
            addCriterion("reply_key <", value, "replyKey");
            return (Criteria) this;
        }

        public Criteria andReplyKeyLessThanOrEqualTo(String value) {
            addCriterion("reply_key <=", value, "replyKey");
            return (Criteria) this;
        }

        public Criteria andReplyKeyLike(String value) {
            addCriterion("reply_key like", value, "replyKey");
            return (Criteria) this;
        }

        public Criteria andReplyKeyNotLike(String value) {
            addCriterion("reply_key not like", value, "replyKey");
            return (Criteria) this;
        }

        public Criteria andReplyKeyIn(List<String> values) {
            addCriterion("reply_key in", values, "replyKey");
            return (Criteria) this;
        }

        public Criteria andReplyKeyNotIn(List<String> values) {
            addCriterion("reply_key not in", values, "replyKey");
            return (Criteria) this;
        }

        public Criteria andReplyKeyBetween(String value1, String value2) {
            addCriterion("reply_key between", value1, value2, "replyKey");
            return (Criteria) this;
        }

        public Criteria andReplyKeyNotBetween(String value1, String value2) {
            addCriterion("reply_key not between", value1, value2, "replyKey");
            return (Criteria) this;
        }

        public Criteria andReplyValueIsNull() {
            addCriterion("reply_value is null");
            return (Criteria) this;
        }

        public Criteria andReplyValueIsNotNull() {
            addCriterion("reply_value is not null");
            return (Criteria) this;
        }

        public Criteria andReplyValueEqualTo(String value) {
            addCriterion("reply_value =", value, "replyValue");
            return (Criteria) this;
        }

        public Criteria andReplyValueNotEqualTo(String value) {
            addCriterion("reply_value <>", value, "replyValue");
            return (Criteria) this;
        }

        public Criteria andReplyValueGreaterThan(String value) {
            addCriterion("reply_value >", value, "replyValue");
            return (Criteria) this;
        }

        public Criteria andReplyValueGreaterThanOrEqualTo(String value) {
            addCriterion("reply_value >=", value, "replyValue");
            return (Criteria) this;
        }

        public Criteria andReplyValueLessThan(String value) {
            addCriterion("reply_value <", value, "replyValue");
            return (Criteria) this;
        }

        public Criteria andReplyValueLessThanOrEqualTo(String value) {
            addCriterion("reply_value <=", value, "replyValue");
            return (Criteria) this;
        }

        public Criteria andReplyValueLike(String value) {
            addCriterion("reply_value like", value, "replyValue");
            return (Criteria) this;
        }

        public Criteria andReplyValueNotLike(String value) {
            addCriterion("reply_value not like", value, "replyValue");
            return (Criteria) this;
        }

        public Criteria andReplyValueIn(List<String> values) {
            addCriterion("reply_value in", values, "replyValue");
            return (Criteria) this;
        }

        public Criteria andReplyValueNotIn(List<String> values) {
            addCriterion("reply_value not in", values, "replyValue");
            return (Criteria) this;
        }

        public Criteria andReplyValueBetween(String value1, String value2) {
            addCriterion("reply_value between", value1, value2, "replyValue");
            return (Criteria) this;
        }

        public Criteria andReplyValueNotBetween(String value1, String value2) {
            addCriterion("reply_value not between", value1, value2, "replyValue");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}