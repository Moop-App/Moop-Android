#!/bin/bash
# Check script for PR submission
# Validates dependencies, code formatting, lint.

# Exit immediately if any command fails
set -e

echo "Starting check validations..."
echo ""

# Verify dependency changes
echo "🔍 [1/3] Checking dependency guard..."
./gradlew dependencyGuard
echo "✓ Dependency guard check passed"
echo ""

# Verify code formatting
echo "🔍 [2/3] Checking code formatting..."
./gradlew spotlessCheck --init-script gradle/init.gradle.kts
echo "✓ Code formatting check passed"
echo ""

# Static analysis and lint checks
echo "🔍 [3/3] Running lint checks..."
./gradlew lintDebug
echo "✓ Lint check passed"
echo ""

echo "✅ All checks passed successfully!"
