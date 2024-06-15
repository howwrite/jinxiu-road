package com.github.howwrite.jinxiu.core.globalValue;

import com.github.howwrite.jinxiu.core.model.ValueMeta;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.function.Supplier;

@Getter
public class GlobalValueMeta extends ValueMeta {

    private final Supplier<?> globalValueSupplier;

    public GlobalValueMeta(@Nullable String valueKey, @Nonnull Type valueType, @Nonnull Supplier<?> globalValueSupplier) {
        super(valueKey, valueType);
        this.globalValueSupplier = globalValueSupplier;
    }
}
