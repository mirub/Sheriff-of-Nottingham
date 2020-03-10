// Copyright 2019 Banu Miruna Elena 321CA
package com.tema1.main;

import com.tema1.common.Constants;
import com.tema1.goods.GoodsFactory;

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

        GoodsFactory goods = GoodsFactory.getInstance();
        Constants ct = Constants.getInstance();
        List<Integer> assetID = gameInput.getAssetIds();
        List<String> playerTypes = gameInput.getPlayerNames();
        int rounds = gameInput.getRounds();
        Queue<Integer> cards = new LinkedList<Integer>();

        for (int i = 0; i < assetID.size(); ++i) {
            cards.add(assetID.get(i));
        }

        PlayGame play = PlayGame.getInstance(cards, playerTypes, rounds);
        play.play(goods, ct);
        //TODO implement homework logic
    }
}
