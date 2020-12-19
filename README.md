# Fluent Formatter

<a href="README-EN.md">中文文档</a>

Format the code in a fluent style.

## Example

```java
IntStream.range(1, 2).map(i -> i + 1).count(); 
```

After formatted --->

```java
IntStream.range(1, 2)
         .map(i -> i + 1)
         .count();
```

## Step

1. Select the code
2. Use shortcut keys `Alt+Enter`
3. Select `Fluent format`