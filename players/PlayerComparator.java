// Copyright 2019 Banu Miruna Elena 321CA
package com.tema1.players;

import java.util.Comparator;

public class PlayerComparator implements Comparator<Player> {
    @Override
    // Compares by money
    public final int compare(final Player c1, final Player c2) {
        return c2.getMoney() - c1.getMoney();
    }
}
