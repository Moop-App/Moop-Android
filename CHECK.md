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

- moduleRules
```bash
./gradlew moduleRules
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

- moduleRules
```bash
./gradlew moduleRulesBaseline
```

- spotless
```bash
./gradlew spotlessApply --init-script gradle/init.gradle.kts
```
