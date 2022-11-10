## UI Load Testing Plugin

### Пример вызова задачи 

В терминале IntelliJ IDEA или в CMD перейти в корневую папку проекта, затем выполнить следующую команду:

```
gradle convertHarFileToJmeterScript [-Pparam1 -Pparam2 ... -PparamN]
```

### Инициализация значений параметров через Свойства в командной строке
#### Обязательные параметры
В данной таблице представлены параметры, в которых обязательно должны быть установлены _актуальные_ значения. В противном случае конвертация HAR-файла в JMeter Тест-план будет выполнена некорректно. 

**Примечание:** Если параметр имеет значение по умолчанию, и оно совпадает с настройками вашего тестового окружения, явно его инициализировать при вызове задачи не нужно.

| Наименование | Описание | Пример использования | Значение по умолчанию |
| -------------| ---------| ------ | --------------------- |
| protocol | Протокол в URL: http / https | -Pprotocol="https" | http |
| host | IP сервера тестируемого приложения | -Phost="10.3.25.33" | localhost |
| port | Порт на сервере приложений | -Pport="8081" | 8080 | 
| jmeterHome | Путь до /bin директории Jmeter | -PjmeterHome="C:\Jmeter\apache-jmeter-5.4.1" | Не задано |
| pathDelimiter | Разделитель между элементами пути к папкам и файлам: / или \ в зависимости от ОС | -PpathDelimiter= | \ |
| harPath | Путь до папки, в которой содержится HAR-файл | -PharPath="C:\PluginTest\HarFile" | Не задано |
| harFileName | Имя HAR-файла, который должен быть сконвертирован в .jmx Тест-план | -PharFileName="ExampleTestLog.har" | HarLog.har |
| testPlanPath | Путь до папки, в которой должен быть сохранен .jmx Тест-план | -PtestPlanPath="C:\PluginTest\TestPlan" | Не задано |
| testPlanFileName | Имя созданного Тест-плана в формате .jmx |-PtestPlanFileName="LoadTest.jmx" | Jmeter_Test_Plan.jmx |


#### Опциональные параметры
В таблице представлены параметры, относящиеся к элементу Тест-плана: [Thread Group](https://jmeter.apache.org/usermanual/component_reference.html#Thread_Group), которые имеют значения, установленные по умолчанию в коде плагина, и могут быть заданы как при вызове задачи, так и после открытия сгенерированного Тест-плана в JMeter UI.

| Наименование | Описание | Пример использования | Значение по умолчанию |
| -------------| ---------| ------ | --------------------- |
| threadGroupName | Поле **"Name"** на экране **"Thread Group"** | -PthreadGroupName="Custom Group" | Main Thread Group |
| loopsCount | Поле **"Loop Count"** на экране **"Thread Group"** | -PloopsCount=3 | 1 |
| isLoopContinueForever | Чекбокс **"Infinite"** на экране **"Thread Group"** | -PisLoopContinueForever=true | false |
| numThreads | Поле **"Num of Threads (users)"** на экране **"Thread Group"** | -PnumThreads=10 | 2 |
| rampUp | Поле **"Ramp-up period (seconds)"** на экране **"Thread Group"** | -PrampUp=15 | 0 |
| isSameUserOnNextIteration | Чекбокс **"Same user on each iteration"** на экране **"Thread Group"** | -PisSameUserOnNextIteration=true | false |


