package com.github.howwrite.jinxiu.core.globalValue;

import com.github.howwrite.jinxiu.core.model.ValueMeta;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

@Getter
public class GlobalValueMeta<T> extends ValueMeta {

    private final Supplier<T> globalValueSupplier;

    public GlobalValueMeta(@Nullable String valueKey, @Nonnull Class<T> valueType, @Nonnull Supplier<T> globalValueSupplier) {
        super(valueKey, valueType);
        this.globalValueSupplier = globalValueSupplier;
    }
}
