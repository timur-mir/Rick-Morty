package ru.level2.jetpackcompose.data.network

import ru.level2.jetpackcompose.data.network.Api
import ru.level2.jetpackcompose.entities.Character
import ru.level2.jetpackcompose.entities.Episode

class CharacterRepository {
    suspend fun getCharacters(page: Int) : List<Character> {
        val response = Api.retrofit.loadList(page = page)
        return response.body()?.results ?: emptyList()
    }

    suspend fun getEpisodeById(id: Int) : Episode {
        val response = Api.retrofit.getEpisode(id)
        return response.body() ?: Episode(0, "", "","")
    }
}