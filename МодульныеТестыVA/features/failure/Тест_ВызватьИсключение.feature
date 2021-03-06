# language: ru

@tree
@classname=ModuleExceptionPath

Функционал: МодульныеТестыVA.Тест_ВызватьИсключение
	Как Разработчик
	Я Хочу чтобы возвращаемое значение метода совпадало с эталонным
	Чтобы я мог гарантировать работоспособность метода

@OnServer
Сценарий: ПроверитьРаботуИсключения
	И я выполняю код встроенного языка на сервере
	| 'Тест_ВызватьИсключение.ПроверитьРаботуИсключения(Контекст());' |

@OnServer
Сценарий: ПроверитьДобавитьОшибкуСценария
	И я выполняю код встроенного языка на сервере
	| 'Тест_ВызватьИсключение.ПроверитьДобавитьОшибкуСценария(Контекст());' |

Сценарий: ПроверитьРаботуИсключения
	И я выполняю код встроенного языка
	| 'Тест_ВызватьИсключение.ПроверитьРаботуИсключения(Контекст());' |

Сценарий: ПроверитьДобавитьОшибкуСценария
	И я выполняю код встроенного языка
	| 'Тест_ВызватьИсключение.ПроверитьДобавитьОшибкуСценария(Контекст());' |