package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.IntentCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.medialib.ui.NewPlaylistFragment
import com.example.playlistmaker.player.domain.mapper.TrackMapper
import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.player.domain.models.TracksData
import com.example.playlistmaker.player.ui.models.AddToPlaylistState
import com.example.playlistmaker.player.ui.presenter.MediaViewModel
import com.example.playlistmaker.player.ui.presenter.PlaylistBottomSheetAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class MediaActivity : AppCompatActivity() {

    companion object {
        const val TRACK_KEY = "TRACK"
    }

    private val mediaViewModel by viewModel<MediaViewModel>()

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    private var albumCover: ImageView? = null
    private var trackName: TextView? = null
    private var artistName: TextView? = null
    private var duration: TextView? = null
    private var albumName: TextView? = null
    private var year: TextView? = null
    private var genre: TextView? = null
    private var play: ImageView? = null
    private var time: TextView? = null
    private var country: TextView? = null
    private var favorite: ImageView? = null
    private var urlMedia: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        albumCover = findViewById(R.id.album_cover_media)
        trackName = findViewById(R.id.track_name_media)
        artistName = findViewById(R.id.artist_name_media)
        time = findViewById(R.id.time_media)
        duration = findViewById(R.id.duration_value_media)
        albumName = findViewById(R.id.albom_value_media)
        year = findViewById(R.id.year_value_media)
        genre = findViewById(R.id.genre_value_media)
        country = findViewById(R.id.country_value_media)
        play = findViewById(R.id.play_track_media)
        favorite = findViewById((R.id.add_favorites_media))

        val fragmentContainer = findViewById<FrameLayout>(R.id.fragment_container)
        val mainContainer = findViewById<ConstraintLayout>(R.id.container)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (fragmentContainer.isVisible) {
                    supportFragmentManager.popBackStack()
                    fragmentContainer.isVisible = false
                    mainContainer.isVisible = true
                } else {
                    finish()
                }
            }
        })

        //Активация тулбара для окна Медиа
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_media)
        if (toolbar != null) {
            setSupportActionBar(toolbar)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        IntentCompat.getParcelableExtra(intent, TRACK_KEY, TracksData::class.java)?.let {
            val track = TrackMapper.convertToTrack(it)
            mediaViewModel.preparePlayer(track)
        }

        mediaViewModel.mediaStateLive.observe(this, Observer { state ->
            state.track?.let {
                showDetailsTrack(it)
                play?.setImageResource(
                    if (state.isPlaying)
                        R.drawable.ic_pause
                    else
                        R.drawable.ic_play
                )
                time?.text = state.currentTime

                favorite?.setImageResource(
                    if (it.isFavorite)
                        R.drawable.ic_favorites
                    else
                        R.drawable.ic_add_favorites
                )
            }
        })

        play?.setOnClickListener {
            mediaViewModel.playbackControl()
        }

        favorite?.setOnClickListener {
            mediaViewModel.onFavoriteClicked()
        }

//___________________________________________________________________
        //для плейлистов BottomSheet
        mediaViewModel.showToPlaylistStateLive.observe(this) {
            if (it) {
                showBottomSheet()
            } else {
                hideBottomSheet()
            }
        }

        //добавление в плейлист, показ сообщения
        mediaViewModel.addToPlaylistStateLive.observe(this) { state ->
            when (state) {
                is AddToPlaylistState.Success -> {
                    Toast.makeText(
                        this,
                        getString(R.string.added_to_playlist, state.playlistName),
                        Toast.LENGTH_LONG
                    ).show()
                }

                is AddToPlaylistState.AlreadyAdded -> {
                    Toast.makeText(
                        this,
                        getString(R.string.already_in_playlist, state.playlistName),
                        Toast.LENGTH_LONG
                    ).show()
                }

                else -> {}
            }
        }
        //кнопка добавить в плейлист
        findViewById<ImageView>(R.id.add_track_media).setOnClickListener {
            mediaViewModel.addToPlaylistClicked()
        }
    }

    private fun showDetailsTrack(track: Track) {
        trackName?.text = track.trackName
        artistName?.text = track.artistName
        time?.text = dateFormat.format(0)
        duration?.text = dateFormat.format(track.trackTimeMillis)
        albumName?.text = track.collectionName
        year?.text = ZonedDateTime.parse(
            track.releaseDate,
            DateTimeFormatter.ISO_ZONED_DATE_TIME
        ).year.toString()
        genre?.text = track.primaryGenreName
        country?.text = track.country
        urlMedia = track.previewUrl
        Glide.with(albumCover!!.context)
            .load(track.artworkUrl)
            .centerCrop()
            .transform(RoundedCorners(albumCover!!.context.dpToPx(2f)))
            .placeholder(R.drawable.ic_placeholder)
            .into(albumCover!!)
    }

    //Обработка кнопки Назад
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == android.R.id.home) finish()
//        return true

        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        mediaViewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaViewModel.releaseMedia()
    }

    //------------------------------------------------------------------------
    //BottomSheet с плейлистами
    private fun showBottomSheet() {
        val bottomSheet = findViewById<LinearLayout>(R.id.playlists_bottom_sheet)
        val overlay = findViewById<View>(R.id.overlay)
        val fragment = findViewById<FrameLayout>(R.id.fragment_container)

        bottomSheet.isVisible = true
        //затемнение
        overlay.isVisible = true
        fragment.isVisible = false

        val behavior = BottomSheetBehavior.from(bottomSheet).apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            // изменение состояния BottomSheet:
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        hideBottomSheet()
                    }
                }
                //прокрутка bottomsheet вниз:
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    //полностью скрыт
                    overlay.alpha = slideOffset.coerceAtLeast(0f)
                }
            })
        }
        //загрузка списка плейлистов в Recycler
        loadPlaylistsRecycler()
    }

    private fun hideBottomSheet() {
        val bottomSheet = findViewById<LinearLayout>(R.id.playlists_bottom_sheet)
        val overlay = findViewById<View>(R.id.overlay)
        val fragment = findViewById<FrameLayout>(R.id.fragment_container)

        val behavior = BottomSheetBehavior.from(bottomSheet)
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
        //скрыть затемнение
        overlay.isVisible = false
        fragment.isVisible = false
    }

    private fun loadPlaylistsRecycler() {
        val recyclerView = findViewById<RecyclerView>(R.id.playlists_recycler_bottom_sheet)
        val newPlaylistBtn = findViewById<MaterialButton>(R.id.new_playlist_btn)

        recyclerView.layoutManager = LinearLayoutManager(this@MediaActivity)

        //адаптер с пустым списком
        val adapter = PlaylistBottomSheetAdapter(emptyList()).apply {
            onPlaylistClick = { playlist ->
//                mediaViewModel.mediaStateLive.value?.track?.let { track ->
//                    mediaViewModel.playlistSelected(playlist, track)
//                }
                //смотрим есть ли трек в плейлисте
                mediaViewModel.mediaStateLive.value?.track?.let { track ->
                    lifecycleScope.launch {
                        if (mediaViewModel.isTrackInPlaylist(track, playlist)) {
                            Toast.makeText(
                                this@MediaActivity,
                                getString(R.string.already_in_playlist, playlist.title),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            mediaViewModel.playlistSelected(playlist, track)
                            hideBottomSheet()
                        }
                    }
                }
            }
        }
        recyclerView.adapter = adapter

        //обновляем адаптер если есть изенения
        mediaViewModel.playlistsLive.observe(this) { playlists ->
            Log.d("AAA", "Observed playlists MediaActivity: ${playlists.size}")
            adapter.updatePlaylists(playlists)

            if (playlists.isEmpty()) {
                Log.w("AAA", "Playlists list is empty! MediaActivity")
            }
        }

        //обработка кнопки Новый плейлист
        newPlaylistBtn.setOnClickListener {
            hideBottomSheet()
            // Навигация
            findViewById<ConstraintLayout>(R.id.container).isVisible = false
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, NewPlaylistFragment.newInstance(true))
                .addToBackStack(null)
                .commit()

            findViewById<FrameLayout>(R.id.fragment_container).isVisible = true
        }
        mediaViewModel.loadPlaylists()
    }
}