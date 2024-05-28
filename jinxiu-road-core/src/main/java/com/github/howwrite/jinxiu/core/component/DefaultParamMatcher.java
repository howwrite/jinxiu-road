package com.github.howwrite.jinxiu.core.component;

import com.github.howwrite.jinxiu.core.model.ValueMeta;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.TypeUtils;

import javax.annotation.Nonnull;
import java.lang.reflect.Type;

public class DefaultParamMatcher implements ParamMatcher {

    @Override
    public boolean match(String needValueKey, @Nonnull Type needValueType, @Nonnull ValueMeta providerValueMeta) {
        if (StringUtils.isNotBlank(needValueKey) && !needValueKey.equals(providerValueMeta.getValueKey())) {
            return false;
        }

        return TypeUtils.isAssignable(providerValueMeta.getValueType(), needValueType);
    }
}
