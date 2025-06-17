package com.example.smsSender

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.activity.viewModels
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.smsSender.room.AppDatabase
import com.example.smsSender.smsWorker.SmsWorker
import com.example.smsSender.ui.theme.SmsSenderTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SmsSenderTheme {
                val navController = rememberNavController()

                val visitsViewModel: VisitsViewModel   = hiltViewModel()
                val contactsViewModel: ContactsViewModel = hiltViewModel()
                val descriptionViewModel: DescriptionViewModel = hiltViewModel()

                MainScaffold(
                    navController = navController,
                    visitsViewModel = visitsViewModel,
                    descriptionViewModel = descriptionViewModel,
                    contactsViewModel = contactsViewModel
                )
//                val context = LocalContext.current
//                // 2. LifecycleOwner (czyli zazwyczaj Twoja Activity)
//                val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
//                // 3. Jeśli potrzebujesz castować na Activity:
//                val activity = context as? Activity
//                    ?: throw IllegalStateException("Composable nie jest w Activity")
//
//                Row(
//                    modifier = Modifier.fillMaxSize(),
//                    horizontalArrangement = Arrangement.Center,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//
//                    SendSmsButton(onClick = {
//                        sendSms(
//                            context = context,
//                        )
//                    }, modifier = Modifier.weight(1f))
//
//                    SeeAllTasksButton(
//                        onClick = {
//                            seeAllTasks(
//                                context = context,
//                                lifecycleOwner = lifecycleOwner
//                            )
//                        }, modifier = Modifier.weight(1f)
//                    )
//
//
//                    DeclineTasksButton(onClick = {
//                        declineTask(
//                            context = context
//                        )
//                    }, modifier = Modifier.weight(1f))
            }
        }
    }
}


@Composable
fun SendSmsButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(onClick = onClick, modifier = modifier) {
        Text("Sens sms to sonia")
    }
}

@Composable
fun SeeAllTasksButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(onClick = onClick, modifier = modifier) {
        Text("See all tasks in logCat")
    }
}

@Composable
fun DeclineTasksButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(onClick = onClick, modifier = modifier) {
        Text("Decline task")
    }
}

fun declineTask(context: Context) {
    WorkManager.getInstance(context).cancelAllWork()
}

fun seeAllTasks(context: Context, lifecycleOwner: LifecycleOwner) {
    WorkManager
        .getInstance(context)
        .getWorkInfosByTagLiveData("sms_task")
        .apply { println() }

        .observe(lifecycleOwner) { workInfos ->
            // tu dostaniesz List<WorkInfo> za każdym razem, gdy coś się zmieni
            workInfos.forEach { wi ->
                println("ID=${wi.id} stan=${wi.state}")
            }
        }
}


