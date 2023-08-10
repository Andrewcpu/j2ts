package net.andrewcpu.j2ts;


import net.andrewcpu.j2ts.annotations.API;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import static net.andrewcpu.j2ts.EndpointTransformer.parseMethod;


public class JTSTransformer {

    public static String PREFIX = "I";
    public static String MODEL_IMPORT_NAME = "backend";

    public static void main(String[] args) throws IOException {
//        generateAPI("example.rest", "example.models");
    }


    public static void writeApiToFile(File file, String api) throws IOException {
        if(file.exists()) {
            file.delete();
        }
        Files.writeString(file.toPath(), api, StandardOpenOption.CREATE_NEW);
    }

    public static List<Endpoint> scanForApiAnnotations(Class<?> clazz) {
        List<Endpoint> endpoints = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(API.class)) {
                endpoints.add(parseMethod(method));
            }
        }
        return endpoints;
    }
}