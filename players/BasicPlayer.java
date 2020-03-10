// Copyright 2019 Banu Miruna Elena 321CA
package com.tema1.players;

import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;
import com.tema1.common.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.HashMap;

public class BasicPlayer extends Player {
    public BasicPlayer() {
    }

    public BasicPlayer(final Integer id, final Integer type) {
        this.setId(id);
        this.setPlayerType(type);
    }

    /* Creates the hand of 10 cards and updates the number
    of appearances of a given card */
    public void createBag(final Queue<Integer> cards,
                                final GoodsFactory goods,
                                final List<Integer> thisBag,
                                final Map<Goods, Integer> thisFreq,
                                final Constants ct) {
        for (int i = 0; i < ct.getDeckSize(); ++i) {
            Integer x = cards.remove();
            thisBag.add(x);
            Goods currentGood = goods.getGoodsById(x);
            if (thisFreq.containsKey(currentGood)) {
                Integer newFreq = thisFreq.get(currentGood) + 1;
                thisFreq.replace(currentGood, newFreq);
            } else {
                thisFreq.put(currentGood, 1);
            }
        }
    }

    /* Checks to see if there are any legal goods in hand */
    public boolean hasLegalGoods(final List<Integer> bag,
                                       final Constants ct) {
        for (int i = 0; i < bag.size(); ++i) {
            if (bag.get(i) <= ct.getMaxLegalId()) {
                return true;
            }
        }
        return false;
    }


    /* Returns the illegal card that has the biggest profit */
    public Integer findMaxProfitCard(final GoodsFactory goods,
                                           final List<Integer> bag,
                                           final Constants ct) {
        Integer max = -1, card = -1;
        for (int i = 0; i < bag.size(); ++i) {
            Goods currentGood = goods.getGoodsById(bag.get(i));
            if ((bag.get(i) >= ct.getMinIllegalId()) && (currentGood.getProfit() > max)) {
                card = bag.get(i);
                max = currentGood.getProfit();
            }
        }
        return card;
    }

    /* Returns the biggest frequency */
    public Integer getMaxFreq(final Map<Goods, Integer> freq,
                                    final Constants ct) {
        int max = -1;
        for (Goods g : freq.keySet()) {
            if (g.getId() <= ct.getMaxLegalId() && freq.get(g) > max) {
                max = freq.get(g);
            }
        }
        return max;
    }

    /* Returns the goods with the biggest frequency */
    public List<Integer> getMostFreqGood(final GoodsFactory goods,
                                         final Map<Goods, Integer> freq,
                                         final Constants ct) {
        List<Integer> obj = new ArrayList<Integer>();
        Integer max = this.getMaxFreq(freq, ct);
        for (Goods g : freq.keySet()) {
            if (g.getId() <= ct.getMaxLegalId() && freq.get(g).equals(max)) {
                obj.add(g.getId());
            }
        }
        return obj;
    }

    /* Returns the legal good chosen */
    public Integer getLegalChoice(final GoodsFactory goods,
                                  final Map<Goods, Integer> freq,
                                  final Constants ct) {
        List<Integer> possibleChoices = this.getMostFreqGood(goods, freq, ct);
        int maxProfit = 0;
        int chosenGood = -1;
        for (int i = 0; i < possibleChoices.size(); ++i) {
            Goods currentGood
                    = goods.getGoodsById(possibleChoices.get(i));
            if (currentGood.getProfit() > maxProfit) {
                maxProfit = currentGood.getProfit();
                chosenGood = currentGood.getId();

            } else {
                if (currentGood.getProfit() == maxProfit
                        && currentGood.getId() > chosenGood) {
                    chosenGood = currentGood.getId();
                }
            }
        }
        return chosenGood;
    }

    /* Changes the frequency of the good in the tableGoods map */
    public void changeFrequency(final Map<Goods, Integer> freq,
                                final Map<Goods, Integer> tableGoods,
                                final Goods currentGood,
                                final Integer value) {
        if (tableGoods.containsKey(currentGood)) {
            Integer newFreq = tableGoods.get(currentGood)
                    + value;
            tableGoods.replace(currentGood, newFreq);
        } else {
            tableGoods.put(currentGood, value);
        }
    }

    /* Returns the goods chosen for the bag */
    public void getGood(final GoodsFactory goods, final int parity,
                        final List<Integer> bag, final Map<Goods, Integer> freq,
                        final Map<Goods, Integer> tableGoods,
                        final List<Integer> chosenItems,
                        final Constants ct) {
        if (this.hasLegalGoods(bag, ct)) {
            Integer chosenGood = this.getLegalChoice(goods, freq, ct);
            Goods currentGood = goods.getGoodsById(chosenGood);
            if (freq.get(currentGood) <= ct.getDeckSize()) {
                this.changeFrequency(freq, tableGoods, currentGood, freq.get(currentGood));
                for (int i = 0; i < freq.get(currentGood); ++i) {
                    chosenItems.add(currentGood.getId());
                }
            } else {
                for (int i = 0; i < ct.getDeckSize(); ++i) {
                    chosenItems.add(currentGood.getId());
                }
                this.changeFrequency(freq, tableGoods, currentGood, ct.getDeckSize());
            }
            this.setDeclaredGood(chosenGood);
        } else {
            Integer max = this.findMaxProfitCard(goods, bag, ct);
            Integer penalty = goods.getGoodsById(max).getPenalty();
            if (this.getMoney() >= penalty) {
                this.setDeclaredGood(0);
                chosenItems.add(max);
                Goods currentGood = goods.getGoodsById(max);
                this.changeFrequency(freq, tableGoods, currentGood, 1);
            }
            bag.remove(max);
        }
    }

    /* Returns whether or not the player being verified is a liar */
    public boolean isLiar(final Constants ct) {
        for (int i = 0; i < this.getChosenItems().size(); ++i) {
            if (!this.getChosenItems().get(i).equals(this.getDeclaredGood())
                    || (this.getChosenItems().get(i) >= ct.getMinIllegalId())) {
                return true;
            }
        }
        return false;
    }

    /* Returns the bag created by the current player */
    public void commerciant(final Queue<Integer> cards,
                            final GoodsFactory goods,
                            final int parity, final Constants ct) {
        this.createBag(cards, goods, this.getBag(), this.getFreq(), ct);
        this.getGood(goods, parity, this.getBag(), this.getFreq(),
                this.getTableGoods(), this.getChosenItems(), ct);
    }

    /* Checks the player */
    public void sheriff(final Player p, final GoodsFactory goods,
                        final Queue<Integer> cards, final Integer thisBribe,
                        final int thisId, final int numPlayers,
                        final Constants ct) {
        Integer declaredObj = p.getDeclaredGood();
        List<Integer> bagItems = p.getChosenItems();
        boolean liar = p.isLiar(ct);
        if (this.getMoney() >= ct.getMinSheriffMoney()) {
            if (liar) {
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
                Goods currentGood = goods.getGoodsById(p.getDeclaredGood());
                if (p.getChosenItems().size() > 0) {
                    this.setMoney(this.getMoney()
                            - (p.getChosenItems().size() * currentGood.getPenalty()));
                    p.setMoney(p.getMoney()
                            + (p.getChosenItems().size() * currentGood.getPenalty()));
                }
            }
        }
    }

    /* Adds the bonus items to the player's bag */
    public void receiveBonusItems(final GoodsFactory goods,
                                  final Constants ct) {
        Map<Goods, Integer> copyTableGoods = new HashMap<Goods, Integer>();
        for (Goods g : this.getTableGoods().keySet()) {
            copyTableGoods.put(g, this.getTableGoods().get(g));
        }
        for (Goods g : copyTableGoods.keySet()) {
            if (g.getId() >= ct.getMinIllegalId()) {
                Map<Goods, Integer> illegals = g.getIllegalBonus();
                for (Goods i : illegals.keySet()) {
                    if (this.getTableGoods().containsKey(i)) {
                        Integer newFreq = this.getTableGoods().get(i)
                                + illegals.get(i) * copyTableGoods.get(g);
                        this.getTableGoods().replace(i, newFreq);
                    } else {
                        this.getTableGoods().put(i, illegals.get(i)
                                * copyTableGoods.get(g));
                    }
                }
            }
        }
    }

    /* Gives the current player the bonus for the goods in their bag */
    public void getReward(final GoodsFactory goods) {
        for (Goods g : this.getTableGoods().keySet()) {
            this.setMoney(this.getMoney() + g.getProfit()
                    * this.getTableGoods().get(g));
        }
    }
}
