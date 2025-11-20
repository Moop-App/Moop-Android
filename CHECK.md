# Check before PR

### Run all checks at once:

```bash
./check.sh
```

### Run checks individually

- dependencyGuard
```bash
./gradlew dependencyGuard
```

- spotless
```bash
./gradlew spotlessCheck --init-script gradle/init.gradle.kts
```

- lint
```bash
./gradlew lintDebug
```

## Update baselines

- dependencyGuard
```bash
./gradlew dependencyGuardBaseline
```

- spotless
```bash
./gradlew spotlessApply --init-script gradle/init.gradle.kts
```
