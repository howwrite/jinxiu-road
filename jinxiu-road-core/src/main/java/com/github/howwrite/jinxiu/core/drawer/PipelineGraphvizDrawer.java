package com.github.howwrite.jinxiu.core.drawer;

import com.github.howwrite.jinxiu.core.globalValue.GlobalValueMeta;
import com.github.howwrite.jinxiu.core.model.paramsource.*;
import com.github.howwrite.jinxiu.core.node.NodeMeta;
import com.github.howwrite.jinxiu.core.pipeline.PipelineMeta;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

public class PipelineGraphvizDrawer {

    public static String draw2Graphviz(PipelineMeta pipelineMeta) {
        if (pipelineMeta == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder("digraph " + pipelineMeta.getName() + " {graph [rankdir = \"TB\"];\n" +
                "node [fontsize = \"16\"shape = \"ellipse\"];\n" +
                "edge [];\n");
        sb.append(drawInitValue(pipelineMeta.getInitValueType())).append('\n');
        for (int i = 0; i < pipelineMeta.getGlobalValueMetas().length; i++) {
            sb.append(drawGlobalValue(i, pipelineMeta.getGlobalValueMetas()[i])).append('\n');
        }

        for (int i = 0; i < pipelineMeta.getNodes().length; i++) {
            NodeMeta nodeMeta = pipelineMeta.getNodes()[i];
            sb.append(drawNode(nodeMeta)).append('\n');
            sb.append(drawLine(nodeMeta)).append('\n');
        }
        sb.append("\"node_").append(pipelineMeta.getReturnValueNodeIndex()).append("\":return -> end\n");
        sb.append("}");
        return sb.toString();
    }

    public static String drawLine(NodeMeta nodeMeta) {
        String nodeName = "node_" + nodeMeta.getIndex();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nodeMeta.getParamSources().length; i++) {
            ParamSource paramSource = nodeMeta.getParamSources()[i];
            if (paramSource instanceof GlobalValueSource) {
                sb.append("\"globalValue_").append(((GlobalValueSource) paramSource).getIndex()).append("\":self").append("->\"").append(nodeName).append("\":").append("arg").append(i);
            } else if (paramSource instanceof InitValueSource) {
                sb.append("\"initValue\":self").append("->\"").append(nodeName).append("\":").append("arg").append(i);
            } else if (paramSource instanceof InitValueFieldSource) {
                sb.append("\"initValue\":").append(((InitValueFieldSource) paramSource).getField().getName()).append("->\"").append(nodeName).append("\":").append("arg").append(i);
            } else if (paramSource instanceof ForwardParamSource) {
                sb.append("\"node_").append(((ForwardParamSource) paramSource).getIndex()).append("\":return->\"").append(nodeName).append("\":").append("arg").append(i);
            }
        }
        return sb.append('\n').toString();
    }

    public static String drawGlobalValue(int index, GlobalValueMeta<?> globalValueMeta) {
        StringBuilder sb = new StringBuilder();
        String globalValueName = "globalValue_" + index;
        sb.append("\"").append(globalValueName).append("\" [shape = \"record\"label = \"").append(globalValueName);
        sb.append("|").append(buildTypeName(globalValueMeta.getValueType().getTypeName()));
        if (StringUtils.isNotBlank(globalValueMeta.getValueKey())) {
            sb.append("-").append(globalValueMeta.getValueKey());
        }
        return sb.append("\"];").toString();
    }

    public static String drawInitValue(Class<?> initValueType) {
        StringBuilder sb = new StringBuilder();
        sb.append("\"initValue").append("\" [shape = \"record\"label = \"initValue|").append("<self>").append(initValueType.getSimpleName());
        for (Field declaredField : initValueType.getDeclaredFields()) {
            String fieldName = declaredField.getName();
            Type genericType = declaredField.getGenericType();
            sb.append("|<").append(fieldName).append(">").append(buildTypeName(genericType.getTypeName())).append('(').append(fieldName).append(')');
        }
        return sb.append("\"];").toString();
    }

    public static String drawNode(NodeMeta nodeMeta) {
        StringBuilder sb = new StringBuilder();
        String nodeName = "node_" + nodeMeta.getIndex();
        sb.append("\"").append(nodeName).append("\" [shape = \"record\"label = \"<return>").append(nodeMeta.getNodeClass().getSimpleName()).append('(')
                .append(buildTypeName(nodeMeta.getExecuteMethod().getGenericReturnType().getTypeName()))
                .append(')');

        for (int i = 0; i < nodeMeta.getExecuteMethod().getParameters().length; i++) {
            Parameter parameter = nodeMeta.getExecuteMethod().getParameters()[i];
            String name = "arg" + i;
            Type genericType = parameter.getParameterizedType();
            sb.append("|<").append(name).append(">").append(buildTypeName(genericType.getTypeName())).append('(').append(name).append(')');
        }
        return sb.append("\"];").toString();
    }


    public static String buildTypeName(String typeName) {
        return typeName.replace("<", "\\<").replace(">", "\\>");
    }
}
