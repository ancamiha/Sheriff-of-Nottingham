package com.tema1.players;

import com.tema1.goods.GoodsFactory;

import java.util.Comparator;

/**
 * Sorteaza o lista de carti in functie de profit, iar daca profitul este identic
 * sunt sortate dupa id descrescator.
 */
public class ProfitComparatorBribed implements Comparator<Integer> {
    public final int compare(final Integer o1, final Integer o2) {
        if ((GoodsFactory.getInstance().getGoodsById(o2).getProfit()
                - GoodsFactory.getInstance().getGoodsById(o1).getProfit()) == 0) {
            return o2 - o1;
        }
        return GoodsFactory.getInstance().getGoodsById(o2).getProfit()
                - GoodsFactory.getInstance().getGoodsById(o1).getProfit();
    }
}
