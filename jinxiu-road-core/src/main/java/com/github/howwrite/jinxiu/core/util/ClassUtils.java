package com.github.howwrite.jinxiu.core.util;

import java.lang.reflect.*;
import java.util.Arrays;

/**
 * @author howwrite
 */
public class ClassUtils {
    /**
     * 判断subClass是否是superClass的子类，如果有泛型，会带着泛型一起校验
     *
     * @param superclass 父类
     * @param subclass   子类
     * @return subClass是否是superclass的子类
     */
    public static boolean isSubclassOfGenerics(Class<?> superclass, Class<?> subclass) {
        // 检查是否是直接的继承关系
        if (superclass.isAssignableFrom(subclass)) {
            // 获取实际的类型参数
            Type superClassType = superclass.getGenericSuperclass();
            Type subClassType = subclass.getGenericSuperclass();

            // 如果父类或子类不是参数化的类型，则直接返回true
            if (!(superClassType instanceof ParameterizedType) || !(subClassType instanceof ParameterizedType)) {
                return true;
            }

            // 转换为参数化类型以比较类型参数
            ParameterizedType superType = (ParameterizedType) superClassType;
            ParameterizedType subType = (ParameterizedType) subClassType;

            // 获取并比较类型参数
            Type[] superTypeArgs = superType.getActualTypeArguments();
            Type[] subTypeArgs = subType.getActualTypeArguments();

            // 如果泛型参数数量不匹配，则不是正确的子类
            if (superTypeArgs.length != subTypeArgs.length) {
                return false;
            }

            // 比较每个泛型参数
            for (int i = 0; i < superTypeArgs.length; i++) {
                if (!typeEquals(superTypeArgs[i], subTypeArgs[i])) {
                    // 如果泛型参数不匹配，则不是正确的子类
                    return false;
                }
            }
            // 所有泛型参数都匹配，是正确的子类
            return true;
        }
        return false;
    }

    private static boolean typeEquals(Type type1, Type type2) {
        if (type1 instanceof Class && type2 instanceof Class) {
            return ((Class<?>) type1).isAssignableFrom((Class<?>) type2);
        } else if (type1 instanceof ParameterizedType && type2 instanceof ParameterizedType) {
            ParameterizedType pType1 = (ParameterizedType) type1;
            ParameterizedType pType2 = (ParameterizedType) type2;
            return typeEquals(pType1.getRawType(), pType2.getRawType()) &&
                    Arrays.equals(pType1.getActualTypeArguments(), pType2.getActualTypeArguments());
        } else if (type1 instanceof GenericArrayType && type2 instanceof GenericArrayType) {
            GenericArrayType gType1 = (GenericArrayType) type1;
            GenericArrayType gType2 = (GenericArrayType) type2;
            return typeEquals(gType1.getGenericComponentType(), gType2.getGenericComponentType());
        } else if (type1 instanceof TypeVariable && type2 instanceof TypeVariable) {
            TypeVariable<?> tType1 = (TypeVariable<?>) type1;
            TypeVariable<?> tType2 = (TypeVariable<?>) type2;
            return tType1.equals(tType2);
        } else if (type1 instanceof WildcardType && type2 instanceof WildcardType) {
            WildcardType wType1 = (WildcardType) type1;
            WildcardType wType2 = (WildcardType) type2;
            return Arrays.equals(wType1.getUpperBounds(), wType2.getUpperBounds()) &&
                    Arrays.equals(wType1.getLowerBounds(), wType2.getLowerBounds());
        }
        return false;
    }
}
