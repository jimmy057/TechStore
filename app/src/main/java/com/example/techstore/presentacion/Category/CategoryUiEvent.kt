package com.example.techstore.presentacion.Category

sealed class CategoryUiEvent {
    object LoadCategories : CategoryUiEvent()
    object RefreshCategories : CategoryUiEvent()
}