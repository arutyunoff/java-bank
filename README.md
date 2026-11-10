# java-bank

## ДЗ 8. Валидация

```sh
cd validation
mvn test
```

## ДЗ 7. Коллекции

Тесты:

```
cd collections
mvn test
```

Эксперимент:

```
cd collections
mvn compile
java -cp target/classes CollectionPerformanceExperiment
```

Задания:

1. `collections/src/main/java/CustomList.java`, `CustomArrayList.java`, тесты в `CustomArrayListTest.java`
2. `collections/src/main/java/CollectionPerformanceExperiment.java`
3. `collections/src/main/java/Student.java`, `StudentRegistry.java`, тесты в `StudentRegistryTest.java`

Результаты (медиана из 7 замеров, 3 прогрева):

| Операция | Элементов | ArrayList, мкс | LinkedList, мкс |
|---|---|---|---|
| Добавление в конец | 10000 | 186 | 109 |
| Добавление в начало | 10000 | 3754 | 147 |
| Вставка в середину | 10000 | 1945 | 38290 |
| Доступ по индексу | 10000 | 165 | 33268 |
| Удаление из начала | 10000 | 3685 | 152 |
| Удаление из конца | 10000 | 206 | 168 |

Выводы:

В конце оба списка добавляют и удаляют за O(1), время близкое: ArrayList иногда расширяет массив, LinkedList выделяет узел на каждый элемент.
В начале ArrayList медленнее, потому что сдвигает все элементы, а LinkedList только меняет ссылки.
При вставке в середину LinkedList проигрывает: позицию надо искать проходом по узлам за O(n).
ArrayList тоже сдвигает половину массива, но копирование идёт подряд в памяти и работает быстро.
Доступ по индексу у ArrayList O(1), а LinkedList каждый раз идёт по цепочке узлов.
Узлы LinkedList разбросаны по памяти, поэтому у него хуже локальность данных.
