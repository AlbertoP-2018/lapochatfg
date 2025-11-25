package com.tfdevelopment.pochaonlinetfd.tests.AI_BetAnswer;

import static com.tfdevelopment.pochaonlinetfd.MainSuiteTest.gC;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.Gdx;
import com.tfdevelopment.pochaonlinetfd.MainSuiteTest;
import com.tfdevelopment.pochaonlinetfd.model.card.Card;
import com.tfdevelopment.pochaonlinetfd.model.player.ai.AI_BetAnswer;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.Suit;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

public class BetChance1 {
    private static final String TAG = BetChance1.class.getName();
    private static final int CODE_FUNCTION_TEST = 1;
    private static final int NUM_CHANCE = 1;
    private static final Suit TRUMP_SUIT = Suit.OROS;

    private ArrayList<Card> alCard;

    @BeforeClass
    public static void instance(){
        MainSuiteTest.initPochaOnline(new String[]{});
        Gdx.app.log(TAG, "** Taking tests... **");
    }

    @Before
    public void initData(){
        alCard = new ArrayList<>();
    }

    @Test
    public void noTrumpsNoBets(){ // NO tengo pintes, NO existen apuestas realizadas
        int totalBets = 0;
        boolean firstPlayer, lastPlayer;

        /** First Player **/
        Gdx.app.log(TAG, "NO Trumps - NO Bets - First Player");
        firstPlayer = true;
        lastPlayer = false;

        alCard.clear();
        alCard.add(gC("KCopas"));
        AI_BetAnswer.initData(NUM_CHANCE, TRUMP_SUIT, totalBets, alCard);
        assertEquals(0, AI_BetAnswer.doTEST(CODE_FUNCTION_TEST, firstPlayer, lastPlayer));
        assertFalse(alCard.get(0).isWinning());

        /** Intermediate Player **/
        Gdx.app.log(TAG, "NO Trumps - NO Bets - Intermediate Player");
        firstPlayer = false;
        lastPlayer = false;

        alCard.clear();
        alCard.add(gC("KCopas"));
        AI_BetAnswer.initData(NUM_CHANCE, TRUMP_SUIT, totalBets, alCard);
        assertEquals(0, AI_BetAnswer.doTEST(CODE_FUNCTION_TEST, firstPlayer, lastPlayer));
        assertFalse(alCard.get(0).isWinning());

        alCard.clear();
        alCard.add(gC("ASCopas"));
        AI_BetAnswer.initData(NUM_CHANCE, TRUMP_SUIT, totalBets, alCard);
        assertEquals(0, AI_BetAnswer.doTEST(CODE_FUNCTION_TEST, firstPlayer, lastPlayer));
        assertFalse(alCard.get(0).isWinning());

        /** Last Player **/
        Gdx.app.log(TAG, "NO Trumps - NO Bets - Last Player");
        firstPlayer = false;
        lastPlayer = true;

        alCard.clear();
        alCard.add(gC("KCopas"));
        AI_BetAnswer.initData(NUM_CHANCE, TRUMP_SUIT, totalBets, alCard);
        assertEquals(0, AI_BetAnswer.doTEST(CODE_FUNCTION_TEST, firstPlayer, lastPlayer));
        assertFalse(alCard.get(0).isWinning());

        alCard.clear();
        alCard.add(gC("ASCopas"));
        AI_BetAnswer.initData(NUM_CHANCE, TRUMP_SUIT, totalBets, alCard);
        assertEquals(0, AI_BetAnswer.doTEST(CODE_FUNCTION_TEST, firstPlayer, lastPlayer));
        assertFalse(alCard.get(0).isWinning());
    }

    @Test
    public void yesTrumpsNoBets(){ // SÍ tengo pintes, NO existen apuestas realizadas
        int totalBets = 0;
        boolean firstPlayer, lastPlayer;

        /** First Player **/
        Gdx.app.log(TAG, "YES Trumps - NO Bets - First Player");
        firstPlayer = true;
        lastPlayer = false;

        alCard.clear();
        alCard.add(gC("ASOros"));
        AI_BetAnswer.initData(NUM_CHANCE, TRUMP_SUIT, totalBets, alCard);
        assertEquals(1, AI_BetAnswer.doTEST(CODE_FUNCTION_TEST, firstPlayer, lastPlayer));
        assertTrue(alCard.get(0).isWinning());

        alCard.clear();
        alCard.add(gC("2Oros"));
        AI_BetAnswer.initData(NUM_CHANCE, TRUMP_SUIT, totalBets, alCard);
        assertEquals(1, AI_BetAnswer.doTEST(CODE_FUNCTION_TEST, firstPlayer, lastPlayer));
        assertTrue(alCard.get(0).isWinning());

        /** Intermediate Player **/
        Gdx.app.log(TAG, "YES Trumps - NO Bets - Intermediate Player");
        firstPlayer = false;
        lastPlayer = false;

        alCard.clear();
        alCard.add(gC("ASOros"));
        AI_BetAnswer.initData(NUM_CHANCE, TRUMP_SUIT, totalBets, alCard);
        assertEquals(1, AI_BetAnswer.doTEST(CODE_FUNCTION_TEST, firstPlayer, lastPlayer));
        assertTrue(alCard.get(0).isWinning());

        alCard.clear();
        alCard.add(gC("2Oros"));
        AI_BetAnswer.initData(NUM_CHANCE, TRUMP_SUIT, totalBets, alCard);
        assertEquals(1, AI_BetAnswer.doTEST(CODE_FUNCTION_TEST, firstPlayer, lastPlayer));
        assertTrue(alCard.get(0).isWinning());

        /** Last Player **/
        Gdx.app.log(TAG, "YES Trumps - NO Bets - Last Player");
        firstPlayer = false;
        lastPlayer = true;

        alCard.clear();
        alCard.add(gC("ASOros"));
        AI_BetAnswer.initData(NUM_CHANCE, TRUMP_SUIT, totalBets, alCard);
        assertEquals(0, AI_BetAnswer.doTEST(CODE_FUNCTION_TEST, firstPlayer, lastPlayer));
        assertFalse(alCard.get(0).isWinning());

        alCard.clear();
        alCard.add(gC("2Oros"));
        AI_BetAnswer.initData(NUM_CHANCE, TRUMP_SUIT, totalBets, alCard);
        assertEquals(0, AI_BetAnswer.doTEST(CODE_FUNCTION_TEST, firstPlayer, lastPlayer));
        assertFalse(alCard.get(0).isWinning());
    }

    @Test
    public void noTrumpsYesBets() { // NO tengo pintes, SÍ existen apuestas realizadas
        boolean firstPlayer, lastPlayer;

        /** First Player **/
        //No puede haber apuestas>0 siendo el primero

        /** Intermediate Player **/
        Gdx.app.log(TAG, "NO Trumps - YES Bets - Intermediate Player");
        firstPlayer = false;
        lastPlayer = false;

        alCard.clear();
        alCard.add(gC("ASCopas"));
        AI_BetAnswer.initData(NUM_CHANCE, TRUMP_SUIT, 1, alCard);
        assertEquals(0, AI_BetAnswer.doTEST(CODE_FUNCTION_TEST, firstPlayer, lastPlayer));
        assertFalse(alCard.get(0).isWinning());

        alCard.clear();
        alCard.add(gC("2Copas"));
        AI_BetAnswer.initData(NUM_CHANCE, TRUMP_SUIT, 1, alCard);
        assertEquals(0, AI_BetAnswer.doTEST(CODE_FUNCTION_TEST, firstPlayer, lastPlayer));
        assertFalse(alCard.get(0).isWinning());

        /** Last Player **/
        Gdx.app.log(TAG, "NO Trumps - YES Bets - Last Player");
        firstPlayer = false;
        lastPlayer = true;

        alCard.clear();
        alCard.add(gC("ASCopas"));
        AI_BetAnswer.initData(NUM_CHANCE, TRUMP_SUIT, 1, alCard);
        assertEquals(1, AI_BetAnswer.doTEST(CODE_FUNCTION_TEST, firstPlayer, lastPlayer));
        assertTrue(alCard.get(0).isWinning());
        alCard.clear();
        alCard.add(gC("ASCopas"));
        AI_BetAnswer.initData(NUM_CHANCE, TRUMP_SUIT, 2, alCard);
        assertEquals(0, AI_BetAnswer.doTEST(CODE_FUNCTION_TEST, firstPlayer, lastPlayer));
        assertFalse(alCard.get(0).isWinning());

        alCard.clear();
        alCard.add(gC("2Copas"));
        AI_BetAnswer.initData(NUM_CHANCE, TRUMP_SUIT, 1, alCard);
        assertEquals(1, AI_BetAnswer.doTEST(CODE_FUNCTION_TEST, firstPlayer, lastPlayer));
        assertTrue(alCard.get(0).isWinning());
        alCard.clear();
        alCard.add(gC("2Copas"));
        AI_BetAnswer.initData(NUM_CHANCE, TRUMP_SUIT, 2, alCard);
        assertEquals(0, AI_BetAnswer.doTEST(CODE_FUNCTION_TEST, firstPlayer, lastPlayer));
        assertFalse(alCard.get(0).isWinning());
    }

    @Test
    public void yesTrumpsYesBets(){ // Sí tengo pintes, SÍ existen apuestas realizadas
        boolean firstPlayer, lastPlayer;

        /** First Player **/
        //No puede haber apuestas>0 siendo el primero

        /** Intermediate Player **/
        Gdx.app.log(TAG, "YES Trumps - YES Bets - Intermediate Player");
        firstPlayer = false;
        lastPlayer = false;

        alCard.clear();
        alCard.add(gC("ASOros"));
        AI_BetAnswer.initData(NUM_CHANCE, TRUMP_SUIT, 1, alCard);
        assertEquals(1, AI_BetAnswer.doTEST(CODE_FUNCTION_TEST, firstPlayer, lastPlayer));
        assertTrue(alCard.get(0).isWinning());

        alCard.clear();
        alCard.add(gC("QOros"));
        AI_BetAnswer.initData(NUM_CHANCE, TRUMP_SUIT, 1, alCard);
        assertEquals(1, AI_BetAnswer.doTEST(CODE_FUNCTION_TEST, firstPlayer, lastPlayer));
        assertTrue(alCard.get(0).isWinning());

        alCard.clear();
        alCard.add(gC("5Oros"));
        AI_BetAnswer.initData(NUM_CHANCE, TRUMP_SUIT, 1, alCard);
        assertEquals(1, AI_BetAnswer.doTEST(CODE_FUNCTION_TEST, firstPlayer, lastPlayer));
        assertTrue(alCard.get(0).isWinning());

        alCard.clear();
        alCard.add(gC("4Oros"));
        AI_BetAnswer.initData(NUM_CHANCE, TRUMP_SUIT, 1, alCard);
        assertEquals(0, AI_BetAnswer.doTEST(CODE_FUNCTION_TEST, firstPlayer, lastPlayer));
        assertFalse(alCard.get(0).isWinning());

        alCard.clear();
        alCard.add(gC("2Oros"));
        AI_BetAnswer.initData(NUM_CHANCE, TRUMP_SUIT, 1, alCard);
        assertEquals(0, AI_BetAnswer.doTEST(CODE_FUNCTION_TEST, firstPlayer, lastPlayer));
        assertFalse(alCard.get(0).isWinning());

        Gdx.app.log(TAG, "YES Trumps - YES Bets - Last Player");
        firstPlayer = false;
        lastPlayer = true;

        alCard.clear();
        alCard.add(gC("ASOros"));
        AI_BetAnswer.initData(NUM_CHANCE, TRUMP_SUIT, 1, alCard);
        assertEquals(1, AI_BetAnswer.doTEST(CODE_FUNCTION_TEST, firstPlayer, lastPlayer));
        assertTrue(alCard.get(0).isWinning());
        alCard.clear();
        alCard.add(gC("ASOros"));
        AI_BetAnswer.initData(NUM_CHANCE, TRUMP_SUIT, 2, alCard);
        assertEquals(1, AI_BetAnswer.doTEST(CODE_FUNCTION_TEST, firstPlayer, lastPlayer));
        assertTrue(alCard.get(0).isWinning());

        alCard.clear();
        alCard.add(gC("QOros"));
        AI_BetAnswer.initData(NUM_CHANCE, TRUMP_SUIT, 1, alCard);
        assertEquals(1, AI_BetAnswer.doTEST(CODE_FUNCTION_TEST, firstPlayer, lastPlayer));
        assertTrue(alCard.get(0).isWinning());
        alCard.clear();
        alCard.add(gC("QOros"));
        AI_BetAnswer.initData(NUM_CHANCE, TRUMP_SUIT, 2, alCard);
        assertEquals(1, AI_BetAnswer.doTEST(CODE_FUNCTION_TEST, firstPlayer, lastPlayer));
        assertTrue(alCard.get(0).isWinning());

        alCard.clear();
        alCard.add(gC("5Oros"));
        AI_BetAnswer.initData(NUM_CHANCE, TRUMP_SUIT, 1, alCard);
        assertEquals(1, AI_BetAnswer.doTEST(CODE_FUNCTION_TEST, firstPlayer, lastPlayer));
        assertTrue(alCard.get(0).isWinning());
        alCard.clear();
        alCard.add(gC("5Oros"));
        AI_BetAnswer.initData(NUM_CHANCE, TRUMP_SUIT, 2, alCard);
        assertEquals(1, AI_BetAnswer.doTEST(CODE_FUNCTION_TEST, firstPlayer, lastPlayer));
        assertTrue(alCard.get(0).isWinning());

        alCard.clear();
        alCard.add(gC("4Oros"));
        AI_BetAnswer.initData(NUM_CHANCE, TRUMP_SUIT, 1, alCard);
        assertEquals(1, AI_BetAnswer.doTEST(CODE_FUNCTION_TEST, firstPlayer, lastPlayer));
        assertTrue(alCard.get(0).isWinning());
        alCard.clear();
        alCard.add(gC("4Oros"));
        AI_BetAnswer.initData(NUM_CHANCE, TRUMP_SUIT, 2, alCard);
        assertEquals(0, AI_BetAnswer.doTEST(CODE_FUNCTION_TEST, firstPlayer, lastPlayer));
        assertFalse(alCard.get(0).isWinning());

        alCard.clear();
        alCard.add(gC("2Oros"));
        AI_BetAnswer.initData(NUM_CHANCE, TRUMP_SUIT, 1, alCard);
        assertEquals(1, AI_BetAnswer.doTEST(CODE_FUNCTION_TEST, firstPlayer, lastPlayer));
        assertTrue(alCard.get(0).isWinning());
        alCard.clear();
        alCard.add(gC("2Oros"));
        AI_BetAnswer.initData(NUM_CHANCE, TRUMP_SUIT, 2, alCard);
        assertEquals(0, AI_BetAnswer.doTEST(CODE_FUNCTION_TEST, firstPlayer, lastPlayer));
        assertFalse(alCard.get(0).isWinning());
    }
}
