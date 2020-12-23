# Fluent Formatter

<a href="README-CH.md">中文文档</a> |[Plugin homepage](https://plugins.jetbrains.com/plugin/15631-fluent-formatter)

Formats the Java code in a fluent style, and generates chained methods for the Java builder.

## **Features**

● Wrap the invocation of each chained method on a separate line

● Generate the all chained methods for the builder

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

#### Fluent format

Select the code -> press **Alt+Enter** -> select **Fluent format**



#### Fluent build

Cursor at the code of Java builder -> press **Alt+Enter** -> select **Fluent build**