package com.example.tennismobileapp.ui.court.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tennismobileapp.core.ui.AppTypography

data class CourtItemUiModel(
    val courtId: Long,
    val thumbnail: String,
    val name: String,
    val address: String
)

@Composable
fun CourtItemCard(
    court: CourtItemUiModel,
    onClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(court.courtId) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            CourtThumbnail(
                court.thumbnail,
                court.name
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = court.name,
                style = AppTypography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = court.address,
                style = AppTypography.bodyLarge
            )

        }
    }



}



