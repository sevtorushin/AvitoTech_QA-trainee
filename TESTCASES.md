## Test-cases для тестирования API (Задание №2)

### Описание тестов

Все тесты находятся в файле [TestApi.java](src/main/test/TestApi.java).  
Тесты для невалидных данных находятся в файле [BugsTest.java](src/main/test/BugsTest.java).  
Тесты для невалидных данных проваливаются, так как API имеет баги, описанные в [BUGS.md](BUGS.md).

---

### Успешные тесты. Файл [TestApi.java](src/main/test/TestApi.java)

---

* **Тест №1** (Tag - "Create")  
  Проверка HTTP status code и Response body при сохранении валидного объекта  
  **Шаги:**

1. Отправить POST запрос на https://qa-internship.avito.com/api/1/item, в теле запроса передать объект:

```
{
    "name": "Телефон",
    "price": 1000,
    "sellerId": 1111119,
    "statistics": {
        "contacts": 0,
        "likes": 0,
        "viewCount": 0
    }
}
```

**Ожидаемый результат**  
HTTP status code - 200  
Тело ответа сервера:

```
{
    "status": "Сохранили объявление - некий ID"
}
```

---

* **Тест №2** (Tag - "Retrieve")  
  Проверка получения существующего объявления по его ID  
  **Шаги:**

1. Отправить POST запрос на https://qa-internship.avito.com/api/1/item, в теле запроса передать объект:

```
{
    "name": "Телефон",
    "price": 1000,
    "sellerId": 1111119,
    "statistics": {
        "contacts": 0,
        "likes": 0,
        "viewCount": 0
    }
}
```

2. Из тела ответа скопировать переданный сервером ID сохраненного объекта.
3. Выполнить GET запрос на адрес https://qa-internship.avito.com/api/1/item/IDСохраненногоОбъекта

**Ожидаемый результат**  
HTTP status code - 200

---

* **Тест №3** (Tag - "Comparison")
  Проверка соответствия сохраненного объявления с объектом, который был до отправки  
  **Шаги:**

1.
    1. Отправить POST запрос на https://qa-internship.avito.com/api/1/item, в теле запроса передать объект:

```
{
    "name": "Телефон",
    "price": 1000,
    "sellerId": 1111119,
    "statistics": {
        "contacts": 0,
        "likes": 0,
        "viewCount": 0
    }
}
```

2. Из тела ответа скопировать переданный сервером ID сохраненного объекта.
3. Выполнить GET запрос на адрес https://qa-internship.avito.com/api/1/item/IDСохраненногоОбъекта
4. Убедиться, что полученный и переданный объекты совпадают.
   **Ожидаемый результат**  
   Тело ответа сервера:

```
{
    "createdAt": "Дата_и_время_сохранения",
    "id": "ID_сохраненного_объекта",
    "name": "Телефон",
    "price": 1000,
    "sellerId": 1111119,
    "statistics": {
        "contacts": 0,
        "likes": 0,
        "viewCount": 0
    }
}
```

---

* **Тест №4** (Tag - "CheckAmount")  
  Проверка получения списка сохраненных объявлений для продавца, предварительно не имеющего объявлений
  **Шаги:**

1. Выбрать ID продавца, у которого нет объявлений
2. Отправить POST запрос на https://qa-internship.avito.com/api/1/item, в теле запроса передать объект:

```
{
    "name": "Телефон",
    "price": 1000,
    "sellerId": ID_пустого_продавца,
    "statistics": {
        "contacts": 0,
        "likes": 0,
        "viewCount": 0
    }
}
```

3. Из тела ответа сохранить переданный сервером ID сохраненного объекта.
4. Отправить POST запрос на https://qa-internship.avito.com/api/1/item, в теле запроса передать объект:

```
{
    "name": "Телефон",
    "price": 5555,
    "sellerId": ID пустого продавца,
    "statistics": {
        "contacts": 0,
        "likes": 0,
        "viewCount": 0
    }
}
```

5. Из тела ответа сохранить переданный сервером ID сохраненного объекта.
6. Выполнить GET запрос на адрес https://qa-internship.avito.com/api/1/ID_пустого_продавца/item/
7. Убедиться, что в теле ответа два объекта

**Ожидаемый результат**
Тело ответа сервера:

```
[
    {
        "createdAt": "Дата_и_время_сохранения",
        "id": "ID_первого_сохраненного_объекта",
        "name": "Телефон",
        "price": 1000,
        "sellerId": ID_пустого_продавца,
        "statistics": {
            "contacts": 0,
            "likes": 0,
            "viewCount": 0
        }
    }
    {
        "createdAt": "Дата_и_время_сохранения",
        "id": "ID_второго_сохраненного_объекта",
        "name": "Телефон",
        "price": 5555,
        "sellerId": ID пустого продавца,
        "statistics": {
            "contacts": 0,
            "likes": 0,
            "viewCount": 0
        }
    }
]
```

---

* **Тест №5** (Tag - "CheckMissingId")  
  Проверка ответа сервера при запросе несуществующего объявления по его ID  
  **Шаги:**

1. Выполнить GET запрос на адрес https://qa-internship.avito.com/api/1/item/123

**Ожидаемый результат**  
HTTP status code - 404
Тело ответа сервера

```
{
    "result": {
        "message": "item 123 not found",
        "messages": null
    },
    "status": "404"
}
```

---

### Bug тесты. Файл [BugsTest.java](src/main/test/BugsTest.java)

Тесты, находящиеся в этом файле, проверяют API на корректность обработки невалидных полей передаваемых объектов.  
Ожидаемые результаты тестов - HTTP Status code - 400.  
API некорректно обрабатывает такие данные, поэтому тесты проваливаются.

---

* **Тест №1** (Tag - "SavingInvalidData")  
  Тест параметрический. Последовательно отправляются POST запросы с телом в виде JSON объекта.  
  Набор данных для тестирования:
1.
```
{
    "name": "",
    "price": 5555,
    "sellerId": 1111119,
    "statistics": {
        "contacts": 0,
        "likes": 0,
        "viewCount": 0
    }
}
```
2.
```
{
    "name": " ",
    "price": 5555,
    "sellerId": 1111119,
    "statistics": {
        "contacts": 0,
        "likes": 0,
        "viewCount": 0
    }
}
```
3.
```
{
    "name": "Телефон",
    "price": -1,
    "sellerId": 1111119,
    "statistics": {
        "contacts": 0,
        "likes": 0,
        "viewCount": 0
    }
}
```
4.
```
{
    "name": "Телефон",
    "price": 5555,
    "sellerId": -1,
    "statistics": {
        "contacts": 0,
        "likes": 0,
        "viewCount": 0
    }
}
```
5.
```
{
    "name": "Телефон",
    "price": 555,
    "sellerId": 1111119,
    "statistics": {
        "contacts": -1,
        "likes": 0,
        "viewCount": 0
    }
}
```
6.
```
{
    "name": "Телефон",
    "price": 555,
    "sellerId": 1111119,
    "statistics": {
        "contacts": 0,
        "likes": -1,
        "viewCount": 0
    }
}
```
7.
```
{
    "name": "Телефон",
    "price": 555,
    "sellerId": 1111119,
    "statistics": {
        "contacts": 0,
        "likes": 0,
        "viewCount": -1
    }
}
```
8.
```
{
    "name": null,
    "price": 555,
    "sellerId": 1111119,
    "statistics": {
        "contacts": 0,
        "likes": 0,
        "viewCount": 0
    }
}
```
9.
```
{
    "name": "Телефон",
    "price": null,
    "sellerId": 1111119,
    "statistics": {
        "contacts": 0,
        "likes": 0,
        "viewCount": 0
    }
}
```
10.
```
{
    "name": "Телефон",
    "price": 555,
    "sellerId": null,
    "statistics": {
        "contacts": 0,
        "likes": 0,
        "viewCount": 0
    }
}
```
11.
```
{
    "name": "Телефон",
    "price": 555,
    "sellerId": 1111119,
    "statistics": {
        "contacts": null,
        "likes": 0,
        "viewCount": 0
    }
}
```
12.
```
{
    "name": "Телефон",
    "price": 555,
    "sellerId": 1111119,
    "statistics": {
        "contacts": 0,
        "likes": null,
        "viewCount": 0
    }
}
```
13.
```
{
    "name": "Телефон",
    "price": 555,
    "sellerId": 1111119,
    "statistics": {
        "contacts": 0,
        "likes": 0,
        "viewCount": null
    }
}
```
14.
```
{
    "name": "Телефон",
    "price": 555,
    "sellerId": 1111119,
    "statistics": null
}
```
15.
```
null
```

**Шаги:**  
1. Отправить POST запрос на https://qa-internship.avito.com/api/1/item, в теле запроса передать поочередно указанные выше объекты.  
**Ожидаемый результат**  
HTTP status code - 400  

---

* **Тест №2** (Tag - "GetFromInvalidSellerID")  
Тест для проверки корректности обработки GET запроса данных у несуществующего продавца.  
**Шаги:**  
1. Отправить GET запрос на адрес https://qa-internship.avito.com/api/1/sfg3nd5f4hn/item  
**Ожидаемый результат**  
HTTP status code - 404  

---

* **Тест №3** (Tag - "CheckOtherNumber")  
Тест для проверки корректности обработки POST запроса при передаче в качестве значений полей объекта больших чисел.  
**Шаги:**  
1. Отправить POST запрос на https://qa-internship.avito.com/api/1/item, в теле запроса передать объект:

```
{
    "name": "Телефон",
    "price": 10000000000000000000000,
    "sellerId": 1111119,
    "statistics": {
        "contacts": 0,
        "likes": 0,
        "viewCount": 0
    }
}
```
**Ожидаемый результат**  
HTTP status code - 200 или 400 с указанием предела по значению

---

* **Тест №4** (Tag - "CheckOtherNumber")  
  Тест для проверки корректности обработки POST запроса при передаче строки в числовом поле объекта.  
  **Шаги:**
1. Отправить POST запрос на https://qa-internship.avito.com/api/1/item, в теле запроса передать объект:

```
{
    "name": "Телефон",
    "price": "sfg3nd5f4hn",
    "sellerId": 1111119,
    "statistics": {
        "contacts": 0,
        "likes": 0,
        "viewCount": 0
    }
}
```
**Ожидаемый результат**  
HTTP status code - 400

---

* **Тест №5** (Tag - "CheckOtherNumber")  
  Тест для проверки корректности обработки POST запроса при передаче вещественного числа в числовом поле объекта.  
  **Шаги:**
1. Отправить POST запрос на https://qa-internship.avito.com/api/1/item, в теле запроса передать объект:

```
{
    "name": "Телефон",
    "price": "1000.5",
    "sellerId": 1111119,
    "statistics": {
        "contacts": 0,
        "likes": 0,
        "viewCount": 0
    }
}
```
**Ожидаемый результат**  
HTTP status code - 200 или 400 с указанием вводить только целые числа