# Fluent Formatter

<a href="README-CH.md">中文文档</a> |[Plugin homepage](https://plugins.jetbrains.com/plugin/15631-fluent-formatter)

Formats the selected Java code in a `fluent style`. It will wrap the invocation of each chained method on a separate line.

## Example
Before:
```java
IntStream.range(1, 2).map(i -> i + 1).count(); 
```

After formatted:

```java
IntStream.range(1, 2)
         .map(i -> i + 1)
         .count();
```

## Usage

1. Select the code
2. Press shortcut key <kbd>Alt</kbd>+<kbd>Enter</kbd>
3. Select `Fluent format`
