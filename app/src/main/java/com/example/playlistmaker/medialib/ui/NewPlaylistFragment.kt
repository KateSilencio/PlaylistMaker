package com.example.playlistmaker.medialib.ui

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.medialib.ui.presentation.PlaylistsViewModel
import com.example.playlistmaker.medialib.ui.presentation.models.PlaylistCreationState
import com.example.playlistmaker.player.ui.MediaActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlaylistFragment : Fragment() {

    //новый экземпляр `NewPlaylistFragment` передаем аргументы через Bundle
    companion object {
        private const val ARG_CALLED_FROM_ACTIVITY = "called_from_activity"

        fun newInstance(calledFromActivity: Boolean = false): NewPlaylistFragment {
            return NewPlaylistFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_CALLED_FROM_ACTIVITY, calledFromActivity)
                }
            }
        }
    }
    //был ли фрагмент вызван из активности или нет
    private var calledFromActivity: Boolean = false

    private val playlistViewModel by viewModel<PlaylistsViewModel>()
    private lateinit var binding: FragmentNewPlaylistBinding
    private var hasTitle = false
    private var hasDescription = false


    private var selectedImageUri: Uri? = null
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                selectedImageUri = it
                loadCoverImage(it)
                updateDataInViewModel()
            }
        }

    // извлек переданные аргументы до создания фрагмента
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        calledFromActivity = arguments?.getBoolean(
            ARG_CALLED_FROM_ACTIVITY,
            false
        ) ?: false
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // тулбар обработка Назад
        binding.toolbarNewPlaylist.setNavigationOnClickListener {
            if (playlistViewModel.hasChanges) {
                // есть изменения -> показываем диалог
                playlistViewModel.onBackPressed()
            } else {
                // нет изменений -> закрываем
                handleBackNavigation()
            }
        }

        //наблюдение за плейлистом
        playlistViewModel.creationStateLiveData.observe(viewLifecycleOwner){ state ->
            when(state){
                is PlaylistCreationState.Saving -> {  Log.d("DEBUG", "Сохранение ")}
                is PlaylistCreationState.Success -> {
                    showSuccessToast(state.playlistName)
                    handleBackNavigation()
                }
                is PlaylistCreationState.Canceled -> { handleBackNavigation() }
                else -> { /*Заглушка*/ }
            }

        }
        //наблюдение за диалогом
        playlistViewModel.showDiscardDialogLiveData.observe(viewLifecycleOwner){show->
            if (show){
                showDiscardDialog()
            }
        }

        // для поля "Название"
        binding.inputTextTitle.apply {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    updateDataInViewModel()
                    hasTitle = !s.isNullOrEmpty()
                    setButtonColor(hasTitle)
                    updateTitle(hasFocus())
                }

                override fun afterTextChanged(s: Editable?) {
                    updateTitle(hasFocus())
                }
            })
        }

        // для поля "Описание"
        binding.inputTextDescription.apply {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    updateDataInViewModel()
                    hasDescription = !s.isNullOrEmpty()
                    updateDescription(hasFocus())
                }

                override fun afterTextChanged(s: Editable?) {
                    updateDescription(hasFocus())
                }
            })
        }

        //выбор обложки
        binding.playlistCover.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        //кнопка Создать
        binding.createPlaylistBtn.setOnClickListener {
            playlistViewModel.savePlaylist()
        }
        //обработчик нажатия Назад
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )


    }
    //работа с диалогом
    private fun showDiscardDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.playlist_creation_title))
            .setMessage(getString(R.string.playlist_creation_message))
            .setNegativeButton(getString(R.string.cancel)){ dialog, _ ->
                playlistViewModel.cancelDiscard()
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.finish)){ dialog, _ ->
                playlistViewModel.confirmDiscard()
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    private  fun showSuccessToast(playlistName: String){

        val snackbarLayout = layoutInflater.inflate(
            R.layout.snackbar_message,
            //null,
            requireView().findViewById(android.R.id.content),
            false
        ) as ViewGroup
        val textView = snackbarLayout.findViewById<TextView>(R.id.snackbar_text)
        textView.text = getString(R.string.created_message,playlistName)

        val snackbar = Snackbar.make(requireView(),"",Snackbar.LENGTH_LONG)
            //из-за ограничений Snackbar.SnackbarLayout работаем через рефлексию
        try {
            val snackbarView = snackbar.view
            val snackbarLayoutClass = snackbarView.javaClass
            val addViewMethod = snackbarLayoutClass.getMethod(
                "addView",
                View::class.java,
                Int::class.java
            )
            addViewMethod.invoke(snackbarView, snackbarLayout, 0)
        } catch (e: Exception) {
            snackbar.setText(getString(R.string.created_message, playlistName))
        }
        snackbar.show()
//        Toast.makeText(
//            requireContext(),
//            getString(R.string.created_message,playlistName),
//            Toast.LENGTH_LONG).show()
    }

    private fun updateDataInViewModel(){
        playlistViewModel.updatePlaylistData(
            title = binding.inputTextTitle.text.toString(),
            description = binding.inputTextDescription.text?.toString(),
            coverUri = selectedImageUri
        )
    }
    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun setButtonColor(hasText: Boolean) {
        //актив/неактив кнопка
        binding.createPlaylistBtn.isEnabled = hasText
        binding.createPlaylistBtn.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                if (hasText) R.color.background_main else R.color.text_search_color
            )
        )
    }

    private fun updateColor(hasContent: Boolean, inputLayout: TextInputLayout) {
        val colorRes = if (hasContent) {
            R.color.background_main
        } else {
            R.color.text_search_color
        }
        inputLayout.boxStrokeColor = ContextCompat.getColor(requireContext(), colorRes)
    }

    private fun updateTitle(hasFocus: Boolean) {
        updateColor(hasTitle, binding.inputTitlePlaylist)
    }


    private fun updateDescription(hasFocus: Boolean) {
        updateColor(hasDescription, binding.inputDescriptionPlaylist)
    }

    //Методы работы с загрузкой изображения
    private fun loadCoverImage(uri: Uri) {

        try {
            val source = ImageDecoder.createSource(requireContext().contentResolver, uri)
            val drawable = ImageDecoder.decodeDrawable(source)

            binding.playlistCover.apply {
                setImageDrawable(drawable)
                background = ContextCompat.getDrawable(context, R.drawable.rounded_corners)
            }

        } catch (e: Exception) {
            Log.e("ERROR", "Error loading image photo picker", e)
        }
    }

    //нажатие назад
    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            playlistViewModel.onBackPressed()
        }
    }

    private fun handleBackNavigation() {
        if (calledFromActivity) {
            parentFragmentManager.popBackStack()
            // Явно скрываем контейнер и показываем основной контент
            (requireActivity() as MediaActivity).apply {
                findViewById<FrameLayout>(R.id.fragment_container).isVisible = false
                findViewById<ConstraintLayout>(R.id.container).isVisible = true
            }
        } else {
            // вызван из фрагмента
            findNavController().navigateUp()
        }
    }
}