package com.packt.list.presentation.list


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.packt.list.presentation.list.model.Genre
import com.packt.list.presentation.list.model.Movie
import com.packt.list.presentation.list.model.MoviesViewState

@Composable
fun MoviesScreenUI(moviesViewState: MoviesViewState = sampleMoviesScreen()) {
    Scaffold(
        topBar = { PacktflixTopBar() },
        bottomBar = { PacktflixBottomBar() }
    ) { innerPadding ->
       GenreList(genres = moviesViewState.genres, modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun GenreList(genres: List<Genre>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(genres.size) { index ->
            //GenreRow(genre = genres[index])
        }
    }
}


@Composable
fun GenreRow(genre: Genre) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = genre.name, style = MaterialTheme.typography.headlineSmall)
        LazyRow {
            items(genre.movies.size) { index ->
                MovieCard(movie = genre.movies[index])
            }
        }
    }
}

@Composable
fun MovieCard(movie: Movie) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .size(120.dp, 180.dp)

    ) {
        Image(
            painter = rememberAsyncImagePainter(model = movie.imageUrl),
            contentDescription = movie.title,
            contentScale = ContentScale.Crop
        )
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MoviesScreenUI(moviesViewState = sampleMoviesScreen())
}

// This function is just for previewing and testing
fun sampleMoviesScreen(): MoviesViewState {
    return MoviesViewState(listOf())
}

