package com.practicum.playlistmaker.playlist.ui.fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
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
import com.practicum.playlistmaker.utils.dpToPx
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import java.sql.Timestamp
import java.time.Instant

class PlaylistMakerFragment : Fragment() {

    companion object {
        private const val POSTER_RADIUS = 8.0F
    }

    private var _binding: FragmentPlaylistMakerBinding? = null
    private val binding get() = _binding!!


    private val confirmDialog by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.playlist_message_title)
            .setMessage(R.string.playlist_message_description)
            .setPositiveButton(R.string.playlist_message_done) { dialog, which -> findNavController().navigateUp() }
            .setNegativeButton(R.string.playlist_message_cancel) { dialog, which -> }
    }

    private fun genFileName(salt: String): String = md5Hash(salt)

    private fun md5Hash(salt: String): String {
        val md = MessageDigest.getInstance("MD5")
        val bigInt = BigInteger(1, md.digest(salt.toByteArray(Charsets.UTF_8)))
        return String.format("%032x", bigInt)
    }

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {

                //binding.poster
                val filename = genFileName(File(it.toString()).name)
                Log.d("SPRINT_22", uri.path.toString())
                File(it.toString()).name
                Glide.with(this)
                    .load(uri)
                    .transform(
                        CenterCrop(),
                        RoundedCorners(dpToPx(POSTER_RADIUS, requireContext()))
                    )
                    .into(binding.poster)
                binding.poster.tag = requireActivity().getString(R.string.playlist_image_not_empty_tag)
            }
            if (uri != null) {

                Log.d("SPRINT_22", uri.path.toString())
            } else {
                Log.d("SPRINT_22", "No media selected")
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
            Log.d("SPRINT_22", text.toString())
            binding.createPlaylist.isEnabled = text.toString().isNotEmpty()
        }

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateBack()
            }
        })
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}