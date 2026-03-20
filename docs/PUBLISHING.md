# Publishing (CurseForge + Modrinth)

CI uses **[mc-publish](https://github.com/Kir-Antipov/mc-publish)** (`Kir-Antipov/mc-publish@v3.3`).

## What you need once

### 1. Projects on sites

- **CurseForge**: create a project for MothLib (type: *Mods* / Minecraft), note the numeric **Project ID** (in the project URL or dashboard).
- **Modrinth**: create a project, note **Project ID** (Settings → General; can be slug or UUID depending on UI — mc-publish accepts Modrinth project id string).

### 2. API tokens (GitHub Secrets)

In the GitHub repo: **Settings → Secrets and variables → Actions → New repository secret**

| Secret | Where to get it |
|--------|------------------|
| `MODRINTH_TOKEN` | [Modrinth → Settings → API](https://modrinth.com/settings) — create token with **Create versions** (and read if asked). |
| `CURSEFORGE_TOKEN` | [CurseForge → Authors → API](https://console.curseforge.com/) — generate an API token for uploads. |

### 3. Project IDs (GitHub Variables)

**Settings → Secrets and variables → Actions → Variables**

| Variable | Example | Notes |
|----------|---------|--------|
| `CURSEFORGE_PROJECT_ID` | `123456` | Numeric CurseForge project id only. |
| `MODRINTH_PROJECT_ID` | `mothlib` or UUID | Value Modrinth shows as project id for API. |

Until these variables and secrets are set, the publish workflow will fail at the upload step.

## How a release works

1. Bump **`mod_version`** in `gradle.properties` **or** rely on the tag / workflow input (see below).
2. **Recommended**: create a [GitHub Release](https://docs.github.com/en/repositories/releasing-projects-on-github/managing-releases-in-a-repository) with a tag, e.g. `v0.0.2`, and publish it.  
   - Workflow **`publish.yml`** runs on `release: types: [published]`.  
   - It strips a leading `v` from the tag and passes **`-Pmod_version=…`** to Gradle so the built JAR matches the tag (overrides `gradle.properties` for that build).
3. **Manual**: **Actions → Publish → Run workflow** and enter **version** (no `v`, e.g. `0.0.2`). Same `-Pmod_version` is applied.

Published artifact: **`build/libs/mothlib-<version>.jar`** (main mod only; **testmod** is not packaged there).

## Game / loader metadata

Workflow pins **Minecraft `1.20.1`** and loader **forge** for both platforms. Change `.github/workflows/publish.yml` if you port the mod.

## CI (no upload)

**`.github/workflows/ci.yml`** runs `./gradlew test build` on pushes/PRs to `main` or `master` — no secrets required.
