package com.github.howwrite.jinxiu.core.model;

import com.github.howwrite.jinxiu.core.annotation.Execute;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Type;

@Getter
@AllArgsConstructor
public class ValueMeta {
    /**
     * 值key，可空 {@link Execute#valueKey()}
     */
    @Nullable
    private final String valueKey;
    @Nonnull
    private final Type valueType;
}
