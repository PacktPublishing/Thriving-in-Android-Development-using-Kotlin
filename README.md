# Thriving in Android Development Using Kotlin

<a href="[Packt UTM URL of the Book](https://www.packtpub.com/en-in/product/thriving-in-android-development-using-kotlin-9781837631292)"><img src="https://content.packt.com/_/image/original/B19443/cover_image_large.jpg" alt="Book Name" height="256px" align="right"></a>

This is the code repository for [Thriving in Android Development Using Kotlin](https://www.packtpub.com/en-in/product/thriving-in-android-development-using-kotlin-9781837631292), published by Packt.

**Use the newest features of the Android framework to develop production-grade apps**

## What is this book about?
This book will teach you how to build robust and production-ready apps using Jetpack Compose, Room, DataStore, CameraX, ExoPlayer, and more, as well as help you solve common problems that you'll face while creating real-world apps.

This book covers the following exciting features:
* Create complex UIs with Jetpack Compose
* Structure and modularize apps with a focus on further scaling
* Connect your app to synchronous and asynchronous remote sources
* Store and cache information and manage the lifecycle of this data
* Execute periodic tasks using WorkManager
* Capture and edit photos and videos using CameraX
* Authenticate your users securely
* Play videos in the foreground and background and cast them to other devices

If you feel this book is for you, get your [copy](https://www.amazon.com/Thriving-Android-Development-using-Kotlin/dp/1837631298) today!

<a href="https://www.packtpub.com/?utm_source=github&utm_medium=banner&utm_campaign=GitHubBanner"><img src="https://raw.githubusercontent.com/PacktPublishing/GitHub/master/GitHub.png" 
alt="https://www.packtpub.com/" border="5" /></a>


## Instructions and Navigations
All of the code is organized into folders. For example, Chapter-1/WhatsPackt

The code will look like the following:
```
class MessagesRepository @Inject constructor(
    private val dataSource: MessagesSocketDataSource
): IMessagesRepository {

    override suspend fun getMessages(): Flow<Message> {
        return dataSource.connect()
    }
```

**Following is what you need for this book:**
If you're a mid-level Android engineer, this book is for you as it will not only teach you how to solve issues that occur in real-world apps but also benefit you in your day-to-day work. This book will also help junior engineers who want to get exposed to complex problems and explore best practices to solve them. A basic understanding of Android and Kotlin concepts such as views, activities, lifecycle, and Kotlin coroutines will be useful to get the most out of this book.

With the following software and hardware list you can run all code files present in the book (Chapter 1-9).

### Software and Hardware List

| Chapter  | Software required                   | OS required                        |
| -------- | ------------------------------------| -----------------------------------|
| 1-9      | Android Studio Jellyfish 2023.3.1 | Windows, macOS, or Linux           |


### Related products
* How to Build Android Apps with Kotlin - Second Edition [[Packt]](https://www.packtpub.com/en-us/product/how-to-build-android-apps-with-kotlin-9781837634934) [[Amazon]](https://www.amazon.com/How-Build-Android-Apps-Kotlin/dp/1837634939)

* Kickstart Modern Android Development with Jetpack and Kotlin [[Packt]](https://www.packtpub.com/en-us/product/kickstart-modern-android-development-with-jetpack-and-kotlin-9781801811071) [[Amazon]](https://www.amazon.com/Kickstart-Modern-Android-Development-Jetpack/dp/1801811075)

## Get to Know the Author
**Gema Socorro Rodríguez** is a Google Developer Expert for Android with over 15 years experience. After finishing her studies building a mobile project, in 2009, she started working on more mobile apps and fell in love with Android. Since then, she has been working on mobile apps as part of several teams.
As her experience grew, she realised that she wanted to share with the community what she was learning, hence she started giving talks and organising workshops. She has also been an instructor in a mobile specialised bootcamp. She is currently working as a Senior Android Engineer in Cabify, a popular ride hailing company with presence in Spain and the majority of the Latin American countries.
