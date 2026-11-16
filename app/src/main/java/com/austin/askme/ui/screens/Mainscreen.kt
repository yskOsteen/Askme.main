package com.austin.askme.ui.screens

// ------------------------------------------------------------------
// Paste everything BELOW your own "package ..." line.
// If you create this file with New > Kotlin Class/File, Android Studio
// writes the package line for you. Keep it, and replace the rest.
// ------------------------------------------------------------------

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// The four states of the security system. Each one has its own
// text, colour and icon, so the screen can change by swapping the state.
enum class SecurityState(
    val label: String,
    val message: String,
    val color: Color,
    val icon: ImageVector
) {
    DISARMED("Disarmed", "The system is off. Move around freely.", Color(0xFF2E7D32), Icons.Default.Check),
    ARMED("Armed", "Watching the door.", Color(0xFFB71C1C), Icons.Default.Lock),
    ENTRY_COUNTDOWN("Entry countdown", "Someone came in. Disarm within 30 seconds.", Color(0xFFF9A825), Icons.Default.Notifications),
    TRIGGERED("ALARM", "Intruder detected! Photos are being saved.", Color(0xFFD50000), Icons.Default.Warning)
}

// The whole dashboard screen.
@Composable
fun DashboardScreen(initialState: SecurityState = SecurityState.DISARMED) {
    // "State": when this value changes, Compose redraws the screen.
    var state by remember { mutableStateOf(initialState) }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Header()
            StatusCard(state)

            Button(
                onClick = {
                    state = if (state == SecurityState.DISARMED) SecurityState.ARMED
                    else SecurityState.DISARMED
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = if (state == SecurityState.DISARMED) "Arm system" else "Disarm system",
                    fontSize = 18.sp
                )
            }

            Text("Room readings", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ReadingCard("Temperature", "27°C", Modifier.weight(1f))
                ReadingCard("Humidity", "55%", Modifier.weight(1f))
                ReadingCard("Light", "Bright", Modifier.weight(1f))
            }

            OutlinedButton(
                onClick = { /* later: open the live camera screen */ },
                modifier = Modifier.fillMaxWidth()
            ) { Text("View live camera") }

            OutlinedButton(
                onClick = { /* later: open the event log screen */ },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Event log") }
        }
    }
}

@Composable
private fun Header() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("Home Security", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text(
                "Front door node",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(Color(0xFF2E7D32), CircleShape)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Online")
        }
    }
}

@Composable
private fun StatusCard(state: SecurityState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = state.color)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Icon(
                imageVector = state.icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(state.label, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(state.message, color = Color.White)
        }
    }
}

@Composable
private fun ReadingCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(16.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(label, fontSize = 12.sp)
        }
    }
}

// Previews: these show the screen inside Android Studio without running the app.
@Preview(showBackground = true)
@Composable
fun DashboardDisarmedPreview() {
    MaterialTheme { DashboardScreen() }
}

@Preview(showBackground = true)
@Composable
fun DashboardTriggeredPreview() {
    MaterialTheme { DashboardScreen(SecurityState.TRIGGERED) }
}