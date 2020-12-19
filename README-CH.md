# Fluent Formatter

<a href="README.md">English Documentation</a>  

格式化代码为流式风格

## 示例

```java
IntStream.range(1, 2).map(i -> i + 1).count(); 
```

After formatted --->

```java
IntStream.range(1, 2)
         .map(i -> i + 1)
         .count();
```

## 使用

1. 选中需格式化的代码
2. 使用快捷键Alt+Enter
3. 选择Fluent format
