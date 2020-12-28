package com.zt.endexam.logic.model

data class tranSetGameStatus(val setGameStatus: Int)

private val gameStatus = mapOf(
        1 to tranSetGameStatus(8),
        2 to tranSetGameStatus(16),
        3 to tranSetGameStatus(24)
)

fun tranGamesetStatus( setGameStatus: Int):tranSetGameStatus {
    return gameStatus[setGameStatus] ?: gameStatus[1]!!
}