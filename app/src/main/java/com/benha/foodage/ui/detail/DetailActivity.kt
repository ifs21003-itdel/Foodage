package com.benha.foodage.ui.detail

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.navArgs
import com.benha.core.data.Resource
import com.benha.core.domain.model.Recipe
import com.benha.core.utils.htmlParser
import com.benha.core.utils.setImageViewTint
import com.benha.core.utils.setTextViewColor
import com.benha.foodage.R
import com.benha.foodage.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val navArgs: DetailActivityArgs by navArgs()
    private val viewModel: DetailViewModel by viewModels()
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            show()
            title = "Recipes"
        }

        val selectedRecipeId = navArgs.id
        observeRecipeDetail(selectedRecipeId)

        binding.fabBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun observeRecipeDetail(recipeId: Int) {
        viewModel.getDetailRecipe(recipeId).observe(this) { result ->
            when (result) {
                is Resource.Loading -> showLoadingState()
                is Resource.Success -> showRecipeDetail(result.data)
                is Resource.Error -> showErrorState(result.message)
                else -> Unit
            }
        }
    }

    private fun showLoadingState() {
        binding.apply {
            pbDetail.visibility = View.VISIBLE
            layFrame.visibility = View.GONE
            clBadge.visibility = View.GONE
        }
    }

    private fun showRecipeDetail(recipe: Recipe?) {
        binding.pbDetail.visibility = View.GONE

        if (recipe == null) {
            showErrorState(getString(R.string.something_wrong))
            return
        }

        binding.apply {
            msgError.root.visibility = View.GONE
            layFrame.visibility = View.VISIBLE
            clBadge.visibility = View.VISIBLE

            Glide.with(this@DetailActivity)
                .load(recipe.image)
                .into(imgDetail)

            tvTitleDetail.text = recipe.title
            tvDesc.text = htmlParser(recipe.summary)

            updateBadges(recipe)

            viewModel.getRecipeById(recipe.recipeId).observe(this@DetailActivity) { dbRecipe ->
                isFavorite = dbRecipe.isFavorite == true
                updateFavoriteIcon(isFavorite)
            }

            fabFavbutton.setOnClickListener {
                isFavorite = !isFavorite
                viewModel.setFavoriteRecipe(recipe, isFavorite)
                updateFavoriteIcon(isFavorite)
            }
        }
    }

    private fun updateBadges(recipe: Recipe) = binding.run {
        setBadge(tvBadgeCheap, cbBadgeCheap, recipe.cheap)
        setBadge(tvBadgeVegan, cbBadgeVegan, recipe.vegan)
        setBadge(tvBadgeVegetarian, cbBadgeVegetarian, recipe.vegetarian)
        setBadge(tvBadgeHealthy, cbBadgeHealthy, recipe.veryHealthy)
        setBadge(tvBadgeGlutenFree, cbBadgeGlutenFree, recipe.glutenFree)
        setBadge(tvBadgeDairyFree, cbBadgeDairyFree, recipe.dairyFree)
    }

    private fun setBadge(textView: View, imageView: View, status: Boolean) {
        if (textView is android.widget.TextView) {
            setTextViewColor(applicationContext, textView, status.toString())
        }
        if (imageView is android.widget.ImageView) {
            setImageViewTint(applicationContext, imageView, status.toString())
        }
    }

    private fun updateFavoriteIcon(favorite: Boolean) {
        val iconRes = if (favorite) R.drawable.ic_bookmark else R.drawable.ic_no_bookmark
        binding.fabFavbutton.setImageDrawable(ContextCompat.getDrawable(this, iconRes))
    }

    private fun showErrorState(message: String?) {
        binding.apply {
            msgError.root.visibility = View.VISIBLE
            msgError.tvErrorText.text = message ?: getString(R.string.something_wrong)
            pbDetail.visibility = View.GONE
            layDetail.visibility = View.GONE
            layFrame.visibility = View.GONE
        }
    }
}
