package ru.level2.jetpackcompose.presentation

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.level2.jetpackcompose.entities.Character
import ru.level2.jetpackcompose.data.network.CharacterRepository

class CharactersViewModel(
    charactersRepository: CharacterRepository
) : ViewModel() {
    val movies: Flow<PagingData<Character>> = Pager(PagingConfig(pageSize = 20)) {
        CharacterSource(charactersRepository)
    }.flow
}