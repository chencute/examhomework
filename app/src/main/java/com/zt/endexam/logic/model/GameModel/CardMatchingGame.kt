package com.zt.endexam.logic.model.GameModel

//抽牌核心逻辑 count抽牌数
class CardMatchingGame(val count:Int) {
    var score = 0
        private set

    //游戏的牌
    var cards: MutableList<Card>

    init {
        val deck = Deck()
        cards = mutableListOf()
//        构造count张牌
        for (i in 1..count) {
            val card:Card? = deck.drawRandomCard()
            if (card != null) {
                cards.add(card)
            }
        }
    }

    //    重新设置
    fun reset(cardCount:Int) {
        val deck = Deck()
        score = 0
        cards.clear()
//        构造count张牌
        for (i in 1..cardCount) {
            val card:Card? = deck.drawRandomCard()
            if (card != null) {
                cards.add(card)
            }
        }
    }

    //    获得当前牌的索引
    fun cardAtIndex(index: Int):Card {
        return cards.get(index)
    }

    val MISMATCH_PENALTY = 2 //猜错扣2分
    val MTACH_BONUS = 3 //猜对得分
    val COST_TO_SHOOSE = 1//翻牌扣1分

    //    根据下标来抽牌
    fun chooseCardAtIndex(index: Int) {
        val card = cardAtIndex(index)
        if (!card.isMatched) {//没有被匹配
            if (card.isChosen) {//被选择，则翻到另一面
                card.isChosen = false
            } else {
                for (otherCard in cards) {
                    if (otherCard.isChosen && !otherCard.isMatched) {
                        val matchScore = card.match(arrayOf(otherCard))
                        if (matchScore > 0) {//匹配
                            score += matchScore * MTACH_BONUS
                            otherCard.isMatched = true
                            card.isMatched = true
                        } else {
                            score -= MISMATCH_PENALTY
                            otherCard.isChosen = true
                            otherCard.isMatched = true
                            card.isMatched = true
                        }
                        break
                    }
                }
                score -= COST_TO_SHOOSE
                card.isChosen = true
            }
        }
    }
}