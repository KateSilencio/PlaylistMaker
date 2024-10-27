package com.example.playlistmaker.medialib.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.playlistmaker.databinding.FragmentFavoriteBinding
import com.example.playlistmaker.medialib.ui.presentation.FavoriteTracksViewModel

class FavoriteTracksFragment: Fragment() {

    private val favoriteTracksViewModel by viewModels<FavoriteTracksViewModel>()

    private var _binding: FragmentFavoriteBinding?= null
    private val binding get() = _binding!! //доступ к объекту ViewBinding вызов геттера

    companion object{
        fun newInstance() = FavoriteTracksFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater,container,false)
        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}