package com.benha.foodage.di

import com.benha.core.domain.usecase.RecipeInteractor
import com.benha.core.domain.usecase.RecipeUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun provideRecipeUseCase(recipeInteractor: RecipeInteractor): RecipeUseCase
}