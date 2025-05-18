package com.example.playlistmaker.medialib.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.BundleCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistScreenBinding
import com.example.playlistmaker.medialib.domain.model.Playlist
import com.example.playlistmaker.medialib.ui.presentation.PlaylistScreenViewModel
import com.example.playlistmaker.medialib.ui.presentation.models.PlaylistShareState
import com.example.playlistmaker.player.domain.models.TracksData
import com.example.playlistmaker.player.ui.MediaActivity
import com.example.playlistmaker.search.ui.TracksAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlaylistScreenFragment : Fragment() {

    private val screenViewModel by viewModel<PlaylistScreenViewModel>()

    private lateinit var binding: FragmentPlaylistScreenBinding
    private lateinit var tracksAdapter: TracksAdapter
    //BottomSheet для списка треков
    private lateinit var bottomSheetListBehavior: BottomSheetBehavior<LinearLayout>
    //BottomSheet для menu
    private lateinit var menuBottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    //затемнение
    private lateinit var menuOverlay: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //кнопка назад
        binding.toolbarPlaylistScreen.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val playlist =
            BundleCompat.getParcelable(requireArguments(), "playlist", Playlist::class.java)
        playlist?.let {
            screenViewModel.loadPlaylistData(it.id)
        }
// ****************************************************************************
        // Инициализация Bottom Sheet меню
        val menuBottomSheet = binding.root.findViewById<LinearLayout>(R.id.menu_bottom_sheet)
        menuOverlay = binding.root.findViewById(R.id.menu_overlay)
        menuBottomSheetBehavior = BottomSheetBehavior.from(menuBottomSheet)

        menuBottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> hideMenu()
                    else -> {}
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // Анимация затемнения
                menuOverlay.alpha = slideOffset.coerceAtLeast(0f)
            }
        })

// ****************************************************************************
        //инициализация BottomSheet список пока такая
        bottomSheetListBehavior = BottomSheetBehavior.from(binding.tracksPlaylistContent).apply {


            //isFitToContents = false
//            skipCollapsed = false

            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            // прокрутка RecyclerView при раскрытии
                            binding.tracksInPlaylistRecycler.isNestedScrollingEnabled = false
                        }

                        else -> {
                            binding.tracksInPlaylistRecycler.isNestedScrollingEnabled = true
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {

                }
            })
            //state = BottomSheetBehavior.STATE_COLLAPSED
        }
// **************************************************************************
        //инициализацция recycler и adapter для списка треков
//        tracksAdapter = TracksAdapter(mutableListOf()).apply {
//            setOnClickListener { track ->
//                // переход в аудиоплеер
//                val intent = Intent(requireContext(), MediaActivity::class.java).apply {
//                    putExtra(MediaActivity.TRACK_KEY, track)
//                }
//                startActivity(intent)
//            }
//        }
//        binding.tracksInPlaylistRecycler.apply {
//            adapter = tracksAdapter
//            layoutManager = LinearLayoutManager(requireContext())
//        }

        //кнопка поделиться
        binding.sharePlaylist.setOnClickListener {
            screenViewModel.prepareShareContent()
        }

        // Подписка на состояние Поделиться
        screenViewModel.shareStateLive.observe(viewLifecycleOwner) { state ->
            setupShareState(state)
        }
        //кн Меню
        binding.menuPlaylist.setOnClickListener {
            showMenu()
        }

        // кн меню Поделиться
        menuBottomSheet.findViewById<TextView>(R.id.share_menu_item).setOnClickListener {
            screenViewModel.prepareShareContent()
            hideMenu()
        }

        // Подписки на данные плейлиста
        setupObservers()
    }

    private fun setupShareState(state: PlaylistShareState) {

        when (state) {
            is PlaylistShareState.Empty -> {
                showEmptyPlaylistMessage()
            }
            is PlaylistShareState.WithTracks -> {
                screenViewModel.executeShare()
            }
        }
    }

    //показ сообщения при пустом плейлисте
    private fun showEmptyPlaylistMessage() {

//        Toast.makeText(
//            requireContext(),
//            getString(R.string.empty_playlist_share_message),
//            Toast.LENGTH_LONG
//        ).show()
        Snackbar.make(
            binding.root,
            getString(R.string.empty_playlist_share_message),
            Snackbar.LENGTH_LONG
        ).apply {
//            setBackgroundTint(
//                ContextCompat.getColor(requireContext(), R.color.snackbar_background)
//            )
            animationMode = Snackbar.ANIMATION_MODE_FADE
        }.show()
    }

    private fun setupObservers() {
        //Подписка на данные плейлиста
        screenViewModel.playlistScrLive.observe(viewLifecycleOwner) { playlist ->
            with(binding) {
                namePlaylistScreen.text = playlist.title
                descriptionPlaylistScreen.text = playlist.description ?: ""
                countTracksPlaylistScreen.text =
                    resources.getQuantityString(
                        R.plurals.tracks_count,
                        playlist.trackCount,
                        playlist.trackCount
                    )
                if (playlist.coverUri != null) {
                    Glide.with(playlistCoverScreen)
                        .load(playlist.coverUri)
                        .into(playlistCoverScreen)
                } else {
                    playlistCoverScreen.setImageResource(R.drawable.ic_placeholder)
                }
            }
        }

        // Подписка на длительность
        screenViewModel.durationLive.observe(viewLifecycleOwner) { duration ->
            //binding.minutesPlaylistScreen.text = duration

            binding.minutesPlaylistScreen.text = resources.getQuantityString(
                R.plurals.minutes_count,
                duration,
                duration
            )
        }

        // Подписка на список треков
        screenViewModel.tracksLive.observe(viewLifecycleOwner) { tracks ->
            if (tracks.isNotEmpty()) {


                tracksAdapter = TracksAdapter(mutableListOf()).apply {
                    setOnClickListener { track ->
                        // переход в аудиоплеер
                        val intent = Intent(requireContext(), MediaActivity::class.java).apply {
                            putExtra(MediaActivity.TRACK_KEY, track)
                        }
                        startActivity(intent)
                    }
                }
                binding.tracksInPlaylistRecycler.apply {
                    adapter = tracksAdapter
                    layoutManager = LinearLayoutManager(requireContext())
                }

                // bottomsheet только если есть треки
                //binding.tracksPlaylistContent.isVisible = true
                tracksAdapter.updateTracks(tracks)

                //работа с размером bottomsheet
                setupBottomSheetMaxHeight()
//
//                //разворачиваем bottomsheet
//                binding.tracksPlaylistContent.post {
//                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
//                }


//                binding.tracksPlaylistContent.post {
                //setupBottomSheetMaxHeight()

                // Раскрытие с задержкой EXPANDED
//                    binding.tracksPlaylistContent.postDelayed({
//                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
//                    }, 100)
                //               }

                // Обработка долгого нажатия для удаления трека
                tracksAdapter.onLongClickListener = { track ->
                    showDeleteDialog(track)
                    true
                }
            } else {
                binding.tracksPlaylistContent.isVisible = false
                bottomSheetListBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    private fun setupBottomSheetMaxHeight() {
        view?.post {
//            // Вычисляем доступную высоту от нижней границы кнопок до низа экрана
//            val shareButton = binding.sharePlaylist
//            //получаем высоту экрана
//            val screenHeight = resources.displayMetrics.heightPixels
//            //вычисляем нижнюю границу кнопки
//            val buttonBottom = shareButton.bottom + shareButton.height
//
//            // устанавливаем максимальную высоту
//            // это разность между высотой экрана и нижней границей кнопки
//            val maxHeight = (screenHeight - buttonBottom).coerceAtLeast(bottomSheetBehavior.peekHeight)
//            //применяем эту высоту к bottomsheet
//            binding.tracksPlaylistContent.layoutParams.height = maxHeight
//            binding.tracksPlaylistContent.requestLayout()
//            //обновляем поведение bottomsheet
//            bottomSheetBehavior.isFitToContents = false
//            bottomSheetBehavior.maxHeight = maxHeight
//_________________________________________________________________________________
            //        Получаем высоту экрана
            val displayMetrics = resources.displayMetrics
            val screenHeight = displayMetrics.heightPixels

            // Получаем положение кнопок share
            val shareButton = binding.sharePlaylist

            // Находим нижнюю границу кнопок (берем максимальную из двух)
            val buttonsBottom = shareButton.bottom

            // Вычисляем доступное пространство от нижней границы кнопок до низа экрана
            val availableHeight = screenHeight - buttonsBottom

            // Устанавливаем максимальную высоту (все доступное пространство)
            //bottomSheetBehavior.maxHeight = availableHeight

            binding.tracksPlaylistContent.layoutParams.height = availableHeight
            binding.tracksPlaylistContent.requestLayout()

            // Настраиваем поведение дублирование кода???
            //bottomSheetBehavior.isHideable = false
            bottomSheetListBehavior.isFitToContents = false
            //bottomSheetBehavior.skipCollapsed = false
//_____________________________________________________________________

        }
    }

    private fun showDeleteDialog(track: TracksData) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_track_title))
            .setMessage(getString(R.string.delete_track_message))
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.delete)) { dialog, _ ->
                val playlist = BundleCompat.getParcelable(
                    requireArguments(),
                    "playlist",
                    Playlist::class.java
                )
                playlist?.let {
                    screenViewModel.deleteTrack(it.id, track.trackID)
                }
                dialog.dismiss()
            }
            .show()
    }

// показать меню
private fun showMenu() {
    // Заполняем информацию о плейлисте в шапке
    val playlist = screenViewModel.playlistScrLive.value
    playlist?.let {
        val playlistInfo = binding.root.findViewById<ConstraintLayout>(R.id.playlist_info_header)
        playlistInfo.findViewById<ImageView>(R.id.image_cover_recycler).let { imageView ->
            if (playlist.coverUri != null) {
                Glide.with(imageView)
                    .load(playlist.coverUri)
                    .into(imageView)
            } else {
                imageView.setImageResource(R.drawable.ic_placeholder)
            }
        }
        playlistInfo.findViewById<TextView>(R.id.playlist_name_recycler).text = playlist.title
        playlistInfo.findViewById<TextView>(R.id.count_tracks_recycler).text =
            resources.getQuantityString(R.plurals.tracks_count, playlist.trackCount, playlist.trackCount)
    }

    // Показываем меню BottomSheet
    menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    menuOverlay.isVisible = true
    menuOverlay.setOnClickListener { hideMenu() }
}

    private fun hideMenu() {
        menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        menuOverlay.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}