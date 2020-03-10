// Copyright 2019 Banu Miruna Elena 321CA
package com.tema1.players;

import com.tema1.goods.Goods;

import java.util.Comparator;

public class ProfitComparator implements Comparator<Goods> {
    @Override
    // Compares by profit and id
    public final int compare(final Goods c1, final Goods c2) {
        int c = c2.getProfit() - c1.getProfit();
        if (c == 0) {
            c = c2.getId() - c1.getId();
        }
        return c;
    }
}
