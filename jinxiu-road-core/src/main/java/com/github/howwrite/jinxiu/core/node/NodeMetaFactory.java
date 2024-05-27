package com.github.howwrite.jinxiu.core.node;

import com.github.howwrite.jinxiu.core.annotation.Execute;
import com.github.howwrite.jinxiu.core.annotation.Param;
import com.github.howwrite.jinxiu.core.component.ParamMatcher;
import com.github.howwrite.jinxiu.core.exception.BuildException;
import com.github.howwrite.jinxiu.core.model.ValueMeta;
import com.github.howwrite.jinxiu.core.model.paramsource.ForwardParamSource;
import com.github.howwrite.jinxiu.core.model.paramsource.InitValueSource;
import com.github.howwrite.jinxiu.core.model.paramsource.ParamSource;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;

@RequiredArgsConstructor
public class NodeMetaFactory {
    /**
     * 参数匹配器
     */
    private final ParamMatcher paramMatcher;

    public NodeMeta buildNodeMeta(int nodeIndex, Class<? extends Node> nodeClass, Class<?> initValueType, ValueMeta[] forwardReturnValueMetas,
                                  NodeMeta[] nodeList) {
        Pair<Execute, Method> executeMethod = findExecuteMethod(nodeClass);
        Method method = executeMethod.getValue();
        method.setAccessible(true);
        Parameter[] parameters = method.getParameters();
        ParamSource[] paramSources = new ParamSource[parameters.length];
        ArrayList<Integer> parentNodeIndexList = new ArrayList<>();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            ParamSource paramSource = buildParamSource(parameter, initValueType, forwardReturnValueMetas);
            paramSources[i] = paramSource;
            if (paramSource instanceof ForwardParamSource) {
                ForwardParamSource forwardParamSource = (ForwardParamSource) paramSource;
                int parentNodeIndex = forwardParamSource.getIndex();
                nodeList[parentNodeIndex].addChildIndex(nodeIndex);
                parentNodeIndexList.add(parentNodeIndex);
            }
        }
        int[] parentNodeIndexes = new int[parentNodeIndexList.size()];
        for (int i = 0; i < parentNodeIndexList.size(); i++) {
            parentNodeIndexes[i] = parentNodeIndexList.get(i);
        }
        return new NodeMeta(nodeIndex, nodeClass, method, executeMethod.getKey(), paramSources, parentNodeIndexes);
    }

    private ParamSource buildParamSource(Parameter parameter, Class<?> initValueType, ValueMeta[] forwardReturnValueMetas) {
        String paramName = buildParamName(parameter);
        Class<?> paramType = parameter.getType();
        for (int i = forwardReturnValueMetas.length - 1; i >= 0; i--) {
            if (forwardReturnValueMetas[i] != null && paramMatcher.match(paramName, paramType, forwardReturnValueMetas[i])) {
                return new ForwardParamSource(i);
            }
        }

        // 如果需要的是初始值
        if (paramMatcher.match(paramName, paramType, new ValueMeta(null, initValueType))) {
            return new InitValueSource();
        }

        // 如果需要的是初始值的某个字段
        Field[] declaredFields = initValueType.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (paramMatcher.match(paramName, paramType, new ValueMeta(declaredField.getName(), declaredField.getType()))) {
                declaredField.setAccessible(true);
                return new InitValueSource(declaredField);
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
        if (parameter.isNamePresent()) {
            return parameter.getName();
        }
        return null;
    }

    /**
     * 查询该类型中标注{@link Execute}的方法
     *
     * @param clazz
     * @return
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
