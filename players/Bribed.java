package com.tema1.players;

import com.tema1.goods.GoodsFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Bribed extends Basic {

    public Bribed(final int id, final PlayersType type, final String strategy,
                  final int coins) {
        super(id, type, strategy, coins);
    }

    /**
     * Creeaza sacul comerciantului cu strategie bribed si se declara tipul bunului.
     * @param cards lista de carti
     * @return returneaza lista de carti alese pentru inspectie
     */
    public final ArrayList<Integer> makeBagBribed(final ArrayList<Integer> cards) {
        ProfitComparatorBribed profitComparatorBribed = new ProfitComparatorBribed();
        Collections.sort(cards, profitComparatorBribed);

        final int firstIllegalCard = 20;
        final int maxCardsInHand = 8;
        final int forMax2 = 5;
        final int forMoreThan2 = 10;
        int  isIllegal = 0; //pp ca nu are carti ilegale
        //verific daca are carti ilegale in mana
        for (int card : cards) {
            if (card >= firstIllegalCard) {
                isIllegal = 1;
                break;
            }
        }
        //daca nu mai are suficienti bani sau nu are carti ilegale
        //se aplica strategia basic
        if (isIllegal == 0 || this.getCoins() <= forMax2) {
            return super.makeBag(cards);
        } else {
            ArrayList<Integer> goodCards = new ArrayList<>();
            int money = this.getCoins();

            //se adauga cartile ilegale in sac
            Iterator<Integer> iterator = cards.iterator();
            while (iterator.hasNext()) {
                Integer card = iterator.next();
                if (money - GoodsFactory.getInstance().getGoodsById(card).getPenalty() > 0) {
                    if (card >= firstIllegalCard && goodCards.size() < maxCardsInHand) {
                        goodCards.add(card);
                        iterator.remove();
                        money -= GoodsFactory.getInstance().getGoodsById(card).getPenalty();
                    }
                } else {
                    break;
                }
            }

            //in functie de cate carti ilegale are in sac, setez mita
            if (goodCards.size() == 1 || goodCards.size() == 2) {
                this.setBribe(forMax2);
                this.updateHonesty(PlayerHonesty.Liar);
            }
            if (goodCards.size() > 2) {
                this.setBribe(forMoreThan2);
                this.updateHonesty(PlayerHonesty.Liar);
            }

            //se adauga carti legale in caz ca nu s-a completat sacul
            for (Integer card : cards) {
                if (money - GoodsFactory.getInstance().getGoodsById(card).getPenalty() > 0) {
                    if (card < firstIllegalCard && goodCards.size() < maxCardsInHand) {
                        goodCards.add(card);
                        money -= GoodsFactory.getInstance().getGoodsById(card).getPenalty();
                    }
                }
            }
            this.setDeclaredCardType(0);
            return goodCards;
        }
    }
    /**
     * Are loc inspectia jucatorului din stanga si din dreapta si de la ceilalti se ia mita
     * daca aceasta exista, la finalul subrundei, dupa ce sunt verificati vecinii pentru a nu
     * afecta conditia de bani minimi pentru verificare.
     * @param merchant jucatorul ce este comerciant
     * @param nrPlayers nr de jucatori
     * @param goodsInBag lista de carti din sac
     * @return returneaza mita ce trebuie adaugata la final
     */
    public final int whenItsSheriffBribed(final Player merchant, final int nrPlayers,
                                           final ArrayList<Integer> goodsInBag) {
        final int minCoins = 15;
        final int idSheriff = this.getId();
        final int idMerchant = merchant.getId();
        int penalty = 0;
        final int nine = 9;
        //daca comerciantul se afla in stanga sau in dreapta serifului
        if (idMerchant == idSheriff - 1 || idMerchant == idSheriff + 1
                || (idSheriff == nrPlayers - 1 && idMerchant == 0)
                || (idSheriff == 0 && idMerchant == nrPlayers - 1)) {
            if (this.getCoins() > minCoins) {
                    if ((merchant.getHonesty()).equals(PlayerHonesty.Honest)) {
                        for (int good : goodsInBag) {
                            penalty = penalty + (GoodsFactory.getInstance().
                                    getGoodsById(good).getPenalty());
                        }
                        //daca a fost onest bunurile sunt adaugate pe taraba
                        merchant.onStand(goodsInBag);
                        merchant.updateCoins(penalty);
                        this.payPenalty(penalty);
                    }
                    //daca este mincinos
                    if ((merchant.getHonesty()).equals(PlayerHonesty.Liar)) {
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
        } else {
            if (merchant.getBribe() != 0) {
                merchant.payPenalty(merchant.getBribe());
                merchant.onStand(goodsInBag);
                return merchant.getBribe();
            }
            merchant.onStand(goodsInBag);
        }
        return 0;
    }
}
