package com.tfdevelopment.pochaonlinetfd.model;

public class PochaEnum {
    public static ScreenType screenType = null;

    public enum ScreenType { MENU, LOGIN, SERVER_MAIN, OFFLINE_GAME, ONLINE_GAME }

    public enum Suit { OROS, COPAS, ESPADAS, BASTOS }
    public enum CardType { DOS, CUATRO, CINCO, SEIS, SIETE, J, Q, K, TRES, AS} // Don't change the order

    public enum PhasePlayer { BET, CARD, WAITING, CONTINUE }
    public enum Phase {BET, CARD, CONTINUE, EXIT}
    public enum DecisionB { BET, CARD, WAITING, CONTINUE }
    public enum GameMode { ONLINE, OFFLINE }
}
