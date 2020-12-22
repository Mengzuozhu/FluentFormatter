# Fluent Formatter(IntelliJ插件)

<a href="README.md">English Documentation</a>  

使用该插件，格式化代码为流式风格。

## 示例
格式化前：

```java
IntStream.range(1, 2).map(i -> i + 1).count(); 
```

格式化后：

```java
IntStream.range(1, 2)
         .map(i -> i + 1)
         .count();
```

## 使用

1. 选中需格式化的代码
2. 按下快捷键<kbd>Alt</kbd>+<kbd>Enter</kbd>
3. 选择`Fluent format`

