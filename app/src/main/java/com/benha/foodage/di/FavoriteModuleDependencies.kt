package com.benha.foodage.di

import com.benha.core.domain.usecase.RecipeUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface FavoriteModuleDependencies {
    fun recipeUseCase(): RecipeUseCase
}