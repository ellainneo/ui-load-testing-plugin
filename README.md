## UI Load Testing Plugin

### Установка плагина в локальный репозиторий
1) Создать папку на локальном ПК, перейти в нее.
2) Склонировать репозиторий, выполнив в консоли команду

```
git clone https://github.com/ellainneo/ui-load-testing-plugin.git
```

При успешном клонировании результат будет таким, как на скриншоте

![image](https://user-images.githubusercontent.com/114524355/201515124-ef5863d2-dbc8-4c92-8387-d6a99c2f9249.png)

3) Открыть созданный проект в IDE: в меню перейти в раздел **Open** -> **Project**.
4) Выделить корневую директорию плагина и нажать кнопку **"ОК"** в модальной форме выбора.

![image](https://user-images.githubusercontent.com/114524355/201515866-4016a504-19b6-415a-b9af-0fc3271712db.png)

5) После загрузки проекта в IDE со всеми необходимыми зависимостями, необходимо собрать плагин, выполнив в терминале команду

```
gradle clean build
```

![image](https://user-images.githubusercontent.com/114524355/201516875-17d7e0ce-9d3d-44e5-8d62-5846e4da6f12.png)

6) По завершении сборки плагин может быть опубликован в локальный Maven-репозиторий выполнением в терминале команды

```
gradle publishToMavenLocal
```
Если публикация выполнилась без ошибок, результат в консоли будет как на приведенном ниже скриншоте

![image](https://user-images.githubusercontent.com/114524355/201517819-e925feec-af52-4578-93ae-10dc4a60e7aa.png)

После публикации в локальный репозиторий плагин может быть подключен к вашему проекту, **из которого** необходимо вызывать задачи прохождения тестов и конвертации HAR-файла в .jmx-скрипт.

### Подключение плагина к проекту

Чтобы подключить плагин к проекту, необходимо в файл `build.gradle` своего проекта добавить блок `buildscript` с указанием зависимостей для подключаемого плагина из локального Maven-репозитория. 

**Примечание:** содержимое `buildscript` всегда должно располагаться в `build.gradle` файле вашего проекта в самом начале - **до** остальных содержащихся в нем блоков.

```
buildscript {
    repositories {
        mavenLocal()
    }

    dependencies {
        classpath "com.haulmont.ui.load.testing:ui-load-test-plugin:1.0"
    }
}
```

Далее в файле `build.gradle` вашего проекта необходимо указать строку для применения подключаемого плагина. 

**Примечание:** если в `build.gradle` есть блок `plugins`, то указанную команду необходимо расположить **после** него.

```
apply plugin: 'ui-load-testing'
```

![image](https://user-images.githubusercontent.com/114524355/201520170-fb996d1c-72d7-4ec3-ab21-2fb743bd691d.png)


### Формат вызова задачи конвертации HAR-файла в JMeter Тест-план

Задача convertHarFileToJmeterScript выполняет конвертацию HAR-файла, полученного в результате ручного или автоматического выполнения тестов в JMeter Тест-план (файл формата .jmx). Полученный JMeter Тест-план может быть успешно открыт в JMeter UI для просмотра либо последующего редактирования.

Вызов задачи конвертации осуществляется через cmd или терминал IDE путем выполнения команды следующего формата

```
gradle convertHarFileToJmeterScript [-Pparam1 -Pparam2 ... -PparamN]
```

Пример использования с указанием значений параметров:

```
gradle convertHarFileToJmeterScript -PharPath="C:\PluginTest\HarFile" -PharFileName="TestExec.har" -PtestPlanPath="C:\PluginTest\TestPlan" -PtestPlanFileName="CustomLoadTestPlan.jmx" -PjmeterHome="C:\Jmeter\apache-jmeter-5.4.1" -Phost="XXX.XX.XX.XX"

```

Также свойства можно задать в файле build.gradle вашего проекта, использующего плагин, в следующем формате:

```
jmeterExtensions {
    host = 'XXX.X.XX.XX'
    jmeterHome = 'C:\\Jmeter\\apache-jmeter-5.4.1'
    harPath = 'C:\\PluginTest\\HarFile'
    harFileName = 'TestExec.har'
    testPlanPath = 'C:\\PluginTest\\TestPlan'
    testPlanFileName = 'Load TPL.jmx'
}
```
После этого задачу можно вызвать без параметров через терминал IDE:

```
 gradle convertHarFileToJmeterScript
```

Также можно осуществить выполнение задачи двойным нажатием по соответствующему элементу в Gradle-панели:

![image](https://user-images.githubusercontent.com/114524355/201529108-327615c3-2b7f-4cea-876f-32c7b10d14db.png)


### Доступные свойства плагина
#### Обязательные параметры
В данной таблице представлены параметры, в которых обязательно должны быть установлены _актуальные_ значения. В противном случае конвертация HAR-файла в JMeter Тест-план будет выполнена некорректно. 

**Примечание:** Если параметр имеет значение по умолчанию, и оно совпадает с настройками вашего тестового окружения, явно его инициализировать при вызове задачи не нужно.

| Наименование | Описание | Пример использования | Значение по умолчанию |
| -------------| ---------| ------ | --------------------- |
| protocol | Протокол в URL: http / https | -Pprotocol="https" | http |
| host | IP сервера тестируемого приложения | -Phost="10.3.25.33" | localhost |
| port | Порт, на котором развернуто приложение | -Pport="8081" | 8080 | 
| jmeterHome | Путь до /bin директории JМeter | -PjmeterHome="C:\Jmeter\apache-jmeter-5.4.1" | Не задано |
| pathDelimiter | Разделитель между элементами пути к папкам и файлам: / или \ в зависимости от ОС | -PpathDelimiter="/" | \ |
| harPath | Путь, включающий папку, в которой хранится HAR-файл | -PharPath="C:\PluginTest\HarFile" | Не задано |
| harFileName | Имя HAR-файла, который должен быть сконвертирован в .jmx Тест-план | -PharFileName="ExampleTestLog.har" | HarLog.har |
| testPlanPath | Путь, включающий папку, в которой должен быть сохранен .jmx Тест-план | -PtestPlanPath="C:\PluginTest\TestPlan" | Не задано |
| testPlanFileName | Имя созданного Тест-плана в формате .jmx |-PtestPlanFileName="LoadTest.jmx" | Jmeter_Test_Plan.jmx |


#### Опциональные параметры

##### Общие настройки Тест-плана

В таблице представлены параметры, относящиеся к компонентам Тест-плана [Test Plan](https://jmeter.apache.org/usermanual/component_reference.html#Test_Plan) и [HTTP Cookie Manager](https://jmeter.apache.org/usermanual/component_reference.html#HTTP_Cookie_Manager), которые имеют значения, установленные по умолчанию в коде плагина, и могут быть заданы как при вызове задачи, так и после открытия сгенерированного Тест-плана в JMeter UI.

| Наименование | Описание | Пример использования | Значение по умолчанию |
| -------------| ---------| ------ | --------------------- |
| testPlanName | Наименование Тест-плана, отображаемое в корневом элементе дерева в JMeter UI | -PtestPlanName="Custom TPL" | Project Test Plan |
| cookieManagerName | Наименование, отображаемое в дереве Тест-плана для компонента HTTP Cookie Manager | -PcookieManagerName="Custom Cookies" | Cookie Manager |
| isClearEachIteration | Чекбокс **"Clear cookies each iteration"** на экране **"HTTP Cookie Manager"** | -PisClearEachIteration=false | true |


##### Настройки Thread Group

В таблице представлены параметры, относящиеся к компоненту Тест-плана [Thread Group](https://jmeter.apache.org/usermanual/component_reference.html#Thread_Group), которые имеют значения, установленные по умолчанию в коде плагина, и могут быть заданы как при вызове задачи, так и после открытия сгенерированного Тест-плана в JMeter UI.

| Наименование | Описание | Пример использования | Значение по умолчанию |
| -------------| ---------| ------ | --------------------- |
| threadGroupName | Количество пользователей (потоков), задается в поле **"Name"** на экране **"Thread Group"** | -PthreadGroupName="Custom Group" | Main Thread Group |
| loopsCount | Количество повторений Тест-плана, задается в поле **"Loop Count"** на экране **"Thread Group"** | -PloopsCount=3 | 1 |
| isLoopContinueForever | Чекбокс **"Infinite"** на экране **"Thread Group"** | -PisLoopContinueForever=true | false |
| numThreads | Поле **"Num of Threads (users)"** на экране **"Thread Group"** | -PnumThreads=10 | 2 |
| rampUp | Поле **"Ramp-up period (seconds)"** на экране **"Thread Group"** | -PrampUp=15 | 0 |
| isSameUserOnNextIteration | Чекбокс **"Same user on each iteration"** на экране **"Thread Group"** | -PisSameUserOnNextIteration=true | false |



