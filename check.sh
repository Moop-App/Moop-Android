#!/bin/bash
# Check script for PR submission
# Validates dependencies, merged manifest, code formatting, lint.

# Exit immediately if any command fails
set -e

echo "Starting check validations..."
echo ""

# Verify dependency changes
echo "🔍 [1/4] Checking dependency guard..."
./gradlew dependencyGuard
echo "✓ Dependency guard check passed"
echo ""

# Verify merged manifest changes
echo "🔍 [2/4] Checking manifest shield..."
./gradlew manifestShield
echo "✓ Manifest shield check passed"
echo ""

# Verify code formatting
echo "🔍 [3/4] Checking code formatting..."
./gradlew spotlessCheck --init-script gradle/init.gradle.kts
echo "✓ Code formatting check passed"
echo ""

# Static analysis and lint checks
echo "🔍 [4/4] Running lint checks..."
./gradlew lintDebug
echo "✓ Lint check passed"
echo ""

echo "✅ All checks passed successfully!"
