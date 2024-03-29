package net.andrewcpu.j2ts;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.List;

public class Endpoint {
    private String path;
    private String requestType;
    private Parameter body;
    private String methodName;
    private List<Parameter> queryParameters;
    private List<Parameter> pathParameters;
    private List<Parameter> headerParameters;
    private String endpointDescription;
    private Class<?> returnType;
    private Type genericReturnType;
    private final String returnDescription;
    private List<String> storeKeys;
    private boolean isMultipart;
    private List<Parameter> multipartParameters;

    public Endpoint(String path,
                    String endpointDescription,
                    String requestType,
                    Parameter body,
                    List<Parameter> queryParameters,
                    List<Parameter> pathParameters,
                    List<Parameter> headerVariables,
                    String methodName,
                    Class<?> returnType,
                    Type genericReturnType,
                    String returnDescription,
                    List<String> storeKeys,
                    boolean isMultipart,
                    List<Parameter> multipartParameters) {
        this.path = path;
        this.endpointDescription = endpointDescription;
        this.genericReturnType = genericReturnType;
        this.body = body;
        this.queryParameters = queryParameters;
        this.pathParameters = pathParameters;
        this.headerParameters = headerVariables;
        this.methodName = methodName;
        this.requestType = requestType;
        this.returnType = returnType;
        this.returnDescription = returnDescription;
        this.storeKeys = storeKeys;
        this.isMultipart = isMultipart;
        this.multipartParameters = multipartParameters;
    }

    public List<Parameter> getMultipartParameters() {
        return multipartParameters;
    }

    public void setMultipartParameters(List<Parameter> multipartParameters) {
        this.multipartParameters = multipartParameters;
    }

    public Type getGenericReturnType() {
        return genericReturnType;
    }

    public String getReturnDescription() {
        return returnDescription;
    }

    public String getEndpointDescription() {
        return endpointDescription;
    }

    public void setEndpointDescription(String endpointDescription) {
        this.endpointDescription = endpointDescription;
    }

    public List<Parameter> getHeaderParameters() {
        return headerParameters;
    }

    public void setHeaderParameters(List<Parameter> headerParameters) {
        this.headerParameters = headerParameters;
    }

    public List<Parameter> getPathParameters() {
        return pathParameters;
    }

    public void setPathParameters(List<Parameter> pathParameters) {
        this.pathParameters = pathParameters;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    public List<Parameter> getQueryParameters() {
        return queryParameters;
    }

    public void setQueryParameters(List<Parameter> queryParameters) {
        this.queryParameters = queryParameters;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public Parameter getBody() {
        return body;
    }

    public void setBody(Parameter body) {
        this.body = body;
    }

    public void setGenericReturnType(Type genericReturnType) {
        this.genericReturnType = genericReturnType;
    }

    public List<String> getStoreKeys() {
        return storeKeys;
    }

    public void setStoreKeys(List<String> storeKeys) {
        this.storeKeys = storeKeys;
    }

    public boolean isMultipart() {
        return isMultipart;
    }

    public void setMultipart(boolean multipart) {
        isMultipart = multipart;
    }
}
