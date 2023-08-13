package net.andrewcpu.j2ts;

import net.andrewcpu.j2ts.annotations.API;
import net.andrewcpu.j2ts.annotations.ParamDescription;
import net.andrewcpu.j2ts.annotations.StoredKey;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

import static net.andrewcpu.j2ts.utils.ParameterUtils.getNullableParameterName;
import static net.andrewcpu.j2ts.utils.ParameterUtils.getTypeString;

public class EndpointTransformer {


    public static Endpoint parseMethod(Method method) {
        Parameter[] parameters = method.getParameters();
        List<Parameter> pathVariables = Arrays.stream(parameters).filter(f -> f.isAnnotationPresent(PathVariable.class)).toList();
        List<Parameter> headerVariables = Arrays.stream(parameters).filter(f -> f.isAnnotationPresent(RequestHeader.class)).toList(); // Identify header parameters
        List<Parameter> queryVariables = Arrays.stream(parameters).filter(f -> f.isAnnotationPresent(RequestParam.class)).toList();
        List<Parameter> nonTagged = Arrays.stream(parameters).collect(Collectors.toList());

        nonTagged.removeAll(pathVariables);
        nonTagged.removeAll(queryVariables);
        nonTagged.removeAll(headerVariables);

        String methodName = method.getName();
        String requestType = getRequestType(method);
        Parameter body = null;
        if(nonTagged.size() != 0) {
            body = nonTagged.get(0);
        }
        String endpoint = getRequestPath(method);
        API apiAnnotation = method.getAnnotation(API.class);
        return new Endpoint(endpoint,
                apiAnnotation.description(),
                requestType,
                body,
                queryVariables,
                pathVariables,
                headerVariables,
                methodName,
                method.getReturnType(),
                method.getGenericReturnType(),
                apiAnnotation.returnValue());
    }

    public static String getRequestPath(Method method) {
        if (method.isAnnotationPresent(API.class)) {
            if (method.isAnnotationPresent(GetMapping.class)) {
                GetMapping getMapping = method.getAnnotation(GetMapping.class);
                return String.join(", ", getMapping.value());
            } else if (method.isAnnotationPresent(PostMapping.class)) {
                PostMapping postMapping = method.getAnnotation(PostMapping.class);
                return String.join(", ", postMapping.value());
            } else if (method.isAnnotationPresent(PutMapping.class)) {
                PutMapping putMapping = method.getAnnotation(PutMapping.class);
                return String.join(", ", putMapping.value());
            } else if (method.isAnnotationPresent(DeleteMapping.class)) {
                DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
                return String.join(", ", deleteMapping.value());
            } else if (method.isAnnotationPresent(PatchMapping.class)) {
                PatchMapping patchMapping = method.getAnnotation(PatchMapping.class);
                return String.join(", ", patchMapping.value());
            }
        }
        return null;
    }
    public static String getRequestType(Method method) {
        String requestType = "GET";
        if (method.isAnnotationPresent(GetMapping.class)) {
            requestType = "GET";
        } else if (method.isAnnotationPresent(PostMapping.class)) {
            requestType = "POST";
        } else if (method.isAnnotationPresent(PutMapping.class)) {
            requestType = "PUT";
        } else if (method.isAnnotationPresent(DeleteMapping.class)) {
            requestType = "DELETE";
        } else if (method.isAnnotationPresent(PatchMapping.class)) {
            requestType = "PATCH";
        }
        return requestType.toLowerCase();
    }

    public static String getFunctionComment(Endpoint endpoint, Set<Class<?>> types) {
        /**
         * Adds two numbers together.
         *
         * @param {number} num1 - The first number.
         * @param {number} num2 - The second number.
         * @returns {number} The sum of num1 and num2.
         *
         */

        //" * @param {number} num1 - The first number.\n"
        List<Parameter> allParams = new ArrayList<>();
        allParams.addAll(endpoint.getPathParameters());
        allParams.addAll(endpoint.getQueryParameters());
        allParams.addAll(endpoint.getHeaderParameters());
        allParams.add(endpoint.getBody());

        String paramComments = allParams.stream().filter(Objects::nonNull).map(param -> {
            String description = "";
            if(param.isAnnotationPresent(ParamDescription.class)){
                ParamDescription descriptParam = param.getAnnotation(ParamDescription.class);
                description = descriptParam.value();
            }
            String type = "";
            type = getTypeString(param, endpoint.getGenericReturnType(), types);
            return " * @param {%s} %s%s".formatted(type, getNullableParameterName(param, types), description.length() != 0 ? " - " + description : "");
        }).collect(Collectors.joining("\n"));

        String format = "/**\n" +
                "%s" +
                paramComments + "\n" +
                " * @returns {%s} %s\n" +
                " */";
        return format.formatted(endpoint.getEndpointDescription().length() != 0 ? " * " + endpoint.getEndpointDescription() + "\n * \n" : "", getTypeString(endpoint.getReturnType(), endpoint.getGenericReturnType(), types), endpoint.getReturnDescription() );
    }

    public static String getFunctionParameterString(Endpoint endpoint, Set<Class<?>> types) {
        List<String> p = new ArrayList<>();
        for(Parameter parameter : endpoint.getPathParameters()) {
            p.add(getNullableParameterName(parameter, types) + ": " + getTypeString(parameter, endpoint.getGenericReturnType(), types));
        }
        for(Parameter parameter : endpoint.getQueryParameters()) {
            p.add(getNullableParameterName(parameter, types) + ": " + getTypeString(parameter, endpoint.getGenericReturnType(), types));
        }
        for(Parameter parameter : endpoint.getHeaderParameters()) {
            if(!parameter.isAnnotationPresent(StoredKey.class)){
                p.add(getNullableParameterName(parameter, types) + ": " + getTypeString(parameter, endpoint.getGenericReturnType(), types));
            }
        }
        if(endpoint.getBody() != null) {
            p.add(getNullableParameterName(endpoint.getBody(), types) + ": " + getTypeString(endpoint.getBody(), endpoint.getGenericReturnType(), types));
        }
        return p.stream().collect(Collectors.joining(", "));
    }

}
