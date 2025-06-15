# LogMe 🚀

![Lightweight](https://img.shields.io/badge/Weight-Lightweight-brightgreen)  ![Easy to Use](https://img.shields.io/badge/Usage-Simple-blue)

---
⚡️ LogMe — forget `Log.d()`, live smarter.

If you want to debug like a GOD but hate writing boilerplate — this is your place.

---

## 🤘 What does LogMe do?

- ✅ Automatically logs function calls  
- ✅ Shows parameters and call site  
- ✅ Compile-time only — zero runtime overhead  
- ✅ No reflection — we’re not into that stuff  
- ✅ Kotlin 2.0 compatible — yeah, we live in 2025
- 🍒 The cherry on top? Clickable link to the exact line of code. Boom — you're there.

---

## 🦾 What is LogMe?

It's a Kotlin Compiler Plugin that injects logs straight into your bytecode.  
You annotate with `@LogMe` — it stitches the logs right in.

No frameworks.  
No reflection.  
No pain.

---

## 💣 Example

```kotlin
@LogMe
fun hackTheSystem(login: String, password: String) {
    ...
}
```

Here’s what you get:

```
🚀 LOG_ME:(Hacker.kt:42) hackTheSystem(login="neo", password="1234")
```

Beautiful, right?  
Zero extra lines of code. Just logs — clean and simple.

---

## 🧪 3-Step Setup

### 1. Apply the plugin

```kotlin
plugins {
  id("org.droidbaza.logme") version "1.0.0-alpha03"
}
```

### 2. Copy/past this class

```kotlin
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.CLASS
)
@Retention(AnnotationRetention.SOURCE)
annotation class LogMe(
    val msg: String = "",
    val tag: String = "LOG_ME",
    val showArgs: Boolean = true
)
```

### 3. Annotate everything and enjoy

```kotlin
class MyClass {
    @LogMe(msg = "Important function call")
    fun doWork(param1: Int, param2: String) {
        // ...
    }
}
```

---

## 🧨 Why LogMe?

- Because we hate boilerplate  
- Because the compiler can do your dirty work  
- Because `println("tag")` isn’t logging — it’s despair  
- Because debugging hurts. And LogMe is aspirin.

---

## 📌 Roadmap

- [ ] Execution time logging  
- [ ] Filtering by classes, packages, or conditions  
- [ ] Support for `suspend`, `inline`, and `lambda` functions  
- [ ] Auto-generated log reports for CI/test pipelines  

---

## 🛎 Support & Contribute

Open an issue, suggest a feature, or send a pull request.  
**LogMe** is open source — and it’s waiting for you.

---

© MIT License — use it, tweak it, spread the love, and never forget to log 😎
