package com.benha.core.data.source.remote

import android.util.Log
import com.benha.core.data.source.remote.retrofit.ApiResponse
import com.benha.core.data.source.remote.retrofit.ApiService
import com.benha.core.data.source.remote.response.RecipeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkDataSource @Inject constructor(
    private val service: ApiService
) {

    suspend fun fetchAllRecipes(): Flow<ApiResponse<List<RecipeResponse>>> {
        return flow {
            try {
                val result = service.getRecipes()
                val recipes = result.results
                if (recipes.isNotEmpty()) {
                    emit(ApiResponse.Success(recipes))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (exception: Exception) {
                emit(ApiResponse.Error(exception.toString()))
                Log.e("NetworkDataSource", exception.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun fetchRecipeDetailById(recipeId: Int): Flow<ApiResponse<RecipeResponse>> {
        return flow {
            try {
                val detailResult = service.getDetailRecipe(recipeId)
                emit(ApiResponse.Success(detailResult))
            } catch (exception: Exception) {
                emit(ApiResponse.Error(exception.toString()))
                Log.e("NetworkDataSource", exception.toString())
            }
        }.flowOn(Dispatchers.IO)
    }
}
