// Copyright 2019 Banu Miruna Elena 321CA
package com.tema1.players;

import com.tema1.common.Constants;
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class GreedyPlayer extends BasicPlayer {

    public GreedyPlayer() {
    }

    public GreedyPlayer(final Integer id, final Integer type) {
        this.setId(id);
        this.setPlayerType(type);
    }

    @Override
    // Creates the hand of 10 cards and updates the number
    // of appearances of a given card
    public final void createBag(final Queue<Integer> cards,
                                final GoodsFactory goods,
                                final List<Integer> thisBag,
                                final Map<Goods, Integer> thisFreq,
                                final Constants ct) {
        super.createBag(cards, goods, thisBag, thisFreq, ct);
    }

    @Override
    // Checks to see if there are any legal goods in hand
    public final boolean hasLegalGoods(final List<Integer> bag,
                                       final Constants ct) {
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

    @Override
    // Returns the legal good chosen
    public final Integer getLegalChoice(final GoodsFactory goods,
                                        final Map<Goods, Integer> freq,
                                        final Constants ct) {
        return super.getLegalChoice(goods, freq, ct);
    }

    @Override
    // Creates the bag
    public final void getGood(final GoodsFactory goods, final int parity,
                              final List<Integer> bag, final Map<Goods, Integer> freq,
                              final Map<Goods, Integer> tableGoods,
                              final List<Integer> chosenItems,
                              final Constants ct) {
        super.getGood(goods, parity, this.getBag(), this.getFreq(),
                this.getTableGoods(), this.getChosenItems(), ct);
        if (parity % 2 == 1) {
            Integer max = this.findMaxProfitCard(goods, this.getBag(), ct);
            if (max != -1) {
                if (this.getMoney() >= goods.getGoodsById(max).getPenalty()
                        && this.getChosenItems().size() < ct.getBagSize()) {
                    this.getChosenItems().add(max);
                    Goods currentGood = goods.getGoodsById(max);
                    if (this.getTableGoods().containsKey(currentGood)) {
                        Integer newFreq = this.getTableGoods().get(currentGood) + 1;
                        this.getTableGoods().replace(currentGood, newFreq);
                    } else {
                        this.getTableGoods().put(currentGood, 1);
                    }
                }
            }
            this.getBag().remove(max);
        }
    }

    @Override
    // Returns the bag created by the current player
    public final void commerciant(final Queue<Integer> cards,
                                  final GoodsFactory goods,
                                  final int parity, final Constants ct) {
        this.createBag(cards, goods, this.getBag(), this.getFreq(), ct);
        this.getGood(goods, parity, this.getBag(), this.getFreq(),
                this.getTableGoods(), this.getChosenItems(), ct);
    }

    @Override
    // Checks the player
    public final void sheriff(final Player p, final GoodsFactory goods,
                              final Queue<Integer> cards,
                              final Integer thisBribe, final int thisId,
                              final int numPlayers, final Constants ct) {
        Integer declaredObj = p.getDeclaredGood();
        List<Integer> bagItems = p.getChosenItems();
        boolean liar = p.isLiar(ct);
        if (this.getMoney() >= ct.getMinSheriffMoney()) {
            if (liar && (thisBribe.equals(0))) {
                List<Integer> bagItemsCopy = new ArrayList<Integer>();
                for (Integer i : bagItems) {
                    bagItemsCopy.add(i);
                }
                for (Integer i : bagItemsCopy) {
                    Goods currentGood = goods.getGoodsById(i);
                    if (!i.equals(declaredObj)) {
                        this.setMoney(this.getMoney() + currentGood.getPenalty());
                        p.setMoney(p.getMoney() - currentGood.getPenalty());
                        Integer f = p.getTableGoods().get(currentGood);
                        if (f > 1) {
                            p.getTableGoods().replace(currentGood, f - 1);
                        } else {
                            if (f == 1) {
                                p.getTableGoods().remove(currentGood);
                            }
                        }
                        cards.add(i);
                        p.getChosenItems().remove(i);
                    }
                }
            } else {
                if (!thisBribe.equals(0)) {
                    this.setMoney(this.getMoney() + thisBribe);
                    p.setMoney(p.getMoney() - thisBribe);
                } else {
                    Goods currentGood = goods.getGoodsById(p.getDeclaredGood());
                    this.setMoney(this.getMoney()
                           - (p.getChosenItems().size() * currentGood.getPenalty()));
                    p.setMoney(p.getMoney()
                            + (p.getChosenItems().size() * currentGood.getPenalty()));
                }
            }
        }
    }

    @Override
    // Gives the current player the bonus for the goods in their bag
    public final void getReward(final GoodsFactory goods) {
        super.getReward(goods);
    }

    @Override
    // Adds the bonus items to the player's bag
    public final void receiveBonusItems(final GoodsFactory goods, final Constants ct) {
        super.receiveBonusItems(goods, ct);
    }
}
