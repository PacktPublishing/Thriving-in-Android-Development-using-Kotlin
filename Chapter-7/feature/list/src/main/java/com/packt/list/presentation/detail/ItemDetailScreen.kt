package com.packt.list.presentation.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.packt.list.presentation.detail.model.Episode
import com.packt.list.presentation.detail.model.ItemDetail


@Composable
fun ItemDetailScreen(item: ItemDetail = createFakeItemDetail()) {
    val scrollState = rememberScrollState()
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.fillMaxSize().background(Color.Black).padding(all = 8.dp)
            .verticalScroll(scrollState)
    ) {
        ItemBannerImage(item.imageUrl)
        ItemTitleAndMetadata(item.title, item.isHD, item.year, item.duration)
        ItemActions(item.movieUrl)
        Text(text = item.description, color = Color.Gray)
        CastAndCreatorsList(item.cast, item.creators)
        AdditionalMovieDetails(item)
    }
}

@Composable
fun ItemBannerImage(imageUrl: String) {
    Box(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        Image(
            painter = rememberAsyncImagePainter(model = imageUrl),
            contentDescription = "Movie Banner",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        )

        IconButton(onClick = { /* TODO: Handle back action */ },
            modifier = Modifier
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }
    }
}


@Composable
fun ItemTitleAndMetadata(
    title: String,
    isHD: Boolean,
    year: String,
    duration: String
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isHD) {
                Box(
                    modifier = Modifier
                        .border(BorderStroke(1.dp, Color.White), shape = RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "HD",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = year,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
        Text(
            text = duration,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

@Composable
fun ItemActions(
    itemUrl: String
) {
    Row(
        modifier = Modifier
            .padding(top = 16.dp, bottom = 16.dp),
    ) {
        ActionButton(
            icon = Icons.Filled.PlayArrow,
            label = "Play",
            onClick = { /* TODO: Handle play action */ }
        )
        ActionButton(
            icon = Icons.Default.Add,
            label = "My List",
            onClick = { /* TODO: Handle add to list action */ }
        )
    }
}

@Composable
fun ActionButton(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick).padding(start = 16.dp, end = 16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White
        )
        Text(text = label, color = Color.White)
    }
}

@Composable
fun CastAndCreatorsList(cast: List<String>, creators: List<String>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Cast",
            style = MaterialTheme.typography.titleSmall,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(cast) { actorName ->
                Text(
                    text = actorName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    modifier = Modifier.background(
                        color = Color.DarkGray,
                        shape = RoundedCornerShape(4.dp)
                    ).padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Creada por",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(creators) { creatorName ->
                Text(
                    text = creatorName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    modifier = Modifier.background(
                        color = Color.DarkGray,
                        shape = RoundedCornerShape(4.dp)
                    ).padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}


@Composable
fun AdditionalMovieDetails(item: ItemDetail) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Episodes", color = Color.White, modifier = Modifier.padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 32.dp))
        item.episodes.forEach { episode ->
            EpisodeItem(episode = episode)
        }
    }
}

@Composable
fun EpisodeItem(episode: Episode) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO: Handle episode playback */ }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Episode image
        Image(
            painter = rememberAsyncImagePainter(model = episode.imageUrl),
            contentDescription = "Episode Thumbnail",
            modifier = Modifier.size(width = 120.dp, height = 68.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop
        )

        // Space between image and text details
        Spacer(modifier = Modifier.width(16.dp))

        // Episode title and duration
        Column {
            Text(
                text = episode.title,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
            Text(
                text = "Duration: ${episode.duration}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
    Divider(color = Color.Gray, thickness = 0.5.dp)
}

fun createFakeItemDetail(): ItemDetail {
    return ItemDetail(
        type = ItemDetail.Type.SERIES,
        title = "The Adventures of Example",
        imageUrl = "https://www.pluggedin.com/wp-content/uploads/2019/12/the-hickerhikers-guide-to-the-galaxy-1200x711.jpg",
        rating = "8.5",
        isHD = true,
        year = "2021",
        duration = "30 min",
        cast = listOf("John Doe", "Jane Doe"),
        description = "A captivating tale of adventure and discovery in the world of examples.",
        creators = listOf("Alice Example", "Bob Example"),
        episodes = listOf(
            Episode(
                title = "Pilot",
                imageUrl = "https://static1.srcdn.com/wordpress/wp-content/uploads/2020/07/The-Hitchhikers-Guide-To-The-Galaxy-Ford-and-the-Bulldozer-scene.jpg",
                duration = "45 min",
                episodeUrl = "https://example.com/episode1"
            ),
            Episode(
                title = "The Next Step",
                imageUrl = "https://m.media-amazon.com/images/S/pv-target-images/122da2c3a8fe83259c706813525a83c0ffb84c9c443e4e0914ef9c27607de43a._SX1080_FMjpg_.jpg",
                duration = "50 min",
                episodeUrl = "https://example.com/episode2"
            )
        ),
        movieUrl = "https://example.com/movie"
    )
}

