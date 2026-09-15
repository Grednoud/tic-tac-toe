# Крестики-нолики / Tic-Tac-Toe

Современное desktop-приложение для классической игры в крестики-нолики, написанное на Java 21 с использованием JavaFX.

A modern desktop application for the classic Tic-Tac-Toe game, built with Java 21 and JavaFX.

![Java](https://img.shields.io/badge/Java-21-orange)
![JavaFX](https://img.shields.io/badge/JavaFX-21-blue)
![Gradle](https://img.shields.io/badge/Gradle-Kotlin_DSL-green)

## Возможности / Features

- **Два режима игры / Two game modes:**
  - Игрок против компьютера (PvAI)
  - Игрок против игрока (PvP)
  
- **Настраиваемое поле / Customizable board:**
  - Размер от 3×3 до 10×10
  - Настраиваемая длина выигрышной линии

- **Современный интерфейс / Modern UI:**
  - Тёмная тема с акцентными цветами
  - Плавные анимации и hover-эффекты
  - Адаптивный дизайн

## Требования / Requirements

- **JDK 21** или новее / JDK 21 or newer
- Gradle (включён wrapper) / Gradle (wrapper included)

## Запуск / Running

```bash
# Клонировать репозиторий / Clone the repository
git clone <repository-url>
cd tic-tac-toe

# Запустить приложение / Run the application
./gradlew run

# Собрать проект / Build the project
./gradlew build

# Запустить тесты / Run tests
./gradlew test
```

На Windows используйте `gradlew.bat` вместо `./gradlew`.

## Архитектура / Architecture

Проект использует чистое разделение слоёв:

```
src/main/java/ru/mts/tictactoe/
├── TicTacToeApp.java       # Точка входа JavaFX / JavaFX entry point
├── model/                  # Бизнес-логика / Business logic
│   ├── Board.java          # Игровое поле и проверка победы
│   ├── GameState.java      # Состояние игры и управление ходами
│   ├── Player.java         # Enum игроков (X, O)
│   ├── GameMode.java       # Enum режимов игры
│   └── GameResult.java     # Enum результатов игры
├── ai/
│   └── AiPlayer.java       # ИИ с эвристикой: победа → блок → центр → случайный
└── ui/
    ├── MainView.java       # Главное окно приложения
    ├── GameBoardView.java  # Визуализация игрового поля
    ├── NewGameDialog.java  # Диалог настройки новой игры
    └── GameController.java # Связывает модель и UI
```

### Ключевые принципы / Key principles

1. **Разделение Model/View** — вся игровая логика в `model/`, UI не содержит бизнес-правил
2. **Тестируемость** — модель полностью покрыта unit-тестами
3. **Читаемость** — код написан для обучения, избегает «умных» трюков

## AI-стратегия / AI Strategy

Компьютерный противник использует простую, но эффективную эвристику:

1. **Победа** — если есть выигрышный ход, сделать его
2. **Блокировка** — если противник может выиграть следующим ходом, заблокировать
3. **Центр** — занять центр поля если свободен
4. **Случайный ход** — выбрать случайную свободную клетку

## Тестирование / Testing

```bash
# Запустить все тесты
./gradlew test

# Запустить с детальным выводом
./gradlew test --info
```

Тесты покрывают:
- Проверку победы (горизонталь, вертикаль, диагонали)
- Определение ничьей
- Корректность ходов
- AI-логику (выигрыш, блокировка, стратегия)

## Структура проекта / Project Structure

```
tic-tac-toe/
├── build.gradle.kts        # Gradle build с JavaFX plugin
├── settings.gradle.kts     # Настройки Gradle
├── src/
│   ├── main/
│   │   ├── java/           # Исходный код
│   │   └── resources/      # CSS стили
│   └── test/
│       └── java/           # Unit-тесты
└── README.md
```

## История / History

Этот проект — модернизация учебного Swing-приложения (~2016) с целью демонстрации современного подхода к разработке Java desktop-приложений.

## Лицензия / License

Educational project / Учебный проект
