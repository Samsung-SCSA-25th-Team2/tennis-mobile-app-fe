package com.example.tennismobileapp.ui.court.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter

@Composable
fun CourtThumbnail(
    thumbnailUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    Image(
        painter = rememberAsyncImagePainter(thumbnailUrl),
        contentDescription = contentDescription,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16 / 9f),
        contentScale = ContentScale.Crop
    )
}