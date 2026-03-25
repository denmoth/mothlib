# План миграции MothLib в мультилоадер и добавления тегов биомов

Этот план описывает процесс конвертации существующего проекта на Forge (1.20.1) в проект-мультилоадер (Common, Forge, NeoForge, Fabric, Quilt) без использования Architectury Loom, а также разработку собственного DataGen для создания детальных тегов биомов для пяти крупных модов.

## Часть 1: Реструктуризация проекта (Мультилоадер)

1.  **Создание модулей:**
    *   Создать папки для загрузчиков: `common`, `forge`, `fabric`, `neoforge`, `quilt`.
    *   Переместить весь исходный код (`src/main/java`, `src/main/resources`) и тесты (`src/testmod`) из корневой директории в `common`.
2.  **Настройка сборок Gradle:**
    *   **Root (`build.gradle`, `settings.gradle`):** Обновить для поддержки подпроектов (subprojects). Удалить ForgeGradle из корня.
    *   **Common:** Настроить компиляцию с использованием Vanilla/Parchment маппингов (без привязки к загрузчикам).
    *   **Forge (`forge/build.gradle`):** Использовать ForgeGradle (как было). Настроить зависимость от `common`.
    *   **NeoForge (`neoforge/build.gradle`):** Использовать NeoGradle. Настроить зависимость от `common`.
    *   **Fabric (`fabric/build.gradle`):** Использовать Fabric Loom. Настроить зависимость от `common`. Добавить `fabric.mod.json`.
    *   **Quilt (`quilt/build.gradle`):** Использовать Quilt Loom (или Fabric Loom с quilt-плагинами). Настроить зависимость от `common`. Добавить `quilt.mod.json`.
3.  **Адаптация кода платформы:**
    *   В `common` создать платформонезависимые интерфейсы (например, для регистрации блоков/предметов: `MothPlacements`, `MothConfigRegistry`).
    *   Использовать `ServiceLoader` (или аналогичный паттерн) для загрузки специфичных для платформы реализаций, если потребуется. На данный момент мы используем прямой доступ через сервисы.
    *   Удалить специфичные аннотации Forge (например, `@Mod.EventBusSubscriber`) из кода `common` и перенести их в модуль `forge` (и создать аналоги в `fabric`, `neoforge`, `quilt`).
    *   `MothLib.java` переименовать или разделить на платформенные точки входа (например, `MothLibForge`, `MothLibFabric` и т.д.).

## Часть 2: Разработка собственного DataGen

Поскольку мы хотим избежать зависимости от DataGen каждого отдельного загрузчика и нам нужны универсальные JSON-файлы в `common`, мы напишем свой платформонезависимый генератор JSON (по аналогии с тем, как сейчас работает генерация мира).

1.  **Создание `MothBiomeTagsProvider` (в `common/src/main/java/com/denmoth/mothlib/datagen`):**
    *   Создать абстрактный класс (или сервис), который не зависит от `net.minecraft.data.tags.TagsProvider` из Forge/Fabric, а использует чистую запись JSON через `Gson` (или ванильный `PackOutput`).
    *   Так как у нас уже есть `MothWorldGenProvider`, который умеет писать JSON, мы можем переиспользовать этот механизм.
2.  **Регистрация тегов (API):**
    *   Создать класс `MothBiomeTags` (в `common/src/main/java/com/denmoth/mothlib/api/tags`), содержащий константы `TagKey<Biome>`.

## Часть 3: Создание тегов биомов для ТОП-5 модов

Мы сгенерируем богатый набор тегов, которые будут включать в себя биомы из:
*   Biomes O' Plenty (`biomesoplenty`)
*   Terralith (`terralith`)
*   Oh The Biomes You'll Go (`byg`)
*   Blue Skies (`blue_skies`)
*   Autumnity (`autumnity`)

### Категории тегов:

1.  **По типам деревьев (Trees):**
    *   `mothlib:trees/oak`, `mothlib:trees/birch`, `mothlib:trees/spruce`, `mothlib:trees/jungle`, `mothlib:trees/acacia`, `mothlib:trees/dark_oak`, `mothlib:trees/cherry`, `mothlib:trees/mangrove`.
    *   `mothlib:trees/coniferous` (хвойные), `mothlib:trees/deciduous` (лиственные).
2.  **По типу поверхности (Ground/Surface):**
    *   `mothlib:ground/podzol`, `mothlib:ground/mycelium`, `mothlib:ground/sand`, `mothlib:ground/snow`, `mothlib:ground/mud`, `mothlib:ground/gravel`, `mothlib:ground/stone`.
3.  **Климат/Атмосфера:**
    *   `mothlib:climate/hot`, `mothlib:climate/cold`, `mothlib:climate/dry`, `mothlib:climate/wet`, `mothlib:climate/lush`, `mothlib:climate/barren`.
4.  **Ванильные расширения (опционально):**
    *   Добавление модовых биомов в ванильные теги (например, `#minecraft:is_forest`, `#minecraft:is_mountain`), если моды сами этого не делают.

### Реализация DataGen:

1.  В `MothBiomeTagsProvider` создать мапы (или списки) соответствий между `ResourceLocation` биомов модов и нашими `TagKey<Biome>`.
2.  Написать логику генерации `data/mothlib/tags/worldgen/biome/*.json`.
3.  Теги должны генерироваться как `optional`, чтобы при отсутствии мода игра не выдавала ошибок.
4.  Вывод DataGen направить в `common/src/generated/resources` (или `common/src/main/resources`).

## Порядок выполнения

1.  Создание новой git-ветки для масштабных изменений (согласно правилам): `git checkout -b V-multi-loader`.
2.  Реструктуризация папок (`common`, `forge`, `fabric`, `neoforge`, `quilt`).
3.  Настройка `build.gradle` и `settings.gradle`.
4.  Проверка компиляции `common` и `forge`.
5.  Настройка платформенных `Main` классов.
6.  Создание `MothBiomeTags` и `MothBiomeTagsProvider`.
7.  Заполнение списков биомов для 5 модов.
8.  Запуск DataGen и проверка сгенерированных JSON-файлов в `common/src/generated/resources/data/mothlib/tags/worldgen/biome`.
9.  Проверка сборки всех 4-х загрузчиков (Forge, NeoForge, Fabric, Quilt).
10. Коммит изменений.
