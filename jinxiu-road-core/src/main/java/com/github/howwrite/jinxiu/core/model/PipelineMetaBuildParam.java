package com.github.howwrite.jinxiu.core.model;

import com.github.howwrite.jinxiu.core.node.Node;
import lombok.Data;

import javax.annotation.Nonnull;
import java.util.Arrays;

@Data
public class PipelineMetaBuildParam {
    @Nonnull
    private final String name;

    private final Class<? extends Node>[] nodeTypes;

    private final Class<? extends Node> returnValueNodeType;

    private final Class<?> initValueType;

    public static PipelineMetaBuildParamBuilder builder() {
        return new PipelineMetaBuildParamBuilder();
    }

    public static class PipelineMetaBuildParamBuilder {
        private String name;
        private Class<? extends Node>[] nodeTypes;
        private Class<? extends Node> returnValueNodeType;
        private Class<?> initValueType;

        PipelineMetaBuildParamBuilder() {
        }

        public PipelineMetaBuildParamBuilder name(@Nonnull String name) {
            this.name = name;
            return this;
        }

        public PipelineMetaBuildParamBuilder nodeTypes(Class<? extends Node>... nodeTypes) {
            this.nodeTypes = nodeTypes;
            return this;
        }

        public PipelineMetaBuildParamBuilder returnValueNodeType(Class<? extends Node> returnValueNodeType) {
            this.returnValueNodeType = returnValueNodeType;
            return this;
        }

        public PipelineMetaBuildParamBuilder initValueType(Class<?> initValueType) {
            this.initValueType = initValueType;
            return this;
        }

        public PipelineMetaBuildParam build() {
            return new PipelineMetaBuildParam(this.name, this.nodeTypes, this.returnValueNodeType, this.initValueType);
        }

        public String toString() {
            return "PipelineMetaBuildParam.PipelineMetaBuildParamBuilder(name=" + this.name + ", nodeTypes=" + Arrays.deepToString(this.nodeTypes) + ", returnValueNodeType=" + this.returnValueNodeType + ", initValueType=" + this.initValueType + ")";
        }
    }
}
