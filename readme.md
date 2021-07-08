[![Build Status](https://travis-ci.com/AndrewAlyonkin/alenkinDrive.svg?branch=master)](https://travis-ci.com/AndrewAlyonkin/alenkinDrive)
# Read Me First
Технологии: Java, MySQL, Spring (IoC, Data, Security), AWS SDK,
MySQL, Travis, Docker, JUnit, Mockito, Maven.

Приложение представляет собой REST API, которое взаимодействует с файловым хранилищем
AWS S3 и предоставляет возможность получать доступ к файлам и истории загрузок.  
Сущности:  
User  
Event (User user, File file)  
File (id, location, ...)  
User -> … List<Events> events ...

Служебная информация приложения хранится в AWS RDS.

Тестами покрыты слои сервиса и контроллера. Слой сервиса тестируется с помощью Mockito,
слой контроллера тестируется с помощью Spring MockMVC.

Доступ к REST API предоставляется по JWT - токену.

## EndPoins для использования REST API приложения:   
## Пользователи  
>GET v1/users/ - получить информацию о всех пользователях приложения  
>GET v1/users/{userId} - получить подробную информацию о пользователе  
>POST v1/users/ - добавить нового пользователя  
>PUT v1/users/ - обновить информацию о пользователе  
>DELETE v1/users/{userId} - далить пользователя с userId  

### Файлы
>GET v1/files/{userId} - получить все файлы для пользователя с userId  
>GET v1/files/{userId}/{fileId} - скачать файл с fileId пользователя с userId  
>PUT v1/files/{userId}/{fileId} - обновить файл с fileId для пользователя с userId  
>POST v1/files/{userId} - загрузить файл для пользователя с userId. Прикрепить к телу запроса фаил с ключем 'file'   
>DELETE v1/files/{userId}/{fileId} - удалить файл с fileId пользователя с userId  

### История загрузок
>GET v1/events/{userId}/{id} - получить информацию о загрузке с id пользователя с userId  
>GET v1/events/{userId} - получить информацию о загрузках пользователя с userId  
>PUT v1/events/{userId}/{id} - редактировать историю о загрузке с id для пользователя с userId  
>POST v1/events/{userId} - вручную добавить информацию о загрузке(по умолчанию история формируется автоматически)  
>DELETE v1/events/{userId}/{id} - очистить историю загрузки с id для пользователя с userId  

## Аутентификация и верификация в приложении
Приложение взаимодействует с S3 с помощью AWS SDK.  
Уровни доступа:  
 - ADMIN - полный доступ к приложению  
 - MODERATOR - добавление и удаление файлов  
 - USER - только чтение всех данных, кроме User  

|Role|username|password|  
|----|--------|--------|  
|ADMIN|admin |admin |
|MODERATOR|moderator |moderator |
|USER|user |user |

## Для авторизации в приложении отправить запрос
> POST /v1/auth/login  
> с парой name/password в формате {"name":"admin","password":"admin"}  
>  - в теле ответа придет JWT токен для доступа к приложению  
>  - полученный токен установить в качестве заголовка последующих запросов с ключем Authorization  
>  - токен будет действителен в течение часа
>  - при входе в приложение с другой парой имя пользователя/пароль JWT токен пересчитывается и его нужно обновить в заголовке запроса

## При запуске приложения локально при старте Java установить переменные окружения:
Приложение работает с AWS RDS  AWS S3:  
> Ключи доступа у Amazon ewb services S3  
> aws.access=  
> aws.secret=  
> aws.bucket=  

Также в application.properties откорректировать credentials подключения к AWS RDS, в которой будет храниться служебная информация проиложения
> spring.datasource.url=  
> spring.datasource.driver-class-name=
> spring.datasource.username=
> spring.datasource.password=