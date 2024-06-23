package com.github.howwrite.jinxiu.core.pipeline;

import com.github.howwrite.jinxiu.core.globalValue.GlobalValueMeta;
import com.github.howwrite.jinxiu.core.node.NodeMeta;
import lombok.Data;

@Data
public class PipelineMeta {
    /**
     * 管道名称，用于运行时识别执行的是哪条管道
     */
    private final String name;

    /**
     * 管道中节点元信息列表，节点顺序有依赖关系
     */
    private final NodeMeta[] nodes;

    /**
     * 初始值类型
     */
    private final Class<?> initValueType;

    private final GlobalValueMeta<?>[] globalValueMetas;

    /**
     * 当前产生返回值的节点下标。
     * 执行结束后会将该节点的返回值作为管道的返回值返回。
     */
    private final int returnValueNodeIndex;

    /**
     * pipeline唯一表示是name
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof PipelineMeta)) {
            return false;
        }
        return name.equals(((PipelineMeta) object).getName());
    }
}