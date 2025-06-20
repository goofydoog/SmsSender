[![Work in Progress](https://img.shields.io/badge/status-WIP-orange.svg)](#)


# SMS Sender

SMS Sender is a native Android app written in Kotlin with Jetpack Compose that automates sending reminder text messages to your clients. It’s ideal for anyone who manages a high volume of appointments—barbers, hairstylists, consultants, therapists, you name it—so you can stop worrying about manually firing off SMS reminders. The UI fully adapts to both **light** and **dark** system themes for a seamless user experience at any time of day.

## Key Features & Screens

### 1. Contacts Screen
- Add, edit or delete contacts with first name, last name, and phone number  
- Tap a contact to view or manage their details

### 2. Scheduled SMS Screen
- Schedule up to five reminder messages per client appointment  
- Tap **“Add”** to open an AlertDialog with date & time pickers  
- Select one of your saved contacts, choose up to five send times, then confirm  
- View a list of all scheduled SMS reminders showing the send date/time, recipient’s name and phone number  
- Edit or delete any reminder; deleting will also cancel the background job in WorkManager and remove it from the database so no stray messages are sent

### 3. Message Template (Description) Screen
- Compose the body of your reminder SMS in a multiline TextField  
- Use placeholders `[name]` and `[date]` anywhere in your text—at send time these will be automatically replaced with the client’s actual name and appointment date/time  
- Toggle between **Edit** and **Save** modes; the keyboard automatically appears when you start typing

Under the hood, SMS Sender uses WorkManager to reliably dispatch both single- and multi-part SMS messages (including Unicode), Room for persistent storage of contacts and schedules, and DataStore for modern key-value persistence. All together, it delivers a robust, efficient, and user-friendly reminder system on modern Android devices. (Yes, you are right, AI generated this description)


## Technologies & Libraries Used

<!-- Technology Badges -->
[![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Compose-4285F4?logo=jetpack&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Material 3](https://img.shields.io/badge/Material%203-6200EE?logo=material-io&logoColor=white)](https://m3.material.io/)
[![Dark/Light Theme](https://img.shields.io/badge/Theme-Dark%20%7C%20Light-00BCD4)](#)
[![Dagger Hilt](https://img.shields.io/badge/Hilt-009688?logo=dagger&logoColor=white)](https://dagger.dev/hilt/)
[![Android Arch Components](https://img.shields.io/badge/Arch%20Components-3DDC84?logo=android&logoColor=white)](https://developer.android.com/topic/libraries/architecture)
[![ViewModel](https://img.shields.io/badge/ViewModel-FF6F00?logo=android&logoColor=white)](#)
[![DataStore](https://img.shields.io/badge/DataStore-03A9F4?logo=android&logoColor=white)](#)
[![Room](https://img.shields.io/badge/Room-795548?logo=sqlite&logoColor=white)](https://developer.android.com/training/data-storage/room)
[![WorkManager](https://img.shields.io/badge/WorkManager-2196F3?logo=android&logoColor=white)](https://developer.android.com/topic/libraries/architecture/workmanager)
[![Coroutines](https://img.shields.io/badge/Coroutines-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org/docs/coroutines-overview.html)
[![SmsManager](https://img.shields.io/badge/SmsManager-607D8B?logo=android&logoColor=white)](#)
[![Date%20%26%20Time%20Picker](https://img.shields.io/badge/Picker-Date%20%26%20Time-FF9800?logo=android&logoColor=white)](#)




<p align="center">
  <img src="https://github.com/user-attachments/assets/ed1b900b-5361-4625-b7e4-23195346022a" width="300"/>
  <img src="https://github.com/user-attachments/assets/a468842f-3345-4deb-954d-2b16da02260d" width="300"/>
</p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/7dd2e618-69ee-43d6-86d4-81d6f428bc4f" width="300"/>
  <img src="https://github.com/user-attachments/assets/8b1dd0e8-9dd8-4a11-88e3-ebfb51a8fba7" width="300"/>
</p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/1c218f18-c2be-4990-bfa9-b0b420e989a9" width="300"/>
  <img src="https://github.com/user-attachments/assets/d2ee540e-5e76-4a46-9e35-357839381e25" width="300"/>
</p>
