package com.austin.askme.ui.screens

// ------------------------------------------------------------------
// Paste everything BELOW your own "package ..." line.
// This file uses SecurityEvent, EventType and sampleEvents from
// EventLogScreen.kt, so that file must already be in your project.
// ------------------------------------------------------------------

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Shows one event in detail: a summary card, a few facts, and its photos.
@Composable
fun EventDetailScreen(
    event: SecurityEvent,
    onBack: () -> Unit = {}
) {
    Scaffold { innerPadding ->
        // A grid that scrolls. The header items take the full width,
        // and the photos sit two per row below them.
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Back button + summary card + details (full width).
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    TextButton(onClick = onBack) { Text("Back") }
                    SummaryCard(event)
                    DetailsCard(event)
                }
            }

            // Photos section title (full width).
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = if (event.photoCount > 0) "Photos (${event.photoCount})" else "Photos",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (event.photoCount > 0) {
                // One grey placeholder box per photo.
                // Later these will show the real photos saved by Django.
                items(event.photoCount) { index ->
                    PhotoPlaceholder(number = index + 1)
                }
            } else {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        text = "No photos were saved for this event.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(event: SecurityEvent) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = event.type.color)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Icon(
                imageVector = event.type.icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = event.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(text = event.time, color = Color.White)
        }
    }
}

@Composable
private fun DetailsCard(event: SecurityEvent) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DetailRow("Type", event.type.label)
            DetailRow("Time", event.time)
            DetailRow("Photos saved", event.photoCount.toString())
            DetailRow("Event number", "#${event.id}")
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun PhotoPlaceholder(number: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f) // makes the box a perfect square
            .background(Color(0xFFBDBDBD), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text("Photo $number", color = Color.White, fontWeight = FontWeight.Medium)
    }
}

// Previews: one event with photos, one without.
@Preview(showBackground = true)
@Composable
fun EventDetailWithPhotosPreview() {
    MaterialTheme { EventDetailScreen(event = sampleEvents[0]) }
}

@Preview(showBackground = true)
@Composable
fun EventDetailNoPhotosPreview() {
    MaterialTheme { EventDetailScreen(event = sampleEvents[1]) }
}