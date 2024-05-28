package com.github.howwrite.jinxiu.core.model;

import com.github.howwrite.jinxiu.core.annotation.Execute;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Type;

@Getter
@AllArgsConstructor
public class ValueMeta {
    /**
     * 值key，可空 {@link Execute#valueKey()}
     */
    private final String valueKey;
    private final Type valueType;
}
