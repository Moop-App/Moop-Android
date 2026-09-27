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

- manifestShield
```bash
./gradlew manifestShield
```

- proguardShield
```bash
./gradlew proguardShieldFast
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

- manifestShield
```bash
./gradlew manifestShieldBaseline
```

- proguardShield
```bash
./gradlew proguardShieldFastBaseline
```

- spotless
```bash
./gradlew spotlessApply --init-script gradle/init.gradle.kts
```
