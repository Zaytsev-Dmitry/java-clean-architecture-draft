package com.clean.architecture.openapi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Простой парсер OpenAPI YAML-файла, который извлекает информацию об endpoint'ах REST API.
 * <p>
 * Парсер предназначен для быстрого получения списка endpoint'ов, их HTTP-методов, operationId,
 * типа тела запроса (requestBody) и типа возвращаемого значения (Return Type) из секции responses (200, 201, 204).
 * Не использует сторонние библиотеки YAML и не поддерживает все нюансы спецификации, но подходит для простых задач анализа.
 * </p>
 */
public class OpenApiParser {
    
    /**
     * Парсит OpenAPI YAML-файл и возвращает список endpoint'ов.
     *
     * @param openApiFilePath путь к YAML-файлу OpenAPI спецификации
     * @return список endpoint'ов (метод, путь, operationId, Return Type, Request Body, параметры)
     */
    public List<EndpointInfo> parseEndpoints(String openApiFilePath) {
        try {
            System.out.println("Парсинг OpenAPI файла: " + openApiFilePath);
            String content = Files.readString(Path.of(openApiFilePath));
            return parseYamlContent(content);
        } catch (IOException e) {
            System.err.println("Ошибка чтения OpenAPI файла: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    
    /**
     * Парсит содержимое YAML и извлекает endpoint'ы.
     *
     * @param content содержимое YAML-файла
     * @return список endpoint'ов
     */
    private List<EndpointInfo> parseYamlContent(String content) {
        List<EndpointInfo> endpoints = new ArrayList<>();
        String[] lines = content.split("\n");
        
        String currentPath = null;
        EndpointInfo currentEndpoint = null;
        
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            
            // Ищем пути (начинаются с /)
            if (line.startsWith("/") && line.endsWith(":")) {
                currentPath = line.substring(0, line.length() - 1);
                continue;
            }
            
            // Ищем HTTP методы
            if (currentPath != null && isHttpMethod(line)) {
                // Сохраняем предыдущий endpoint если есть
                if (currentEndpoint != null) {
                    endpoints.add(currentEndpoint);
                }
                
                String method = line.substring(0, line.length() - 1);
                currentEndpoint = new EndpointInfo();
                currentEndpoint.setPath(currentPath);
                currentEndpoint.setMethod(method);
                continue;
            }
            
            // Парсим operationId
            if (currentEndpoint != null && line.startsWith("operationId:")) {
                String operationId = extractValue(line);
                currentEndpoint.setOperationId(operationId);
            }
            
            // Парсим параметры
            if (currentEndpoint != null && line.startsWith("- name:")) {
                parseParameter(lines, i, currentEndpoint);
            }
            
            // Парсим requestBody
            if (currentEndpoint != null && line.startsWith("requestBody:")) {
                parseRequestBody(lines, i, currentEndpoint);
            }
            
            // Парсим responses
            if (currentEndpoint != null && line.startsWith("responses:")) {
                parseResponses(lines, i, currentEndpoint);
            }
            
            // Если встретили новый путь, сохраняем текущий endpoint
            if (line.startsWith("/") && line.endsWith(":") && currentEndpoint != null) {
                endpoints.add(currentEndpoint);
                currentPath = line.substring(0, line.length() - 1);
                currentEndpoint = null;
            }
        }
        
        // Добавляем последний endpoint
        if (currentEndpoint != null) {
            endpoints.add(currentEndpoint);
        }
        
        System.out.println("Всего найдено endpoint'ов: " + endpoints.size());
        return endpoints;
    }
    
    /**
     * Проверяет, является ли строка HTTP-методом (get, post, put, delete, patch).
     *
     * @param line строка для проверки
     * @return true, если строка соответствует HTTP-методу
     */
    private boolean isHttpMethod(String line) {
        return line.equals("get:") || line.equals("post:") || 
               line.equals("put:") || line.equals("delete:") || 
               line.equals("patch:");
    }
    
    /**
     * Извлекает значение из строки вида "key: value".
     *
     * @param line строка с ключом и значением
     * @return значение после двоеточия
     */
    private String extractValue(String line) {
        int colonIndex = line.indexOf(':');
        if (colonIndex != -1 && colonIndex < line.length() - 1) {
            return line.substring(colonIndex + 1).trim();
        }
        return "";
    }
    
    /**
     * Парсит параметры endpoint'а (только имя и тип).
     *
     * @param lines      все строки YAML-файла
     * @param startIndex индекс строки с параметром
     * @param endpoint   endpoint, в который добавляется параметр
     */
    private void parseParameter(String[] lines, int startIndex, EndpointInfo endpoint) {
        ParameterInfo param = new ParameterInfo();
        param.setName(extractValue(lines[startIndex]));
        
        for (int i = startIndex + 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.startsWith("type:")) {
                param.setType(extractValue(line));
                break;
            } else if (line.startsWith("-") && !line.startsWith("- name:")) {
                break;
            }
        }
        
        endpoint.getParameters().add(param);
    }
    
    /**
     * Парсит requestBody и извлекает тип схемы, если есть $ref.
     *
     * @param lines      все строки YAML-файла
     * @param startIndex индекс строки с requestBody
     * @param endpoint   endpoint, в который добавляется тип тела запроса
     */
    private void parseRequestBody(String[] lines, int startIndex, EndpointInfo endpoint) {
        for (int i = startIndex + 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.startsWith("$ref:")) {
                String ref = extractValue(line);
                if (ref.contains("/")) {
                    String schemaName = ref.substring(ref.lastIndexOf("/") + 1);
                    endpoint.setRequestBodyType(schemaName);
                }
                break;
            } else if (line.startsWith("responses:") || line.startsWith("/")) {
                break;
            }
        }
    }
    
    /**
     * Парсит responses и извлекает Return Type из первого успешного ответа (200, 201, 204).
     *
     * @param lines      все строки YAML-файла
     * @param startIndex индекс строки с responses
     * @param endpoint   endpoint, в который добавляется Return Type
     */
    private void parseResponses(String[] lines, int startIndex, EndpointInfo endpoint) {
        for (int i = startIndex + 1; i < lines.length; i++) {
            String line = lines[i].trim();
            // Если встретили новый endpoint, выходим
            if (line.startsWith("/")) {
                break;
            }
            // Только если Return Type еще не найден
            if (endpoint.getReturnType() != null) {
                break;
            }
            // Проверяем успешные коды ответов без регулярок
            if (line.equals("200:") || line.equals("201:") || line.equals("204:") ||
                line.equals("'200':") || line.equals("'201':") || line.equals("'204':")) {
                // Ищем $ref в этом response
                for (int j = i + 1; j < lines.length; j++) {
                    String responseLine = lines[j].trim();
                    if (responseLine.startsWith("$ref:")) {
                        String ref = extractValue(responseLine);
                        if (ref.contains("/")) {
                            String schemaName = ref.substring(ref.lastIndexOf("/") + 1);
                            endpoint.setReturnType(schemaName);
                        }
                        return;
                    } else if (responseLine.startsWith("/")) {
                        break;
                    }
                }
            }
        }
    }
    
    /**
     * Выводит информацию о всех найденных endpoint'ах.
     *
     * @param endpoints список endpoint'ов
     */
    public void printEndpoints(List<EndpointInfo> endpoints) {
        System.out.println("\n=== Найденные endpoint'ы ===");
        for (EndpointInfo endpoint : endpoints) {
            System.out.println(String.format("%s %s", endpoint.getMethod(), endpoint.getPath()));
            System.out.println("  OperationId: " + endpoint.getOperationId());
            System.out.println("  Return Type: " + endpoint.getReturnType());
            System.out.println("  Request Body: " + endpoint.getRequestBodyType());
            System.out.println("  Parameters: " + endpoint.getParameters().size());
            for (ParameterInfo param : endpoint.getParameters()) {
                System.out.println("    - " + param.getName() + " (" + param.getType() + ")");
            }
            System.out.println();
        }
    }
    
    /**
     * Информация об одном endpoint'е OpenAPI.
     */
    public static class EndpointInfo {
        private String path;
        private String method;
        private String operationId;
        private String returnType;
        private String requestBodyType;
        private List<ParameterInfo> parameters = new ArrayList<>();
        
        /**
         * @return путь endpoint'а (например, /users/{id})
         */
        public String getPath() { return path; }
        public void setPath(String path) { this.path = path; }
        
        /**
         * @return HTTP-метод (get, post, ...)
         */
        public String getMethod() { return method; }
        public void setMethod(String method) { this.method = method; }
        
        /**
         * @return operationId из OpenAPI
         */
        public String getOperationId() { return operationId; }
        public void setOperationId(String operationId) { this.operationId = operationId; }
        
        /**
         * @return тип возвращаемого значения (Return Type) из responses (например, UserDto)
         */
        public String getReturnType() { return returnType; }
        public void setReturnType(String returnType) { this.returnType = returnType; }
        
        /**
         * @return тип тела запроса (Request Body), если есть
         */
        public String getRequestBodyType() { return requestBodyType; }
        public void setRequestBodyType(String requestBodyType) { this.requestBodyType = requestBodyType; }
        
        /**
         * @return список параметров endpoint'а
         */
        public List<ParameterInfo> getParameters() { return parameters; }
        public void setParameters(List<ParameterInfo> parameters) { this.parameters = parameters; }
    }
    
    /**
     * Информация о параметре endpoint'а (только имя и тип).
     */
    public static class ParameterInfo {
        private String name;
        private String type;
        
        /**
         * @return имя параметра
         */
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        /**
         * @return тип параметра (например, string, integer)
         */
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }
} 