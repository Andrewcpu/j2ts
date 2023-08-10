package net.andrewcpu.j2ts.utils;

import net.andrewcpu.j2ts.annotations.NullableField;
import net.andrewcpu.j2ts.annotations.OptionalField;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static net.andrewcpu.j2ts.JTSTransformer.MODEL_IMPORT_NAME;
import static net.andrewcpu.j2ts.JTSTransformer.PREFIX;


public class ParameterUtils {
    public static String getTypeString(Parameter parameter, Type type, Set<Class<?>> types) {
        String typeString = getTypeString(parameter.getType(), type, types);
        if (parameter.isAnnotationPresent(NullableField.class)) {
            return typeString + " | null";
        }
        return typeString;
    }

    public static String getTypeString(Class<?> paramType, Type type, Set<Class<?>> types) {
        if (paramType == null) return "void";

        String value;
        boolean isArrayOrList = paramType.isArray() || List.class.isAssignableFrom(paramType);
        Class<?> componentType = paramType;

        if (isArrayOrList) {
            if (paramType.isArray()) {
                componentType = paramType.getComponentType();
            }
            else if (type instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType)type).getActualTypeArguments();
                for (Type typeArg : actualTypeArguments) {
                    componentType = (Class<?>)typeArg;
                }
            }
        }

        if (types.contains(componentType)) {
            value = MODEL_IMPORT_NAME + "." + PREFIX + componentType.getSimpleName();
        } else if (componentType == int.class || componentType == double.class || componentType == float.class || componentType == byte.class || componentType == short.class || componentType == long.class) {
            value = "number";
        } else if (componentType == Integer.class || componentType == Double.class || componentType == Float.class || componentType == Byte.class || componentType == Short.class || componentType == Long.class) {
            value = "number";
        } else if (componentType == String.class) {
            value = "string";
        } else if (componentType == Boolean.class || componentType == boolean.class) {
            value = "boolean";
        } else if (componentType == void.class) {
            value = "void";
        } else {
            value = "any";
        }

        if (isArrayOrList) {
            value += "[]";
        }

        return value;
    }

    public static String getParameterName(Parameter parameter) {
        if (parameter.isAnnotationPresent(RequestParam.class)) {
            RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
            if (requestParam.value().length() != 0) {
                return requestParam.value();
            }
        }
        if (parameter.isAnnotationPresent(PathVariable.class)) {
            PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
            if (pathVariable.value().length() != 0) {
                return pathVariable.value();
            }
        }
        if (parameter.isAnnotationPresent(RequestHeader.class)) {
            RequestHeader header = parameter.getAnnotation(RequestHeader.class);
            if (header.value().length() != 0) {
                return header.value();
            }
        }
        return parameter.getName();
    }

    public static String getNullableParameterName(Parameter parameter, Set<Class<?>> types) {
        String optionality = parameter.isAnnotationPresent(OptionalField.class) ? "?" : "";
        return getParameterName(parameter) + optionality;
    }
}
