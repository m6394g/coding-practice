# Coding Practice

Java 21 solutions and JUnit 5 tests for coding problems.

## Requirements

- Java 21
- No global Maven installation is required.

## Build and test

The checked-in Maven Wrapper downloads the pinned Maven version automatically:

```bash
./mvnw test
```

Run one test class:

```bash
./mvnw -Dtest=DetectCyclesIn2DGridTest test
```

If macOS does not discover Java automatically:

```bash
JAVA_HOME="$HOME/Library/Java/JavaVirtualMachines/corretto-21.0.12/Contents/Home" ./mvnw test
```

## Branch and commit strategy

Branches and commits follow the DSA topic packages (`com.practice.graphs`,
`com.practice.arrays`, …).

**Branch:** `{package}/{problem-kebab-case}`

```text
graphs/detect-cycles-in-2d-grid
arrays/two-sum
```

**Commit message:** topic prefix + what changed

```text
Graphs: add Detect Cycles in 2D Grid solution and tests
chore: add Maven Wrapper and project conventions
```

One problem per branch. Prefer focused commits; do not mix unrelated topics.
See `.cursor/rules/git-branch-commit.mdc` for the full convention.
