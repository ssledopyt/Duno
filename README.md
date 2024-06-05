# Duno
Duno - это мобильное приложение для организации и поиска настольных игровых мероприятий.

Также с проектом связан репозиторий [duno_api], отвечающий за REST API, для корректной работы приложения.
## Демонстрация приложения
Пользователь может зарегистрироваться в приложении и создавать мероприятия, чтобы другие пользователи имели информацию о нём.

1. Регистрация и вход
![Регистрация](https://github.com/ssledopyt/Duno/assets/73230398/c8d20168-d6ad-4abc-bf91-1d41ef53045b)
![Вход](https://github.com/ssledopyt/Duno/assets/73230398/ee8d2b33-6840-48f0-9ee1-612068cd3157) 
2. Просмотр мероприятий в окне "Мероприятия"
![Screenshot_3](https://github.com/ssledopyt/Duno/assets/73230398/f7c59353-4439-4097-a474-bfc7222bf0ad)

3. Добавление мероприятий в "Избранное"
![Screenshot_4](https://github.com/ssledopyt/Duno/assets/73230398/1d6bebe4-114b-46a1-87c6-4d8dc2676635)

4. Создание мероприятия
![Screenshot_5](https://github.com/ssledopyt/Duno/assets/73230398/7c9cb3ff-a72b-44ce-b4fb-e41a0836241b)
![Screenshot_6](https://github.com/ssledopyt/Duno/assets/73230398/519135e7-9e7e-4166-9288-5aa55196af70)

5. Редактирование мероприятия
![Screenshot_7](https://github.com/ssledopyt/Duno/assets/73230398/ba5ab27a-6ce2-4033-b6e0-7bdfeef95cd8)

6. Удаление мероприятия (и просмотр "Архива событий")
![Screenshot_8](https://github.com/ssledopyt/Duno/assets/73230398/ff9a6d55-0c3b-4909-b8eb-d6e7eb32105f)
8. Просмотр игровых клубов и антикафе, а также их мероприятий
![Screenshot_9](https://github.com/ssledopyt/Duno/assets/73230398/7d041512-7c84-466b-b2bf-6f062df8847d)

## Стэк технологий

- [Kotlin] - язык программирования. Использован для разработки приложения
- [Jetpack Compose] - рекомендуемый современный инструментарий для создания собственного пользовательского интерфейса. Использован для разработки приложения
- [Retrofit] - библиотека, которая упрощает работу с сетевыми запросами в приложениях на Android.
- [PostgresSQL] - мощная объектно-реляционная база данных с открытым исходным кодом
- [Python] - язык программирования. Использован для разработки API
- [YandexMapKit SDK] - библиотека, которая позволяет использовать возможности Яндекс.Карт в мобильных приложениях для Android
  Также:
- [Android Studio] - IDE для работы с платформой Android
- [DBeaver] - это бесплатный универсальный инструмент баз данных с открытым исходным кодом для разработчиков и администраторов баз данных.
  
## Установка
> [!WARNING]
> Т.к. это MVP приложение, установка воспроизводится вручную. С развитием приложения будет автоматизировано.

Для начала нужно чтобы телефон с приложением были подключены к одной локальной сети.
1. На компьютер\сервер переместить файлы из директории [duno_api] в одну директорию (Например DunoApi).
2. Установить python3.
3. В командной строке ввести 
```sh
pip3 install -r requirements.txt
```
4. Установить pgAdmin (от PostgreSQL)
5. Найдите каталог, где расположен PostgreSQL. Если вы не меняли путь по умолчанию при установке, то он должен находиться в каталоге «Program Files» на диске C. Откройте CMD и выполните команду для запуска сервера PostgreSQL.
```sh
pg_ctl -D "C:\Program Files\PostgreSQL\15\data" start 
```
6. Создайте базу данных "duno" через ПО pgAdmin. Далее правой кнопкой мыши выберите "Restore" и загрузите загруженный файл "duno_bd" ничего не меняя в окне.
7. Воспользуйтесь эмулятором, либо загрузите приложение на телефон через IDE, предварительно загрузив директорию


[Kotlin]: <https://kotlinlang.org/>
[Jetpack Compose]: <https://developer.android.com/develop/ui/compose>
[Retrofit]: <https://square.github.io/retrofit/>
[PostgresSQL]: <https://www.postgresql.org/>
[Python]: <https://www.python.org/>
[YandexMapKit SDK]: <https://yandex.ru/maps-api/products/mapki>


[Android Studio]: <https://developer.android.com/studio>
[DBeaver]: <https://dbeaver.io/>

[duno_api]: <https://github.com/ssledopyt/duno_api>
