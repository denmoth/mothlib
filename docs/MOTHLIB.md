# MothLib — documentation

Library mod for **Minecraft 1.20.1 only** (`mods.toml` range `[1.20.1,1.20.2)`) + **Forge 47.x** (Java **17**). Other mods add it as a **jar dependency** (implementation / compileOnly + runtime copy), not as a Gradle library from Maven Central unless you publish it yourself.

---

## Contents

1. [What it provides](#what-it-provides)
2. [Project layout](#project-layout)
3. [Installation for dependent mods](#installation-for-dependent-mods)
4. [CurseForge vs CurseMaven](#curseforge-vs-cursemaven)
5. [Usage overview](#usage-overview)
6. [Configuration & structure placement](#configuration--structure-placement)
7. [Data generation](#data-generation)
8. [Building this repo](#building-this-repo)

---

## What it provides

| Area | Purpose |
|------|---------|
| **Config** | `MothConfig` (Forge common config) + `MothConfigRegistry` — string ids map to spacing/separation/spread for structure placement. |
| **Worldgen** | `ConfigurableStructurePlacement` — `StructurePlacementType` that reads overrides from `MothConfigRegistry`. Builders: `MothPoolBuilder`, `MothStructureBuilder`, `MothStructureSetBuilder`, `MothProcessorBuilder`, `MothJigsawHelper`. |
| **Registration** | `RegHelper` — deferred registers for blocks/items/sounds/entities/structure types; block + `BlockItem` helper. |
| **Creative tabs** | `TabManager` / `MothTab`. |
| **Networking** | `MothNetwork` + `IMothPacket` — `SimpleChannel` wrapper. |
| **Datagen** | `MothWorldGenProvider` (empty `RegistrySetBuilder` hook), plus reusable providers/helpers (`MothRecipeProvider`, tags, loot, language, `MothAdvancementBuilder`, `MothStructureProvider`, …). |

Constants for defaults and config keys live on **`MothConfig`** (`DEFAULT_OVERWORLD_SPACING`, `CONFIG_ID_OVERWORLD`, …).

---

## Project layout

- **`src/main`** — shipped **library** (`mothlib` mod id). This is what dependents use.
- **`src/testmod`** — internal **example / test** mod (`mothlib_test`). Not included in the main `jar` task; used for `runClient` / `runData` in this workspace only.

---

## Installation for dependent mods

You need the **reobfuscated** mod jar (what players run), not only sources on classpath.

### Option A — Local jar (simplest)

1. In MothLib: `./gradlew build` → take `build/libs/mothlib-<version>.jar`.
2. In your mod project, copy into e.g. `libs/` and:

```gradle
dependencies {
    implementation fg.deobf(files('libs/mothlib-0.0.1.jar'))
}
```

Use your real file name. **`fg.deobf(...)`** remaps the jar to your mappings; required for a Forge mod depending on another Forge mod jar.

### Option B — Composite build (monorepo / sibling folder)

Include MothLib as an included build and depend on its outputs (Gradle composite builds). Good when you develop library and game mod side by side; no upload required.

### Option C — Your own Maven

Run `maven-publish` (you’d add the plugin) and host on GitHub Packages, self-hosted Nexus, etc. Dependents add `maven { url '...' }` and `implementation fg.deobf("com.denmoth:mothlib:...")` as you configure.

### Option D — CurseMaven (after file exists on CurseForge)

See [next section](#curseforge-vs-cursemaven). You need a **published file** on CurseForge to resolve via Cursemaven.

**Runtime for players:** list **`mothlib`** as a dependency in your `mods.toml` (or ship it in your modpack). The library must be present in the instance `mods` folder like any other mod.

---

## CurseForge vs CurseMaven

| Question | Answer |
|----------|--------|
| **Must the library be on CurseForge?** | **No**, unless you want discoverability or Cursemaven-style resolution. Local jar, composite build, or private Maven work fine. |
| **What is CurseMaven?** | Unofficial resolver: **`https://cursemaven.com`** maps CurseForge **project + file** IDs to Maven coordinates. |
| **Do I upload MothLib to CurseForge?** | Only if you want it listed as a separate project and/or want dependents to use Cursemaven without hosting jars yourself. |

**CurseMaven Gradle example** (after you know **project ID** and **file ID** from the CurseForge file page):

```gradle
repositories {
    maven {
        url = "https://cursemaven.com"
        content { includeGroup "curse.maven" }
    }
}

dependencies {
    // Pattern: curse.maven:<project-slug>-<PROJECT_ID>:<FILE_ID>
    implementation fg.deobf("curse.maven:mothlib-<PROJECT_ID>:<FILE_ID>")
}
```

Replace `<PROJECT_ID>` / `<FILE_ID>` with numbers from CurseForge. The slug before the first `-` is conventional (often mod id); what matters for resolution is the **numeric IDs**.

**Modrinth:** similar idea exists via Modrinth’s Maven endpoint; same story — upload first, then depend by coordinate.

---

## Usage overview

### Registering structure placement type

MothLib already registers **`mothlib:configurable`** in `MothPlacements`. Dependent mods only need to depend on the jar and use the codec / JSON that references that type.

### Using config-driven spacing

1. In your mod, call **`MothConfigRegistry.register(...)`** with your own id (or rely on MothLib’s keys if defaults fit).
2. In datagen (or JSON), use **`ConfigurableStructurePlacement`** with matching **`config_id`** (see `MothConfig.CONFIG_ID_OVERWORLD` for the built-in overworld profile).

### Worldgen bootstrap

Same as vanilla/Forge: `RegistrySetBuilder` + `DatapackBuiltinEntriesProvider`, with bootstrap methods that call **`MothPoolBuilder`**, **`MothStructureBuilder`**, etc. See **`TestWorldGen`** in `src/testmod` for a full chain (processors → pool → structure → structure set).

### RegHelper / tabs / network

Instantiate **`RegHelper`** / **`TabManager`** with **your** mod id, register on your mod event bus. **`MothNetwork`**: `new MothNetwork(modId, protocolVersion)` or pass a custom channel segment via the 3-arg constructor; default channel path is **`MothNetwork.DEFAULT_CHANNEL`** (`"main"`).

---

## Configuration & structure placement

- **Forge config file** is defined by `MothConfig` (overworld/nether spacing & separation defaults).
- **`MothConfigRegistry`** ties **string ids** (e.g. `mothlib:overworld`) to live **`Supplier`s** so `ConfigurableStructurePlacement` can override spacing/separation (and spread type string) at runtime.
- **Generated datapack JSON** should use the **same default spacing/separation** as the config defaults if you want files to match behavior when config is unchanged (see how **testmod** uses `MothConfig.DEFAULT_*`).

---

## Data generation

- Library’s **`MothDataGen`** registers **`MothWorldGenProvider`** with an empty **`RegistrySetBuilder`** — extend that pattern in **your** mod by adding your own `RegistrySetBuilder` entries (as **testmod** does in `TestDatagen`).
- Other providers under `com.denmoth.mothlib.datagen` are helpers; wire them in **`GatherDataEvent`** when you need them.

**JDK:** ForgeGradle 6 + Gradle 8 need a **JDK 17** to run Gradle. This repo’s **`gradlew`** prefers **`./.jdks/jdk-17`** if present; run **`tools/fetch-jdk17.sh`** once on a new machine.

---

## Building this repo

```bash
./gradlew build
```

Outputs:

- **`build/libs/mothlib-<version>.jar`** — the artifact to depend on or ship.

Testmod compiles with `compileTestmodJava`; it is not packaged into that main jar.

---

## Automated publishing (CurseForge / Modrinth)

See **[PUBLISHING.md](PUBLISHING.md)** for GitHub Secrets, Variables, and release flow.

---

## Version alignment

Single source for versions in **`gradle.properties`**: `minecraft_version`, `forge_version`, `mod_version`, `minecraft_version_range_upper` (exclusive end for `minecraft` in `mods.toml`, currently **1.20.2** so only **1.20.1** loads). Dependent mods should match that line unless you port MothLib.
