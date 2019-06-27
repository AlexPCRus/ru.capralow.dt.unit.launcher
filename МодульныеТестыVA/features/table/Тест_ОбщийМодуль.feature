# language: ru

@tree
@classname=ModuleExceptionPath

Функционал: Общий модуль - сервер
	Как Разработчик
	Я Хочу чтобы возвращаемое значение метода совпадало с эталонным
	Чтобы я мог гарантировать работоспособность метода

@OnServer
Сценарий: Тест_ОбщийМодуль (сервер): ПроверитьРавенствоКоллекцийНаИстина
	И я выполняю код встроенного языка на сервере
	| 'Тест_ОбщийМодуль.ПроверитьРавенствоКоллекцийНаИстина(Контекст());' |

@OnServer
Сценарий: Тест_ОбщийМодуль (сервер): ПроверитьРавенствоКоллекцийНаЛожь
	И я выполняю код встроенного языка на сервере
	| 'Тест_ОбщийМодуль.ПроверитьРавенствоКоллекцийНаЛожь(Контекст());' |