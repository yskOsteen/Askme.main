package com.austin.askme.ui.screens

// ------------------------------------------------------------------
// Paste everything BELOW your own "package ..." line.
// If you create this file with New > Kotlin Class/File, Android Studio
// writes the package line for you. Keep it, and replace the rest.
// ------------------------------------------------------------------

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// The kinds of things that can appear in the log.
enum class EventType(val label: String, val color: Color, val icon: ImageVector) {
    ARMED("Armed", Color(0xFFB71C1C), Icons.Default.Lock),
    DISARMED("Disarmed", Color(0xFF2E7D32), Icons.Default.Check),
    DOOR_TRIGGERED("Door triggered", Color(0xFFF9A825), Icons.Default.Notifications),
    FIRE("Fire detected", Color(0xFFD50000), Icons.Default.Warning),
    DEVICE_OFFLINE("Device offline", Color(0xFF616161), Icons.Default.Close)
}

// One row of data. A "data class" is a simple container for related values.
data class SecurityEvent(
    val id: Int,
    val type: EventType,
    val title: String,
    val time: String,
    val photoCount: Int = 0
)

// Fake data for now. Later this list will come from the Django server.
val sampleEvents = listOf(
    SecurityEvent(1, EventType.DOOR_TRIGGERED, "Movement at the door", "Today, 18:42", photoCount = 6),
    SecurityEvent(2, EventType.DISARMED, "Disarmed with the button", "Today, 18:41"),
    SecurityEvent(3, EventType.ARMED, "Armed from the app", "Today, 08:05"),
    SecurityEvent(4, EventType.DEVICE_OFFLINE, "Lost connection to the node", "Yesterday, 22:10"),
    SecurityEvent(5, EventType.FIRE, "Flame sensor triggered", "Yesterday, 19:30", photoCount = 3),
    SecurityEvent(6, EventType.ARMED, "Armed with the button", "Yesterday, 07:58")
)

// The whole event log screen.
@Composable
fun EventLogScreen(
    events: List<SecurityEvent> = sampleEvents,
    onEventClick: (SecurityEvent) -> Unit = {}
) {
    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text(
                text = "Event log",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
            )

            if (events.isEmpty()) {
                Text(
                    text = "No events yet.",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                // LazyColumn only draws the rows you can see, so long lists stay fast.
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(events, key = { it.id }) { event ->
                        EventRow(event = event, onClick = { onEventClick(event) })
                    }
                }
            }
        }
    }
}

@Composable
private fun EventRow(event: SecurityEvent, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Coloured circle with the icon inside.
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(event.type.color, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = event.type.icon,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(event.title, fontWeight = FontWeight.SemiBold)
                Text(
                    event.time,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Only show the photo count when there are photos.
            if (event.photoCount > 0) {
                Text(
                    text = "${event.photoCount} photos",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// Previews.
@Preview(showBackground = true)
@Composable
fun EventLogPreview() {
    MaterialTheme { EventLogScreen() }
}

@Preview(showBackground = true)
@Composable
fun EventLogEmptyPreview() {
    MaterialTheme { EventLogScreen(events = emptyList()) }
}