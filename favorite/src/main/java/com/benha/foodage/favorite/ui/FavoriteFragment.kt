package com.benha.foodage.favorite.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.benha.core.domain.model.Recipe
import com.benha.core.ui.RecipeListAdapter
import com.benha.foodage.di.FavoriteModuleDependencies
import com.benha.foodage.favorite.databinding.FragmentFavoriteBinding
import com.benha.foodage.favorite.di.FavoriteComponent
import com.archico.foodage.favorite.viewmodel.FavoriteViewModel
import com.archico.foodage.favorite.viewmodel.ViewModelFactory
import com.benha.foodage.favorite.di.DaggerFavoriteComponent
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var factory: ViewModelFactory
    private val favoriteViewModel: FavoriteViewModel by viewModels {
        factory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFavorite.layoutManager = layoutManager
        binding.rvFavorite.setHasFixedSize(true)

        favoriteViewModel.getFavoriteRecipe().observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.rvFavorite.visibility = View.GONE
                binding.msgNoData.visibility = View.VISIBLE
            } else {
                binding.rvFavorite.visibility = View.VISIBLE
                binding.msgNoData.visibility = View.GONE
                setData(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onAttach(context: Context) {
        DaggerFavoriteComponent.builder()
            .context(requireActivity())
            .appDependencies(
                EntryPointAccessors.fromApplication(
                    requireActivity().applicationContext,
                    FavoriteModuleDependencies::class.java
                )
            )
            .build()
            .inject(this)
        super.onAttach(context)
    }

    private fun setData(it: List<Recipe>) {
        val adapter = RecipeListAdapter(it)
        adapter.setOnRecipeItemClickListener(object : RecipeListAdapter.OnRecipeItemClickListener { // Corrected interface name
            override fun onRecipeItemClicked(data: Recipe) { // Corrected method name
                val action = FavoriteFragmentDirections.actionNavigationFavoriteToDetailActivity(
                    data.recipeId
                )
                findNavController().navigate(action)
            }
        })
        binding.rvFavorite.adapter = adapter
    }

}