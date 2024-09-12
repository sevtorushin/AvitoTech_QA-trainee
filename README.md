# AvitoTech_QA-trainee</h1>

Здравствуйте, меня зовут Сергей. Ниже привожу описание выполненного задания.

### Структура тестового проекта

Файл [Task1_bugs_description.md](Task1_bugs_description.md) содержит решение первого задания.  
К нему в директории [Task1_pictures](Task1_pictures) хранятся скрины.  
Файл [TESTCASES.md](TESTCASES.md) содержит тест-кейсы для выполнения второго задания для Варианта 1.  
Файл [BUGS.md](BUGS.md) содержит отчет по выявленным багам в результате выполнения авто-тестов.  
Для BUGS.md имеется папка со скринами [Task2_pictures](Task2_pictures).  
Файлы с автотестами расположены в директории [test](src/main/test). Так же файлы продублированы в корне.  
Инструкция для тестирования на ОС Windows.

---

### Задание №1

В задании нужно найти все баги на скриншоте страницы выдачи объявлений по указанному фильтру.  
Описание багов оформлено в отчете [Task1_bugs_description.md](Task1_bugs_description.md)

---

### Задание №2

Необходимо протестировать API с использованием автотестов, составить тест-кейсы и оформить bug report в случае наличия багов в тестируемом API.

Задание выполнено с использованием следующих инструментов:
* Java 21.0.3
* Apache Maven 3.8.8
* Junit Jupiter 5
* Lombok 1.18.30
* Jackson databind 2.15.4

### Инструкция по запуску тестов

1. Склонируйте к себе репозиторий, в котором хранится проект тестового задания, через выполнение команды в терминале
    ```
   https://github.com/sevtorushin/AvitoTech_QA-trainee.git
   ```
2. Для запуска тестов необходимо установить JDK версии не ниже 8 (лучше 17 или выше).
    Установить можно с официального сайта [Oracle](https://www.oracle.com/cis/java/technologies/downloads/).
3. Так же необходимо сборщик Apache Maven (желательно версии 3.8.8) с официального сайта [Apache Maven](https://maven.apache.org/download.cgi).
4. Убедиться, что JDK и Apache Maven установлены, выполнив следующие команды в консоли

   `java --version`

   `mvn --version`

5. Установить следующие зависимости

   `mvn dependency:get -Dartifact=org.junit.jupiter:junit-jupiter-api:5.10.2`

   `mvn dependency:get -Dartifact=org.junit.jupiter:junit-jupiter-params:5.8.2`

   `mvn dependency:get -Dartifact=org.projectlombok:lombok:1.18.30`
    
   `mvn dependency:get -Dartifact=com.fasterxml.jackson.core:jackson-databind:2.15.4`

   `mvn dependency:get -Dartifact=org.apache.maven.plugins:maven-compiler-plugin:3.12.0`

   `mvn dependency:get -Dartifact=org.apache.maven.plugins:maven-surefire-plugin:3.2.5`

6. Через командную строку/терминал перейдите в корневую директорию проекта, выполнив команду
    ```
   cd/здесь укажите путь до директории с проектом
   ```
7. Запустите тесты, выполнив команду

   `mvn test`