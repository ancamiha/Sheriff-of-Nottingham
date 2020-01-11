package com.tema1.players;

import com.tema1.goods.GoodsFactory;
import com.tema1.goods.GoodsType;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public class Basic extends Player {

    public Basic(final int id, final PlayersType type, final String strategy,
                 final int coins) {
        super(id, type, strategy, coins);
    }

    /**
     * Calculeaza frecventa cartilor legale din pachetul de carti. In caz ca nu exista carti
     * legale se sorteaza lista de carti ilegale in functie de profit si se returneaza
     * prima carte(cu profitul cel mai mare).
     * @param cardsL lista de carti a jucatorului
     * @return returneaza un HashMap ce contine cartile legale si frecventa lor
     * sau cea mai valoroasa carte ilegala.
     */
    public final Integer freqCalc(final ArrayList<Integer> cardsL) {
        Map<Integer, Integer> freqMap = new HashMap<>();

        for (int card : cardsL) {
            final int nine = 9; //id ul maxim pentru un bun legal
            if (card <= nine) {
                freqMap.put(card, freqMap.getOrDefault(card, 0) + 1);
            }
        }

        if (!freqMap.isEmpty()) {
            return getMaxGood(freqMap);
        } else {
            ProfitComparatorBribed profitComparator = new ProfitComparatorBribed();
            Collections.sort(cardsL, profitComparator);
            return cardsL.get(0);
        }
    }

    /**
     * Cauta cartea cu frecventa cea mai mare, daca sunt mai multe carti cu aceeasi
     * frecventa se va cauta cartea cu profitul cel mai mare si daca exista carti cu
     * acelasi profit se ia cartea cu id-ul cel mai mare.
     * @param freqMap hashmap-ul ce contine cartile legale si frecventa lor
     * @return returneaza id-ul cartii pe care o va alege
     */
    public final Integer getMaxGood(final Map<Integer, Integer> freqMap) {
        //caut bunurile cu cea mai mare frecventa
        Map<Integer, Integer> bestGoods = new HashMap<>();
        int maxFreq = (Collections.max(freqMap.values()));
        for (Map.Entry<Integer, Integer> entry : freqMap.entrySet()) {
            if (entry.getValue() == maxFreq) {
                bestGoods.put(entry.getKey(), entry.getValue());
            }
        }

        Integer maxId = getMaxId(getMaxProfit(bestGoods));
        return maxId;
    }

    /**
     * Cauta cartile cu cel mai mare profit si le adauga in hashmap.
     * @param bestGoods hashmap-ul ce contine cartile cu cea mai mare frecventa
     * @return returneaza hashmap-ul ce contine cartile cu cel mai mare profit din
     * cele cu cea mai mare frecventa
     */
    public final Map<Integer, Integer> getMaxProfit(final Map<Integer, Integer> bestGoods) {
        GoodsFactory allGoods = GoodsFactory.getInstance();
        int maxProfit = allGoods.getGoodsById(0).getProfit();

        Map<Integer, Integer> bestGoodsByProfit = new HashMap<>();
        for (Map.Entry<Integer, Integer> good : bestGoods.entrySet()) {
            if (allGoods.getGoodsById(good.getKey()).getProfit() > maxProfit) {
                maxProfit = allGoods.getGoodsById(good.getKey()).getProfit();
            }
        }
        for (Map.Entry<Integer, Integer> good : bestGoods.entrySet()) {
            if (allGoods.getGoodsById(good.getKey()).getProfit() == maxProfit) {
                bestGoodsByProfit.put(good.getKey(), good.getValue());
            }
        }
        return bestGoodsByProfit;
    }

    /**
     * Cauta id-ul cel mai mare.
     * @param bestGoods un hashmap ce contine cartile cu cel mai mare profit
     * @return returneaza id-ul cel mai mare
     */
    public final Integer getMaxId(final Map<Integer, Integer> bestGoods) {
        int maxId = (Collections.max(bestGoods.keySet()));
        return maxId;
    }

    /**
     * Se creeaza sacul si se declara tipul bunului adaugat in sac.
     * @param cards lista de carti
     * @return returneaza lista de carti alese pentru inspectie
     */
    //creearea sacului + declararea bunurilor
    public final ArrayList<Integer> makeBag(final ArrayList<Integer> cards) {
        ArrayList<Integer> goodCards = new ArrayList<>();
        int id = freqCalc(cards);
        final int nine = 9; //id ul maxim pentru un bun legal
        final int maxCardsInHand = 8;
        this.setHonesty(PlayerHonesty.Honest);

        if (id > nine) {
            Iterator<Integer> iterator = cards.listIterator();
            while (iterator.hasNext()) {
                Integer card = iterator.next();
                if (card.equals(id)) {
                    goodCards.add(card);
                    iterator.remove();
                    break;
                }
            }
        }
        //se adauga cartile in sac
        Iterator<Integer> iterator = cards.listIterator();
        while (iterator.hasNext()) {
            Integer card = iterator.next();
            if (card.equals(id) && goodCards.size() <= maxCardsInHand && card <= nine) {
                goodCards.add(card);
                iterator.remove();
            }
        }
        //se declara bunul
        if ((GoodsFactory.getInstance().getGoodsById(id).getType()).equals(GoodsType.Legal)) {
            setDeclaredCardType(id);
            setHonesty(PlayerHonesty.Honest);
        } else {
            setDeclaredCardType(0); //daca este un bun ilegal il va declara ca mere(id = 0)
            setHonesty(PlayerHonesty.Liar);
        }
        this.setBribe(0);
        return goodCards;
    }

    /**
     * Are loc inspectia, inspectandu-se fiecare jucator indiferent daca acestia vor sa dea mita.
     * @param merchant jucatorul ce este comerciant
     * @param goodsInBag lista de carti din sac
     */
    public final void whenItsSheriff(final Player merchant, final ArrayList<Integer> goodsInBag) {
        final int minCoins = 16;
        final int nine = 9;
        if (this.getCoins() >= minCoins) {
            int penalty = 0;
            if ((merchant.getHonesty()).equals(PlayerHonesty.Honest)) {
                for (int good : goodsInBag) {
                    penalty = penalty + (GoodsFactory.getInstance().
                                         getGoodsById(good).getPenalty());
                }
                //daca a fost onest bunurile sunt adaugate pe taraba
                merchant.onStand(goodsInBag);
                merchant.updateCoins(penalty);
                this.payPenalty(penalty);
            } else {
                //se calculeaza penalty-ul corespunzator cartilor nedeclarate
                for (int good : goodsInBag) {
                    if (good != merchant.getDeclaredCardType() || good > nine) {
                        penalty = penalty + (GoodsFactory.getInstance().
                                getGoodsById(good).getPenalty());
                    }
                }
                //creez o lista ce contine numai cartile declarate
                ArrayList<Integer> goodsInBagLiar = new ArrayList<>();
                for (int card : goodsInBag) {
                    if (card == merchant.getDeclaredCardType()) {
                        goodsInBagLiar.add(card);
                    }
                }
                merchant.onStand(goodsInBagLiar);
                merchant.payPenalty(penalty);
                this.updateCoins(penalty);
            }
        } else {
            merchant.onStand(goodsInBag);
        }
    }
}
