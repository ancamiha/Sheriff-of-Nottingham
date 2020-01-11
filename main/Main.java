package com.tema1.main;

import com.tema1.players.Player;
import com.tema1.players.PlayerHonesty;
import com.tema1.players.PlayersType;
import com.tema1.players.Basic;
import com.tema1.players.Greedy;
import com.tema1.players.Bribed;
import com.tema1.players.ScoreCalculator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public final class Main {
    private Main() {
        // just to trick checkstyle
    }

    public static void main(final String[] args) {
        GameInputLoader gameInputLoader = new GameInputLoader(args[0], args[1]);
        GameInput gameInput = gameInputLoader.load();
        int nrPlayers = gameInput.getPlayerNames().size();
        final int coins = 80;
        final int nrCards = 10;

        List<Player> players = new LinkedList<>();
        for (int i = 0; i < nrPlayers; i++) {
            String name = gameInput.getPlayerNames().get(i);
            if (name.equals("basic")) {
                players.add(new Basic(i, PlayersType.Merchant, name, coins));
            }
            if (name.equals("greedy")) {
                players.add(new Greedy(i, PlayersType.Merchant, name, coins));
            }
            if (name.equals("bribed")) {
                players.add(new Bribed(i, PlayersType.Merchant, name, coins));
            }
        }

        Queue<Integer> listOfCards = new LinkedList<>(gameInput.getAssetIds());
        for (int i = 1; i <= gameInput.getRounds(); i++) {
            //pentru fiecare subrunda
            for (int j = 0; j < nrPlayers; j++) {
                //setez jucatorul ce va fi serif in runda j
                players.get(j).setType(PlayersType.Sheriff);
                //impart cartile tuturor jucatorilor in afara de serif
                for (int k = 0; k < nrPlayers; k++) {
                    if (k != j) {
                        //asignez cate 10 carti fiecarui comerciant
                        for (int l = 0; l < nrCards; l++) {
                            players.get(k).getCards().add(l, listOfCards.remove());
                        }
                    }
                }

                ArrayList<Integer> bag = new ArrayList<>();
                int sumBribe = 0;
                for (int k = 0; k < nrPlayers; k++) {
                    if ((players.get(k).getType()).equals(PlayersType.Merchant)) {
                        //comerciantul is creeaza sacul
                        if ((players.get(k).getStrategy()).equals("basic")) {
                            bag = players.get(k).makeBag(players.get(k).getCards());
                        }
                        if ((players.get(k).getStrategy()).equals("greedy")) {
                            bag = ((Greedy) players.get(k)).makeBagGreedy(players.get(k).
                                    getCards(), i);
                        }
                        if ((players.get(k).getStrategy()).equals("bribed")) {
                            bag = ((Bribed) players.get(k)).makeBagBribed(players.get(k).
                                    getCards());
                        }
                        //are loc inspectia
                        if ((players.get(j).getStrategy()).equals("basic")) {
                            players.get(j).whenItsSheriff(players.get(k), bag);
                        }
                        if ((players.get(j).getStrategy()).equals("greedy")) {
                            ((Greedy) players.get(j)).whenItsSheriffGreedy(players.get(k), bag);
                        }
                        if ((players.get(j).getStrategy()).equals("bribed")) {
                            sumBribe += ((Bribed) players.get(j)).whenItsSheriffBribed(players.
                                         get(k), nrPlayers, bag);
                        }
                        //decartez cartile din saci
                        bag.clear();
                    }
                }
                players.get(j).updateCoins(sumBribe);
                players.get(j).setType(PlayersType.Merchant);
                for (int k = 0; k < nrPlayers; k++) {
                    players.get(k).updateHonesty(PlayerHonesty.Honest);
                    players.get(k).getCards().clear();
                }
            }
            //decardez cartile si resetez onestitatea fiecarui jucator
            //la finalul fiecarei rundei
            for (int k = 0; k < nrPlayers; k++) {
                players.get(k).updateHonesty(PlayerHonesty.Honest);
                players.get(k).getCards().clear();
            }
        }
        ScoreCalculator.compute(players);
        for (Player player : players) {
            System.out.println(player.getId() + " " + player.getStrategy().toUpperCase() + " "
                    + player.getCoins());
        }
    }
}
