package tech.fremeaux.mygarage.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.fremeaux.mygarage.data.model.Car
import tech.fremeaux.mygarage.data.repo.CarRepository
import tech.fremeaux.mygarage.ui.dialog.CarDetailDialog
import tech.fremeaux.mygarage.ui.theme.Gold
import tech.fremeaux.mygarage.ui.theme.LightBlue
import tech.fremeaux.mygarage.ui.theme.LightGold
import tech.fremeaux.mygarage.ui.theme.LightGreen
import tech.fremeaux.mygarage.ui.theme.LightPurple

val color = listOf<Color>(LightBlue,LightGreen,LightPurple ,LightGold)
val emoji = listOf<String>("🏎️","🚗","🚙")

@Composable
fun GarageScreen(){
    val repo = CarRepository(LocalContext.current)
    var cars by remember { mutableStateOf(repo.getCars()) }

    var selectedCarForDetail by remember { mutableStateOf<Car?>(null) }


    Column (Modifier.padding(20.dp,35.dp,20.dp,0.dp)){
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                    append("MON ")
                }
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    append("GARAGE 🏎️")
                }
            },
            style = MaterialTheme.typography.displayLarge
        )
        Text(cars.size.toString()+" véhicule(s)",color = MaterialTheme.colorScheme.tertiary, modifier = Modifier.padding(bottom = 10.dp))

        if (cars.isEmpty()){
            Box(Modifier.fillMaxSize(), Alignment.Center){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.DirectionsCar, "", Modifier.size(80.dp))
                    Text("Nothing here....")
                }
            }
        }
        else{
            LazyColumn() {
                items(cars) { item ->

                    var showDialog by remember { mutableStateOf(false) }
                    val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
                        confirmValueChange = {
                            if (it == SwipeToDismissBoxValue.EndToStart) {
                                showDialog = true
                            }
                            it != SwipeToDismissBoxValue.StartToEnd
                        }
                    )
                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            title = { Text("Supprimer ?") },
                            text = { Text("Voulez-vous vraiment supprimer cet élément ?") },
                            confirmButton = {
                                TextButton(onClick = {
                                    showDialog = false
                                    repo.deleteCar(item.id)
                                    cars = repo.getCars()
                                }) {
                                    Text("Supprimer", color = Color.Red)
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showDialog = false }) {
                                    Text("Annuler")
                                }
                            }
                        )
                    }

                    SwipeToDismissBox(state = swipeToDismissBoxState, modifier = Modifier.fillMaxSize(), enableDismissFromStartToEnd = false,
                        backgroundContent = {
                            when (swipeToDismissBoxState.dismissDirection) {
                                SwipeToDismissBoxValue.StartToEnd -> {}
                                SwipeToDismissBoxValue.EndToStart -> {
                                    Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Remove item",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .drawBehind { drawRect(lerp(Color.White, Color.Red, swipeToDismissBoxState.progress)) }
                                        .wrapContentSize(Alignment.CenterEnd)
                                        .padding(12.dp),
                                    tint = Color.White
                                )}
                                SwipeToDismissBoxValue.Settled -> {}
                            }
                        }
                    ) {
                        Surface(modifier=Modifier.background(Color(0,0,0,0)).padding(vertical = 10.dp).shadow(8.dp, shape = RoundedCornerShape(10.dp)), onClick = { selectedCarForDetail = item }) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(0.dp, shape = RoundedCornerShape(10.dp))
                                    .background(
                                        color = MaterialTheme.colorScheme.surface,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                            ) {
                                Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min))
                                {
                                    Box(Modifier.fillMaxHeight().aspectRatio(1f)
                                        .background(Color(item.color.toULong())),
                                        contentAlignment = Alignment.Center
                                    ){
                                        Text(emoji.random(), fontSize = 32.sp)
                                    }
                                    Column(Modifier.padding(start = 5.dp).padding(10.dp)) {
                                        Text(item.make, color = MaterialTheme.colorScheme.tertiary)
                                        Text(item.model, color = MaterialTheme.colorScheme.secondary, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight(800))
                                        Row(Modifier.fillMaxWidth()) {
                                            Text(
                                                buildAnnotatedString {
                                                    withStyle(SpanStyle(Gold, )) {
                                                        append(item.hp.toString())
                                                    }
                                                    withStyle(SpanStyle(MaterialTheme.colorScheme.tertiary)) {
                                                        append(" ch")
                                                    }
                                                },
                                                color = Gold,
                                                style = MaterialTheme.typography.titleLarge,
                                                fontWeight = FontWeight(800),
                                                modifier = Modifier.padding(end = 15.dp)
                                            )
                                            Text(
                                                buildAnnotatedString {
                                                    withStyle(SpanStyle(Gold )) {
                                                        append(item.nm.toString())
                                                    }
                                                    withStyle(SpanStyle(MaterialTheme.colorScheme.tertiary)) {
                                                        append(" nm")
                                                    }
                                                },
                                                color = Gold,
                                                style = MaterialTheme.typography.titleLarge,
                                                fontWeight = FontWeight(800)
                                            )
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
                item {
                    Row(Modifier.fillMaxWidth().padding(top = 10.dp),horizontalArrangement = Arrangement.Center) {
                        Text("← Swipe pour supprimer", color = MaterialTheme.colorScheme.tertiary, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
    selectedCarForDetail?.let { car -> CarDetailDialog(car = car, onDismiss = { selectedCarForDetail = null }) }
}