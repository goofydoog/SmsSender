package com.example.smsSender

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.smsSender.ui.screens.ContactsScreen
import com.example.smsSender.ui.screens.DescriptionScreen
import com.example.smsSender.ui.screens.ListOfPlannedScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    navController: NavHostController,
    visitsViewModel: VisitsViewModel,
    descriptionViewModel: DescriptionViewModel,
    contactsViewModel: ContactsViewModel,
    modifier: Modifier = Modifier
) {


    Scaffold(
        topBar = {
            TopAppBar(colors = topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ), title = {
                Text("SMS Sender", style = MaterialTheme.typography.displayLarge)
            })
        },
        bottomBar = {
            BottomAppBar(modifier = Modifier.clip(RoundedCornerShape(16.dp)),
                actions = {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                    ) {
                        // przycisk odpowiadajacy za sekcje Accounts
                        IconButton(
                            onClick = { navController.navigate("contacts") },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Default.Group,
                                contentDescription = "list of contacts",
                                modifier = Modifier.size(45.dp)
                            )
                        }

                        VerticalDivider()

                        // przycisk odpowiadajacy za sekcje List of planned visits
                        IconButton(
                            onClick = { navController.navigate("list_of_planned") },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.FormatListBulleted,
                                contentDescription = "List of planned visits",
                                modifier = Modifier.size(45.dp)
                            )
                        }

                        VerticalDivider()

                        // przycisk odpowiadajacy za sekcje description
                        IconButton(
                            onClick = { navController.navigate("description") },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.MenuBook,
                                contentDescription = "text description",
                                modifier = Modifier.size(45.dp)
                            )
                        }
                    }

                }
            )
        }
    ) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = "contacts"
            ) {
                composable("contacts") { ContactsScreen(contactsViewModel) }
                composable("list_of_planned") { ListOfPlannedScreen(visitsViewModel, contactsViewModel, descriptionViewModel) }
                composable("description") { DescriptionScreen(descriptionViewModel) }
            }
        }
    }
}

