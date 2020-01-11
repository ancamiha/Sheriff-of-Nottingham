package com.tema1.players;

import com.tema1.goods.GoodsFactory;
import java.util.ArrayList;
import java.util.Collections;

public class Greedy extends Basic {

    public Greedy(final int id, final PlayersType type, final String strategy,
                  final int coins) {
        super(id, type, strategy, coins);
    }

    /**
     * Creeaza sacul comerciantului cu strategie greedy si se declara tipul bunului.
     * @param cards lista de carti
     * @param round runda necesara alegerii cartilor
     * @return returneaza lista de carti alese pentru inspectie
     */
    public final ArrayList<Integer> makeBagGreedy(final ArrayList<Integer> cards, final int round) {
        ArrayList<Integer> goodCards = new ArrayList<>();
        GoodsFactory goodsFactory = GoodsFactory.getInstance();
        final int maxCardsInHand = 8;
        final int ten = 10;
        int ok = 0; //pp ca nu a adaugat nicio carte ilegala
        goodCards = makeBag(cards);
        //daca se afla in runda para adauga o carte ilegala
        if (round % 2 == 0) {
            int maxProfitCard = 0;
            if (goodCards.size() < maxCardsInHand) {
                ProfitComparatorBribed profitComparatorBribed = new ProfitComparatorBribed();
                Collections.sort(cards, profitComparatorBribed);
                for (int card : cards) {
                    if (card > ten) {
                        maxProfitCard = card;
                        break;
                    }
                }
                //se adauga cartea ilegala
                if (maxProfitCard != 0) {
                    ok = 1;
                    goodCards.add(maxProfitCard);
                }
            }
        }
        if (ok == 1) {
            this.setHonesty(PlayerHonesty.Liar);
        }
        this.setBribe(0);
        return goodCards;
    }

    /**
     * Are loc inspectia jucatorilor cu exceptia celor care ofera mita.
     * @param merchant jucatorul ce este comerciant
     * @param goodsInBag lista de carti din sac
     */
    public final void whenItsSheriffGreedy(final Player merchant,
                                           final ArrayList<Integer> goodsInBag) {
        if (merchant.getBribe() == 0) {
            super.whenItsSheriff(merchant, goodsInBag);
        } else {
            this.updateCoins(merchant.getBribe());
            merchant.payPenalty(merchant.getBribe());
            merchant.onStand(goodsInBag);
        }
    }
}
