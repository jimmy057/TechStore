package com.example.techstore.presentacion.Category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techstore.domain.usecase.CategoryUseCase.CategoryUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryUseCases: CategoryUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(CategoryUiState())
    val state: StateFlow<CategoryUiState> = _state.asStateFlow()

    init {
        onEvent(CategoryUiEvent.LoadCategories)
        onEvent(CategoryUiEvent.RefreshCategories)
    }

    fun onEvent(event: CategoryUiEvent) {
        when (event) {
            is CategoryUiEvent.LoadCategories -> {
                viewModelScope.launch {
                    categoryUseCases.getCategories().collect { list ->
                        _state.update { it.copy(categories = list) }
                    }
                }
            }
            is CategoryUiEvent.RefreshCategories -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    categoryUseCases.refreshCategories()
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }
}