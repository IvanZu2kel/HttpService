# Сервис для сокращения ссылок.

Работу данного сервиса можно протестировать по ссылке: https://service-short-url.herokuapp.com/swagger-ui/index.html

Данный сервис был создан с использованием Spring Boot, Spring Security, Spring Data JPA , Spring Data REST. База данных MySQL. Миграции на FlyWay.

Документирование проекта осуществляется с использованием Swagger.

В проекте реализованы: 
- регистрация (при регистрации указывается email(в формате *@*.*) и пароль(не менее 4 символов)); 
- авторизация; 
- выход авторизированного пользователя;

Без авторизации возможен лишь переход по уже созданным сслыкам.

- создание короткой ссылки и возможность добавить ей время "жизни";
- возмонжость перехода по короткой ссылке;
- получение пользователем всех созданых им ссылок со статистикой переходов (кол-во анонимных переходов и кол-во переходов с определенных email);
- возможность изменить адресс в уже созданной ссылке;
- возможность удалить и восстановить ссылку.

<img width="1430" alt="Снимок экрана 2022-01-21 в 09 08 55" src="https://user-images.githubusercontent.com/85585191/150476757-91161b10-cbd9-405c-b622-8b0eb0c91be4.png">



