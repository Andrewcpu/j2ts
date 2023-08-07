package net.andrewcpu.j2ts;

import cz.habarta.typescript.generator.*;
import net.andrewcpu.j2ts.annotations.Model;
import net.andrewcpu.j2ts.annotations.NullableField;
import net.andrewcpu.j2ts.annotations.OptionalField;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static net.andrewcpu.j2ts.EndpointTransformer.getFunctionComment;
import static net.andrewcpu.j2ts.EndpointTransformer.getFunctionParameterString;
import static net.andrewcpu.j2ts.JTSTransformer.*;
import static net.andrewcpu.j2ts.utils.ParameterUtils.getParameterName;
import static net.andrewcpu.j2ts.utils.ParameterUtils.getTypeString;
import static net.andrewcpu.j2ts.utils.StringUtils.getSpacing;
import static net.andrewcpu.j2ts.utils.StringUtils.toKebabCase;


public class APIBuilder {
	public static String buildAPI(List<Endpoint> endpoints, Set<Class<?>> classes) {
		String functions = endpoints.stream().map(i -> convertEndpointToFunction(i, classes)).collect(Collectors.joining("\n\n"));

		String output = "/* tslint:disable */\n" +
				"/* eslint-disable */\nimport * as " + MODEL_IMPORT_NAME + " from '../types'\nconst request = require('axios');\n\n";
		output += "\n" + functions;
		return output;
	}

	public static String getFunctionHeaderDeclaration(Endpoint endpoint, Set<Class<?>> types) {
		String format = "export function %s(%s): Promise<%s> {";
		String name = endpoint.getMethodName();
		String params = getFunctionParameterString(endpoint, types);
		String returnType = getTypeString(endpoint.getReturnType(), types);
		return format.formatted(name, params, returnType);
	}

	public static String convertEndpointToFunction(Endpoint endpoint, Set<Class<?>> types) {
		StringBuilder stringBuilder = new StringBuilder();
		String header = getFunctionHeaderDeclaration(endpoint, types);
		stringBuilder.append(getFunctionComment(endpoint, types));
		stringBuilder.append("\n");
		stringBuilder.append(header);
		stringBuilder.append("\n" + getSpacing(2));
		String quoteChar = "\"";
		if (endpoint.getPathParameters().size() != 0) {
			quoteChar = "`";
		}
		String req = "return request.%s(" + quoteChar + "%s" + quoteChar + "%s);";
		List<String> params = new ArrayList<>();
		if (endpoint.getBody() != null) {
			params.add(getParameterName(endpoint.getBody()));
		}
		if (endpoint.getQueryParameters().size() != 0) {
			params.add("{\n" + getSpacing(3) + "params: {\n" + getSpacing(4) + endpoint.getQueryParameters().stream().map(m -> getParameterName(m)).collect(Collectors.joining(", ")) + "\n" + getSpacing(3) + "}\n" + getSpacing(2) + "}");
		}
		String path = endpoint.getPath();
		if (endpoint.getPathParameters().size() != 0) {
			for (Parameter parameter : endpoint.getPathParameters()) {
				String name = getParameterName(parameter);
				path = path.replaceAll("\\{" + name + "}", "\\$\\{" + name + "}");
			}
		}
		String formattedReq = req.formatted(endpoint.getRequestType(), path, params.size() != 0 ? ", " + String.join(", ", params) : "");
		stringBuilder.append(formattedReq);
		stringBuilder.append("\n}");
		return stringBuilder.toString();
	}

	public static void generateAPI(Reflections reflections, ClassLoader classLoader, String packageRoot, File directory) throws IOException {
		File outputDirectory = new File(directory, "ts-api");
		outputDirectory.mkdirs();
		Set<Class<?>> packageClasses = reflections.getAll(Scanners.TypesAnnotated).stream()
				.filter(n -> n.startsWith(packageRoot))
				.map(n -> {
					try {
						return reflections.forClass(n, classLoader);
//                return classLoader.loadClass(n);
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
				})
				.filter(Objects::nonNull).collect(Collectors.toSet());
		Set<Class<?>> classes = packageClasses.stream()
				.filter(n -> n.isAnnotationPresent(Model.class))
				.collect(Collectors.toSet());
		List<Type> types = new ArrayList<>(classes);

		Settings settings = setupSettings();
		generateTypescript(settings, types, outputDirectory);


		Set<Class<?>> restClasses = packageClasses.stream()
				.filter(n -> n.isAnnotationPresent(RestController.class))
				.collect(Collectors.toSet());

		List<String> fileNames = new ArrayList<>();

		for (Class<?> clazz : restClasses) {
			System.out.println(clazz.getSimpleName() + "--!");
			List<Endpoint> endpoints = scanForApiAnnotations(clazz);
			String api = buildAPI(endpoints, classes);
			String fileName = toKebabCase(clazz.getSimpleName()) + "-api.ts";
			File file = new File(new File(outputDirectory, "controllers"), fileName);
			file.mkdirs();
			writeApiToFile(file, api);
			fileNames.add(fileName);
		}

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("/* tslint:disable */\n" +
				"/* eslint-disable */");
		stringBuilder.append("\n");
		for (String fileName : fileNames) {
			stringBuilder.append("export * from './controllers/" + fileName.substring(0, fileName.indexOf(".")) + "'");
			stringBuilder.append("\n");
		}

		writeApiToFile(new File(outputDirectory, "api.ts"), stringBuilder.toString());
		//export * from './controllers/fileName
	}

	public static Settings setupSettings() {
		Settings settings = new Settings();
		settings.outputKind = TypeScriptOutputKind.module;
		settings.jackson2ModuleDiscovery = true;
		settings.nullableAnnotations = List.of(NullableField.class);
		settings.nullabilityDefinition = NullabilityDefinition.nullAndUndefinedInlineUnion;
		settings.optionalAnnotations = List.of(OptionalField.class);
		settings.outputFileType = TypeScriptFileType.declarationFile;
		settings.jsonLibrary = JsonLibrary.jackson2;
		settings.addTypeNamePrefix = PREFIX;
		return settings;
	}

	public static void generateTypescript(Settings settings, List<Type> types, File outputDirectory) {
		new TypeScriptGenerator(settings).generateTypeScript(
				Input.from(types.toArray(Type[]::new)),
				Output.to(new File(outputDirectory, "types.d.ts"))
		);
	}
}
