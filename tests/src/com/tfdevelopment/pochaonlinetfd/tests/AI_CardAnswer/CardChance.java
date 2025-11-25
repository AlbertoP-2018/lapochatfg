package com.tfdevelopment.pochaonlinetfd.tests.AI_CardAnswer;

import static com.tfdevelopment.pochaonlinetfd.MainSuiteTest.gC;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.Gdx;
import com.tfdevelopment.pochaonlinetfd.MainSuiteTest;
import com.tfdevelopment.pochaonlinetfd.model.card.Card;
import com.tfdevelopment.pochaonlinetfd.model.player.ai.AI_CardAnswer;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum.Suit;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

public class CardChance {
    private static final String TAG = CardChance.class.getName();
    private static final Suit TRUMP_SUIT = Suit.OROS;

    private ArrayList<Card> alPlayerCards;
    private ArrayList<Card> alTableCards;

    @BeforeClass
    public static void instance(){
        MainSuiteTest.initPochaOnline(new String[]{});
        Gdx.app.log(TAG, "** Taking tests... **");
    }

    @Before
    public void initData(){
        alPlayerCards = new ArrayList<>();
        alTableCards = new ArrayList<>();
    }

    @Test
    public void noMoreChances(){ //NO tengo que ganar más bazas
        int bet = 0; int win = 1;
        boolean lastPlayer;

        /** First Player **/
        Gdx.app.log(TAG, "NO more chances - First Player");
        lastPlayer = false;
        {
            //Tengo pintes. Empiezo por la más alta para quitármela.
            alPlayerCards.clear(); alTableCards.clear();
            alPlayerCards.add(gC("3Oros")); alPlayerCards.add(gC("JOros"));
            AI_CardAnswer.initData(alPlayerCards, bet, win, lastPlayer, TRUMP_SUIT, alTableCards);
            assertEquals(0, AI_CardAnswer.doTEST());

            //Tengo pìntes. Empiezo por la más alta para quitármela.
            alPlayerCards.clear(); alTableCards.clear();
            alPlayerCards.add(gC("2Oros")); alPlayerCards.add(gC("ASEspadas")); alPlayerCards.add(gC("6Oros"));
            AI_CardAnswer.initData(alPlayerCards, bet, win, lastPlayer, TRUMP_SUIT, alTableCards);
            assertEquals(2, AI_CardAnswer.doTEST());

            //Tengo pìntes. Empiezo por la más alta para quitármela.
            alPlayerCards.clear(); alTableCards.clear();
            alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("4Oros"));
            alPlayerCards.add(gC("ASCopas")); alPlayerCards.add(gC("3Espadas"));
            AI_CardAnswer.initData(alPlayerCards, bet, win, lastPlayer, TRUMP_SUIT, alTableCards);
            assertEquals(0, AI_CardAnswer.doTEST());

            //No tengo pintes. Empiezo por la más baja.
            alPlayerCards.clear(); alTableCards.clear();
            alPlayerCards.add(gC("4Copas")); alPlayerCards.add(gC("ASEspadas"));
            AI_CardAnswer.initData(alPlayerCards, bet, win, lastPlayer, TRUMP_SUIT, alTableCards);
            assertEquals(0, AI_CardAnswer.doTEST());

            //No tengo pintes. Empiezo por la más baja.
            alPlayerCards.clear(); alTableCards.clear();
            alPlayerCards.add(gC("ASCopas")); alPlayerCards.add(gC("4Espadas"));
            AI_CardAnswer.initData(alPlayerCards, bet, win, lastPlayer, TRUMP_SUIT, alTableCards);
            assertEquals(1, AI_CardAnswer.doTEST());
        }

        /** Intermediate Player **/
        Gdx.app.log(TAG, "NO more chances - Intermediate Player");
        lastPlayer = false;
        {
            //Empieza por pinte. Echo la más baja que supere
            alPlayerCards.clear(); alTableCards.clear();
            alPlayerCards.add(gC("3Oros")); alPlayerCards.add(gC("JOros"));
            alTableCards.add(gC("7Oros")); alTableCards.add(gC("2Copas"));
            AI_CardAnswer.initData(alPlayerCards, bet, win, lastPlayer, TRUMP_SUIT, alTableCards);
            assertEquals(1, AI_CardAnswer.doTEST());

            //Empieza por pinte. Echo la más baja que supere
            alPlayerCards.clear(); alTableCards.clear();
            alPlayerCards.add(gC("5Oros")); alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("3Oros"));
            alTableCards.add(gC("2Oros")); alTableCards.add(gC("6Oros"));
            AI_CardAnswer.initData(alPlayerCards, bet, win, lastPlayer, TRUMP_SUIT, alTableCards);
            assertEquals(1, AI_CardAnswer.doTEST());

            //NO empieza por pinte. Echo la más baja que supere
            alPlayerCards.clear(); alTableCards.clear();
            alPlayerCards.add(gC("KCopas")); alPlayerCards.add(gC("JCopas")); alPlayerCards.add(gC("5Copas"));
            alTableCards.add(gC("4Copas")); alTableCards.add(gC("6Copas"));
            AI_CardAnswer.initData(alPlayerCards, bet, win, lastPlayer, TRUMP_SUIT, alTableCards);
            assertEquals(1, AI_CardAnswer.doTEST());

            //NO empieza por pinte, pero hay pinte en mesa. No tengo pintes, echo la más alta
            alPlayerCards.clear(); alTableCards.clear();
            alPlayerCards.add(gC("KCopas")); alPlayerCards.add(gC("JCopas"));
            alTableCards.add(gC("7Copas")); alTableCards.add(gC("2Oros"));
            AI_CardAnswer.initData(alPlayerCards, bet, win, lastPlayer, TRUMP_SUIT, alTableCards);
            assertEquals(0, AI_CardAnswer.doTEST());

            //NO empieza por pinte, pero hay pinte en mesa. No tengo pintes, echo la más alta
            alPlayerCards.clear(); alTableCards.clear();
            alPlayerCards.add(gC("KCopas")); alPlayerCards.add(gC("3Copas")); alPlayerCards.add(gC("6Copas"));
            alTableCards.add(gC("7Copas")); alTableCards.add(gC("2Oros"));
            AI_CardAnswer.initData(alPlayerCards, bet, win, lastPlayer, TRUMP_SUIT, alTableCards);
            assertEquals(1, AI_CardAnswer.doTEST());
        }

        /** Last Player **/
        Gdx.app.log(TAG, "NO more chances - Last Player");
        lastPlayer = true;
        {
            //Empieza por pinte y tengo pintes. Soy el último y tengo para superar, echo la más alta
            alPlayerCards.clear(); alTableCards.clear();
            alPlayerCards.add(gC("5Oros")); alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("3Oros"));
            alTableCards.add(gC("2Oros")); alTableCards.add(gC("KEspadas")); alTableCards.add(gC("6Oros"));
            AI_CardAnswer.initData(alPlayerCards, bet, win, lastPlayer, TRUMP_SUIT, alTableCards);
            assertEquals(2, AI_CardAnswer.doTEST());

            //Empieza por pinte y tengo pintes. Soy el último y NO tengo para superar, echo la más alta
            alPlayerCards.clear(); alTableCards.clear();
            alPlayerCards.add(gC("5Oros")); alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("QOros"));
            alTableCards.add(gC("2Oros")); alTableCards.add(gC("KEspadas")); alTableCards.add(gC("KOros"));
            AI_CardAnswer.initData(alPlayerCards, bet, win, lastPlayer, TRUMP_SUIT, alTableCards);
            assertEquals(2, AI_CardAnswer.doTEST());

            //NO Empieza por pinte, pero hay pinte en mesa. Tengo del palo de salida, echo la más alta
            alPlayerCards.clear(); alTableCards.clear();
            alPlayerCards.add(gC("5Oros")); alPlayerCards.add(gC("ASEspadas")); alPlayerCards.add(gC("QOros"));
            alTableCards.add(gC("2Espadas")); alTableCards.add(gC("KEspadas")); alTableCards.add(gC("KOros"));
            AI_CardAnswer.initData(alPlayerCards, bet, win, lastPlayer, TRUMP_SUIT, alTableCards);
            assertEquals(1, AI_CardAnswer.doTEST());

            //NO empieza por pinte, pero hay pinte en mesa. No tengo pintes, echo la más alta
            alPlayerCards.clear(); alTableCards.clear();
            alPlayerCards.add(gC("KCopas")); alPlayerCards.add(gC("JCopas")); alPlayerCards.add(gC("6Copas")); alPlayerCards.add(gC("3Copas"));
            alTableCards.add(gC("7Copas")); alTableCards.add(gC("2Oros")); alTableCards.add(gC("4Copas"));
            AI_CardAnswer.initData(alPlayerCards, bet, win, lastPlayer, TRUMP_SUIT, alTableCards);
            assertEquals(3, AI_CardAnswer.doTEST());
        }
    }

    @Test
    public void moreChances(){ // SÍ tengo que ganar más bazas
        boolean lastPlayer;

        /** First Player **/
        Gdx.app.log(TAG, "YES more chances - First Player");
        lastPlayer = false;
        {
            //1. Tengo triunfos - 1.1 Son todas triunfos - 1.1.1 Tengo que ganar 1
            {
                //Tengo 2/2 triunfos. Tengo que ganar 1. Echo el más bajo
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("6Oros")); alPlayerCards.add(gC("JOros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(0, AI_CardAnswer.doTEST());

                //Tengo 3/3 triunfos. Tengo que ganar 1. Echo el más alto
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("3Oros")); alPlayerCards.add(gC("6Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 4/4 triunfos. Tengo que ganar 1. Echo el más alto
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("3Oros")); alPlayerCards.add(gC("7Oros")); alPlayerCards.add(gC("4Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());
            }
            //1. Tengo triunfos - 1.1 Son todas triunfos - 1.1.2 Tengo que ganar >=2
            {
                //Tengo 2/2 triunfos. Tengo que ganar más de 1. Echo el más bajo
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("6Oros")); alPlayerCards.add(gC("JOros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(0, AI_CardAnswer.doTEST());

                //Tengo 3/3 triunfos. Tengo que ganar más de 1. Echo el más bajo
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("3Oros")); alPlayerCards.add(gC("6Oros")); alPlayerCards.add(gC("JOros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());
            }
            //1. Tengo triunfos - 1.2 NO todas son triunfos - 1.2.1 Tengo que ganar 1
            {
                //Tengo 1/2 triunfos. Tengo que ganar 1. Me lo guardo para el final
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("4Oros")); alPlayerCards.add(gC("2Espadas"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 1/3 triunfos. Tengo que ganar 1. Me lo guardo para el final
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("4Oros")); alPlayerCards.add(gC("ASCopas")); alPlayerCards.add(gC("5Bastos"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());

                //Tengo 1/4 triunfos. Tengo que ganar 1. Me lo guardo para el final
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("ASCopas")); alPlayerCards.add(gC("7Bastos")); alPlayerCards.add(gC("ASEspadas")); alPlayerCards.add(gC("4Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 2/3 triunfos. Tengo que ganar 1. Echo la pinte más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("ASEspadas")); alPlayerCards.add(gC("6Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());

                //Tengo 2/4 triunfos. Tengo que ganar 1. Echo la pinte más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("ASEspadas")); alPlayerCards.add(gC("6Oros")); alPlayerCards.add(gC("JEspadas"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());

                //Tengo 3/4 triunfos. Tengo que ganar 1. Echo la pinte más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("3Copas")); alPlayerCards.add(gC("2Oros"));
                alPlayerCards.add(gC("6Oros")); alPlayerCards.add(gC("KOros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 3/5 triunfos. Tengo que ganar 1. Echo la pinte más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("ASEspadas")); alPlayerCards.add(gC("6Oros"));
                alPlayerCards.add(gC("5Oros")); alPlayerCards.add(gC("JEspadas"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(3, AI_CardAnswer.doTEST());
            }
            //1. Tengo triunfos - 1.2 NO todas son triunfos - 1.2.2 Tengo que ganar >=2
            {
                //Tengo 1/2 triunfos. Tengo que ganar 2. Me lo guardo para el final
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("4Oros")); alPlayerCards.add(gC("2Espadas"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(0, AI_CardAnswer.doTEST());

                //Tengo 1/3 triunfos. Tengo que ganar 2. Me lo guardo para el final
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("4Oros")); alPlayerCards.add(gC("ASCopas")); alPlayerCards.add(gC("5Bastos"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());

                //Tengo 1/4 triunfos. Tengo que ganar 2. Me lo guardo para el final
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("ASCopas")); alPlayerCards.add(gC("7Bastos"));
                alPlayerCards.add(gC("ASEspadas")); alPlayerCards.add(gC("4Oros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 2/3 triunfos. Tengo que ganar 2. Echo la que no es pinte
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("ASEspadas"));
                alPlayerCards.add(gC("6Oros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 2/4 triunfos. Tengo que ganar 2. Echo la que no es pinte baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("ASEspadas"));
                alPlayerCards.add(gC("6Oros")); alPlayerCards.add(gC("JEspadas"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(3, AI_CardAnswer.doTEST());

                //Tengo 3/4 triunfos. Tengo que ganar 2. Echo la pinte más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("2Oros"));
                alPlayerCards.add(gC("6Oros")); alPlayerCards.add(gC("3Copas"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 3/5 triunfos. Tengo que ganar 2. Echo la pinte más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("ASEspadas")); alPlayerCards.add(gC("6Oros"));
                alPlayerCards.add(gC("5Oros")); alPlayerCards.add(gC("JEspadas"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(3, AI_CardAnswer.doTEST());
            }
            //2. NO tengo triunfos - 2.1 Tengo que ganar 1
            {
                //Tengo 0/2 triunfos. Tengo que ganar 1. Echo la más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("KCopas")); alPlayerCards.add(gC("JEspadas"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 0/3 triunfos. Tengo que ganar 1. Echo la más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("KCopas")); alPlayerCards.add(gC("JEspadas")); alPlayerCards.add(gC("7Bastos"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());

                //Tengo 0/4 triunfos. Tengo que ganar 1. Echo la más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("KCopas")); alPlayerCards.add(gC("JEspadas"));
                alPlayerCards.add(gC("7Bastos")); alPlayerCards.add(gC("6Copas"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(3, AI_CardAnswer.doTEST());
            }
            //2. NO tengo triunfos - 2.2 Tengo que ganar >=2
            {
                //Tengo 0/2 triunfos. Tengo que ganar 2. Echo la más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("KCopas")); alPlayerCards.add(gC("JEspadas"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 0/3 triunfos. Tengo que ganar 2. Echo la más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("KCopas")); alPlayerCards.add(gC("JEspadas")); alPlayerCards.add(gC("7Bastos"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());

                //Tengo 0/4 triunfos. Tengo que ganar 2. Echo la más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("KCopas")); alPlayerCards.add(gC("JEspadas"));
                alPlayerCards.add(gC("7Bastos")); alPlayerCards.add(gC("6Copas"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(3, AI_CardAnswer.doTEST());
            }
        }

        /** Intermediate Player **/
        Gdx.app.log(TAG, "YES more chances - Intermediate Player");
        lastPlayer = false;
        {
            //1. Todas son triunfos - 1.1 Tengo que ganar 1 -  1.1.1 Se empieza por triunfo
            {
                //Tengo 2/2 triunfos. Tengo que ganar 1. Tengo para superar, echo el más bajo para tener otra oportunidad si me lo ganan
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("3Oros")); alPlayerCards.add(gC("JOros"));
                alTableCards.add(gC("7Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 2/2 triunfos. Tengo que ganar 1. NO tengo para superar, echo el más bajo
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("7Oros"));
                alTableCards.add(gC("3Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 3/3 triunfos. Tengo que ganar 1. Tengo para superar, echo el más alto porque tengo demasiados triunfos
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("6Oros")); alPlayerCards.add(gC("ASOros"));
                alTableCards.add(gC("7Oros")); alTableCards.add(gC("JOros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());

                //Tengo 3/3 triunfos. Tengo que ganar 1. NO tengo para superar. Como existe demasiada diferencia entre triunfos y por ganar, echo el segundo mejor
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("6Oros")); alPlayerCards.add(gC("JOros"));
                alTableCards.add(gC("7Oros")); alTableCards.add(gC("3Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());

                //Tengo 4/4 triunfos. Tengo que ganar 1. Tengo para superar, echo el más alto
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("3Oros"));
                alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("4Oros"));
                alTableCards.add(gC("7Oros")); alTableCards.add(gC("JOros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 4/4 triunfos. Tengo que ganar 1. NO tengo para superar. Como existe demasiada diferencia entre triunfos y por ganar, echo el segundo mejor
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("JOros"));
                alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("4Oros"));
                alTableCards.add(gC("7Oros")); alTableCards.add(gC("3Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(0 , AI_CardAnswer.doTEST());
            }
            //1. Todas son triunfos - 1.1 Tengo que ganar 1 -  1.1.2 NO se empieza por triunfo, ni hay en mesa
            {
                //2/2 triunfos. Al tener varios triunfos y empezar por una distinta, echo la más alta para quitarmela (es probable que la gane)
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("7Oros")); alPlayerCards.add(gC("JOros"));
                alTableCards.add(gC("7Copas"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //2/2 triunfos. Al tener varios triunfos y empezar por una distinta, echo la más alta para quitarmela (es probable que la gane)
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("7Oros")); alPlayerCards.add(gC("2Oros"));
                alTableCards.add(gC("3Copas"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(0, AI_CardAnswer.doTEST());

                //Tengo 3/3 triunfos. Al tener varios triunfos y empezar por una distinta, echo la más alta para quitarmela (es probable que la gane)
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("6Oros")); alPlayerCards.add(gC("ASOros"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("JCopas"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());

                //Tengo 4/4 triunfos. Al tener varios triunfos y empezar por una distinta, echo la más alta para quitarmela (es probable que la gane)
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("3Oros"));
                alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("4Oros"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("JCopas"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());
            }
            //1. Todas son triunfos - 1.1 Tengo que ganar 1 -  1.1.3 NO se empieza por triunfo, pero hay en mesa
            {
                //Tengo 2/2 triunfos. Tengo que ganar 1. Tengo para superar, echo la más alta
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("6Oros")); alPlayerCards.add(gC("3Oros"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("4Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 2/2 triunfos. Tengo que ganar 1. NO tengo para superar, echo el más bajo
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("7Oros"));
                alTableCards.add(gC("3Copas")); alTableCards.add(gC("3Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 3/3 triunfos. Tengo que ganar 1. Tengo para superar, echo la más alta
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("ASOros"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("7Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());

                //Tengo 3/3 triunfos. Tengo que ganar 1. Echo la segunda más alta porque tengo demasiados triunfos y no puedo superar
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("6Oros")); alPlayerCards.add(gC("JOros"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("3Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());

                //Tengo 4/4 triunfos. Tengo que ganar 1. Tengo para superar, echo el más alto
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("3Oros"));
                alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("4Oros"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("JOros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 4/4 triunfos. Tengo que ganar 1. NO tengo para superar. Como existe demasiada diferencia entre triunfos y por ganar, echo el segundo mejor
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("QOros"));
                alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("4Oros"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("3Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());
            }

            //1. Todas son triunfos - 1.2 Tengo que ganar >=2   - 1.2.1 Se empieza por triunfo
            {
                //Tengo 2/2 triunfos. Tengo que ganar >=2. Tengo para superar, echo el más alto
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("3Oros"));
                alTableCards.add(gC("7Oros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 2/2 triunfos. Tengo que ganar >=2. NO tengo para superar, echo el más bajo
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("7Oros"));
                alTableCards.add(gC("3Oros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 3/3 triunfos. Tengo que ganar >=2. Tengo para superar, echo el más alto
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("ASOros")); alPlayerCards.add(gC("6Oros"));
                alTableCards.add(gC("7Oros")); alTableCards.add(gC("JOros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 3/3 triunfos. Tengo que ganar >=2. NO tengo para superar, echo el más bajo
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("6Oros")); alPlayerCards.add(gC("JOros"));
                alTableCards.add(gC("7Oros")); alTableCards.add(gC("3Oros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 4/4 triunfos. Tengo que ganar >=2. Tengo para superar, echo el más bajo posible
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("4Oros")); alPlayerCards.add(gC("3Oros"));
                alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("QOros"));
                alTableCards.add(gC("7Oros")); alTableCards.add(gC("JOros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(3, AI_CardAnswer.doTEST());

                //Tengo 4/4 triunfos. Tengo que ganar >=2. NO tengo para superar, echo el más bajo
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("JOros"));
                alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("4Oros"));
                alTableCards.add(gC("7Oros")); alTableCards.add(gC("3Oros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(3 , AI_CardAnswer.doTEST());
            }
            //1. Todas son triunfos - 1.2 Tengo que ganar >=2   - 1.2.2 NO se empieza por triunfo, ni hay en mesa
            {
                //2/2 triunfos. Echo la más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("7Oros"));
                alTableCards.add(gC("7Copas"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //2/2 triunfos. Echo la más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("7Oros")); alPlayerCards.add(gC("QOros"));
                alTableCards.add(gC("3Copas"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(0, AI_CardAnswer.doTEST());

                //Tengo 3/3 triunfos. Echo la más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("6Oros")); alPlayerCards.add(gC("ASOros"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("JCopas"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 4/4 triunfos. Echo la más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("3Oros"));
                alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("4Oros"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("JCopas"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(3, AI_CardAnswer.doTEST());
            }
            //1. Todas son triunfos - 1.2 Tengo que ganar >=2   - 1.2.3 NO se empieza por triunfo, pero hay en mesa
            {
                //Tengo 2/2 triunfos. Tengo que ganar >=2. Tengo para superar, echo la más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("6Oros")); alPlayerCards.add(gC("3Oros"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("4Oros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(0, AI_CardAnswer.doTEST());

                //Tengo 2/2 triunfos. Tengo que ganar >=2. NO tengo para superar, echo la más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("7Oros"));
                alTableCards.add(gC("3Copas")); alTableCards.add(gC("3Oros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 3/3 triunfos. Tengo que ganar >=2. Tengo para superar, echo la más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("ASOros"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("7Oros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(0, AI_CardAnswer.doTEST());

                //Tengo 3/3 triunfos. Tengo que ganar >=2. NO tengo para superar, echo la más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("6Oros")); alPlayerCards.add(gC("JOros"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("3Oros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 4/4 triunfos. Tengo que ganar >=2. Tengo para superar, echo la más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("3Oros"));
                alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("4Oros"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("JOros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(0, AI_CardAnswer.doTEST());

                //Tengo 4/4 triunfos. Tengo que ganar >=2. NO tengo para superar, echo la más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("QOros"));
                alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("4Oros"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("3Oros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(3, AI_CardAnswer.doTEST());
            }

            //2. No todas son triunfos - 2.1 Tengo que ganar 1  - 2.1.1 Se empieza por triunfo
            {
                //Tengo 1/X triunfos. Echo el único triunfo que tengo
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("3Espadas")); alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("7Espadas"));
                alTableCards.add(gC("6Oros")); alTableCards.add(gC("7Copas"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 2/X triunfos. Tengo para superar, al tener que ganar que ganar solo 1, echo la más baja que supere por si me la ganan
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("7Oros")); alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("3Copas"));
                alTableCards.add(gC("5Oros")); alTableCards.add(gC("7Copas"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(0, AI_CardAnswer.doTEST());

                //Tengo 2/X triunfos. NO tengo para superar, al tener que ganar que ganar solo 1, echo la segunda más alta
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("4Oros")); alPlayerCards.add(gC("JCopas"));
                alTableCards.add(gC("3Oros")); alTableCards.add(gC("7Copas"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 3/X triunfos. Tengo para superar, al tener que ganar que ganar solo 1, echo la más alta
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("7Oros")); alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("3Copas"));
                alTableCards.add(gC("JOros")); alTableCards.add(gC("7Copas"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());

                //Tengo 3/X triunfos. NO tengo para superar, al tener que ganar que ganar solo 1, echo la segunda más alta
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("7Oros")); alPlayerCards.add(gC("4Oros")); alPlayerCards.add(gC("JCopas"));
                alTableCards.add(gC("3Oros")); alTableCards.add(gC("7Copas"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 4/X triunfos. Tengo que ganar 1. Tengo para superar, echo el más alto
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("3Oros")); alPlayerCards.add(gC("ASCopas"));
                alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("4Oros"));
                alTableCards.add(gC("7Oros")); alTableCards.add(gC("JOros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 4/X triunfos. Tengo que ganar 1. NO tengo para superar. Como existe demasiada diferencia entre triunfos y por ganar, echo el segundo mejor
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("ASCopas"));
                alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("4Oros"));
                alTableCards.add(gC("7Oros")); alTableCards.add(gC("3Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(0 , AI_CardAnswer.doTEST());
            }
            //2. No todas son triunfos - 2.1 Tengo que ganar 1  - 2.1.2 NO se empieza por triunfo, ni hay en mesa
            {
                //Tengo para superar. Como tengo triunfos, echo la más baja de la pinte
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("7Oros")); alPlayerCards.add(gC("7Copas")); alPlayerCards.add(gC("3Copas"));
                alTableCards.add(gC("6Copas"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //NO tengo para superar. Como tengo triunfos, echo la más alta
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("7Oros")); alPlayerCards.add(gC("KCopas")); alPlayerCards.add(gC("JCopas"));
                alTableCards.add(gC("3Copas"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());
            }
            //2. No todas son triunfos - 2.1 Tengo que ganar 1  - 2.1.3 NO se empieza por triunfo, pero hay en mesa
            {
                //Tengo que ganar 1. Tengo para superar, pero tengo triunfos. Intento ganar con triunfos, echo la más alta para quitarmela
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("6Oros")); alPlayerCards.add(gC("JCopas")); alPlayerCards.add(gC("KCopas"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("4Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());

                //Tengo que ganar 1. No tengo para superar, pero tengo triunfos. Echo la más alta para ganar con triunfos
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("3Oros")); alPlayerCards.add(gC("JCopas")); alPlayerCards.add(gC("QCopas"));
                alTableCards.add(gC("KCopas")); alTableCards.add(gC("6Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());

                //Tengo que ganar 1. NO tengo de la pinte. Tengo triunfos para superar el de mesa. Echo la más alta
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("6Copas")); alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("6Oros"));
                alTableCards.add(gC("7Espadas")); alTableCards.add(gC("4Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());

                //Tengo que ganar 1. NO tengo de la pinte. Tengo triunfos pero NO para superar el de mesa. Echo el segundo más alto
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("6Copas")); alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("6Oros"));
                alTableCards.add(gC("7Espadas")); alTableCards.add(gC("3Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());
            }

            //2. No todas son triunfos - 2.2 Tengo que ganar >=2  - 2.2.1 Se empieza por triunfo                            --> TODO Revisar
            {
                //Tengo 1/X triunfos. Echo el único triunfo que tengo
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("3Espadas")); alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("7Espadas"));
                alTableCards.add(gC("6Oros")); alTableCards.add(gC("7Copas"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 2/X triunfos. Tengo para superar, tengo que ganar >=2. NO tengo demasiados triunfos. Echo la más alta
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("7Oros")); alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("3Copas"));
                alTableCards.add(gC("5Oros")); alTableCards.add(gC("7Copas"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 2/X triunfos. NO tengo para superar, tengo que ganar >=2. Echo la más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("4Oros")); alPlayerCards.add(gC("JCopas")); alPlayerCards.add(gC("2Oros"));
                alTableCards.add(gC("3Oros")); alTableCards.add(gC("7Copas"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(3, AI_CardAnswer.doTEST());

                //Tengo 3/X triunfos. Tengo para superar, tengo que ganar >=2. Echo la más alta
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("7Oros")); alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("3Copas"));
                alTableCards.add(gC("JOros")); alTableCards.add(gC("7Copas"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());

                //Tengo 3/X triunfos. NO tengo para superar, tengo que ganar >=2. Echo la más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("7Oros")); alPlayerCards.add(gC("4Oros")); alPlayerCards.add(gC("JCopas"));
                alTableCards.add(gC("3Oros")); alTableCards.add(gC("7Copas"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());

                //Tengo 4/X triunfos. Tengo que ganar >=2. Echo la más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("3Oros")); alPlayerCards.add(gC("ASCopas"));
                alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("4Oros"));
                alTableCards.add(gC("7Oros")); alTableCards.add(gC("JOros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(0, AI_CardAnswer.doTEST());

                //Tengo 4/X triunfos. NO tengo para superar, tengo que ganar >=2. Echo la más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("ASCopas"));
                alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("4Oros"));
                alTableCards.add(gC("7Oros")); alTableCards.add(gC("3Oros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(4 , AI_CardAnswer.doTEST());
            }
            //2. No todas son triunfos - 2.2 Tengo que ganar >=2  - 2.2.2 NO se empieza por triunfo, ni hay en mesa         --> TODO Revisar
            {
                //Tengo para superar. Como tengo triunfos, echo la más baja de la pinte
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("7Oros")); alPlayerCards.add(gC("7Copas")); alPlayerCards.add(gC("3Copas"));
                alTableCards.add(gC("6Copas"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //NO tengo para superar. Como tengo triunfos, echo la más alta
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("7Oros")); alPlayerCards.add(gC("KCopas")); alPlayerCards.add(gC("JCopas"));
                alTableCards.add(gC("3Copas"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());
            }
            //2. No todas son triunfos - 2.2 Tengo que ganar >=2  - 2.2.3 NO se empieza por triunfo, pero hay en mesa       --> TODO Revisar
            {
                //Tengo que ganar >=2. NO puedo ganar (Tengo de inicio para superar pero hay triunfos en mesa)
                // Como tengo más pedidas que triunfos, echo la segunda segunda más alta
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("6Oros")); alPlayerCards.add(gC("JCopas")); alPlayerCards.add(gC("KCopas"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("4Oros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo que ganar >=2. NO puedo ganar (NO tengo de inicio para superar y hay triunfos en mesa)
                // Como tengo más pedidas que triunfos, echo la segunda segunda más alta de la pinte
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("3Oros")); alPlayerCards.add(gC("QCopas")); alPlayerCards.add(gC("JCopas"));
                alTableCards.add(gC("KCopas")); alTableCards.add(gC("6Oros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());

                //Tengo que ganar >=2. NO puedo ganar (Tengo de inicio para superar pero hay triunfos en mesa)
                // Tengo las mismas pedidas que triunfos, y tengo triunfos altos(winning), echo la más alta de la pinte
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("JCopas")); alPlayerCards.add(gC("KCopas"));
                alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("KOros"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("4Oros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo que ganar >=2. NO puedo ganar (Tengo de inicio para superar y hay triunfos en mesa)
                // Tengo las mismas pedidas que triunfos, pero NO tengo triunfos altos(winning), echo la segunda más alta de la pinte
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("JCopas")); alPlayerCards.add(gC("KCopas"));
                alPlayerCards.add(gC("6Oros")); alPlayerCards.add(gC("7Oros"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("5Oros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                // assertEquals(0, AI_CardAnswer.doTEST()); // TODO
            }

            //3. NO tengo triunfos - 3.1 Tengo que ganar 1  - 3.1.1 Se empieza por triunfo
            {
                //Como no tengo triunfos y tengo que ganar 1, echo la carta más baja que tenga
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("3Espadas")); alPlayerCards.add(gC("JBastos")); alPlayerCards.add(gC("7Espadas"));
                alTableCards.add(gC("6Oros")); alTableCards.add(gC("7Copas"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());
            }
            //3. NO tengo triunfos - 3.1 Tengo que ganar 1  - 3.1.2 NO se empieza por triunfo, ni hay en mesa
            {
                //Tengo cartas de inicio y sin triunfos. Intento ganarla, echo la más alta
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("3Espadas")); alPlayerCards.add(gC("JBastos")); alPlayerCards.add(gC("7Espadas"));
                alTableCards.add(gC("6Espadas")); alTableCards.add(gC("7Copas"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(0, AI_CardAnswer.doTEST());

                //NO tengo cartas de inicio y sin triunfos. Echo la carta más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("3Bastos")); alPlayerCards.add(gC("JBastos")); alPlayerCards.add(gC("7Bastos"));
                alTableCards.add(gC("6Espadas")); alTableCards.add(gC("7Copas"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());
            }
            //3. NO tengo triunfos - 3.1 Tengo que ganar 1  - 3.1.3 NO se empieza por triunfo, pero hay en mesa
            {
                //Tengo cartas de inicio, hay triunfos en mesa y tengo que ganar 1. Echo la carta más baja que tenga
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("3Copas")); alPlayerCards.add(gC("5Copas"));
                alPlayerCards.add(gC("KCopas")); alPlayerCards.add(gC("4Bastos"));
                alTableCards.add(gC("6Copas")); alTableCards.add(gC("7Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //NO tengo cartas de inicio, hay triunfos en mesa y tengo que ganar 1. Echo la carta más baja que tenga
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("3Copas")); alPlayerCards.add(gC("5Copas"));
                alPlayerCards.add(gC("KCopas")); alPlayerCards.add(gC("4Bastos"));
                alTableCards.add(gC("6Bastos")); alTableCards.add(gC("7Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(3, AI_CardAnswer.doTEST());
            }

            //3. NO tengo triunfos - 3.2 Tengo que ganar >=2  - 3.2.1 Se empieza por triunfo
            {
                //Como no tengo triunfos y tengo que ganar 1, echo la carta más baja que tenga
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("3Espadas")); alPlayerCards.add(gC("JBastos")); alPlayerCards.add(gC("7Espadas"));
                alTableCards.add(gC("6Oros")); alTableCards.add(gC("7Copas"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());
            }
            //3. NO tengo triunfos - 3.2 Tengo que ganar >=2  - 3.2.2 NO se empieza por triunfo, ni hay en mesa
            {
                //Tengo cartas de inicio y sin triunfos. Intento ganarla, echo la más alta
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("3Espadas")); alPlayerCards.add(gC("JBastos")); alPlayerCards.add(gC("7Espadas"));
                alTableCards.add(gC("6Espadas")); alTableCards.add(gC("7Copas"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(0, AI_CardAnswer.doTEST());

                //NO tengo cartas de inicio y sin triunfos. Echo la carta más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("3Bastos")); alPlayerCards.add(gC("JBastos")); alPlayerCards.add(gC("7Bastos"));
                alTableCards.add(gC("6Espadas")); alTableCards.add(gC("7Copas"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());
            }
            //3. NO tengo triunfos - 3.2 Tengo que ganar >=2  - 3.2.3 NO se empieza por triunfo, pero hay en mesa
            {
                //Tengo cartas de inicio, hay triunfos en mesa y tengo que ganar 1. Echo la carta más baja que tenga
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("3Copas")); alPlayerCards.add(gC("5Copas"));
                alPlayerCards.add(gC("KCopas")); alPlayerCards.add(gC("4Bastos"));
                alTableCards.add(gC("6Copas")); alTableCards.add(gC("7Oros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //NO tengo cartas de inicio, hay triunfos en mesa y tengo que ganar 1. Echo la carta más baja que tenga
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("3Copas")); alPlayerCards.add(gC("5Copas"));
                alPlayerCards.add(gC("KCopas")); alPlayerCards.add(gC("4Bastos"));
                alTableCards.add(gC("6Bastos")); alTableCards.add(gC("7Oros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(3, AI_CardAnswer.doTEST());
            }
        }

        /** Last Player **/
        Gdx.app.log(TAG, "YES more chances - Last Player");
        lastPlayer = true;
        {
            //1. Todas son triunfos - 1.1 Tengo que ganar 1 -  1.1.1 Se empieza por triunfo
            {
                //Tengo 2/2 triunfos. Tengo que ganar 1. Tengo para superar, echo el más bajo, ya lo he ganado
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("3Oros")); alPlayerCards.add(gC("KOros"));
                alTableCards.add(gC("7Oros")); alTableCards.add(gC("6Oros")); alTableCards.add(gC("QOros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 2/2 triunfos. Tengo que ganar 1. Tengo para superar, echo el más bajo, ya lo he ganado
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("3Oros")); alPlayerCards.add(gC("JOros"));
                alTableCards.add(gC("7Oros")); alTableCards.add(gC("6Oros")); alTableCards.add(gC("QOros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(0, AI_CardAnswer.doTEST());

                //Tengo 2/2 triunfos. Tengo que ganar 1. NO tengo para superar, echo el más bajo
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("7Oros"));
                alTableCards.add(gC("3Oros")); alTableCards.add(gC("5Oros")); alTableCards.add(gC("KOros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 3/3 triunfos. Tengo que ganar 1. Tengo para superar, echo el más alto porque tengo demasiados triunfos
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("6Oros")); alPlayerCards.add(gC("ASOros"));
                alTableCards.add(gC("7Oros")); alTableCards.add(gC("JOros")); alTableCards.add(gC("5Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());

                //Tengo 3/3 triunfos. Tengo que ganar 1. NO tengo para superar. Como existe demasiada diferencia entre triunfos y por ganar, echo el segundo mejor
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("6Oros")); alPlayerCards.add(gC("JOros"));
                alTableCards.add(gC("7Oros")); alTableCards.add(gC("3Oros")); alTableCards.add(gC("KOros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());

                //Tengo 4/4 triunfos. Tengo que ganar 1. Tengo para superar, echo el más alto
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("3Oros"));
                alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("4Oros"));
                alTableCards.add(gC("7Oros")); alTableCards.add(gC("JOros")); alTableCards.add(gC("5Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 4/4 triunfos. Tengo que ganar 1. NO tengo para superar. Como existe demasiada diferencia entre triunfos y por ganar, echo el segundo mejor
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("JOros"));
                alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("4Oros"));
                alTableCards.add(gC("7Oros")); alTableCards.add(gC("3Oros")); alTableCards.add(gC("5Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(0 , AI_CardAnswer.doTEST());
            }
            //1. Todas son triunfos - 1.1 Tengo que ganar 1 -  1.1.2 NO se empieza por triunfo, ni hay en mesa
            {
                //2/2 triunfos. Al tener varios triunfos y empezar por una distinta, echo la más alta para quitarmela (soy el último, la voy a ganar)
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("7Oros")); alPlayerCards.add(gC("JOros"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("ASCopas")); alTableCards.add(gC("KEspadas"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //2/2 triunfos. Al tener varios triunfos y empezar por una distinta, echo la más alta para quitarmela (soy el último, la voy a ganar)
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("7Oros")); alPlayerCards.add(gC("2Oros"));
                alTableCards.add(gC("3Copas")); alTableCards.add(gC("ASCopas")); alTableCards.add(gC("KEspadas"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(0, AI_CardAnswer.doTEST());

                //Tengo 3/3 triunfos. Al tener varios triunfos y empezar por una distinta, echo la más alta para quitarmela (soy el último, la voy a ganar)
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("6Oros")); alPlayerCards.add(gC("ASOros"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("ASCopas")); alTableCards.add(gC("KEspadas"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());

                //Tengo 4/4 triunfos. Al tener varios triunfos y empezar por una distinta, echo la más alta para quitarmela (soy el último, la voy a ganar)
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("3Oros"));
                alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("4Oros"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("JCopas"));  alTableCards.add(gC("ASCopas")); alTableCards.add(gC("KEspadas"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());
            }
            //1. Todas son triunfos - 1.1 Tengo que ganar 1 -  1.1.3 NO se empieza por triunfo, pero hay en mesa
            {
                //Tengo 2/2 triunfos. Tengo que ganar 1. Tengo para superar, echo la más alta
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("6Oros")); alPlayerCards.add(gC("3Oros"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("4Oros")); alTableCards.add(gC("5Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 2/2 triunfos. Tengo que ganar 1. NO tengo para superar, echo el más bajo
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("5Oros"));
                alTableCards.add(gC("3Copas")); alTableCards.add(gC("3Oros")); alTableCards.add(gC("7Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 3/3 triunfos. Tengo que ganar 1. NO tengo para superar, echo algo intermedio
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("5Oros")); alPlayerCards.add(gC("2Oros"));
                alTableCards.add(gC("3Copas")); alTableCards.add(gC("3Oros")); alTableCards.add(gC("7Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 3/3 triunfos. Tengo que ganar 1. Tengo para superar, echo la más alta
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("ASOros")); alPlayerCards.add(gC("KOros"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("7Oros")); alTableCards.add(gC("QOros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 3/3 triunfos. Tengo que ganar 1. Echo la segunda más alta porque tengo demasiados triunfos y no puedo superar
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("6Oros")); alPlayerCards.add(gC("JOros"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("3Oros")); alTableCards.add(gC("QOros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());

                //Tengo 4/4 triunfos. Tengo que ganar 1. Tengo para superar, echo el más alto
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("3Oros"));
                alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("4Oros"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("QOros")); alTableCards.add(gC("7Oros")); alTableCards.add(gC("6Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 4/4 triunfos. Tengo que ganar 1. NO tengo para superar. Como existe demasiada diferencia entre triunfos y por ganar, echo el segundo mejor
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("QOros"));
                alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("4Oros"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("3Oros")); alTableCards.add(gC("7Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());
            }

            //1. Todas son triunfos - 1.2 Tengo que ganar >=2   - 1.2.1 Se empieza por triunfo
            {
                //Tengo 2/2 triunfos. Tengo que ganar >=2. Tengo para superar, echo el más alto
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("3Oros"));
                alTableCards.add(gC("7Oros")); alTableCards.add(gC("6Oros")); alTableCards.add(gC("JOros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 2/2 triunfos. Tengo que ganar >=2. NO tengo para superar, echo el más bajo
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("7Oros"));
                alTableCards.add(gC("3Oros")); alTableCards.add(gC("KOros")); alTableCards.add(gC("QOros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 3/3 triunfos. Tengo que ganar >=2. Tengo para superar, echo el más alto
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("ASOros")); alPlayerCards.add(gC("6Oros"));
                alTableCards.add(gC("7Oros")); alTableCards.add(gC("JOros")); alTableCards.add(gC("5Oros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 3/3 triunfos. Tengo que ganar >=2. NO tengo para superar, echo el más bajo
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("6Oros")); alPlayerCards.add(gC("4Oros"));
                alTableCards.add(gC("7Oros")); alTableCards.add(gC("3Oros")); alTableCards.add(gC("JOros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());

                //Tengo 4/4 triunfos. Tengo que ganar >=2. Tengo para superar, echo el más bajo posible
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("4Oros")); alPlayerCards.add(gC("3Oros"));
                alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("QOros"));
                alTableCards.add(gC("7Oros")); alTableCards.add(gC("5Oros")); alTableCards.add(gC("JOros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(3, AI_CardAnswer.doTEST());

                //Tengo 4/4 triunfos. Tengo que ganar >=2. NO tengo para superar, echo el más bajo
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("JOros"));
                alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("4Oros"));
                alTableCards.add(gC("7Oros")); alTableCards.add(gC("3Oros")); alTableCards.add(gC("5Oros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(3 , AI_CardAnswer.doTEST());
            }
            //1. Todas son triunfos - 1.2 Tengo que ganar >=2   - 1.2.2 NO se empieza por triunfo, ni hay en mesa
            {
                //2/2 triunfos. Echo la más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("7Oros"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("QCopas")); alTableCards.add(gC("JCopas"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //2/2 triunfos. Echo la más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("7Oros")); alPlayerCards.add(gC("KOros"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("QCopas")); alTableCards.add(gC("JCopas"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(0, AI_CardAnswer.doTEST());

                //Tengo 3/3 triunfos. Echo la más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("6Oros")); alPlayerCards.add(gC("ASOros"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("JCopas")); alTableCards.add(gC("ASCopas"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 4/4 triunfos. Echo la más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("3Oros"));
                alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("4Oros"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("JCopas")); alTableCards.add(gC("ASCopas"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(3, AI_CardAnswer.doTEST());
            }
            //1. Todas son triunfos - 1.2 Tengo que ganar >=2   - 1.2.3 NO se empieza por triunfo, pero hay en mesa
            {
                //Tengo 2/2 triunfos. Tengo que ganar >=2. Tengo para superar, echo la más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("3Oros"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("4Oros")); alTableCards.add(gC("QOros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(0, AI_CardAnswer.doTEST());

                //Tengo 2/2 triunfos. Tengo que ganar >=2. NO tengo para superar, echo la más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("7Oros"));
                alTableCards.add(gC("3Copas")); alTableCards.add(gC("3Oros")); alTableCards.add(gC("6Oros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 3/3 triunfos. Tengo que ganar >=2. Tengo para superar, echo la más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("6Oros")); alPlayerCards.add(gC("3Oros")); alPlayerCards.add(gC("KOros"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("4Oros")); alTableCards.add(gC("QOros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());

                //Tengo 3/3 triunfos. Tengo que ganar >=2. NO tengo para superar, echo la más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("6Oros")); alPlayerCards.add(gC("JOros"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("3Oros")); alTableCards.add(gC("KOros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 4/4 triunfos. Tengo que ganar >=2. Tengo para superar, echo la más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("3Oros"));
                alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("4Oros"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("JOros")); alTableCards.add(gC("7Oros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(0, AI_CardAnswer.doTEST());

                //Tengo 4/4 triunfos. Tengo que ganar >=2. NO tengo para superar, echo la más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("QOros"));
                alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("4Oros"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("3Oros")); alTableCards.add(gC("7Oros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(3, AI_CardAnswer.doTEST());
            }

            //2. No todas son triunfos - 2.1 Tengo que ganar 1  - 2.1.1 Se empieza por triunfo
            {
                //Tengo 1/X triunfos. Echo el único triunfo que tengo
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("3Espadas")); alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("7Espadas"));
                alTableCards.add(gC("6Oros")); alTableCards.add(gC("7Copas")); alTableCards.add(gC("7Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 2/X triunfos. Tengo para superar, al tener que ganar que ganar solo 1, echo la más baja que supere por si me la ganan
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("7Oros")); alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("3Copas"));
                alTableCards.add(gC("5Oros")); alTableCards.add(gC("7Copas")); alTableCards.add(gC("6Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(0, AI_CardAnswer.doTEST());

                //Tengo 2/X triunfos. NO tengo para superar, al tener que ganar que ganar solo 1, echo la segunda más alta
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("4Oros")); alPlayerCards.add(gC("JCopas"));
                alTableCards.add(gC("3Oros")); alTableCards.add(gC("7Copas")); alTableCards.add(gC("KOros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 3/X triunfos. Tengo para superar, al tener que ganar que ganar solo 1, echo la más alta
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("7Oros")); alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("3Copas"));
                alTableCards.add(gC("JOros")); alTableCards.add(gC("7Copas")); alTableCards.add(gC("5Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());

                //Tengo 3/X triunfos. NO tengo para superar, al tener que ganar que ganar solo 1, echo la segunda más alta
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("7Oros")); alPlayerCards.add(gC("4Oros")); alPlayerCards.add(gC("JCopas"));
                alTableCards.add(gC("3Oros")); alTableCards.add(gC("7Copas")); alTableCards.add(gC("KOros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 4/X triunfos. Tengo que ganar 1. Tengo para superar, echo el más alto
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("3Oros")); alPlayerCards.add(gC("ASCopas"));
                alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("4Oros"));
                alTableCards.add(gC("7Oros")); alTableCards.add(gC("JOros")); alTableCards.add(gC("5Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 4/X triunfos. Tengo que ganar 1. NO tengo para superar. Como existe demasiada diferencia entre triunfos y por ganar, echo el segundo mejor
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("ASCopas"));
                alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("4Oros"));
                alTableCards.add(gC("7Oros")); alTableCards.add(gC("3Oros")); alTableCards.add(gC("5Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(0 , AI_CardAnswer.doTEST());
            }
            //2. No todas son triunfos - 2.1 Tengo que ganar 1  - 2.1.2 NO se empieza por triunfo, ni hay en mesa
            {
                //Tengo para superar. Como tengo triunfos, echo la más baja de la pinte
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("7Oros")); alPlayerCards.add(gC("QCopas")); alPlayerCards.add(gC("3Copas"));
                alTableCards.add(gC("6Copas")); alTableCards.add(gC("JCopas")); alTableCards.add(gC("7Copas"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //NO tengo para superar. Como tengo triunfos, echo la más alta
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("7Oros")); alPlayerCards.add(gC("KCopas")); alPlayerCards.add(gC("QCopas"));
                alTableCards.add(gC("3Copas")); alTableCards.add(gC("JCopas")); alTableCards.add(gC("7Copas"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());
            }
            //2. No todas son triunfos - 2.1 Tengo que ganar 1  - 2.1.3 NO se empieza por triunfo, pero hay en mesa
            {
                //Tengo que ganar 1. Tengo para superar, pero tengo triunfos. Intento ganar con triunfos, echo la más alta para quitarmela
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("6Oros")); alPlayerCards.add(gC("JCopas")); alPlayerCards.add(gC("KCopas"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("4Oros")); alTableCards.add(gC("7Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());

                //Tengo que ganar 1. No tengo para superar, pero tengo triunfos. Echo la más alta para ganar con triunfos
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("3Oros")); alPlayerCards.add(gC("JCopas")); alPlayerCards.add(gC("QCopas"));
                alTableCards.add(gC("KCopas")); alTableCards.add(gC("6Oros")); alTableCards.add(gC("7Copas"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());

                //Tengo que ganar 1. NO tengo de la pinte. Tengo triunfos para superar el de mesa. Echo la más alta
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("6Copas")); alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("6Oros"));
                alTableCards.add(gC("7Espadas")); alTableCards.add(gC("4Oros")); alTableCards.add(gC("7Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());

                //Tengo que ganar 1. NO tengo de la pinte. Tengo triunfos pero NO para superar el de mesa. Echo el segundo más alto
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("3Oros")); alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("6Oros"));
                alTableCards.add(gC("7Espadas")); alTableCards.add(gC("ASOros")); alTableCards.add(gC("7Oros")); alTableCards.add(gC("QOros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());
            }

            //2. No todas son triunfos - 2.2 Tengo que ganar >=2  - 2.2.1 Se empieza por triunfo
            {
                //Tengo 1/X triunfos. Echo el único triunfo que tengo
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("3Espadas")); alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("7Espadas"));
                alTableCards.add(gC("6Oros")); alTableCards.add(gC("7Copas")); alTableCards.add(gC("QOros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo 2/X triunfos. Tengo para superar, tengo que ganar >=2. NO tengo demasiados triunfos. Soy el último, echo la más baja ganadora
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("7Oros")); alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("3Copas"));
                alTableCards.add(gC("5Oros")); alTableCards.add(gC("7Copas")); alTableCards.add(gC("6Copas"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST()); // TODO : 0

                //Tengo 2/X triunfos. NO tengo para superar, tengo que ganar >=2. Echo la más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("4Oros")); alPlayerCards.add(gC("JCopas")); alPlayerCards.add(gC("2Oros"));
                alTableCards.add(gC("3Oros")); alTableCards.add(gC("7Copas")); alTableCards.add(gC("7Oros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(3, AI_CardAnswer.doTEST());

                //Tengo 3/X triunfos. Tengo para superar, tengo que ganar >=2. Soy el último, echo la más baja ganadora
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("7Oros")); alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("3Copas"));
                alTableCards.add(gC("7Oros")); alTableCards.add(gC("7Copas")); alTableCards.add(gC("QOros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());

                //Tengo 3/X triunfos. NO tengo para superar, tengo que ganar >=2. Echo la más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("7Oros")); alPlayerCards.add(gC("4Oros")); alPlayerCards.add(gC("2Copas"));
                alTableCards.add(gC("3Oros")); alTableCards.add(gC("7Copas")); alTableCards.add(gC("JOros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());

                //Tengo 4/X triunfos. Tengp para superar. Tengo que ganar >=2. Soy el último, echo la más baja ganadora
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("3Oros")); alPlayerCards.add(gC("ASCopas"));
                alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("4Oros"));
                alTableCards.add(gC("7Oros")); alTableCards.add(gC("7Oros")); alTableCards.add(gC("QOros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(3, AI_CardAnswer.doTEST());

                //Tengo 4/X triunfos. NO tengo para superar, tengo que ganar >=2. Echo la más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("QOros")); alPlayerCards.add(gC("ASCopas"));
                alPlayerCards.add(gC("KOros")); alPlayerCards.add(gC("4Oros"));
                alTableCards.add(gC("7Oros")); alTableCards.add(gC("3Oros")); alTableCards.add(gC("JOros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(3 , AI_CardAnswer.doTEST());
            }
            //2. No todas son triunfos - 2.2 Tengo que ganar >=2  - 2.2.2 NO se empieza por triunfo, ni hay en mesa
            {
                //Tengo para superar. Como tengo triunfos y soy el último, echo la más baja ganadora
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("7Oros")); alPlayerCards.add(gC("KCopas")); alPlayerCards.add(gC("3Copas"));
                alTableCards.add(gC("6Copas")); alTableCards.add(gC("ASEspadas")); alTableCards.add(gC("QCopas"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //NO tengo para superar. Tengo triunfos pero no suficietnes. Echo la más baja para ganar el resto
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("7Oros")); alPlayerCards.add(gC("KCopas")); alPlayerCards.add(gC("2Copas"));
                alTableCards.add(gC("3Copas")); alTableCards.add(gC("QCopas"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST()); // TODO : 2
            }
            // TODO ↓
            //2. No todas son triunfos - 2.2 Tengo que ganar >=2  - 2.2.3 NO se empieza por triunfo, pero hay en mesa
            {
                //Tengo que ganar >=2. NO puedo ganar (Tengo de inicio para superar pero hay triunfos en mesa)
                // Como tengo más pedidas que triunfos, echo la segunda segunda más alta
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("6Oros")); alPlayerCards.add(gC("JCopas")); alPlayerCards.add(gC("KCopas"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("4Oros")); alTableCards.add(gC("7Oros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo que ganar >=2. NO puedo ganar (NO tengo de inicio para superar y hay triunfos en mesa)
                // Como tengo más pedidas que triunfos, echo la segunda segunda más alta de la pinte
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("3Oros")); alPlayerCards.add(gC("QCopas")); alPlayerCards.add(gC("JCopas"));
                alTableCards.add(gC("KCopas")); alTableCards.add(gC("6Oros")); alTableCards.add(gC("7Oros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());

                //Tengo que ganar >=2. NO puedo ganar (Tengo de inicio para superar pero hay triunfos en mesa)
                // Tengo las mismas pedidas que triunfos, y tengo triunfos altos(winning), echo la más alta de la pinte
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("JCopas")); alPlayerCards.add(gC("KCopas"));
                alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("KOros"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("4Oros")); alTableCards.add(gC("7Oros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //Tengo que ganar >=2. NO puedo ganar (Tengo de inicio para superar y hay triunfos en mesa)
                // Tengo las mismas pedidas que triunfos, pero NO tengo triunfos altos(winning), echo la segunda más alta de la pinte
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("JCopas")); alPlayerCards.add(gC("KCopas"));
                alPlayerCards.add(gC("6Oros")); alPlayerCards.add(gC("7Oros"));
                alTableCards.add(gC("7Copas")); alTableCards.add(gC("5Oros")); alTableCards.add(gC("5Copas"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST()); // TODO : 0
            }

            //3. NO tengo triunfos - 3.1 Tengo que ganar 1  - 3.1.1 Se empieza por triunfo
            {
                //Como no tengo triunfos y tengo que ganar 1, echo la carta más baja que tenga
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("3Espadas")); alPlayerCards.add(gC("JBastos")); alPlayerCards.add(gC("7Espadas"));
                alTableCards.add(gC("6Oros")); alTableCards.add(gC("7Copas"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());
            }
            //3. NO tengo triunfos - 3.1 Tengo que ganar 1  - 3.1.2 NO se empieza por triunfo, ni hay en mesa
            {
                //Tengo cartas de inicio y sin triunfos. Intento ganarla, echo la más alta
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("3Espadas")); alPlayerCards.add(gC("JBastos")); alPlayerCards.add(gC("7Espadas"));
                alTableCards.add(gC("6Espadas")); alTableCards.add(gC("7Copas"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(0, AI_CardAnswer.doTEST());

                //NO tengo cartas de inicio y sin triunfos. Echo la carta más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("3Bastos")); alPlayerCards.add(gC("JBastos")); alPlayerCards.add(gC("7Bastos"));
                alTableCards.add(gC("6Espadas")); alTableCards.add(gC("7Copas"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());
            }
            //3. NO tengo triunfos - 3.1 Tengo que ganar 1  - 3.1.3 NO se empieza por triunfo, pero hay en mesa
            {
                //Tengo cartas de inicio, hay triunfos en mesa y tengo que ganar 1. Echo la carta más baja que tenga
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("3Copas")); alPlayerCards.add(gC("5Copas"));
                alPlayerCards.add(gC("KCopas")); alPlayerCards.add(gC("4Bastos"));
                alTableCards.add(gC("6Copas")); alTableCards.add(gC("7Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //NO tengo cartas de inicio, hay triunfos en mesa y tengo que ganar 1. Echo la carta más baja que tenga
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("3Copas")); alPlayerCards.add(gC("5Copas"));
                alPlayerCards.add(gC("KCopas")); alPlayerCards.add(gC("4Bastos"));
                alTableCards.add(gC("6Bastos")); alTableCards.add(gC("7Oros"));
                AI_CardAnswer.initData(alPlayerCards, 1, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(3, AI_CardAnswer.doTEST());
            }

            //3. NO tengo triunfos - 3.2 Tengo que ganar >=2  - 3.2.1 Se empieza por triunfo
            {
                //Como no tengo triunfos y tengo que ganar 1, echo la carta más baja que tenga
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("3Espadas")); alPlayerCards.add(gC("JBastos")); alPlayerCards.add(gC("7Espadas"));
                alTableCards.add(gC("6Oros")); alTableCards.add(gC("7Copas"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());
            }
            //3. NO tengo triunfos - 3.2 Tengo que ganar >=2  - 3.2.2 NO se empieza por triunfo, ni hay en mesa
            {
                //Tengo cartas de inicio y sin triunfos. Intento ganarla, echo la más alta
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("3Espadas")); alPlayerCards.add(gC("JBastos")); alPlayerCards.add(gC("7Espadas"));
                alTableCards.add(gC("6Espadas")); alTableCards.add(gC("7Copas"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(0, AI_CardAnswer.doTEST());

                //NO tengo cartas de inicio y sin triunfos. Echo la carta más baja
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("3Bastos")); alPlayerCards.add(gC("JBastos")); alPlayerCards.add(gC("7Bastos"));
                alTableCards.add(gC("6Espadas")); alTableCards.add(gC("7Copas"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(2, AI_CardAnswer.doTEST());
            }
            //3. NO tengo triunfos - 3.2 Tengo que ganar >=2  - 3.2.3 NO se empieza por triunfo, pero hay en mesa
            {
                //Tengo cartas de inicio, hay triunfos en mesa y tengo que ganar 1. Echo la carta más baja que tenga
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("3Copas")); alPlayerCards.add(gC("5Copas"));
                alPlayerCards.add(gC("KCopas")); alPlayerCards.add(gC("4Bastos"));
                alTableCards.add(gC("6Copas")); alTableCards.add(gC("7Oros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(1, AI_CardAnswer.doTEST());

                //NO tengo cartas de inicio, hay triunfos en mesa y tengo que ganar 1. Echo la carta más baja que tenga
                alPlayerCards.clear(); alTableCards.clear();
                alPlayerCards.add(gC("3Copas")); alPlayerCards.add(gC("5Copas"));
                alPlayerCards.add(gC("KCopas")); alPlayerCards.add(gC("4Bastos"));
                alTableCards.add(gC("6Bastos")); alTableCards.add(gC("7Oros"));
                AI_CardAnswer.initData(alPlayerCards, 2, 0, lastPlayer, TRUMP_SUIT, alTableCards);
                assertEquals(3, AI_CardAnswer.doTEST());
            }
        }
    }

    @Test
    public void otherCases(){ // Otros casos
        Gdx.app.log(TAG, "Other cases");

        /** Obligado a una carta **/
        {
            //No empieza por la pinte. Estoy obligado a echar inicio
            alPlayerCards.clear(); alTableCards.clear();
            alPlayerCards.add(gC("ASOros")); alPlayerCards.add(gC("7Bastos"));
            alTableCards.add(gC("4Bastos"));
            AI_CardAnswer.initData(alPlayerCards, 0, 0, false, TRUMP_SUIT, alTableCards);
            assertEquals(1, AI_CardAnswer.doTEST());

            //No empieza por la pinte. Estoy obligado a echar inicio y superar
            alPlayerCards.clear(); alTableCards.clear();
            alPlayerCards.add(gC("ASOros")); alPlayerCards.add(gC("7Bastos")); alPlayerCards.add(gC("4Bastos"));
            alTableCards.add(gC("5Bastos"));
            AI_CardAnswer.initData(alPlayerCards, 0, 0, false, TRUMP_SUIT, alTableCards);
            assertEquals(1, AI_CardAnswer.doTEST());

            //No empieza por la pinte. No tengo inicio. Tengo que echar la pinte.
            alPlayerCards.clear(); alTableCards.clear();
            alPlayerCards.add(gC("6Espadas")); alPlayerCards.add(gC("3Espadas")); alPlayerCards.add(gC("JOros"));
            alTableCards.add(gC("2Copas")); alTableCards.add(gC("KEspadas"));
            AI_CardAnswer.initData(alPlayerCards, 0, 0, false, TRUMP_SUIT, alTableCards);
            assertEquals(2, AI_CardAnswer.doTEST());
        }

        /** Soy el primer jugador **/
        {
            //Si tengo que ganar dos y solo tengo un triunfo, empezar por el triunfo.
            alPlayerCards.clear(); alTableCards.clear();
            alPlayerCards.add(gC("7Oros")); alPlayerCards.add(gC("3Espadas"));
            AI_CardAnswer.initData(alPlayerCards, 2, 0, false, TRUMP_SUIT, alTableCards);
            assertEquals(0, AI_CardAnswer.doTEST());

            //Si tengo que ganar una y tengo triunfo y carta alta, empezar por la alta (si gano el triunfo, ganaré las dos)
            alPlayerCards.clear(); alTableCards.clear();
            alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("ASEspadas"));
            AI_CardAnswer.initData(alPlayerCards, 1, 0, false, TRUMP_SUIT, alTableCards);
            assertEquals(1, AI_CardAnswer.doTEST());

            //Si tengo dos triunfos y dos pedidas, empezar por el triunfo alto
            alPlayerCards.clear(); alTableCards.clear();
            alPlayerCards.add(gC("JOros")); alPlayerCards.add(gC("KOros"));
            AI_CardAnswer.initData(alPlayerCards, 2, 0, false, TRUMP_SUIT, alTableCards);
            assertEquals(1, AI_CardAnswer.doTEST());
        }

        /** NO soy el último jugador **/
        {
            //NO soy el último. Se empieza por triunfo y tengo varias que superan. No tengo que ganar, echo la más baja
            alPlayerCards.clear(); alTableCards.clear();
            alPlayerCards.add(gC("3Oros")); alPlayerCards.add(gC("KOros"));
            alPlayerCards.add(gC("5Oros")); alPlayerCards.add(gC("JOros"));
            alTableCards.add(gC("7Oros")); alTableCards.add(gC("6Oros"));
            AI_CardAnswer.initData(alPlayerCards, 0, 0, false, TRUMP_SUIT, alTableCards);
            assertEquals(3, AI_CardAnswer.doTEST());
        }
    }
}
