package com.example.playlistmaker.medialib.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.medialib.domain.model.Playlist
import com.example.playlistmaker.medialib.ui.presentation.PlaylistAdapter
import com.example.playlistmaker.medialib.ui.presentation.PlaylistShowViewModel
import com.example.playlistmaker.medialib.ui.presentation.models.PlaylistState
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment: Fragment() {

    private val playlistShowViewModel by viewModel<PlaylistShowViewModel>()

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PlaylistAdapter

    companion object{
        fun newInstance() = PlaylistsFragment()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newPlaylistBtn.setOnClickListener {
            parentFragment?.findNavController()?.navigate(R.id.action_mediaLibFragment_to_newPlaylistFragment)
        }

        adapter = PlaylistAdapter()

        binding.playlistsRecycler.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = this@PlaylistsFragment.adapter
        }

        playlistShowViewModel.showPlaylistLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistState.LoadedPlaylist -> showPlaylists(state.playlists)
                is PlaylistState.Empty -> showEmptyState()
                is PlaylistState.Error -> Log.e("ERROR","Error loading playlists")
            }
        }
    }

    private fun showPlaylists(playlists: List<Playlist>) {
        binding.emptyStateView.isVisible = false
        binding.playlistsRecycler.isVisible = true
        adapter.playlists = playlists
    }

    private fun showEmptyState() {
        binding.emptyStateView.isVisible = true
        binding.playlistsRecycler.isVisible = false
    }

    override fun onResume() {
        super.onResume()
        playlistShowViewModel.loadPlaylists()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}