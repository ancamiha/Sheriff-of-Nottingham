package com.tema1.players;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

public abstract  class Player {
    private final int id;
    private PlayersType type;
    private final String strategy;
    private ArrayList<Integer> cards = new ArrayList<>();
    private int coins;
    private PlayerHonesty honesty;
    private int bribe;
    private int declaredCardType;
    private List<Integer> bag = new LinkedList<>();

    /**
     * @param id id-ul fiecarui jucator
     * @param type jucatorul poate fi comerciant sau serif
     * @param strategy basic/greedy/bribed
     * @param coins banii detinuti de jucator pe parcursul jocului
     */
    public Player(final int id, final PlayersType type, final String strategy, final int coins) {
        this.id = id;
        this.type = type;
        this.strategy = strategy;
        this.coins = coins;
    }
    public final int getId() {
        return id;
    }
    public final PlayersType getType()  {
        return type;
    }
    public final String getStrategy() {
        return strategy;
    }
    public final ArrayList<Integer> getCards() {
        return cards;
    }
    public final int getCoins() {
        return coins;
    }
    public final PlayerHonesty getHonesty() {
        return honesty;
    }
    public final int getBribe() {
        return bribe;
    }
    public final int getDeclaredCardType() {
        return declaredCardType;
    }
    public final List<Integer> getBag() {
        return bag;
    }

    public final void setHonesty(final PlayerHonesty honesty) {
        this.honesty = honesty;
    }
    public final void setType(final PlayersType type) {
        this.type = type;
    }
    public final void setBribe(final int bribe) {
        this.bribe = bribe;
    }
    public final void setDeclaredCardType(final int declaredCardType) {
        this.declaredCardType = declaredCardType;
    }

    public final void updateCoins(final int addCoins) {
        this.coins += addCoins;
    }
    public final void payPenalty(final int penalty) {
        this.coins = this.coins - penalty;
    }
    public final void updateHonesty(final PlayerHonesty honestyNew) {
        this.honesty = honestyNew;
    }

    public abstract Integer freqCalc(ArrayList<Integer> cardsL);
    public abstract Integer getMaxGood(Map<Integer, Integer> freqMap);
    public abstract Integer getMaxId(Map<Integer, Integer> bestGoods);
    public abstract  ArrayList<Integer>  makeBag(ArrayList<Integer> cardsL);
    public abstract void whenItsSheriff(Player merchant, ArrayList<Integer> goodsinBag);

    /**
     * Metoda ce adauga cartile ce au trecut de inspectie pe taraba.
     * @param cardsList lista de carti
     */
    public final void onStand(final ArrayList<Integer> cardsList) {
        //adaug bunurile pe taraba
        this.bag.addAll(cardsList);
    }
}
