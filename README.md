# LogMe Plugin

![Lightweight](https://img.shields.io/badge/Weight-Lightweight-brightgreen)
![Easy to Use](https://img.shields.io/badge/Usage-Simple-blue)

---

## Description

`LogMe` is a lightweight Kotlin IR plugin for automatic generation of function call logs with parameters.

---

## Benefits

- **Lightweight**: minimal jar size, low impact on build time.
- **Easy integration**: just one `.jar` and an annotation.
- **Automatic parameter logging** — no need to write logs manually.
- **Call site navigation** — outputs the file and line number where the function is called.

---

## How to connect

1. Add `logme.jar` to your project (recommended only for debug builds, do not include in release builds).

2. In your `build.gradle.kts` or `build.gradle`, add the Kotlin Compiler Plugin with your `.jar` file:

```kotlin
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    if (name.contains("Debug")) {
        kotlinOptions {
            freeCompilerArgs += listOf(
                "-Xplugin=${project.rootDir}/app/libs/logme.jar"
            )
        }
    }
} 
```

## How to Use

1. Create the annotation:

```kotlin
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.CLASS
)
@Retention(AnnotationRetention.SOURCE)
annotation class LogMe(
    val msg: String = "",        // additional message
    val tag: String = "LOG_ME",  // specific log tag
    val showArgs: Boolean = true // whether to log function parameter values
)
```

2. Annotate the functions or classes you want to log:
```kotlin
class MyClass {
    @LogMe(msg = "Important function call")
    fun doWork(param1: Int, param2: String) {
        // ...
    }
}
```

3.During compilation, the plugin automatically inserts a println call at the beginning of the function body, which outputs to the console:
```kotlin
LOG_ME: Called MyClass.doWork (MyClass.kt:25) params: Important function call param1=42, param2=hello)
```

## Note
- The plugin uses the Kotlin IR API.
- It is recommended to enable the plugin only for debug builds to avoid affecting performance and release apk/jar size.
- Logs are printed to the console using println

