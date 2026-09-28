#!/bin/bash
# Check script for PR submission
# Validates dependencies, merged manifest, ProGuard/R8 rules, module rules, code formatting, lint.

# Exit immediately if any command fails
set -e

echo "Starting check validations..."
echo ""

# Verify dependency changes
echo "🔍 [1/6] Checking dependency guard..."
./gradlew dependencyGuard
echo "✓ Dependency guard check passed"
echo ""

# Verify merged manifest changes
echo "🔍 [2/6] Checking manifest shield..."
./gradlew manifestShield
echo "✓ Manifest shield check passed"
echo ""

# Verify ProGuard/R8 rule changes
echo "🔍 [3/6] Checking proguard shield..."
./gradlew proguardShieldFast
echo "✓ ProGuard shield check passed"
echo ""

# Verify module structure rules against the baseline
echo "🔍 [4/6] Checking module rules..."
./gradlew moduleRules
echo "✓ Module rules check passed"
echo ""

# Verify code formatting
echo "🔍 [5/6] Checking code formatting..."
./gradlew spotlessCheck --init-script gradle/init.gradle.kts
echo "✓ Code formatting check passed"
echo ""

# Static analysis and lint checks
echo "🔍 [6/6] Running lint checks..."
./gradlew lintDebug
echo "✓ Lint check passed"
echo ""

echo "✅ All checks passed successfully!"
