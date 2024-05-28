package com.packt.list.presentation.list


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.packt.list.presentation.list.model.Genre
import com.packt.list.presentation.list.model.Movie
import com.packt.list.presentation.list.model.MoviesViewState

@Composable
fun MoviesScreen(moviesViewState: MoviesViewState = sampleMoviesScreen()) {
    Scaffold(
        containerColor = Color.Black,
        topBar = { PacktflixTopBar() },
        bottomBar = { PacktflixBottomBar() }
    ) { innerPadding ->
        GenreList(
            genres = moviesViewState.genres,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun GenreList(genres: List<Genre>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(genres.size) { index ->
            GenreRow(genre = genres[index])
        }
    }
}


@Composable
fun GenreRow(genre: Genre) {
    Column(modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 8.dp, bottom = 8.dp)) {
        Text(text = genre.name,
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White
        )
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
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MoviesScreen(moviesViewState = sampleMoviesScreen())
}


// This function is just for previewing and testing
fun sampleMoviesScreen(): MoviesViewState {
    return MoviesViewState(
        genres = listOf(
            Genre(
                name = "Comedy",
                movies = listOf(
                    Movie(
                        id = 1,
                        title = "The Hangover",
                        imageUrl = "https://upload.wikimedia.org/wikipedia/en/b/b9/Hangoverposter09.jpg"
                    ),
                    Movie(
                        id = 2,
                        title = "Superbad",
                        imageUrl = "https://upload.wikimedia.org/wikipedia/en/8/8b/Superbad_Poster.png"
                    ),
                    Movie(
                        id = 3,
                        title = "Step Brothers",
                        imageUrl = "https://upload.wikimedia.org/wikipedia/en/d/d9/StepbrothersMP08.jpg"
                    ),
                    Movie(
                        id = 4,
                        title = "Anchorman",
                        imageUrl = "https://upload.wikimedia.org/wikipedia/en/6/64/Movie_poster_Anchorman_The_Legend_of_Ron_Burgundy.jpg"
                    )
                )
            ),
            Genre(
                name = "Mystery",
                movies = listOf(
                    Movie(
                        id = 1,
                        title = "Se7en",
                        imageUrl = "https://upload.wikimedia.org/wikipedia/en/6/68/Seven_%28movie%29_poster.jpg"
                    ),
                    Movie(
                        id = 2,
                        title = "Zodiac",
                        imageUrl = "https://upload.wikimedia.org/wikipedia/en/3/3a/Zodiac2007Poster.jpg"
                    ),
                    Movie(
                        id = 3,
                        title = "Gone Girl",
                        imageUrl = "https://upload.wikimedia.org/wikipedia/en/0/05/Gone_Girl_Poster.jpg"
                    ),
                    Movie(
                        id = 4,
                        title = "Shutter Island",
                        imageUrl = "https://upload.wikimedia.org/wikipedia/en/7/76/Shutterislandposter.jpg"
                    )
                )
            ),
            Genre(
                name = "Documentary",
                movies = listOf(
                    Movie(
                        id = 1,
                        title = "March of the Penguins",
                        imageUrl = "https://upload.wikimedia.org/wikipedia/en/1/19/March_of_the_penguins_poster.jpg"
                    ),
                    Movie(
                        id = 2,
                        title = "Bowling for Columbine",
                        imageUrl = "https://upload.wikimedia.org/wikipedia/en/e/e7/Bowling_for_columbine.jpg"
                    ),
                    Movie(
                        id = 3,
                        title = "Blackfish",
                        imageUrl = "https://upload.wikimedia.org/wikipedia/en/b/bd/BLACKFISH_Film_Poster.jpg"
                    ),
                    Movie(
                        id = 4,
                        title = "An Inconvenient Truth",
                        imageUrl = "https://upload.wikimedia.org/wikipedia/en/1/19/An_Inconvenient_Truth_Film_Poster.jpg"
                    )
                )
            )
        )
    )
}


