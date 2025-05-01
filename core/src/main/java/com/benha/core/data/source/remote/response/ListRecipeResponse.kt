package com.benha.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ListRecipeResponse(
    @field:SerializedName("offset")
    val offset: Int,

    @field:SerializedName("number")
    val number: Int,

    @field:SerializedName("totalResults")
    val totalResults: Int,

    @field:SerializedName("results")
    val results: List<RecipeResponse>
)
