package com.practicum.playlistmaker.playlist.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistMakerBinding
import com.practicum.playlistmaker.playlist.presentation.models.Playlist
import com.practicum.playlistmaker.playlist.presentation.state.PlaylistMakerState
import com.practicum.playlistmaker.playlist.presentation.viewmodel.PlaylistViewModel
import com.practicum.playlistmaker.utils.dpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel

open class PlaylistMakerFragment : Fragment() {

    companion object {
        private const val POSTER_RADIUS = 8.0F
        const val NEW_PLAYLIST_RESULT = "NEW_PLAYLIST_RESULT"
    }

    var backPressedCallback: OnBackPressedCallback? = null

    private var _binding: FragmentPlaylistMakerBinding? = null
    val binding get() = _binding!!

    var fileUri: String = ""

    lateinit var playlistItem: Playlist

    open val viewModel: PlaylistViewModel by viewModel()

    private val confirmDialog by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.playlist_message_title)
            .setMessage(R.string.playlist_message_description)
            .setPositiveButton(R.string.playlist_message_done) { _, _ -> findNavController().navigateUp() }
            .setNegativeButton(R.string.playlist_message_cancel) { _, _ -> }
    }

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                fileUri = it.toString()
                Glide.with(this)
                    .load(uri)
                    .transform(
                        CenterCrop(),
                        RoundedCorners(dpToPx(POSTER_RADIUS, requireContext()))
                    )
                    .into(binding.poster)
                binding.poster.tag = requireActivity().getString(R.string.playlist_image_not_empty_tag)
            }
        }

    private fun create() {
        binding.createPlaylist.setOnClickListener {
            if (it.isEnabled) {
                val description = binding.wrapperDescription.editText?.text?.toString() ?: ""
                val title = binding.editTitle.editText?.text?.toString() ?: ""
                playlistItem = Playlist(
                    title = title,
                    description = description,
                    fileUri = fileUri
                )
                viewModel.addPlaylist(
                    playlistItem
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistMakerBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun isNotEmptyFields(): Boolean {
        return binding.playlistTitle.text.toString().isNotEmpty() ||
                binding.playlistDescription.text.toString().isNotEmpty() ||
                binding.poster.tag == requireActivity().getString(R.string.playlist_image_not_empty_tag)
    }

    private fun navigateBack() {
        if (isNotEmptyFields()) {
            confirmDialog.show()
        } else {
            findNavController().navigateUp()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonBack.setOnClickListener {
            navigateBack()
        }

        binding.poster.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.editTitle.editText?.doOnTextChanged { text, _, _, _ ->
            binding.createPlaylist.isEnabled = text.toString().isNotEmpty()
        }

        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateBack()
            }
        }
        backPressedCallback?.let { callback ->
            requireActivity().onBackPressedDispatcher.addCallback(callback)
        }

        create()
        unsetItemHandle()

        viewModel.getScreenStateLiveData().observe(viewLifecycleOwner) { playlistMakerState ->
            if (playlistMakerState is PlaylistMakerState.Create) {
                val text = "${requireContext().getString(R.string.playlist_maker)} ${playlistMakerState.playlistName} ${requireContext().getString(R.string.playlist_maker_create)}"
                Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
                playlistItem.playlistId = playlistMakerState.playlistId
                findNavController().previousBackStackEntry?.savedStateHandle?.set(NEW_PLAYLIST_RESULT, playlistItem)
                findNavController().navigateUp()
            }
        }
    }

    private fun unsetItemHandle() {
        findNavController().previousBackStackEntry?.savedStateHandle?.let { savedState ->
            if (savedState.contains(NEW_PLAYLIST_RESULT)) {
                savedState.remove<Playlist>(NEW_PLAYLIST_RESULT)
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}