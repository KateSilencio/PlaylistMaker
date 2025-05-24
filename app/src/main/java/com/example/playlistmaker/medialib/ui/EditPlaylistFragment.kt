package com.example.playlistmaker.medialib.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.medialib.ui.presentation.EditPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class EditPlaylistFragment : NewPlaylistFragment() {

    private val args: EditPlaylistFragmentArgs by navArgs<EditPlaylistFragmentArgs>()
    private val viewModel: EditPlaylistViewModel by viewModel { parametersOf(args.playlist) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarNewPlaylist.title = getString(R.string.edit_playlist)
        binding.createPlaylistBtn.text = getString(R.string.save)

        // Заполняем данные
        binding.inputTextTitle.setText(args.playlist.title)
        binding.inputTextDescription.setText(args.playlist.description ?: "")
        args.playlist.coverUri?.let { uri ->
            selectedImageUri = uri.toUri()
            loadExistingCover(uri)
        }

        // кн Назад
        binding.toolbarNewPlaylist.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        //системная кн Назад
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            editBackPressedCallback
        )

        //переопределяем кн Создать
        binding.createPlaylistBtn.setOnClickListener {
            //смотрим изменилась ли обложка
            val currentCoverUri = args.playlist.coverUri?.toUri()
            val coverToSave = if (selectedImageUri != currentCoverUri) {
                selectedImageUri
            } else {
                null
            }
            viewModel.savePlaylist(
                binding.inputTextTitle.text.toString(),
                binding.inputTextDescription.text?.toString(),
                coverToSave
            )
        }

        viewModel.saveResultLiveData.observe(viewLifecycleOwner) { success ->
            if (success) {
                findNavController().navigateUp()
            }
        }
    }

    private fun loadExistingCover(coverUri: String) {
        Glide.with(binding.playlistCover)
            .load(coverUri)
            .placeholder(R.drawable.ic_placeholder)
            .into(binding.playlistCover)

        binding.playlistCover.background = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corners)
    }
    //Системная Назад - закрытие без диалога
    private val editBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            findNavController().navigateUp()
        }
    }
}