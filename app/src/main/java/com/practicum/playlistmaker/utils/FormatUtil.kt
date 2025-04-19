package com.practicum.playlistmaker.utils

fun getWordForm(count: Int, list: List<String> = listOf("трек", "трека", "треков")): String {
    val mod10 = count % 10
    val mod100 = count % 100

    val word = when {
        mod10 == 1 && mod100 != 11 -> list[0]
        mod10 in 2..4 && (mod100 !in 12..14) -> list[1]
        else -> list[2]
    }

    return "$count $word"
}

fun getTimeWordForm(ms: Long): String {
    val minutes = ms / 1_000 / 60
    return getWordForm(minutes.toInt(), list = listOf("минута", "минуты", "минут"))
}