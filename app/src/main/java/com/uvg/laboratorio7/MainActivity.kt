package com.uvg.laboratorio7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uvg.laboratorio7.ui.theme.Laboratorio7Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Laboratorio7Theme {
                NotificationScreenSimplified()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreenSimplified() {
    var selectedFilter by remember { mutableStateOf<NotificationType?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notificaciones") },
                navigationIcon = {
                    IconButton(onClick = { /* Handle back press */ }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            FilterBarSimplified(
                selectedFilter = selectedFilter,
                onFilterSelected = { selectedFilter = it }
            )
            NotificationListSimplified(
                notifications = generateFakeNotifications(),
                selectedFilter = selectedFilter
            )
        }
    }
}

@Composable
fun FilterBarSimplified(selectedFilter: NotificationType?, onFilterSelected: (NotificationType?) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        NotificationType.entries.forEach { type ->
            FilterChipSimplified(
                isSelected = selectedFilter == type,
                label = type.name,
                onClick = {
                    onFilterSelected(if (selectedFilter == type) null else type)
                }
            )
        }
    }
}

@Composable
fun FilterChipSimplified(isSelected: Boolean, label: String, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val textColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
    val borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)

    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable(onClick = onClick)
            .background(
                color = if (isSelected) backgroundColor else MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.medium
            )
            .border(width = 1.dp, color = borderColor, shape = MaterialTheme.shapes.medium)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, color = textColor)

            if (isSelected) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = textColor,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun NotificationListSimplified(notifications: List<Notification>, selectedFilter: NotificationType?) {
    val filteredNotifications = if (selectedFilter == null) {
        notifications
    } else {
        notifications.filter { it.type == selectedFilter }
    }

    LazyColumn {
        items(filteredNotifications.size) { index ->
            NotificationCardSimplified(notification = filteredNotifications[index])
        }
    }
}

@Composable
fun NotificationCardSimplified(notification: Notification) {
    val (backgroundColor, iconColor, iconResId) = when (notification.type) {
        NotificationType.Informativas -> Triple(Color(0xFFFFE0B2), Color(0xFFFF9800), Icons.Default.Info)
        NotificationType.Capacitaciones -> Triple(Color(0xFFB2EBF2), Color(0xFF00BCD4), Icons.Default.Event)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(backgroundColor, shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        Icon(
            imageVector = iconResId,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier
                .size(40.dp)
                .background(Color.White, shape = CircleShape)
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = notification.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = notification.description,
                fontSize = 14.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Text(
            text = notification.time,
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.Bottom)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Laboratorio7Theme {
        NotificationScreenSimplified()
    }
}

// Ejemplo de modelo de datos para la notificación
data class Notification(
    val title: String,
    val description: String,
    val time: String,
    val type: NotificationType
)

enum class NotificationType {
    Informativas, Capacitaciones
}

// Función para generar notificaciones falsas
fun generateFakeNotifications(): List<Notification> {
    return listOf(
        Notification("Nueva versión disponible", "La aplicación se ha actualizado a la versión 1.1.0. Ve a la PlayStore y actualízala", "19 ago • 2:30 p. m.", NotificationType.Informativas),
        Notification("Nueva capacitación", "El día Martes 21 de Agosto tendremos una nueva capacitación en el INTECAP, no faltes!", "15 ago • 3:00 p. m.", NotificationType.Capacitaciones),
        Notification("¡Mañana capacitación de ICTA F!", "No te olvides de asistir a esta capacitación mañana, a las 6pm, en el Intecapp.", "05 ago • 11:30 a. m.", NotificationType.Capacitaciones),
        Notification("Nueva versión disponible", "La aplicación se ha actualizado a la versión 1.0.2. Ve a la PlayStore y actualízala", "19 jul • 2:30 p. m.", NotificationType.Informativas)
    )
}
