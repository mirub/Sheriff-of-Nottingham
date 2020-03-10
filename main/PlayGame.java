// Copyright 2019 Banu Miruna Elena 321CA
package com.tema1.main;

import com.tema1.common.Constants;
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;
import com.tema1.players.BasicPlayer;
import com.tema1.players.BribedPlayer;
import com.tema1.players.GreedyPlayer;
import com.tema1.players.Player;
import com.tema1.players.PlayerComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Collections;

public  final class PlayGame {
    private static PlayGame playGame = null;
    private Queue<Integer> cards = new LinkedList<Integer>();
    private List<String> players = new ArrayList<String>();
    private int rounds = 0;

    private PlayGame(final Queue<Integer> cards, final List<String> players,
                     final int rounds) {
        this.setCards(cards);
        this.setPlayers(players);
        this.setRounds(rounds);
    }

    public static PlayGame getInstance(final Queue<Integer> cards,
                                             final List<String> players,
                                             final int rounds) {
        if (playGame == null) {
            playGame = new PlayGame(cards, players, rounds);
        }
        return playGame;
    }

    public Queue<Integer> getCards() {
        return cards;
    }

    public void setCards(final Queue<Integer> cards) {
        this.cards = cards;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(final List<String> players) {
        this.players = players;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(final int rounds) {
        this.rounds = rounds;
    }

    // Creates the player array
    public List<Player> createPlayerArray(final List<String> thisPlayers) {
        List<Player> playerVector = new ArrayList<Player>(thisPlayers.size());
        for (int i = 0; i < thisPlayers.size(); ++i) {
            if (thisPlayers.get(i).equals("basic")) {
                Player p = new BasicPlayer(i, 0);
                playerVector.add(p);
            }
            if (thisPlayers.get(i).equals("greedy")) {
                Player p = new GreedyPlayer(i, 1);
                playerVector.add(p);
            }
            if (thisPlayers.get(i).equals("bribed")) {
                Player p = new BribedPlayer(i, 2);
                playerVector.add(p);
            }
        }
        return playerVector;
    }

    // Prints the ranking
    public void printRanking(final List<Player> thesePlayers) {
        PlayerComparator playerComparator = new PlayerComparator();
        Collections.sort(thesePlayers, playerComparator);

        for (int i = 0; i < thesePlayers.size(); ++i) {
            if (thesePlayers.get(i).getPlayerType() == 0) {
                System.out.println(thesePlayers.get(i).getId() + " BASIC "
                        + thesePlayers.get(i).getMoney());
            }

            if (thesePlayers.get(i).getPlayerType() == 1) {
                System.out.println(thesePlayers.get(i).getId() + " GREEDY "
                        + thesePlayers.get(i).getMoney());
            }

            if (thesePlayers.get(i).getPlayerType() == 2) {
                System.out.println(thesePlayers.get(i).getId() + " BRIBED "
                        + thesePlayers.get(i).getMoney());
            }
        }
    }

    // Gives the King & Queen Bonuses
    public void getKingQueenBonus(final GoodsFactory goods,
                                        final List<Player> thesePlayers) {
        for (int i = 0; i < Constants.getDeckSize(); ++i) {
            Goods currentGood = goods.getGoodsById(i);
            int kingFreq = -1, kingId = -1, queenFreq = -1, queenId = -1;
            List<Integer> kings = new ArrayList<Integer>();
            for (Player p : thesePlayers) {
                if (p.getTableGoods().containsKey(currentGood)) {
                    if (p.getTableGoods().get(currentGood) > kingFreq) {
                        kings.clear();
                        kings.add(0, p.getId());
                        queenFreq = kingFreq;
                        queenId = kingId;
                        kingFreq = p.getTableGoods().get(currentGood);
                        kingId = p.getId();
                    } else {
                        if (p.getTableGoods().get(currentGood).equals(kingFreq) && kingFreq != -1) {
                            kings.add(p.getId());
                        } else if (p.getTableGoods().get(currentGood) > queenFreq) {
                            queenFreq = p.getTableGoods().get(currentGood);
                            queenId = p.getId();
                        }
                    }
                }
            }
            if (kings.size() >= 2) {
                kingId = kings.get(0);
                queenId = kings.get(1);
            }
            if (kingId != -1) {
                thesePlayers.get(kingId).setMoney(thesePlayers.get(kingId).getMoney()
                        + currentGood.getKingBonus());
            }
            if (queenId != -1) {
                thesePlayers.get(queenId).setMoney(thesePlayers.get(queenId).getMoney()
                        + currentGood.getQueenBonus());
            }
        }
    }

    // The main function of the game
    public void play(final GoodsFactory goods,
                           final Constants ct) {
        List<Player> thesePlayers = this.createPlayerArray(this.getPlayers());
        int theseRounds = this.getRounds();
        for (int k = 0; k < theseRounds; k++) {
            for (int i = 0; i < thesePlayers.size(); ++i) {
                Player sh = thesePlayers.get(i);
                for (int j = 0; j < thesePlayers.size(); ++j) {
                    if (j != i) {
                        Player p = thesePlayers.get(j);
                        p.commerciant(this.getCards(), goods, k, ct);
                        if (sh.getMoney() >= Constants.getMinSheriffMoney()) {
                            sh.sheriff(p, goods, this.getCards(), p.getBribe(),
                                    p.getId(), thesePlayers.size(), ct);
                        } else {
                            if (p.getBribe() != 0) {
                                sh.sheriff(p, goods, this.getCards(),
                                        p.getBribe(), p.getId(), thesePlayers.size(), ct);
                            }
                        }
                        p.getBag().clear();
                        p.getChosenItems().clear();
                        p.setDeclaredGood(-1);
                        p.setBribe(0);
                        p.getFreq().clear();
                    }
                }
                sh.setMoney(sh.getMoney() + sh.getCurrentMoney());
                sh.setCurrentMoney(0);
            }
        }

        for (int i = 0; i < thesePlayers.size(); ++i) {
            thesePlayers.get(i).receiveBonusItems(goods, ct);
            thesePlayers.get(i).getReward(goods);
        }

        this.getKingQueenBonus(goods, thesePlayers);
        this.printRanking(thesePlayers);
    }
}
