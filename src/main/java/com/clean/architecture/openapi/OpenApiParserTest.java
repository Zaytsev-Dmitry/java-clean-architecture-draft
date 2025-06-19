package com.clean.architecture.openapi;

import java.util.List;

/**
 * Простой тест для парсера OpenAPI
 */
public class OpenApiParserTest {
    
    public static void main(String[] args) {
        System.out.println("=== Тест парсера OpenAPI ===");
        
        // Создаем парсер
        OpenApiParser parser = new OpenApiParser();
        
        // Путь к OpenAPI файлу
        String openApiFile = "src/main/resources/static/openapi/api-spec.yml";
        
        try {
            // Парсим endpoint'ы
            System.out.println("Парсинг файла: " + openApiFile);
            List<OpenApiParser.EndpointInfo> endpoints = parser.parseEndpoints(openApiFile);
            
            // Выводим результаты
            parser.printEndpoints(endpoints);
            
            // Дополнительная статистика
            System.out.println("\n=== Статистика ===");
            System.out.println("Всего endpoint'ов: " + endpoints.size());
            
            long getCount = endpoints.stream().filter(e -> "get".equals(e.getMethod())).count();
            long postCount = endpoints.stream().filter(e -> "post".equals(e.getMethod())).count();
            long putCount = endpoints.stream().filter(e -> "put".equals(e.getMethod())).count();
            long deleteCount = endpoints.stream().filter(e -> "delete".equals(e.getMethod())).count();
            long patchCount = endpoints.stream().filter(e -> "patch".equals(e.getMethod())).count();
            
            System.out.println("GET: " + getCount);
            System.out.println("POST: " + postCount);
            System.out.println("PUT: " + putCount);
            System.out.println("DELETE: " + deleteCount);
            System.out.println("PATCH: " + patchCount);
            
        } catch (Exception e) {
            System.err.println("Ошибка при парсинге: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 