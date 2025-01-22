package ru.level2.jetpackcompose.presentation

import android.graphics.Canvas
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.level2.jetpackcompose.R
import ru.level2.jetpackcompose.entities.Character
import ru.level2.jetpackcompose.entities.Episode
import ru.level2.jetpackcompose.entities.StatusCharacter
import ru.level2.jetpackcompose.data.network.CharacterRepository
import ru.level2.jetpackcompose.network.GlideImageWithPreview
import ru.level2.jetpackcompose.theme.ComposeSampleTheme

class DetailFragment : Fragment() {
    private val viewModel by viewModels<DetailViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                DetailViewModel(CharacterRepository()) as T
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val character = arguments?.getSerializable("CHARACTER_ENTITY") as Character
        viewModel.setCharacter(character)
        viewModel.getEpisodeInfoList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            ComposeSampleTheme {
                val scrollState = rememberScrollState()
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(Color.DarkGray)
                        .scrollable(
                            state = scrollState,
                            orientation = Orientation.Vertical
                        )
                ) {
                    navigationBar()
                    val  listEpisode: State<List<Episode>?> = viewModel.episodeList.observeAsState()
                    listEpisode.value.let { list: List<Episode>? ->
                        list?.let {
                            ListView(it)
                        }

                    }
                }

            }
        }
    }


    @Composable
    fun ListView(itemsEpisode: List<Episode>) {
        LazyColumn {
            items(itemsEpisode) { entity ->
                CardEpisode(entity as Episode)
            }
        }
    }

    @Composable
    fun CardEpisode(epizode: Episode) {
        if (epizode.id == 0) {
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                viewModel.getCharacter()?.let { character ->
                    val character = (character as Character)
                    aboutCharacter(character)
                }
            }
        } else {
            Card(
                shape = RoundedCornerShape(6.dp),
                backgroundColor = Color.Magenta,
                modifier = Modifier
                    .padding(6.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.Gray)
                        .fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround
                    ){
                    Column(  modifier = Modifier
                        .background(Color.Gray))
                    {
                        Text(
                            text = epizode.name,
                            maxLines = 1,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier.padding(
                                all = 1.dp
                            )
                        )

                        Text(
                            text = epizode.air_date,
                            maxLines = 1,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            color = Color.White,
                            modifier = Modifier.padding(
                                all = 1.dp
                            )
                        )

                    }
                        Row(
                            horizontalArrangement = Arrangement.aligned(Alignment.End),
                                    modifier = Modifier
                                    .background(Color.Gray)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = epizode.episode.toString(),
                                maxLines = 1,
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                color = Color.White,
                                modifier = Modifier.padding(
                                    all = 6.dp
                                ).padding(horizontal = 1.dp)
                                    .width(50.dp)
                                    , textAlign = TextAlign.End
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun navigationBar() {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
                .padding(all=1.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = null,
                modifier = Modifier
                    .width(60.dp)
                    .height(30.dp)
                    .padding(top = 5.dp)
                    .clickable {
                        returnCharacterFragment()
                    },
                alignment = Alignment.Center
            )
            Text(
                text = "Person detail",
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(all = 2.dp),
                color = Color.White,
                textAlign = TextAlign.Start
            )
        }
    }
    @Composable
    fun aboutCharacter(character: Character) {
        Column(modifier = Modifier.fillMaxWidth()
            .padding(20.dp)) {
            GlideImageWithPreview(
                data = character.image,
                Modifier
                    .height(320.dp)
                    .fillMaxWidth()

            )

            Text(
                text = character.name,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all=1.dp),
                color = Color.White,
                textAlign = TextAlign.Start
            )
            val gradientBrush = Brush.horizontalGradient(
                colors = listOf(Color.White, Color.Gray, Color.DarkGray),
                startX = 0f,
                endX = Float.POSITIVE_INFINITY
            )
            Text(
                text = "",
                modifier = Modifier
                    .height(3.dp)
                    .background(brush = gradientBrush)
                    .width(400.dp)
            )

            Text(
                text = "Live status",
                maxLines = 1,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all=1.dp),
                color = Color.LightGray,
                textAlign = TextAlign.Start
            )
            when (StatusCharacter.valueOf(character.status)) {
                StatusCharacter.Alive ->

                    imageStatusDifine(character, R.drawable.ic_alive, false)
                StatusCharacter.Dead ->
                    imageStatusDifine(character, R.drawable.ic_dead, false)
                StatusCharacter.unknown ->
                    imageStatusDifine(character, R.drawable.ic_unknown, false)
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
                    color = Color.Red
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
                    fontSize = 20.sp,
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
                    fontSize = 20.sp,
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
                    fontSize = 20.sp,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Green
                )
            }

            Text(
                text = "Episodes:",
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all=1.dp),
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Start
            )
        }
    }
    @Composable
    private fun imageStatusDifine(character: Character, statusId: Int, isFullName: Boolean = true) {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .padding(all=1.dp)
                .fillMaxWidth(1F)
        ) {
            ResourcesCompat.getDrawable(
                LocalContext.current.resources,
                statusId, LocalContext.current.theme
            )?.let { drawable ->
                val bitmap = Bitmap.createBitmap(30, 30,
                    Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)

                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top=6.dp)
                        .size(10.dp),
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = if (isFullName)
                        "${character.status} - ${character.species}"
                    else
                        "${character.status}",
                    maxLines = 1,
                    modifier = Modifier
                        .padding(all=1.dp),
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }



    private fun returnCharacterFragment() {
        requireActivity().supportFragmentManager.commit {
            replace(R.id.container, CharacterFragment())
            addToBackStack(null)
        }
    }

    companion object {
        var t = 0
        const val CHARACTER_ENTITY = "character"
        fun newInstance(entity: Character): DetailFragment {
            val args = Bundle().apply {
                putSerializable("CHARACTER_ENTITY", entity)
            }
            val fragment = DetailFragment()
            fragment.arguments = args
            return fragment
        }
    }
}

