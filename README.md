# MothLib

Forge **1.20.1** library mod (Java 17). See **[docs/MOTHLIB.md](docs/MOTHLIB.md)** for API overview and how to depend on it.

- **CI**: GitHub Actions — build + tests on push/PR.
- **Releases**: GitHub Release or manual workflow → upload to **Modrinth** + **CurseForge** (configure secrets/variables per **[docs/PUBLISHING.md](docs/PUBLISHING.md)**).

```bash
./gradlew build
./gradlew test
```

JDK 17 for Gradle: this repo’s `gradlew` prefers `./.jdks/jdk-17` if present; use `tools/fetch-jdk17.sh` once on a new machine.
