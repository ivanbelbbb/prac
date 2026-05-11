# prac
**Что уже готово:**
- `Integration.java` — simpson
- `Differentiation.java` — newton, dif, dif2, apache методы

---

**Что делать по порядку:**

**1. Починить `Differentiation.java`** (30 мин)
- заменить `newton()` на чистый метод Ньютона (код выше)
- заменить `NevilleInterpolator` на `SplineInterpolator` в `apacheInterpolation`
- исправить `apacheA` — передавать `apacheT` а не `apacheV`

**2. Создать `DiffService.java`** (30 мин)
- перенести логику из `runDifferentiation()`
- убрать XChart и Scanner
- добавить таймер для своего метода и Apache отдельно
- вернуть `record` с результатами

**3. Создать `IntegService.java`** (20 мин)
- перенести логику из `runIntegration()`
- убрать Scanner и while
- добавить таймер
- вернуть `record` с результатами

**4. Написать `UI.java`** (2 часа)
- `showMenu()` — два экрана
- `showDiff()` — три графика + время выполнения + демо кнопка
- `showInteg()` — поля ввода + валидация + результат + демо кнопка

**5. Удалить `App.java`**

---

Начните с пункта 1 — показывайте код по шагам.