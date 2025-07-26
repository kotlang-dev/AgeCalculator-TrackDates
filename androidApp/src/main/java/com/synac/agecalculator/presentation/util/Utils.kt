package com.synac.agecalculator.presentation.util

import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun String.encodeUrl(): String = URLEncoder.encode(this, StandardCharsets.UTF_8.toString())

fun String.decodeUrl(): String = URLDecoder.decode(this, StandardCharsets.UTF_8.toString())