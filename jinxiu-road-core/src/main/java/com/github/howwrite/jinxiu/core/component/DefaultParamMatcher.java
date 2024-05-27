package com.github.howwrite.jinxiu.core.component;

import com.github.howwrite.jinxiu.core.model.ValueMeta;
import com.github.howwrite.jinxiu.core.util.ClassUtils;
import org.apache.commons.lang3.StringUtils;

public class DefaultParamMatcher implements ParamMatcher {

    @Override
    public boolean match(String needValueKey, Class<?> needValueType, ValueMeta providerValueMeta) {
        if (StringUtils.isNotBlank(needValueKey) && !needValueKey.equals(providerValueMeta.getValueKey())) {
            return false;
        }
        return ClassUtils.isSubclassOfGenerics(needValueType, providerValueMeta.getValueType());
    }
}
