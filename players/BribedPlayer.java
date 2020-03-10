// Copyright 2019 Banu Miruna Elena 321CA
package com.tema1.players;

import com.tema1.common.Constants;
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Collections;

public class BribedPlayer extends BasicPlayer {

    public BribedPlayer(final Integer id, final Integer type) {
        this.setId(id);
        this.setPlayerType(type);
    }

    public BribedPlayer() {
    }

    @Override
    // Creates the hand of 10 cards and updates the number
    // of appearances of a given card
    public final void createBag(final Queue<Integer> cards, final GoodsFactory goods,
                                final List<Integer> thisBag, final Map<Goods, Integer> thisFreq,
                                final Constants ct) {
        super.createBag(cards, goods, thisBag, thisFreq, ct);
    }

    @Override
    // Checks to see if there are any legal goods in hand
    public final boolean hasLegalGoods(final List<Integer> bag, final Constants ct) {
        return super.hasLegalGoods(bag, ct);
    }

    @Override
    // Returns the illegal card that has the biggest profit
    public final Integer findMaxProfitCard(final GoodsFactory goods,
                                           final List<Integer> bag,
                                           final Constants ct) {
        return super.findMaxProfitCard(goods, bag, ct);
    }

    @Override
    // Returns the biggest frequency
    public final Integer getMaxFreq(final Map<Goods, Integer> freq,
                                    final Constants ct) {
        return super.getMaxFreq(freq, ct);
    }

    @Override
    // Returns the goods with the biggest frequency
    public final List<Integer> getMostFreqGood(final GoodsFactory goods,
                                               final Map<Goods, Integer> freq,
                                               final Constants ct) {
        return super.getMostFreqGood(goods, freq, ct);
    }

    @Override
    // Returns whether or not the player being verified is a liar
    public final boolean isLiar(final Constants ct) {
        return super.isLiar(ct);
    }

    // Sorts the bag by the profit and id
    public final List<Integer> sortBag(final List<Integer> bag,
                                       final GoodsFactory goods) {
        List<Goods> sortedBag = new ArrayList<Goods>();
        List<Integer> bagCopy = bag;
        for (Integer i : bagCopy) {
            sortedBag.add(goods.getGoodsById(i));
        }
        bagCopy.clear();
        ProfitComparator comp = new ProfitComparator();
        Collections.sort(sortedBag, comp);
        for (Goods i : sortedBag) {
            bagCopy.add(i.getId());
        }
        return bagCopy;
    }

    // Returns the number of the illegal cards in the bag
    public final int illegalCards(final List<Integer> bag,
                                  final Constants ct) {
        int illegals = 0;
        for (int i = 0; i < bag.size(); ++i) {
            if (bag.get(i) >= ct.getMinIllegalId()) {
                illegals++;
            }
        }
        return illegals;
    }

    @Override
    // Changes the frequency of the good in the tableGoods map
    public final void changeFrequency(final Map<Goods, Integer> freq,
                                      final Map<Goods, Integer> tableGoods,
                                      final Goods currentGood,
                                      final Integer value) {
        super.changeFrequency(freq, tableGoods, currentGood, value);
    }

    @Override
    // Creates the bag
    public final void getGood(final GoodsFactory goods, final int parity,
                              final List<Integer> bag, final Map<Goods, Integer> freq,
                              final Map<Goods, Integer> tableGoods,
                              final List<Integer> chosenItems, final Constants ct) {
        int illegals = this.illegalCards(this.getBag(), ct);
        if (illegals == 0 || this.getMoney() <= ct.getMinBribeMoney()) {
            super.getGood(goods, parity, this.getBag(), this.getFreq(),
                    this.getTableGoods(), this.getChosenItems(), ct);
        } else {
            int possiblePenalty = 0;
            List<Integer> sortedBag = this.sortBag(this.getBag(), goods);
            int illegalsAdded = 0;
            int itemsAdded = 0;
            for (int i = 0; i < sortedBag.size(); ++i) {
                possiblePenalty += goods.getGoodsById(sortedBag.get(i)).getPenalty();
                if (possiblePenalty < this.getMoney() && itemsAdded < ct.getBagSize()) {
                    Goods currentGood = goods.getGoodsById(sortedBag.get(i));
                    this.changeFrequency(this.getFreq(), this.getTableGoods(), currentGood, 1);
                    this.getChosenItems().add(sortedBag.get(i));
                    itemsAdded++;
                    if (sortedBag.get(i) >= ct.getMinIllegalId()) {
                        illegalsAdded++;
                    }
                } else {
                    possiblePenalty -= goods.getGoodsById(sortedBag.get(i)).getPenalty();
                }
            }
            if ((illegalsAdded == 1 || illegalsAdded == 2)) {
                this.setBribe(ct.getBRIBE1());
                this.setDeclaredGood(ct.getAppleId());
            } else {
                if (illegalsAdded > 2) {
                    this.setBribe(ct.getBRIBE2());
                    this.setDeclaredGood(ct.getAppleId());
                }
            }

        }
    }

    @Override
    // Returns the bag created by the current player
    public final void commerciant(final Queue<Integer> cards,
                                  final GoodsFactory goods,
                                  final int parity,
                                  final Constants ct) {
        this.createBag(cards, goods, this.getBag(), this.getFreq(), ct);
        this.getGood(goods, parity, this.getBag(), this.getFreq(),
                this.getTableGoods(), this.getChosenItems(), ct);
    }

    // Checks whether or not the player is to be verified
    public final int toCheck(final int idSheriff, final int idPlayer,
                             final int numPlayers) {
        int check = 0;
        if (idPlayer == idSheriff + 1 || idPlayer == idSheriff - 1) {
            check = 1;
        }
        if (idSheriff == 0 && idPlayer == numPlayers - 1) {
            check = 1;
        }
        if (idSheriff == numPlayers - 1 && idPlayer == 0) {
            check = 1;
        }
        return check;
    }

    @Override
    // Checks the player
    public final void sheriff(final Player p, final GoodsFactory goods,
                              final Queue<Integer> cards,
                              final Integer thisBribe, final int thisId,
                              final int numPlayers, final Constants ct) {
        int check = this.toCheck(this.getId(), p.getId(), numPlayers);
        if (check == 1 && this.getMoney() < ct.getMinSheriffMoney()) {
            check = -1;
        }
        if (check == 1) {
            super.sheriff(p, goods, cards, p.getBribe(), p.getId(), numPlayers, ct);
        } else if (check == 0) {
            if (!thisBribe.equals(0)) {
                this.setCurrentMoney(this.getCurrentMoney() + thisBribe);
                p.setMoney(p.getMoney() - thisBribe);
            }
        }
    }

    @Override
    // Adds the bonus items to the player's bag
    public final void receiveBonusItems(final GoodsFactory goods,
                                        final Constants ct) {
        super.receiveBonusItems(goods, ct);
    }

    @Override
    // Gives the current player the bonus for the goods in their bag
    public final void getReward(final GoodsFactory goods) {
        super.getReward(goods);
    }
}
