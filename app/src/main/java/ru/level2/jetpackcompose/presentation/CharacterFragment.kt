package ru.level2.jetpackcompose.presentation

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import ru.level2.jetpackcompose.R

import ru.level2.jetpackcompose.entities.Character
import ru.level2.jetpackcompose.entities.StatusCharacter
import ru.level2.jetpackcompose.data.network.CharacterRepository
import ru.level2.jetpackcompose.network.GlideImageWithPreview
import ru.level2.jetpackcompose.theme.ComposeSampleTheme

class CharacterFragment : Fragment() {
    private val viewModel by viewModels<CharactersViewModel>
    {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                CharactersViewModel(CharacterRepository()) as T
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = ComposeView(requireContext())
        view.setContent {
            ComposeSampleTheme {
                Column {
                    val gradientBrush = Brush.horizontalGradient(
                        colors = listOf(Color.Green, Color.Blue, Color.Red),
                        startX = 0f,
                        endX = Float.POSITIVE_INFINITY
                    )
                    Box(
                        modifier = Modifier
                            .background(Color.DarkGray)
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {

                    }
                }
                ListView()
            }
        }
        return view
    }


    @Composable
    fun ListView() {
        val characters = viewModel.movies.collectAsLazyPagingItems()
        LazyColumn {
            items(characters) {
                it?.let { character ->
                    when (StatusCharacter.valueOf(character.status)) {
                        StatusCharacter.Alive ->
                            CardInfo(character = it, R.drawable.ic_alive)

                        StatusCharacter.Dead ->
                            CardInfo(character = it, R.drawable.ic_dead)

                        StatusCharacter.unknown ->
                            CardInfo(character = it, R.drawable.ic_unknown)
                    }
                }
            }

            characters.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier.fillParentMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    loadState.append is LoadState.Loading -> {
                        item { CircularProgressIndicator() }
                    }

                    loadState.refresh is LoadState.Error -> {
                        val e = characters.loadState.refresh as LoadState.Error
                        item {
                            Column(
                                modifier = Modifier.fillParentMaxSize(),
                                verticalArrangement = Arrangement.Center
                            ) {
                                e.error.localizedMessage?.let { Text(text = it) }
                                Button(onClick = { retry() }) {
                                    Text(text = "Перезагрузить")
                                }
                            }

                        }
                    }

                    loadState.append is LoadState.Error -> {
                        val e = characters.loadState.append as LoadState.Error
                        item {
                            Column(
                                modifier = Modifier.fillParentMaxSize(),
                                verticalArrangement = Arrangement.Center
                            ) {
                                e.error.localizedMessage?.let { Text(text = it) }
                                Button(
                                    onClick = { retry() }) {
                                    Text(text = "Перезагрузить")
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    @Composable
    fun CardInfo(character: Character, statusId: Int) {
        Row(
            modifier = Modifier
                .padding(all = 10.dp)
                .fillMaxWidth()
                .clickable {
                    detailFragment(character)
                })
        {

            GlideImageWithPreview(
                data = character.image,
                Modifier
                    .size(200.dp)
            )
            Column(
                modifier = Modifier.padding(4.dp)
            ) {
                Text(
                    text = character.name,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier.padding(all = 2.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.Start
                ) {
                    ResourcesCompat.getDrawable(
                        LocalContext.current.resources,
                        statusId, LocalContext.current.theme
                    )?.let { drawable ->
                        val bitmap = Bitmap.createBitmap(
                            30, 30,
                            Bitmap.Config.ARGB_8888
                        )
                        val canvas = Canvas(bitmap)
                        drawable.setBounds(0, 0, canvas.width, canvas.height)
                        drawable.draw(canvas)

                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.size(10.dp),
                            alignment = Alignment.Center,
                            contentScale = ContentScale.Crop
                        )
                        val gradientBrush = Brush.horizontalGradient(
                            colors = listOf(Color.Green, Color.Red, Color.Yellow),
                            startX = 0f,
                            endX = Float.POSITIVE_INFINITY
                        )
                        Text(
                            text = character.status,
                            maxLines = 1,
                            modifier = Modifier
                                .padding(all = 2.dp)
                                .background(brush = gradientBrush),
                            fontSize = 14.sp,
                            overflow = TextOverflow.Ellipsis

                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Species and gender:",
                        maxLines = 1,
                        modifier = Modifier
                            .padding(all = 2.dp),
                        fontSize = 14.sp,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = character.species,
                        maxLines = 1,
                        modifier = Modifier
                            .padding(all = 2.dp),
                        fontSize = 14.sp,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Magenta
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Last known location:",
                        maxLines = 1,
                        modifier = Modifier
                            .padding(all = 2.dp),
                        fontSize = 14.sp,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Blue
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = character.location.name,
                        maxLines = 1,
                        modifier = Modifier
                            .padding(all = 2.dp),
                        fontSize = 14.sp,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Cyan
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "First seen in:",
                        maxLines = 1,
                        modifier = Modifier
                            .padding(all = 2.dp),
                        fontSize = 14.sp,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Yellow
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = character.origin.name,
                        maxLines = 1,
                        modifier = Modifier
                            .padding(all = 2.dp),
                        fontSize = 14.sp,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Green
                    )


                }
            }
        }

    }


    private fun detailFragment(character: Character) {
        requireActivity().supportFragmentManager.commit {
            replace(R.id.container, DetailFragment.newInstance(character))
            addToBackStack(null)
        }
    }
}

