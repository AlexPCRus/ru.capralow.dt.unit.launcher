// Параметры:
// 	Фреймворк - ФреймворкТестирования - Фреймворк тестирования
// @unit-test:failure
Процедура ПроверитьИстинуНаЛожь(Фреймворк) Экспорт
	// FIXME: В Expected и Actual написано false/false, а надо Истина/Ложь
	Результат = Ложь;
	Фреймворк.ПроверитьИстину(Результат, "Результат Не Истина.");
КонецПроцедуры

// Параметры:
// 	Фреймворк - ФреймворкТестирования - Фреймворк тестирования
// @unit-test:failure
Процедура ПроверитьЛожьНаЛожь(Фреймворк) Экспорт
	// FIXME: В Expected и Actual написано false/true, а надо Ложь/Истина
	Результат = Истина;
	Фреймворк.ПроверитьЛожь(Результат, "Результат Не Ложь.");
КонецПроцедуры