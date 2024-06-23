package com.github.howwrite.jinxiu.core.node;

import com.github.howwrite.jinxiu.core.annotation.Execute;
import com.github.howwrite.jinxiu.core.annotation.Param;
import com.github.howwrite.jinxiu.core.component.ParamMatcher;
import com.github.howwrite.jinxiu.core.exception.BuildException;
import com.github.howwrite.jinxiu.core.globalValue.GlobalValueMeta;
import com.github.howwrite.jinxiu.core.model.ValueMeta;
import com.github.howwrite.jinxiu.core.model.paramsource.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

@RequiredArgsConstructor
public class NodeMetaFactory {
    /**
     * 参数匹配器
     */
    private final ParamMatcher paramMatcher;

    public NodeMeta buildNodeMeta(int nodeIndex, Class<? extends Node> nodeClass, Class<?> initValueType, ValueMeta[] forwardReturnValueMetas,
                                  GlobalValueMeta<?>[] globalValueClasses) {
        Pair<Execute, Method> executeMethod = findExecuteMethod(nodeClass);
        Method method = executeMethod.getValue();
        method.setAccessible(true);
        Parameter[] parameters = method.getParameters();
        ParamSource[] paramSources = new ParamSource[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            ParamSource paramSource = buildParamSource(parameter, initValueType, forwardReturnValueMetas, globalValueClasses);
            paramSources[i] = paramSource;
        }
        return new NodeMeta(nodeIndex, nodeClass, method, executeMethod.getKey(), paramSources);
    }

    public ParamSource buildParamSource(Parameter parameter, Class<?> initValueType, ValueMeta[] forwardReturnValueMetas,
                                        GlobalValueMeta<?>[] globalValueClasses) {
        String paramName = buildParamName(parameter);
        Type paramType = parameter.getParameterizedType();

        // 通过前置node返回值匹配
        for (int i = forwardReturnValueMetas.length - 1; i >= 0; i--) {
            if (forwardReturnValueMetas[i] != null && paramMatcher.match(paramName, paramType, forwardReturnValueMetas[i])) {
                return new ForwardParamSource(paramType, i);
            }
        }

        // 如果需要的是初始值
        if (paramMatcher.match(paramName, paramType, new ValueMeta(null, initValueType))) {
            return new InitValueSource(paramType);
        }
        // 如果需要的是初始值的某个字段
        Field[] declaredFields = initValueType.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (paramMatcher.match(paramName, paramType, new ValueMeta(declaredField.getName(), declaredField.getGenericType()))) {
                declaredField.setAccessible(true);
                return new InitValueFieldSource(paramType, declaredField);
            }
        }

        // 全局字段
        for (int i = 0; i < globalValueClasses.length; i++) {
            if (globalValueClasses[i] != null && paramMatcher.match(paramName, paramType, globalValueClasses[i])) {
                return new GlobalValueSource(paramType, i);
            }
        }
        throw new BuildException("not match param, name:" + paramName + ", type:" + paramType);
    }

    @Nullable
    private String buildParamName(@Nonnull Parameter parameter) {
        Param annotation = parameter.getAnnotation(Param.class);
        if (annotation != null && StringUtils.isNotBlank(annotation.valueKey())) {
            return annotation.valueKey();
        }
        return null;
    }

    /**
     * 查询该类型中标注{@link Execute}的方法
     */
    @Nonnull
    private Pair<Execute, Method> findExecuteMethod(@Nonnull Class<?> clazz) {
        for (Method declaredMethod : clazz.getDeclaredMethods()) {
            Execute annotation = declaredMethod.getAnnotation(Execute.class);
            if (annotation != null) {
                return Pair.of(annotation, declaredMethod);
            }
        }
        throw new BuildException(clazz.getName() + " 无@Execute方法");
    }
}
