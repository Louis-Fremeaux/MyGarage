package tech.fremeaux.mygarage

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Layout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Garage
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import tech.fremeaux.mygarage.ui.theme.MyGarageTheme
import tech.fremeaux.mygarage.ui.view.CatalogScreen
import tech.fremeaux.mygarage.ui.view.GarageScreen
import tech.fremeaux.mygarage.ui.view.SettingsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyGarageTheme {
                MyGarageApp()
            }
        }
    }
}

enum class AppDestinations(val label: String, val icon: ImageVector) {
    MYGARAGE("MyGarage", Icons.Default.Garage),
    CATALOG("Catalog", Icons.Default.MenuBook),
    SETTINGS("Settings", Icons.Default.Settings),
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@PreviewScreenSizes
@Composable
fun MyGarageApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.MYGARAGE) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            it.icon,
                            contentDescription = it.label
                        )
                    },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        },
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationBarContainerColor = MaterialTheme.colorScheme.surface,
        )
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background
            )
        {
            when (currentDestination) {
                AppDestinations.MYGARAGE -> {
                    GarageScreen()
                }
                AppDestinations.CATALOG -> {
                    CatalogScreen()
                }
                AppDestinations.SETTINGS -> {
                    SettingsScreen()
                }
            }
        }
    }
}