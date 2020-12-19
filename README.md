# Fluent Formatter(IDEA plugin)

<a href="README-CH.md">中文文档</a>

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
2. Press shortcut key <kbd>Alt</kbd>+<kbd>Enter</kbd>
3. Select `Fluent format`
