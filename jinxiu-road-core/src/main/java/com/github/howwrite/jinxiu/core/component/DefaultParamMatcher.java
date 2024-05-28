package com.github.howwrite.jinxiu.core.component;

import com.github.howwrite.jinxiu.core.model.ValueMeta;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.TypeUtils;

import java.lang.reflect.Type;

public class DefaultParamMatcher implements ParamMatcher {

    @Override
    public boolean match(String needValueKey, Type needValueType, ValueMeta providerValueMeta) {
        if (StringUtils.isNotBlank(needValueKey) && !needValueKey.equals(providerValueMeta.getValueKey())) {
            return false;
        }

        return TypeUtils.isAssignable(needValueType, providerValueMeta.getValueType());
    }
}
