# Hw95

### Условие
Существует 10 аккаунторв. Изначально сохраняем в каждом произвольную сумму. Далее каждый поток переводит с произвольного аккаунта 100 на свой аккаунт. После на каждый из 10 аккаунтов начисляется 1000. 

### Итог
Операции перевода в итоге будут выполнены для всех аккаунтов.

### Анализ

В реализации операций начисления, списания и перевода средств используются condition variables. Операция перевода состоит из двух операций: списание и зачисление средств. Присутствует две реализации списания: приоритетная и обычная.

Если изначально на каком-либо из 10 аккаунтов не хватает денег для перевода, то поток заснет до тех пор, пока аккаунт на пополнят. Заметим, что рано или поздно заснувший поток проснется, тк все аккаунты будут пополнены на 1000 и будет выполнен notifyAll() после каждого пополнения. (1000 достаточно чтобы с любого аккаунта сделать 10 переводов, а аккаунтов всего 10 и сних будет произведено как раз 10 переводов).
Кроме того, поток может заснуть из-за того что произошла попытка выполнить обычное списание, в то время как существовали начатые приоритетные списания. Такие потоки проснутся после окончания выполнения приоритетного списания (будет выполнен notifyAll()).

Рассмотрим обычное списание. Перед тем как списать сумму, у нас выполняется проверка баланса и количества начатых приоритетных списаний условия цикла. Проверка находится под локом. Таким образом, баланс не может стать отрицательным в ходе обычных списаний. Аналогично в случае приоритетного списания, баланс не может стать отрицательным.

Дедлока быть не может. Вложеность локов существует только в операции перевода (сначала берем transferLock, затем лок на объекте класса.) Дедлок может возникнуть только в том случае если в другом месте мы вызываем вложенные локи в другом порядке, но transferLock берется только в операции перевода, поэтому дедлока быть не может.
