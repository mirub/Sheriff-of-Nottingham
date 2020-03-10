// Copyright 2019 Banu Miruna Elena 321CA
package com.tema1.players;

import com.tema1.common.Constants;
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.HashMap;


public abstract class Player {
//    private static final int INITIAL_MONEY = 80;
//    private static final int DECK_SIZE = 10;
//
//    public static int getDeckSize() {
//        return DECK_SIZE;
//    }
//
//    public static int getInitialMoney() {
//        return INITIAL_MONEY;
//    }

    private int money = Constants.getInitialMoney();
    private List<Integer> bag = new ArrayList<Integer>(Constants.getDeckSize());
    private List<Integer> chosenItems = new ArrayList<Integer>();
    private Integer declaredGood = -1;
    private Map<Goods, Integer> freq = new HashMap<Goods, Integer>();
    private Map<Goods, Integer> tableGoods = new HashMap<Goods, Integer>();
    private Integer playerType = -1;
    private Integer id = -1;
    private Integer bribe = 0;
    private Integer currentMoney = 0;


    public Player() {
    }

    public Player(final Integer type) {
        this.setPlayerType(type);
    }

    public abstract void createBag(Queue<Integer> cards, GoodsFactory goods, List<Integer> thisBag,
                                   Map<Goods, Integer> thisFreq, Constants ct);

    public abstract void commerciant(Queue<Integer> cards, GoodsFactory goods, int parity,
                                     Constants ct);

    public abstract void sheriff(Player p, GoodsFactory goods, Queue<Integer> cards,
                                 Integer thisBribe, int thisId, int numPlayers, Constants ct);

    public abstract void getReward(GoodsFactory goods);

    public abstract boolean isLiar(Constants ct);

    public abstract void receiveBonusItems(GoodsFactory goods, Constants ct);

    public final int getMoney() {
        return money;
    }

    public final void setMoney(final int money) {
        this.money = money;
    }

    public final List<Integer> getBag() {
        return bag;
    }

    public final void setBag(final List<Integer> bag) {
        this.bag = bag;
    }

    public final List<Integer> getChosenItems() {
        return chosenItems;
    }

    public final void setChosenItems(final List<Integer> chosenItems) {
        this.chosenItems = chosenItems;
    }

    public final Map<Goods, Integer> getFreq() {
        return freq;
    }

    public final void setFreq(final Map<Goods, Integer> freq) {
        this.freq = freq;
    }

    public final Map<Goods, Integer> getTableGoods() {
        return tableGoods;
    }

    public final void setTableGoods(final Map<Goods, Integer> tableGoods) {
        this.tableGoods = tableGoods;
    }

    public final Integer getDeclaredGood() {
        return declaredGood;
    }

    public final void setDeclaredGood(final Integer declaredGood) {
        this.declaredGood = declaredGood;
    }

    public final Integer getPlayerType() {
        return playerType;
    }

    public final void setPlayerType(final Integer playerType) {
        this.playerType = playerType;
    }

    public final Integer getId() {
        return id;
    }

    public final void setId(final Integer id) {
        this.id = id;
    }

    public final Integer getBribe() {
        return bribe;
    }

    public final void setBribe(final Integer bribe) {
        this.bribe = bribe;
    }

    public final Integer getCurrentMoney() {
        return currentMoney;
    }

    public final void setCurrentMoney(final Integer currentMoney) {
        this.currentMoney = currentMoney;
    }
}
