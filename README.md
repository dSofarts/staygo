# Staygo -  Cервис для бронирования отелей

Выполнил Дронов Егор

## Описание проекта

Проект состоит из 4 модулей:

* eureka-service - сервис регистрации и обнаружения других сервисов (Eureka Server).
* api-gateway-service - шлюз API, который маршрутизирует запросы к соответствующим сервисам.
* hotel-service - сервис для управления информацией об отелях.
* booking-service - сервис для управления бронированиями отелей.

## Технологии

Проект разработан с использованием следующих технологий:

* Java 21
* Spring Boot
* Spring Cloud
* Eureka Security + JWT
* Spring Cloud Gateway
* Spring Data JPA
* H2 Database (в памяти)
* Gradle

## Запуск проекта

1. Клонируйте репозиторий:

   ```bash
   git clone
    ```
   
2. Перейдите в директорию проекта:
   ```bash
   cd staygo
   ```
   
3. Запустите каждый из сервисов в следующем порядке:
    ```
   # 1. eureka-service
   cd eureka-service
   ../gradlew bootRun
   
   # 2. hotel-service
   cd hotel-service
   ../gradlew bootRun
   
   # 3. booking-service
   cd booking-service
   ../gradlew bootRun
   
   # 4. api-gateway-service
   cd api-gateway-service
   ../gradlew bootRun
   ```

4. Доступ к сервисам:

    - Eureka Server: `http://localhost:8761`
    - API Gateway: `http://localhost:8080`
    - Hotel Service: `http://localhost:8081/hotels`
    - Booking Service: `http://localhost:8082/bookings`

5. Тестирование API:

   Вы можете использовать Postman или curl для тестирования API сервисов через API Gateway.
6. Документация API:

   Swagger UI доступен по следующим адресам:

   - Hotel Service: `http://localhost:8082/swagger-ui.html`
   - Booking Service: `http://localhost:8083/swagger-ui.html`
