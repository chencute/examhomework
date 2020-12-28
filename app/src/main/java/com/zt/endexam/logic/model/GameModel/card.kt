package com.zt.endexam.logic.model.GameModel


//suit表示花色，rank表示数字，isChosen表示是否被选中，isMacthed表示是否被匹配
class Card (private var suit: String,private var rank: String,var isChosen: Boolean = false,var isMatched: Boolean = false) {

    companion object {//类似于静态属性
    val rankStrings = arrayOf("A","2","3","4","5","6","7","8","9","10","J","Q","K")
        val validSuits = arrayOf("♥", "♦", "♠", "♣")
    }

    //牌内容
    override fun toString(): String {
        return suit + rank
    }

    //    匹配查看
    fun match(otherCards: Array<Card>): Int {
        var score = 0 //没有匹配成功为0分
    //    只有一张牌的比较
        if (otherCards.size == 1) {
            val otherCards = otherCards.first()//获得第一张牌
            if (otherCards.rank == rank) {//花色相等
                score = 2
            }
            if (otherCards.suit == suit) { //数字相等
                score = score + 1
            }
        }
        return score
    }
}