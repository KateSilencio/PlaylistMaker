package com.example.playlistmaker.medialib.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavoriteBinding
import com.example.playlistmaker.medialib.ui.presentation.FavoriteTracksViewModel
import com.example.playlistmaker.medialib.ui.presentation.models.FavoriteTracksState
import com.example.playlistmaker.player.domain.models.TracksData
import com.example.playlistmaker.player.ui.MediaActivity
import com.example.playlistmaker.search.ui.TracksAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment: Fragment() {

    private val favoriteTracksViewModel by viewModel<FavoriteTracksViewModel>()

    private var _binding: FragmentFavoriteBinding?= null
    private val binding get() = _binding!! //доступ к объекту ViewBinding вызов геттера
    private lateinit var adapter: TracksAdapter
    private val trackList = ArrayList<TracksData>()

    companion object{
        fun newInstance() = FavoriteTracksFragment()

        private const val TRACK_KEY = "TRACK"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = TracksAdapter(trackList)
        binding.recyclerFavorite.adapter = adapter

        favoriteTracksViewModel.favoriteStateLive.observe(viewLifecycleOwner){
            when(it){
                is FavoriteTracksState.Loading -> {}
                is FavoriteTracksState.LoadedTracks -> showLoadedTracksState(it.tracks)
                FavoriteTracksState.NoData -> showNoDataState()
            }
        }
        adapter.setOnClickListener {track->
            val  context = requireContext()
            val intent = Intent(context, MediaActivity::class.java)
            intent.putExtra(TRACK_KEY, track)
            context.startActivity(intent)
        }
    }

    private fun showNoDataState(){
        binding.noDataMediaLib.isVisible = true
        binding.mediaLibIsEmpty.isVisible = true
        binding.recyclerFavorite.isVisible = false
    }

    private fun showLoadedTracksState(tracks: List<TracksData>){
        adapter.updateTracks(tracks)
        binding.noDataMediaLib.isVisible = false
        binding.mediaLibIsEmpty.isVisible = false
        binding.recyclerFavorite.isVisible = true
}


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}