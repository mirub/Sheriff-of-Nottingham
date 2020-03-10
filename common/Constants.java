package com.tema1.common;

public final class Constants {
    // add/delete any constants you think you may use
    private static Constants constants = null;
    private static final int BRIBE1 = 5;
    private static final int BRIBE2 = 10;
    private static final int DECK_SIZE = 10;
    private static final int INITIAL_MONEY = 80;
    private static final int APPLE_ID = 0;
    private static final int BAG_SIZE = 8;
    private static final int MIN_SHERIFF_MONEY = 16;
    private static final int MIN_BRIBE_MONEY = 5;
    private static final int MAX_LEGAL_ID = 9;
    private static final int MIN_ILLEGAL_ID = 20;

    public static int getMinIllegalId() {
        return MIN_ILLEGAL_ID;
    }

    private Constants() {

    }

    public static Constants getInstance() {
        if (constants == null) {
            constants = new Constants();
        }
        return constants;
    }

    public static int getBRIBE2() {
        return BRIBE2;
    }

    public static int getBRIBE1() {
        return BRIBE1;
    }

    public static int getDeckSize() {
        return DECK_SIZE;
    }

    public static int getInitialMoney() {
        return INITIAL_MONEY;
    }

    public static int getAppleId() {
        return APPLE_ID;
    }

    public static int getBagSize() {
        return BAG_SIZE;
    }

    public static int getMinSheriffMoney() {
        return MIN_SHERIFF_MONEY;
    }

    public static int getMinBribeMoney() {
        return MIN_BRIBE_MONEY;
    }

    public static int getMaxLegalId() {
        return MAX_LEGAL_ID;
    }
}
