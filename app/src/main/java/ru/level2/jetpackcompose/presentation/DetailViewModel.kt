package ru.level2.jetpackcompose.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.level2.jetpackcompose.entities.Character
import ru.level2.jetpackcompose.entities.Episode
import ru.level2.jetpackcompose.data.network.CharacterRepository

class DetailViewModel(private val characterRepository: CharacterRepository) : ViewModel() {
    private val _episodeList = MutableLiveData<List<Episode>>()
    val episodeList: LiveData<List<Episode>>
        get() = _episodeList
    var newCharacter: Character? = null


    fun setCharacter(character: Character) {
        newCharacter = character
    }

    fun getCharacter() = newCharacter ?: checkNotNull("not init character")


    fun getEpisodeInfoList() {
        viewModelScope.launch {
            val listEpisode = getEpisodeInfo()

            Log.d("Com", "Список :${listEpisode.toString()}")
            _episodeList.postValue(listEpisode)
        }
    }


    suspend fun getEpisodeInfo(): List<Episode> {
        val mutableListEpisode = emptyList<Episode>().toMutableList()
        mutableListEpisode.add(Episode(0, "TTTTTT", "43424234",""))
        newCharacter?.episode?.forEach { url ->
            val idEpisode = url.split('/').last().toInt()
            val resultListEpisode = characterRepository.getEpisodeById(idEpisode)
            if (resultListEpisode.id != 0) mutableListEpisode.add(resultListEpisode)
        }
        return mutableListEpisode
    }
}


