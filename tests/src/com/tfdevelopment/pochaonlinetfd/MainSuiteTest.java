package com.tfdevelopment.pochaonlinetfd;

import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.tfdevelopment.pochaonlinetfd.model.card.Card;
import com.tfdevelopment.pochaonlinetfd.tests.AI_BetAnswer.AI_Bet;
import com.tfdevelopment.pochaonlinetfd.tests.AI_CardAnswer.AICard;
import com.tfdevelopment.pochaonlinetfd.model.PochaEnum;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AI_Bet.class,
        AICard.class
})
public class MainSuiteTest {
    public static PochaOnlineMain pom;

    public static void initPochaOnline(String[] args) {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        pom = new PochaOnlineMain(null, true);

        new HeadlessApplication(pom, config);
    }

    public static Card gC(String name){
        Card card = null;

        if(name.equals("ASOros")) card = new Card(0, PochaEnum.CardType.AS, PochaEnum.Suit.OROS);
        if(name.equals("2Oros")) card = new Card(1, PochaEnum.CardType.DOS, PochaEnum.Suit.OROS);
        if(name.equals("3Oros")) card = new Card(2, PochaEnum.CardType.TRES, PochaEnum.Suit.OROS);
        if(name.equals("4Oros")) card = new Card(3, PochaEnum.CardType.CUATRO, PochaEnum.Suit.OROS);
        if(name.equals("5Oros")) card = new Card(4, PochaEnum.CardType.CINCO, PochaEnum.Suit.OROS);
        if(name.equals("6Oros")) card = new Card(5, PochaEnum.CardType.SEIS, PochaEnum.Suit.OROS);
        if(name.equals("7Oros")) card = new Card(6, PochaEnum.CardType.SIETE, PochaEnum.Suit.OROS);
        if(name.equals("JOros")) card = new Card(7, PochaEnum.CardType.J, PochaEnum.Suit.OROS);
        if(name.equals("QOros")) card = new Card(8, PochaEnum.CardType.Q, PochaEnum.Suit.OROS);
        if(name.equals("KOros")) card = new Card(9, PochaEnum.CardType.K, PochaEnum.Suit.OROS);

        if(name.equals("ASCopas")) card = new Card(10, PochaEnum.CardType.AS, PochaEnum.Suit.COPAS);
        if(name.equals("2Copas")) card = new Card(11, PochaEnum.CardType.DOS, PochaEnum.Suit.COPAS);
        if(name.equals("3Copas")) card = new Card(12, PochaEnum.CardType.TRES, PochaEnum.Suit.COPAS);
        if(name.equals("4Copas")) card = new Card(13, PochaEnum.CardType.CUATRO, PochaEnum.Suit.COPAS);
        if(name.equals("5Copas")) card = new Card(14, PochaEnum.CardType.CINCO, PochaEnum.Suit.COPAS);
        if(name.equals("6Copas")) card = new Card(15, PochaEnum.CardType.SEIS, PochaEnum.Suit.COPAS);
        if(name.equals("7Copas")) card = new Card(16, PochaEnum.CardType.SIETE, PochaEnum.Suit.COPAS);
        if(name.equals("JCopas")) card = new Card(17, PochaEnum.CardType.J, PochaEnum.Suit.COPAS);
        if(name.equals("QCopas")) card = new Card(18, PochaEnum.CardType.Q, PochaEnum.Suit.COPAS);
        if(name.equals("KCopas")) card = new Card(19, PochaEnum.CardType.K, PochaEnum.Suit.COPAS);

        if(name.equals("ASEspadas")) card = new Card(20, PochaEnum.CardType.AS, PochaEnum.Suit.ESPADAS);
        if(name.equals("2Espadas")) card = new Card(21, PochaEnum.CardType.DOS, PochaEnum.Suit.ESPADAS);
        if(name.equals("3Espadas")) card = new Card(22, PochaEnum.CardType.TRES, PochaEnum.Suit.ESPADAS);
        if(name.equals("4Espadas")) card = new Card(23, PochaEnum.CardType.CUATRO, PochaEnum.Suit.ESPADAS);
        if(name.equals("5Espadas")) card = new Card(24, PochaEnum.CardType.CINCO, PochaEnum.Suit.ESPADAS);
        if(name.equals("6Espadas")) card = new Card(25, PochaEnum.CardType.SEIS, PochaEnum.Suit.ESPADAS);
        if(name.equals("7Espadas")) card = new Card(26, PochaEnum.CardType.SIETE, PochaEnum.Suit.ESPADAS);
        if(name.equals("JEspadas")) card = new Card(27, PochaEnum.CardType.J, PochaEnum.Suit.ESPADAS);
        if(name.equals("QEspadas")) card = new Card(28, PochaEnum.CardType.Q, PochaEnum.Suit.ESPADAS);
        if(name.equals("KEspadas")) card = new Card(29, PochaEnum.CardType.K, PochaEnum.Suit.ESPADAS);

        if(name.equals("ASBastos")) card = new Card(30, PochaEnum.CardType.AS, PochaEnum.Suit.BASTOS);
        if(name.equals("2Bastos")) card = new Card(31, PochaEnum.CardType.DOS, PochaEnum.Suit.BASTOS);
        if(name.equals("3Bastos")) card = new Card(32, PochaEnum.CardType.TRES, PochaEnum.Suit.BASTOS);
        if(name.equals("4Bastos")) card = new Card(33, PochaEnum.CardType.CUATRO, PochaEnum.Suit.BASTOS);
        if(name.equals("5Bastos")) card = new Card(34, PochaEnum.CardType.CINCO, PochaEnum.Suit.BASTOS);
        if(name.equals("6Bastos")) card = new Card(35, PochaEnum.CardType.SEIS, PochaEnum.Suit.BASTOS);
        if(name.equals("7Bastos")) card = new Card(36, PochaEnum.CardType.SIETE, PochaEnum.Suit.BASTOS);
        if(name.equals("JBastos")) card = new Card(37, PochaEnum.CardType.J, PochaEnum.Suit.BASTOS);
        if(name.equals("QBastos")) card = new Card(38, PochaEnum.CardType.Q, PochaEnum.Suit.BASTOS);
        if(name.equals("KBastos")) card = new Card(39, PochaEnum.CardType.K, PochaEnum.Suit.BASTOS);

        return card;
    }
}
