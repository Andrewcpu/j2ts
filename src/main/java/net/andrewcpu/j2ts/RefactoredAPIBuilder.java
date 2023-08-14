package net.andrewcpu.j2ts;

import net.andrewcpu.j2ts.annotations.StoredKey;
import net.andrewcpu.j2ts.utils.ParameterUtils;

import javax.imageio.ImageTypeSpecifier;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static net.andrewcpu.j2ts.EndpointTransformer.getFunctionComment;
import static net.andrewcpu.j2ts.EndpointTransformer.getFunctionParameterString;
import static net.andrewcpu.j2ts.JTSTransformer.PROXY_URL_PREFIX;
import static net.andrewcpu.j2ts.utils.ParameterUtils.getParameterName;
import static net.andrewcpu.j2ts.utils.ParameterUtils.getTypeString;
import static net.andrewcpu.j2ts.utils.StringUtils.getSpacing;

public class RefactoredAPIBuilder {
	public static String getFunctionHeaderDeclaration(Endpoint endpoint, Set<Class<?>> types) {
		String format = "export function %s(%s): Promise<%s> {";
		String name = endpoint.getMethodName();
		String params = getFunctionParameterString(endpoint, types);
		String returnType = getTypeString(endpoint.getReturnType(), endpoint.getGenericReturnType(), types);
		return format.formatted(name, params, returnType);
	}

	public static String convertEndpointToFunction(Endpoint endpoint, Set<Class<?>> types) {
		String returnType = getTypeString(endpoint.getReturnType(), endpoint.getGenericReturnType(), types);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(getFunctionComment(endpoint, types))
				.append("\n")
				.append(getFunctionHeaderDeclaration(endpoint, types))
				.append("\n")
				.append(getSpacing(2));

		String req = buildRequest(endpoint, returnType);

		stringBuilder.append(req);
		stringBuilder.append("\n}");
		return stringBuilder.toString();
	}

	private static String buildRequest(Endpoint endpoint, String returnType) {
		StringBuilder storeLogic = createLocalStorageLogic(endpoint, returnType);
		String multipartLogic = "";
		if (endpoint.isMultipart()) {
			StringBuilder formDataLogic = new StringBuilder();
			formDataLogic.append("const formData = new FormData();\n");
			endpoint.getMultipartParameters().forEach(parameter -> {
				String paramName = getParameterName(parameter);
				formDataLogic.append(getSpacing(2)).append("formData.append('").append(paramName).append("', ").append(paramName).append(");\n");
			});
			multipartLogic = formDataLogic.toString() + "\n" + getSpacing(2);
		}

		String path = formatEndpointPath(endpoint);
		String reqParams = generateRequestParameters(endpoint);
		String quoteChar = "\"";
		if (endpoint.getPathParameters().size() != 0) {
			quoteChar = "`";
		}
		String req = "return request.%s(" + quoteChar + path + quoteChar + "%s)"
				+ ".then((result: any) => result.data as " + returnType + ")";
		if (storeLogic.length() > 0) {
			req += storeLogic.toString() + ";";
		} else {
			req += ";";
		}
		return multipartLogic + req.formatted(endpoint.getRequestType(), reqParams.trim().length() != 0 ? ", " + reqParams : "", returnType);
	}

	private static StringBuilder createLocalStorageLogic(Endpoint endpoint, String returnType) {
		StringBuilder storeLogic = new StringBuilder();
		List<String> storeKeys = endpoint.getStoreKeys();
		if (storeKeys.size() > 0) {
			storeLogic.append("\n").append(getSpacing(3)).append(".then((result: ").append(returnType).append(") => {\n");
			storeKeys.forEach(key -> storeLogic.append(getSpacing(4)).append("localStorage.setItem(\"").append(key).append("\", result.").append(key).append(" ?? emptyString);\n"));
			storeLogic.append(getSpacing(4)).append("return result;\n").append(getSpacing(3)).append("})");
		}
		return storeLogic;
	}

	private static String formatEndpointPath(Endpoint endpoint) {
		String path = endpoint.getPath();
		if (endpoint.getPathParameters().size() != 0) {
			for (Parameter parameter : endpoint.getPathParameters()) {
				String name = getParameterName(parameter);
				path = path.replaceAll("\\{" + name + "}", "\\$\\{" + name + "}");
			}
		}

		String endPointPath = "/";
		if (!PROXY_URL_PREFIX.isEmpty()) {
			endPointPath += PROXY_URL_PREFIX.trim();
			if (endPointPath.endsWith("/")) {
				if (path.startsWith("/")) {
					endPointPath = endPointPath.substring(0, endPointPath.length() - 1);
				}
			} else {
				if (!path.startsWith("/")) {
					endPointPath += "/";
				}
			}
			endPointPath += path;
		} else {
			endPointPath = path;
		}

		return endPointPath;
	}


	private static String generateRequestParameters(Endpoint endpoint) {
		StringBuilder reqParams = new StringBuilder("{\n" + getSpacing(3));
		boolean hasQueryParameters = endpoint.getQueryParameters().size() != 0;
		boolean hasHeaderParameters = endpoint.getHeaderParameters().size() != 0;

		if (endpoint.isMultipart()) {
			reqParams.append("data: formData");
		}

		if(endpoint.getBody() != null) {
			reqParams.insert(0, getParameterName(endpoint.getBody()) + ", ");
		}

		if (hasQueryParameters && !endpoint.isMultipart()) {
			if (endpoint.isMultipart()) {
				reqParams.append(",\n").append(getSpacing(3));
			}
			reqParams.append("params: {\n")
					.append(getSpacing(4))
					.append(endpoint.getQueryParameters().stream().map(ParameterUtils::getParameterName).collect(Collectors.joining(", ")))
					.append("\n")
					.append(getSpacing(3))
					.append("}");
		}

		if (hasHeaderParameters) {
			if (hasQueryParameters) {
				reqParams.append(",\n").append(getSpacing(3));
			}
			reqParams.append("headers: {\n")
					.append(getSpacing(4))
					.append(endpoint.getHeaderParameters().stream().map(parameter -> {
						StoredKey storedKeyAnnotation = parameter.getAnnotation(StoredKey.class);
						if (storedKeyAnnotation != null) {
							String keyName = storedKeyAnnotation.value().isEmpty() ? getParameterName(parameter) : storedKeyAnnotation.value();
							return keyName + ": localStorage.getItem(\"" + keyName + "\")";
						} else {
							return getParameterName(parameter);
						}
					}).collect(Collectors.joining(", ")))
					.append("\n")
					.append(getSpacing(3))
					.append("}");
		}

		reqParams.append("\n").append(getSpacing(2)).append("}");
		return reqParams.toString();
	}

}
