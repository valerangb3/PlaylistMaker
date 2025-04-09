package com.practicum.playlistmaker.playlist.ui.fragments

import android.os.Bundle
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
import java.io.File
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistMakerFragment : Fragment() {

    companion object {
        private const val POSTER_RADIUS = 8.0F
    }

    private var _binding: FragmentPlaylistMakerBinding? = null
    private val binding get() = _binding!!

    private var fileUri: String = ""

    private val viewModel: PlaylistViewModel by viewModel()


    private val confirmDialog by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.playlist_message_title)
            .setMessage(R.string.playlist_message_description)
            .setPositiveButton(R.string.playlist_message_done) { dialog, which -> findNavController().navigateUp() }
            .setNegativeButton(R.string.playlist_message_cancel) { dialog, which -> }
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
                viewModel.addPlaylist(
                    Playlist(
                        title = title,
                        description = description,
                        fileUri = fileUri
                    )
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

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateBack()
            }
        })

        create()

        viewModel.getScreenStateLiveData().observe(viewLifecycleOwner) { playlistMakerState ->
            if (playlistMakerState is PlaylistMakerState.Create) {
                val text = "${requireContext().getString(R.string.playlist_maker)} ${playlistMakerState.playlistName} ${requireContext().getString(R.string.playlist_maker_create)}"
                Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}