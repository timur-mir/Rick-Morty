package ru.level2.jetpackcompose.presentation

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.level2.jetpackcompose.data.network.CharacterRepository
import ru.level2.jetpackcompose.entities.Character

class CharacterSource( private val characterRepository: CharacterRepository):PagingSource<Int,Character>() {
    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        return try {
            val nextPage = params.key ?: 1
            val charactersData = characterRepository.getCharacters(nextPage)
            LoadResult.Page(
                data = charactersData,
                prevKey = params.key?.let { it - 1 },
                nextKey = (params.key ?: 0) + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


}