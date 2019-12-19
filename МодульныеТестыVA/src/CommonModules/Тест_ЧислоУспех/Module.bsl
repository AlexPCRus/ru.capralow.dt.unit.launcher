// Параметры:
// 	Фреймворк - ФреймворкТестирования - Фреймворк тестирования
// @unit-test:success
Процедура ПроверитьРавенствоЧислаНаИстину(Фреймворк) Экспорт
	ЭталонноеЧисло		= 1;
	ПроверяемоеЧисло	= 1;
	
	Фреймворк.ПроверитьРавенство(ЭталонноеЧисло, ПроверяемоеЧисло,
		"Числа равны.");
КонецПроцедуры

// Параметры:
// 	Фреймворк - ФреймворкТестирования - Фреймворк тестирования
// @unit-test:success
Процедура ПроверитьНеРавенствоЧислаНаИстину(Фреймворк) Экспорт
	ЭталонноеЧисло		= 1;
	ПроверяемоеЧисло	= 2;
	
	Фреймворк.ПроверитьНеРавенство(ЭталонноеЧисло, ПроверяемоеЧисло,
		"Числа неравны.");
КонецПроцедуры

// Параметры:
// 	Фреймворк - ФреймворкТестирования - Фреймворк тестирования
// @unit-test:success
Процедура ПроверитьЧислоБольшеНаИстину(Фреймворк) Экспорт
	ЭталонноеЧисло		= 1;
	ПроверяемоеЧисло	= 2;
	
	Фреймворк.ПроверитьБольше(ЭталонноеЧисло, ПроверяемоеЧисло,
		"Число больше.");
КонецПроцедуры

// Параметры:
// 	Фреймворк - ФреймворкТестирования - Фреймворк тестирования
// @unit-test:success
Процедура ПроверитьЧислоБольшеИлиРавноНаИстину(Фреймворк) Экспорт
	ЭталонноеЧисло		= 1;
	ПроверяемоеЧисло	= 1;
	
	Фреймворк.ПроверитьБольшеИлиРавно(ЭталонноеЧисло, ПроверяемоеЧисло,
		"Число больше или равно.");
КонецПроцедуры

// Параметры:
// 	Фреймворк - ФреймворкТестирования - Фреймворк тестирования
// @unit-test:success
Процедура ПроверитьЧислоМеньшеНаИстину(Фреймворк) Экспорт
	ЭталонноеЧисло		= 2;
	ПроверяемоеЧисло	= 1;
	
	Фреймворк.ПроверитьМеньше(ЭталонноеЧисло, ПроверяемоеЧисло,
		"Число меньше.");
КонецПроцедуры

// Параметры:
// 	Фреймворк - ФреймворкТестирования - Фреймворк тестирования
// @unit-test:success
Процедура ПроверитьЧислоМеньшеИлиРавноНаИстину(Фреймворк) Экспорт
	ЭталонноеЧисло		= 1;
	ПроверяемоеЧисло	= 1;
	
	Фреймворк.ПроверитьМеньшеИлиРавно(ЭталонноеЧисло, ПроверяемоеЧисло,
		"Число меньше или равно.");
КонецПроцедуры
