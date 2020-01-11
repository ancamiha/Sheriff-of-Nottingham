package com.tema1.players;

import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;
import com.tema1.goods.GoodsType;
import com.tema1.goods.IllegalGoods;
import com.tema1.goods.LegalGoods;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Collections;
import java.util.Comparator;

public final class ScoreCalculator {
    private static ScoreCalculator instance;

    private ScoreCalculator() {

    }
    public static ScoreCalculator getInstance() {
        if (instance == null) {
            instance = new ScoreCalculator();
        }
        return instance;
    }

    /**
     * Se calculeaza bonusul pentru fiecare carte ilegala pusa pe taraba.
     * @param players lista de jucatori
     */
    private static void applyIllegalBonus(final List<Player> players) {
        for (var player : players) {
            List<Integer> auxBag = new LinkedList<>();
            for (var itemKey : player.getBag()) {
                Goods item = GoodsFactory.getInstance().getGoodsById(itemKey);
                if (item.getType() == GoodsType.Illegal) {
                    for (var bonus : ((IllegalGoods) item).getIllegalBonus().entrySet()) {
                        for (int i = 0; i < bonus.getValue(); i++) {
                            auxBag.add(bonus.getKey().getId());
                        }
                    }
                }
            }
            player.getBag().addAll(auxBag);
        }
    }

    /**
     * Se calculeaza frecventa fiecarui bun legal de pe taraba fiecarui jucator pentru
     * a se decide king-ul si queen-ul fiecarui bun.
     * @param players lista de jucatori
     */
    private static void applyKingAndQueen(final List<Player> players) {
        Set<Integer> allGoods = GoodsFactory.getInstance().getAllGoods().keySet();
        for (var goodId : allGoods) {
            Goods item = GoodsFactory.getInstance().getGoodsById(goodId);
            if (item.getType() == GoodsType.Illegal) {
                continue;
            }

            int kingFreq = 0, queenFreq = 0;
            Player king = null, queen = null;
            for (var player : players) {
                int freq = Collections.frequency(player.getBag(), goodId);
                if (freq > kingFreq) {
                    queenFreq = kingFreq;
                    queen = king;
                    kingFreq = freq;
                    king = player;
               } else {
                    if (freq > queenFreq) {
                        queenFreq = freq;
                        queen = player;
                    }
                }
            }

            if (king != null) {
                king.updateCoins(((LegalGoods) item).getKingBonus());
            }
            if (queen != null) {
                queen.updateCoins(((LegalGoods) item).getQueenBonus());
            }
        }
    }

    /**
     * Se calculeaza profitul corespunzator fiecarui bun pus pe taraba de catre comerciant
     * la sfarsitul tuturor rundelor.
     * @param players lista de jucatori
     */
    private static void applyItemProfit(final List<Player> players) {
        for (var player : players) {
            for (var goodId : player.getBag()) {
                Goods item = GoodsFactory.getInstance().getGoodsById(goodId);
                player.updateCoins(item.getProfit());
            }
        }
    }

    /**
     * Sorteaza o lista de jucatori in functie de banii pe care ii detin.
     */
    private static class CoinsComparator implements Comparator<Player> {
        @Override
        public int compare(final Player o1, final Player o2) {
            return o2.getCoins() - o1.getCoins();
        }
    }

    /**
     * Se realizeaza clasamentul.
     * @param players lista de jucatori
     */
    public static void compute(final List<Player> players) {
        applyIllegalBonus(players);
        applyKingAndQueen(players);
        applyItemProfit(players);
        CoinsComparator cmp = new CoinsComparator();
        Collections.sort(players, cmp);
    }
}
