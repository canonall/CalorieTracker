<h1 align="center">CalorieTracker</h1></br>
<p align="center">  
A calorie tracker app based on Philipp Lackner's <a href="https://pl-coding.com/multi-module-course"> multi module course</a>. With this app you can search for any type of food you desire, whether it's breakfast, lunch, dinner or a snack. Once you have found the food you want, you can add it to your meal section, which helps you keep track of what you're eating throughout the day. You can find the detailed tech stack and architecture in the below sections.
</p>
</br>

<p align="center">
  <a href="https://android-arsenal.com/api?level=24"><img alt="API" src="https://img.shields.io/badge/API-24%2B-darkgreen"/></a>
  <a href="https://github.com/canonall"><img alt="Profile" src="https://img.shields.io/badge/git-canonall-darkgreen"/></a> 
</p>


## Screeshots :camera_flash:

<p align="center">
<img src="preview/calorieTracker5.png" width="20%"/>
<img src="preview/calorieTracker2.png" width="20%"/>
<img src="preview/calorieTracker1.png" width="20%"/>
<img src="preview/calorieTracker3.png" width="20%"/>
</p>

## Tech stack & Libraries :books:

- [Jetpack Compose](https://developer.android.com/jetpack/compose) - is the modern toolkit recommended by Android for building native user UI. It streamlines and speeds up the development process by allowing for the creation of powerful and intuitive UI with less code and easy-to-use Kotlin APIs.
- [Compose Navigation](https://developer.android.com/jetpack/compose/navigation) - Navigation between composables
- [Android Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - Dependency Injection Library for Android
- [Coroutines](https://developer.android.com/kotlin/coroutines) - is a concurrency design pattern that you can use on Android to simplify code that executes asynchronously.
- [Flow](https://developer.android.com/kotlin/flow) - is a type that can emit multiple values sequentially. They are built on top of coroutines and can provide multiple values.
  - [Channels](https://kotlinlang.org/docs/channels.html)- are a good fit for communication between different coroutines, or any use case that requires a producer-consumer approach.
- [Coil Compose](https://coil-kt.github.io/coil/compose/) - is a library for loading images on Android, utilizing Kotlin Coroutines for efficient and responsive performance.
