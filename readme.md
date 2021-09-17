# JКоннектор (jc1c)

## Краткое описание
Данная библиотека призвана упростить и настроить быстрое и простое
взаимодействия **1С** и вашего скрипта на Java, далее по тексту просто - **jar**.

## Общий принцип работы
Взаимодействие **1С** и **jar** - это клиент-серверное взаимодействие по протоколу HTTP.
В качестве клиента выступает 1С, а в качестве сервера выступает jar.
Пряток взаимодействия:
- в **1С** хранится **jar**, в виде двоичных данных;
- при помощи библиотеки для **1С** (далее по тексту - JКоннектор) создается и запускается jar;
- при запуске **jar**, разворачивается небольшой HTTP сервер, который прослушивает
порт(по умолчанию - 8080) на локальной машине (localhost);
- при помощи JКоннектор отправляются HTTP запросы, **jar** их ловит, обрабатывает и отвечает;
- все взаимодействие готово...

## Использование на стороне Java
Для подключения **jc1c** в ваш проект вам необходимо использовать **JitPack**,
следуйте инструкции на официальном сайте - [ссылка](https://jitpack.io/#kaliuzhnyi/jc1c/1.0.1 "https://jitpack.io/#kaliuzhnyi/jc1c/1.0.1")

После этого в main-методе вашего jar вам необходимо:
1. Создать объект сервера и запустить его(сервер)
``` java
public class MyApp {
    public static void main(String[] args) throws IOException {
        JServer.builder()
                .withHostname("localhost")
                .withPort(8080)
                .withApiKey("MySuperSecretKey")
                .withBacklog(3)
                .withThreadPool(3)
                .withHandlersController(MyAppHandlers.class)
                .build()
                .start();
    }
}

// Краткое описание и назначение методов builder-а:
// .withHostname() - хост который будет прослушиваться, значение по умолчанию localhost
// .withPort() - порт который будет прослушиваться, значение по умолчанию 8080
// .withApiKey() - ключ для отсечения лишних запросов(если такие будут)
// .withBacklog() - количество запросов в очереди, значение по умолчанию 3
// .withThreadPool() - количество потоков для работы сервера
// .withHandlersController(MyAppHandlers.class) - указание класса в котором содержатся методы-обработчики запросов
```
2. Создать класс с методами которые будут обрабатывать входящие запросы, например:
``` java
@JHandlerControllers
public class MyAppHandlers {
    @JHandler(methodName = "say_hello")
    public String methodSayHello(Long value, String name) {
        Long resultValue = value * value;
        String resultText = "Hello" + name + ". The result is - " + resultValue;
        return resultText;
    }
}

// Краткое описание:
// - ваш класс с обработчика должен быть помечен аннотацией - JHandlerControllers
// - при обработке запроса будут учитываться только те методы которые помечены фннотацией - JHandler
//   в аннотациие необходимо указать имя метода, именно это имя метода необходимо указывать при 
//   совершении запроса из 1С.
// - все, дальше в этом классе и медотах обработчика можно делать все что угодно ;)

// Важные дополнения:
// - jc1c обрабатывает и передает в ваш метод-обработчик, в примере это метод - public String methodSayHello(Long value, String name)
//   только параметры следующих типов Long, Double, Boolean, String, Instant
// - параметры из 1С должны передоваться именно в той последжовательности в которой они указаны
//   в методе обработчике, в примере это метод - public String methodSayHello(Long value, String name)

```
3. Скомпилировать jar, и любім доступным способ хранить двоичные данные jar в 1С, чтобы их(двоичные данные)
передавать в метод библиотеки **JКоннектор**.
4. Пользуемся. Подробнее о том как работать с библиотекой JКоннектор можно почитать на Инфостарте.

## От автора
Я очень надеюсь, что доступно все объяснил, и надеюсь что данная библиотека будет полезна.
Код библиотеки **JКоннектор** на языке 1С находится в файле JКоннектор.