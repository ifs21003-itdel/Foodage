package com.benha.core.domain.repository

import com.benha.core.data.Resource
import com.benha.core.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface IRecipeRepository {

    fun getRecipes(): Flow<Resource<List<Recipe>>>

    fun getDetailRecipe(id: Int): Flow<Resource<Recipe>>

    fun getRecipeById(id: Int): Flow<Recipe>

    fun getFavoriteRecipe(): Flow<List<Recipe>>

    fun setFavoriteRecipe(recipe: Recipe, state: Boolean)
}