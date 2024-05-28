package com.github.howwrite.jinxiu.core.component;

import com.github.howwrite.jinxiu.core.model.ValueMeta;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class DefaultParamMatcherTest {

    @Test
    public void testMatch_type() {
        DefaultParamMatcher defaultParamMatcher = new DefaultParamMatcher();
        // same class
        Assert.assertTrue(defaultParamMatcher.match(null, findMockClassType("string1"), new ValueMeta(null, findMockClassType("string2"))));

        // child class
        Assert.assertTrue(defaultParamMatcher.match(null, findMockClassType("object1"), new ValueMeta(null, findMockClassType("string2"))));
        Assert.assertFalse(defaultParamMatcher.match(null, findMockClassType("string2"), new ValueMeta(null, findMockClassType("object1"))));

        // interface
        Assert.assertTrue(defaultParamMatcher.match(null, findMockClassType("comparableInteger1"), new ValueMeta(null,
                findMockClassType("integer1"))));
        Assert.assertFalse(defaultParamMatcher.match(null, findMockClassType("comparableString1"), new ValueMeta(null,
                findMockClassType("integer1"))));
        Assert.assertFalse(defaultParamMatcher.match(null, findMockClassType("integer1"), new ValueMeta(null, findMockClassType("comparableInteger1"
        ))));
    }

    /**
     * 泛型类型匹配
     */
    @Test
    public void testMatch_ParameterizedType() {
        DefaultParamMatcher defaultParamMatcher = new DefaultParamMatcher();
        // same class
        Assert.assertTrue(defaultParamMatcher.match(null, findMockClassType("listString1"), new ValueMeta(null, findMockClassType("listString2"))));
        Assert.assertTrue(defaultParamMatcher.match(null, findMockClassType("mapStringInteger1"), new ValueMeta(null, findMockClassType(
                "mapStringInteger2"))));
        Assert.assertFalse(defaultParamMatcher.match(null, findMockClassType("listString1"), new ValueMeta(null, findMockClassType("listInteger1"))));
        Assert.assertFalse(defaultParamMatcher.match(null, findMockClassType("listObject1"), new ValueMeta(null, findMockClassType("listInteger1"))));
    }

    private Type findMockClassType(String fieldName) {
        Field[] declaredFields = MockClass.class.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.getName().equals(fieldName)) {
                return declaredField.getGenericType();
            }
        }
        return null;
    }

    private static class MockClass {
        String string1;
        String string2;
        Object object1;
        Object object2;
        Integer integer1;
        Integer integer2;
        Comparable<Integer> comparableInteger1;
        Comparable<String> comparableString1;
        List<String> listString1;
        List<String> listString2;
        List<Integer> listInteger1;
        List<Integer> listInteger2;
        List<Object> listObject1;
        Map<String, Integer> mapStringInteger1;
        Map<String, Integer> mapStringInteger2;
    }
}