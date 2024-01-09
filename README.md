# Task Management System Application on Java
## Описание:
Приложение для управления задачами в рамках тестового задания, ТЗ прилагается. Программа позволяет регестрировать пользователя, создавать задачи и управлять ими.
## Использованные технологи:
Приложение написанно на Java 11 с использованием Spring Boot. В качестве базы данных выступает PostgreSQL. Взаимодействие с базой данных выполняется с Spring Data JPA. Доступ аутентифицируется с помощью JWT токена через Spring Security. Для создания запросов с гибкой выборкой данных (опционально) в приложени поддерживается язык REST запросов RSQL с помощью библиотеки RSQL-Parser. Приложение покрыто unit тестами с применением фремворка Junit 5. API приложения описан с помощью Open API и настроен Swagger UI. В качестве сборщика приложения применяется Maven. Для локального запуска программы настроен Docker Compose.
## Инструкция по запуску программы:
Проект может быть загружен для локального запуска с помощбю команд: git init в выбранном каталоге и git clone https://github.com/Yreyaaa/TaskManagementSystem.git
Конфигурация приложения нахоится в файле TaskManagementSystem/src/main/resources/application.properties и может быть изменена перед запуском. *Значения базовых параметров:
spring.datasource.url=jdbc:postgresql://localhost:5433/postgres
spring.datasource.username=postgres
spring.datasource.password=postgres
server.port=8091
jwt_secret=YREYAAA
### Вариант 1. Запуск с помощбью Docker Compose:
Для запуска проекта с помощью Docker Compose достаточно загрузть  только каталог docker. Откройте каталог docker через терминал и выполните следующие команды, при запущенном Docker engine на компьютере:
docker-compose -f docker-compose.yml up --no-start для создания образов
docker-compose -f docker-compose.yml start для запуска контейнеров
Инициализация схемы базы данных PostgreSQL происходит автоматически с помощью скрипта init.sql
### Вариант 2. Запуск из среды разработки на примере IntelliJ IDEA:
Для запуска проекта из среды разработки, необходимо прадварительно настроить базу данных PostgreSQL (также можно воспользоваться Docker контейнером с базой данных из первого варианта). Создайте базу данных, примените к ней SQL код из файла CreatingTablesInDatabase.sql для инициализации схемы базы данных. Убедитесь что параметры базы данных, такие как: имя базы данных, логин, пароль, адрес порта - соответствуют файлу application.properties, отредактируйте его при необходимости. Запустите приложение выполнив команду run на основном классе - TaskManagementSystemApplication.java
### Вариант 2. Запуск JAR файла:
Для запуска JAR файла проекта, выполните вышеописанные действия  по насройке базы данных (также можно воспользоваться Docker контейнером с базой данных из первого варианта). JAR файл, находящийся в каталоге TaskManagementSystem/target/TaskManagementSystem-0.0.1-SNAPSHOT.jar собран с значениями* параметров из файла application.properties, если вы изменили эти параметры для соответствия вашей базе данных, то соберите JAR файл заново с помощью команды  mvn clean package spring-boot:repackage в каталоге проекта. Запустите TaskManagementSystem-0.0.1-SNAPSHOT.jar с помощью команды  java -jar TaskManagementSystem-0.0.1-SNAPSHOT.jar в каталоге с файлом.

## Работа с программой:
После запуска Task Management System Application приложения к нему можно обращаться по URL адресу http://localhost:8091 (если параметр server.port=8091 из application.properties не был изменен).
Запросы можно выполнять с помощбю Postman, также, поскольку в приложении насроен Swwagger ui, можно удобно ознакомится с его функционалом с помощбю страници визуализации API, находящейся по URL адресу http://localhost:8091/swagger-ui.html. На странице представлены все пользовательские эндпоинты с пояснительными коментариями и описанием формата взодных данных для запросов. Запросы можно выполнять c помощью кнопки Try it out. Запросы помеченные замком, требуют авторизации, производимой нажатием на этот замок. Рекомендуется начать с запроса регистрации - http://localhost:8091/swagger-ui/index.html#/client-controller/registration для получения JWT токена, который понадобится для всех операций требующих авторизации, время действия токена - 120 минут, новый можно получить для зарегесрированного пользователя, выполнив запрос /login.

**Все эндпоинты приложения:**
`POST http://localhost:8091/login`
`GET http://localhost:8091/registration`
`POST http://localhost:8091/registration`
`DELETE http://localhost:8091/{clientName}/delete`
`GET http://localhost:8091/tasks/getAllTasks`
`POST http://localhost:8091/tasks/createTask`
`GET http://localhost:8091/tasks/{title}`
`GET http://localhost:8091/tasks/getTaskByAuthor/{authorName}`
`GET http://localhost:8091/tasks/getTaskByExecutor/{executorName}`
`PATCH http://localhost:8091/tasks/{title}/updateStatus/{newStatus}`
`PATCH http://localhost:8091/tasks/{title}/update`
`PATCH http://localhost:8091/tasks/{title}/addComment`
`DELETE http://localhost:8091/tasks/{title}/delete`
