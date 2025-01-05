# Amplitude

A Java library for handling ranges, also supports `java.time.*` ranges with temporal units.

[![Build Status](https://github.com/mashimom/amplitude/actions/workflows/ci.yml/badge.svg)](https://github.com/mashimom/amplitude/actions/workflows/ci.yml)

MIT License

![Amplitude library - logo](src/docs/logo.png)

## Features

- Define and manipulate ranges using any comparable type.
- Define and manipulate ranges using `java.time.*` types.
- Operate on ranges as you would with sets, using operations like union, intersection, difference.
- Split a range in two at any internal point.
- Split temporal ranges into smaller uniform ranges.
- Predicates for checking relationships between ranges.

## Getting Started

### Prerequisites

- Java 17

### Installation

Add the following dependency to your `build.gradle` file:

```groovy
dependencies {
	implementation 'org.shimomoto:amplitude:1.0.0'
}
```

Add the following dependency to your `pom.xml` file:

```xml

<dependency>
    <groupId>org.shimomoto</groupId>
    <artifactId>amplitude</artifactId>
    <version>1.0.0</version>
</dependency>
```