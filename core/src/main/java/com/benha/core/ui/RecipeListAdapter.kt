package com.benha.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.benha.core.R
import com.benha.core.databinding.ItemListRecipeBinding
import com.benha.core.domain.model.Recipe
import com.benha.core.utils.htmlParser
import com.bumptech.glide.Glide

class RecipeListAdapter(
    private var recipeItems: List<Recipe>
) : RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder>() {

    private lateinit var onRecipeItemClickListener: OnRecipeItemClickListener

    fun setOnRecipeItemClickListener(listener: OnRecipeItemClickListener) {
        this.onRecipeItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val itemBinding = ItemListRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return recipeItems.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipeItems[position]
        Glide.with(holder.itemView.context)
            .load(recipe.image)
            .into(holder.binding.imgRecipe)

        holder.apply {
            binding.txtTitle.text = recipe.title
            binding.txtDescription.text = htmlParser(recipe.summary)
            binding.icHealth.setImageResource(
                if (recipe.veryHealthy) R.drawable.ic_health else R.drawable.ic_no_health
            )
            binding.icVegetarian.setImageResource(
                if (recipe.vegetarian) R.drawable.ic_vegetarian else R.drawable.ic_non_vegetarian
            )
            binding.icCheap.setImageResource(
                if (recipe.cheap) R.drawable.ic_cheap else R.drawable.ic_expensive
            )
            binding.txtTimeMinute.text = recipe.readyInMinutes.toString()
            binding.cardView.setOnClickListener {
                onRecipeItemClickListener.onRecipeItemClicked(recipeItems[holder.adapterPosition])
            }
        }
    }

    class RecipeViewHolder(val binding: ItemListRecipeBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnRecipeItemClickListener {
        fun onRecipeItemClicked(recipe: Recipe)
    }
}