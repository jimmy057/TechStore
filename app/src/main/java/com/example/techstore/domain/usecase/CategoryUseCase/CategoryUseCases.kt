package com.example.techstore.domain.usecase.CategoryUseCase

data class CategoryUseCases(
    val getCategories: GetCategoriesUseCase,
    val refreshCategories: RefreshCategoriesUseCase
)