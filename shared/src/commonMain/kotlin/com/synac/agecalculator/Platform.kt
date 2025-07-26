package com.synac.agecalculator

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform