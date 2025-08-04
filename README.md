![Banner](https://github.com/kotlang-dev/AgeCalculator-TrackDates/blob/main/.github/assets/banner.png?raw=true)

<div align="center">
  
# Age Calculator - Track Dates

</div>

A modern, offline-first app designed to track the age of important life events. This project showcases modern, multi-platform development best practices - including a clean, shared architecture, a reactive and adaptive UI, and a fully automated CI/CD pipeline for desktop releases—all built with **Kotlin Multiplatform** and **Compose Multiplatform**.

## Download

<p align="center">
  <a href="https://play.google.com/store/apps/details?id=com.synac.agecalculator">
    <img alt="Get it on Google Play" src="https://github.com/kotlang-dev/AgeCalculator-TrackDates/blob/main/.github/assets/button-android-download.png?raw=true" height="100">
  </a>
  &nbsp;
  &nbsp;
  &nbsp;
  <a href="https://github.com/kotlang-dev/AgeCalculator-TrackDates/releases/latest">
    <img alt="Latest Release" src="https://github.com/kotlang-dev/AgeCalculator-TrackDates/blob/main/.github/assets/button-desktop-download.png?raw=true" height="100">
  </a>
</p>

## ✨ Features

* **💻 Multiplatform Support:** A single, shared codebase for Android, Windows, macOS, and Linux.
* **📱 Adaptive UI:** The interface intelligently adapts to different screen sizes, providing a great experience on phones, tablets, and desktops.
* **📅 Track Key Life Events:** Add and manage multiple occasions like birthdays, anniversaries, and milestones.
* **⏱️ Detailed Age Breakdown:** Instantly see the elapsed time in years, months, days, and even down to the second.
* **🎨 Dynamic Theming:** Personalize your experience by switching between Light, Dark, or System themes.
* **🔄 Seamless In-App Updates (Android):** The app checks for new versions and allows you to update directly from the Play Store.
* **🔒 100% Offline & Private:** All your data is stored securely on your device and is never collected or transmitted.

---


## 📸 Screenshots

<div align="center">
  
### Android

</div>

<p align="center">
  <img src="https://github.com/kotlang-dev/AgeCalculator-TrackDates/blob/main/.github/assets/screenshot-android-1.png?raw=true" width="25%%">
  &nbsp;
  &nbsp;
  <img src="https://github.com/kotlang-dev/AgeCalculator-TrackDates/blob/main/.github/assets/screenshot-android-2.png?raw=true" width="25%%">
  &nbsp;
  &nbsp;
  <img src="https://github.com/kotlang-dev/AgeCalculator-TrackDates/blob/main/.github/assets/screenshot-android-4.png?raw=true" width="25%%">
</p>

<div align="center">
  
### Desktop

</div>

<p align="center">
  <img src="https://github.com/kotlang-dev/AgeCalculator-TrackDates/blob/main/.github/assets/screenshot-desktop.png?raw=true" width="80%">
</p>

---

## 🏗️ Architecture & Tech Stack

This project follows the principles of **Clean Architecture**, separating concerns into distinct layers (Data, Domain, and Presentation) to create a scalable and maintainable codebase. The `shared` module contains all business logic and UI, which is then consumed by platform-specific application modules.

| **Category** | **Technologies & Libraries** |
| :--- | :--- |
| **Core** | Kotlin Multiplatform, Kotlin Coroutines, Flow |
| **UI (Shared)** | Compose Multiplatform, Material 3, Compose Navigation, Adaptive Layouts |
| **Architecture** | MVVM with a Coordinator ViewModel, MVI-like UDF, Use Cases |
| **Dependency Injection** | Koin |
| **Data Persistence** | Room (Multiplatform), Preference DataStore |
| **Date & Time** | Kotlinx DateTime |
| **CI/CD** | GitHub Actions (for automated desktop releases) |

---

## Connect with the Author

**Mohammad Arif**

-   **GitHub:** [@kotlang-dev](https://github.com/kotlang-dev)
-   **YouTube:** [Mohammad Arif](https://www.youtube.com/@kotlang)
-   **Udemy Courses:**
    -   [Android Quiz App: Ktor Backend & Jetpack Compose](https://www.udemy.com/course/quiztime/?referralCode=D1F5E08155303110B7A4)
    -   [Android Fitness App: Firebase & Jetpack Compose](https://www.udemy.com/course/measuremate/?referralCode=B3DE352F96BC3C3E9E80)

<a href="https://ko-fi.com/mohammadarif" target="_blank"><img src="https://www.buymeacoffee.com/assets/img/custom_images/orange_img.png" alt="Buy Me A Coffee" style="height: 41px !important;width: 174px" ></a>
