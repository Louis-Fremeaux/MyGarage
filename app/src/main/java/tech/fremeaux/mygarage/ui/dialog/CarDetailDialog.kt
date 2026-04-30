package tech.fremeaux.mygarage.ui.dialog

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import tech.fremeaux.mygarage.data.model.Car
import tech.fremeaux.mygarage.ui.theme.Gold

@Composable
fun CarDetailDialog (car: Car, onDismiss: () -> Unit){
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.fillMaxWidth().height(250.dp).background(Brush.verticalGradient(listOf(Color(car.color.toULong()), Color(217,119,6,40))))) {
                    IconButton(onClick = onDismiss, modifier = Modifier.padding(16.dp)) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }

                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(150.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                    )
                }

                Column(Modifier.padding(20.dp)) {
                    Text(text = "${car.make} · ${car.year}", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
                    Text(text = car.model, style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Bold)
                    Text(text = "${car.fuel} · ${car.drive}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)

                    Spacer(Modifier.height(20.dp))

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        StatCard("PUISSANCE", "${car.hp} ch", Modifier.weight(1f).shadow(5.dp, shape = RoundedCornerShape(10.dp)), Gold)
                        StatCard("0 -> 100", "2.7 s", Modifier.weight(1f).shadow(5.dp,shape = RoundedCornerShape(10.dp)), Color.Blue)
                    }

                    Spacer(Modifier.height(30.dp))

                    Button(
                        onClick = { /* Ta logique de modif */ },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("MODIFIER LE VÉHICULE")
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, modifier: Modifier, color: Color) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Text(
                value,
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Canvas(modifier = Modifier.fillMaxWidth().height(4.dp).padding(top = 4.dp)) {
                drawRoundRect(color = color, size = size, cornerRadius = CornerRadius(2f, 2f))
            }
        }
    }
}