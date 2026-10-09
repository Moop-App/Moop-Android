#!/bin/bash
# Check script for PR submission
# Validates dependencies, merged manifest, ProGuard/R8 rules, duplicate and large files, module rules, code formatting, lint.

# Exit immediately if any command fails
set -e

echo "Starting check validations..."
echo ""

# Verify dependency changes
echo "🔍 [1/7] Checking dependency guard..."
./gradlew dependencyGuard
echo "✓ Dependency guard check passed"
echo ""

# Verify merged manifest changes
echo "🔍 [2/7] Checking manifest shield..."
./gradlew manifestShield
echo "✓ Manifest shield check passed"
echo ""

# Verify ProGuard/R8 rule changes
echo "🔍 [3/7] Checking proguard shield..."
./gradlew proguardShieldFast
echo "✓ ProGuard shield check passed"
echo ""

# Verify duplicate and large file changes
echo "🔍 [4/7] Checking highlander..."
./gradlew highlander
echo "✓ Highlander check passed"
echo ""

# Verify module structure rules against the baseline
echo "🔍 [5/7] Checking module rules..."
./gradlew moduleRules
echo "✓ Module rules check passed"
echo ""

# Verify code formatting
echo "🔍 [6/7] Checking code formatting..."
./gradlew spotlessCheck --init-script gradle/init.gradle.kts
echo "✓ Code formatting check passed"
echo ""

# Static analysis and lint checks
echo "🔍 [7/7] Running lint checks..."
./gradlew lintDebug
echo "✓ Lint check passed"
echo ""

echo "✅ All checks passed successfully!"
