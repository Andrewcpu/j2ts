package net.andrewcpu.j2ts.utils;

import net.andrewcpu.j2ts.annotations.NullableField;
import net.andrewcpu.j2ts.annotations.OptionalField;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Parameter;
import java.util.Set;

import static net.andrewcpu.j2ts.JTSTransformer.MODEL_IMPORT_NAME;
import static net.andrewcpu.j2ts.JTSTransformer.PREFIX;


public class ParameterUtils {
    public static String getTypeString(Parameter parameter, Set<Class<?>> types) {
        String typeString = getTypeString(parameter.getType(), types);
        if(parameter.isAnnotationPresent(NullableField.class)) {
            return typeString + " | null" ;
        }
        return typeString;
    }
    public static String getTypeString(Class<?> paramType, Set<Class<?>> types) {
        String value;
        if(paramType == null) value =  "void";
        else if(types.contains(paramType)) {
            value = MODEL_IMPORT_NAME + "." + PREFIX + paramType.getSimpleName();
        }
        else if(paramType == int.class || paramType == double.class || paramType == float.class || paramType == byte.class || paramType == short.class || paramType == long.class) {
            value = "number";
        }
        else if(paramType == Integer.class || paramType == Double.class || paramType == Float.class || paramType == Byte.class || paramType == Short.class || paramType == Long.class) {
            value = "number";
        }
        else if(paramType == String.class) {
            value = "string";
        }
        else if(paramType == Boolean.class || paramType == boolean.class) {
            value =  "boolean";
        }
        else if(paramType == void.class) {
            value = "void";
        }
        else{
            value = "any";
        }
        return value;
    }
    public static String getParameterName(Parameter parameter) {
        if(parameter.isAnnotationPresent(RequestParam.class)) {
            RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
            if(requestParam.value().length() != 0) {
                return requestParam.value();
            }
        }
        if(parameter.isAnnotationPresent(PathVariable.class)){
            PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
            if(pathVariable.value().length() != 0) {
                return pathVariable.value();
            }
        }
        if(parameter.isAnnotationPresent(RequestHeader.class)) {
            RequestHeader header = parameter.getAnnotation(RequestHeader.class);
            if(header.value().length() != 0) {
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
