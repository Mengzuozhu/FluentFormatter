# Fluent Formatter(IntelliJ插件)

<a href="README.md">English Documentation</a> |[Plugin homepage](https://plugins.jetbrains.com/plugin/15631-fluent-formatter)

![useDemo](https://github.com/Mengzuozhu/FluentFormatter/blob/master/demo/useDemo.gif)

## 功能

- 格式化代码为流式风格
- 一键生成链式调用的`Java builder`代码

## 示例

格式化前：

```java
IntStream.range(1, 2).map(i -> i + 1).count(); 
```

流式风格格式化后：

```java
IntStream.range(1, 2)
         .map(i -> i + 1)
         .count();
```

## 使用

#### Fluent format

1. 选中需格式化的代码
2. 按下快捷键<kbd>Alt</kbd>+<kbd>Enter</kbd>
3. 选择`Fluent format`

#### Fluent build

1. 光标移动到Java builder代码处
2. 按下快捷键<kbd>Alt</kbd>+<kbd>Enter</kbd>
3. 选择`Fluent build`
