# Правила формирования OpenAPI файла для backend-генерации

## 1. Общие требования
- Используйте формат YAML (рекомендуется для читаемости).
- Версия OpenAPI: `openapi: 3.0.0` или выше.
- Все пути (`paths`) должны быть определены на верхнем уровне.
- Каждый endpoint должен содержать:
  - HTTP-метод (`get`, `post`, `put`, `patch`, `delete`)
  - `operationId` (уникальный для каждого endpoint'а)
  - Описание параметров (`parameters`) и тела запроса (`requestBody`), если применимо
  - Описание ответов (`responses`) с успешными кодами (200, 201, 204)

---

## 2. Структура endpoint'а

```yaml
paths:
  /resource/{id}:
    get:
      operationId: getResourceById
      parameters:
        - name: id
          in: path
          required: true
          schema:
            type: string
      responses:
        '200':
          description: OK
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ResourceResponse'
        '404':
          description: Not found
```

### Обязательные элементы:
- **operationId** — уникальный идентификатор операции (используется для генерации методов).
- **parameters** — список параметров (path, query, header). Каждый параметр должен содержать:
  - `name`
  - `in` (path, query, header)
  - `required` (true/false)
  - `schema.type` (например, string, integer)
- **responses** — обязательно должен быть хотя бы один успешный ответ:
  - `200`, `201` или `204` (желательно использовать только эти коды для успешных операций)
  - Для 200/201: должен быть описан `content` с `application/json` и схемой ответа через `$ref`
  - Для 204: тело ответа отсутствует

#### Пример для POST с requestBody:
```yaml
  /resource:
    post:
      operationId: createResource
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/ResourceCreateRequest'
      responses:
        '201':
          description: Created
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ResourceResponse'
```

---

## 3. Соглашения по именованию
- Пути должны быть в стиле REST: `/resource`, `/resource/{id}`, `/resource/{id}/subresource`
- `operationId` должен быть уникальным и отражать действие (`getUserById`, `createOrder`, `updateProfile`)
- Названия схем (`$ref`) должны быть в CamelCase и заканчиваться на `Request` или `Response` (например, `UserCreateRequest`, `UserResponse`)

---

## 4. Секции components/schemas
- Все используемые схемы должны быть определены в разделе `components/schemas`.
- Пример:
```yaml
components:
  schemas:
    ResourceResponse:
      type: object
      properties:
        id:
          type: string
        name:
          type: string
    ResourceCreateRequest:
      type: object
      properties:
        name:
          type: string
```

---

## 5. Ошибки и дополнительные ответы
- Для ошибок используйте стандартные коды (`400`, `401`, `403`, `404`, `500`) с описанием.
- Не обязательно указывать схему для ошибок, если она не используется в генерации.

---

## 6. Пример полного минимального файла
```yaml
openapi: 3.0.0
info:
  title: Example API
  version: 1.0.0
paths:
  /user/{id}:
    get:
      operationId: getUserById
      parameters:
        - name: id
          in: path
          required: true
          schema:
            type: string
      responses:
        '200':
          description: OK
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/UserResponse'
        '404':
          description: Not found
  /user:
    post:
      operationId: createUser
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/UserCreateRequest'
      responses:
        '201':
          description: Created
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/UserResponse'
components:
  schemas:
    UserResponse:
      type: object
      properties:
        id:
          type: string
        name:
          type: string
    UserCreateRequest:
      type: object
      properties:
        name:
          type: string
```

---

## Резюме
- Каждый endpoint должен иметь operationId, параметры, responses (200/201/204) с $ref на схему.
- Все схемы должны быть описаны в components/schemas.
- Не используйте лишние расширения и не усложняйте структуру.
- Соблюдайте единый стиль именования. 