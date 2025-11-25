package com.tfdevelopment.pochaonlinetfd.utils.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Disposable;

public class AssetLoader implements Disposable, AssetErrorListener {
    private static final String TAG = AssetLoader.class.getName();

    private static final String TA_DECK_OF_CARDS_CLASSIC = "images/deckOfCards/classic/deckOfCards.atlas";
    private static final String TA_DECK_OF_CARDS_LINUX = "images/deckOfCards/linux/deckOfCards.atlas";
    private static final String TA_REST_OF_IMAGES = "images/restOfImages/restOfImages.atlas";

    private static final String MUSIC_GAME = "sounds/musicGame.mp3";
    private static final String CLICK_BUTTON_SFX = "sounds/sfx/clickButton.ogg";
    private static final String SELECT_CARD_SFX = "sounds/sfx/selectCard.ogg";
    private static final String TURN_SFX = "sounds/sfx/turnToPlay.ogg";
    private static final String APPLAUSE_WIN_GAME = "sounds/sfx/applauseWinGame.mp3";
    private static final String APPLAUSE_WIN_SET = "sounds/sfx/applauseWinSet.mp3";
    private static final String BOO_LOSE_GAME = "sounds/sfx/booLoseGame.mp3";
    private static final String BOO_LOSE_SET = "sounds/sfx/booLoseGame.mp3";

    public static final AssetLoader aLoader = new AssetLoader();

    private AssetManager assetManager;
    public AssetSound aSound;
    public AssetFont aFont;
    public AssetCard aCClassic, aCLinux;
    public AssetImage aImage;

    public AssetLoader() {}

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;

        assetManager.setErrorListener(this);
        assetManager.load(TA_REST_OF_IMAGES, TextureAtlas.class); // Load before the cars
        assetManager.load(TA_DECK_OF_CARDS_CLASSIC, TextureAtlas.class);
        assetManager.load(TA_DECK_OF_CARDS_LINUX, TextureAtlas.class);

        assetManager.load(MUSIC_GAME, Music.class);
        assetManager.load(CLICK_BUTTON_SFX, Sound.class);
        assetManager.load(SELECT_CARD_SFX, Sound.class);
        assetManager.load(TURN_SFX, Sound.class);
        assetManager.load(APPLAUSE_WIN_GAME, Sound.class);
        assetManager.load(APPLAUSE_WIN_SET, Sound.class);
        assetManager.load(BOO_LOSE_GAME, Sound.class);
        assetManager.load(BOO_LOSE_SET, Sound.class);
        assetManager.finishLoading();

        TextureAtlas taCards_Classic = assetManager.get(TA_DECK_OF_CARDS_CLASSIC);
        TextureAtlas taCards_Linux = assetManager.get(TA_DECK_OF_CARDS_LINUX);
        for (Texture texture : taCards_Classic.getTextures()) {
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
        for (Texture texture : taCards_Linux.getTextures()) {
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
        TextureAtlas taImages = assetManager.get(TA_REST_OF_IMAGES);
        for (Texture texture : taImages.getTextures()) {
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        aFont = new AssetFont();
        aImage = new AssetImage(taImages); // Load before the cars
        aCClassic = new AssetCard(taCards_Classic);
        aCLinux = new AssetCard(taCards_Linux);
        aSound = new AssetSound();
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Error to load asset: '" + asset.fileName + "'", (Exception)throwable);
    }

    @Override
    public void dispose() {
        aSound.dispose();
        aFont.dispose();
        assetManager.dispose();

        Gdx.app.log(TAG, "**DISPOSE**");
    }

    public class AssetSound implements Disposable {
        private final Music musicGame;
        private final Sound clickButton, selectCard, turnToPlay, applauseWinGame, applauseWinChance, booLoseGame, booLoseChance;

        private AssetSound() {
            musicGame = assetManager.get(MUSIC_GAME, Music.class);
            clickButton = assetManager.get(CLICK_BUTTON_SFX, Sound.class);
            selectCard = assetManager.get(SELECT_CARD_SFX, Sound.class);
            turnToPlay = assetManager.get(TURN_SFX, Sound.class);
            applauseWinGame = assetManager.get(APPLAUSE_WIN_GAME, Sound.class);
            applauseWinChance = assetManager.get(APPLAUSE_WIN_SET, Sound.class);
            booLoseGame = assetManager.get(BOO_LOSE_GAME, Sound.class);
            booLoseChance = assetManager.get(BOO_LOSE_SET, Sound.class);
        }

        @Override
        public void dispose() {
            musicGame.dispose();
            clickButton.dispose();
            selectCard.dispose();
            turnToPlay.dispose();
            applauseWinGame.dispose();
            applauseWinChance.dispose();
            booLoseGame.dispose();
            booLoseChance.dispose();
        }

        public Music getMusicGame(){ return musicGame; }
        public Sound getClickButton(){ return clickButton; }
        public Sound getSelectCard(){ return selectCard; }
        public Sound getTurnToPlay(){ return turnToPlay; }
        public Sound getApplauseWinGame(){ return applauseWinGame; }
        public Sound getApplauseWinChance(){ return applauseWinChance; }
        public Sound getBooLoseGame(){ return booLoseGame; }
        public Sound getBooLoseChance(){ return booLoseChance; }
    }

    public class AssetCard {
        private static final int NUM_CARDS = 40;
        private final TextureAtlas.AtlasRegion[] arCards = new TextureAtlas.AtlasRegion[NUM_CARDS];
        private AtlasRegion auxNullCard = null;

        private AssetCard(TextureAtlas textureAtlas) {
            auxNullCard = aImage.smallBlackSquare;

            for (int x = 1; x <= NUM_CARDS; x++) {
                if (x <= 10) this.arCards[x-1] = textureAtlas.findRegion("oros"+((x%10==0) ? 10 : x%10));
                else if (x <= 20) this.arCards[x-1] = textureAtlas.findRegion("copas"+((x%10==0) ? 10 : x%10));
                else if (x <= 30) this.arCards[x-1] = textureAtlas.findRegion("espadas"+((x%10==0) ? 10 : x%10));
                else this.arCards[x-1] = textureAtlas.findRegion("bastos"+((x%10==0) ? 10 : x%10));
            }
        }

        public TextureAtlas.AtlasRegion getAtlasRegionCard(int index) {
            if (index == -1) return auxNullCard;
            return this.arCards[index];
        }
    }

    public static class AssetImage {
        //Menu
        private final AtlasRegion backgroundMenu, bInfo, bTwitter, bGmail, bPlayStore;

        //Buttons
        private final AtlasRegion greyButtonDown, greyButtonUp;

        //Hall
        private final AtlasRegion nameplate;
        //Menu Hall
        private final AtlasRegion backgroundMenuHall;
        private final AtlasRegion iExit, iPlayersUp, iPlayersDown, iChatUp, iChatDown,
            iRankingUp, iRankingDown, iNewsDown, iNewsUp, iUserUp, iUserDown, iInfoMenu;
        private final AtlasRegion leftArrow, rightArrow;
        //Players Hall
        private final AtlasRegion backgroundPlayer, iSendInvitation, iLoad;
        //User Hall
        private final AtlasRegion topBorderUser, iEdit;

        //Iconos jugador
        private final AtlasRegion backgroundPlayerName;
        private final AtlasRegion arPlayerHall, arPlayerHall2; //Icono Azul-Gris -- Negro-Blanco
        private final AtlasRegion iUser6, iUser1, iUser2, iUser3, iUser4, iUser5;
        private final AtlasRegion[] iUsers;

        //Game
        private final AtlasRegion backgroundGameButtons, blackboard, tablecloth;
        private final AtlasRegion bConfDown, bConfUp, bExitDown, bExitUp, bChatDown, bChatUp;
        private final AtlasRegion deck, speechBubbleLeft, speechBubbleRight;
        private final AtlasRegion player, timer;

        //Otros
        private final AtlasRegion roundGraySquare, roundLightGrey;
        private final AtlasRegion smallBlackSquare;

        private AssetImage(TextureAtlas atlas){
            //Menu
            backgroundMenu = atlas.findRegion("backgroundMenu");
            bInfo = atlas.findRegion("iconoInfo");
            bTwitter = atlas.findRegion("iconoTwitter");
            bGmail = atlas.findRegion("iconoGmail");
            bPlayStore = atlas.findRegion("googlePlay");

            //Buttons
            greyButtonDown = atlas.findRegion("buttonDown");
            greyButtonUp = atlas.findRegion("buttonUp");

            //Hall
            nameplate = atlas.findRegion("nameplate");

            //User Hall
            topBorderUser = atlas.findRegion("topBorderUser");
            iEdit = atlas.findRegion("iEdit");
            //Players Hall
            backgroundPlayer = atlas.findRegion("backgroundPlayer");
            iSendInvitation = atlas.findRegion("iSendInvitation");
            iLoad = atlas.findRegion("iLoad");
            //Menu Hall
            backgroundMenuHall = atlas.findRegion("backgroundMenuHall");
            iExit = atlas.findRegion("iExit");
            iUserUp = atlas.findRegion("iUserUp");
            iUserDown = atlas.findRegion("iUserDown");
            iPlayersUp = atlas.findRegion("iPlayersUp");
            iPlayersDown = atlas.findRegion("iPlayersDown");
            iInfoMenu = atlas.findRegion("iInfoMenu");
            iChatUp = atlas.findRegion("iChatUp");
            iChatDown = atlas.findRegion("iChatDown");
            iNewsUp = atlas.findRegion("iNewsUp");
            iNewsDown = atlas.findRegion("iNewsDown");
            iRankingUp = atlas.findRegion("iRankingUp");
            iRankingDown = atlas.findRegion("iRankingDown");
            leftArrow = atlas.findRegion("leftArrow");
            rightArrow = atlas.findRegion("rightArrow");

            //Iconos Jugador
            backgroundPlayerName = atlas.findRegion("backgroundPlayerName");
            arPlayerHall = atlas.findRegion("playerHall");
            arPlayerHall2 = atlas.findRegion("playerHall2");
            iUser1 = atlas.findRegion("usuario1");
            iUser2 = atlas.findRegion("usuario2");
            iUser3 = atlas.findRegion("usuario3");
            iUser4 = atlas.findRegion("usuario4");
            iUser5 = atlas.findRegion("usuario5");
            iUser6 = atlas.findRegion("usuario6");
            iUsers = new AtlasRegion[]{arPlayerHall, iUser1, iUser2, iUser3, iUser4, iUser5, iUser6};

            //Game
            backgroundGameButtons = atlas.findRegion("backgroundGameButtons");
            blackboard = atlas.findRegion("blackboard");
            tablecloth = atlas.findRegion("tablecloth");
            bConfDown = atlas.findRegion("bConfDown");
            bConfUp = atlas.findRegion("bConfUp");
            bExitDown = atlas.findRegion("bExitDown");
            bExitUp = atlas.findRegion("bExitUp");
            bChatDown = atlas.findRegion("bChatDown");
            bChatUp = atlas.findRegion("bChatUp");
            deck = atlas.findRegion("deck");
            speechBubbleLeft = atlas.findRegion("speechBubbleLeft");
            speechBubbleRight = atlas.findRegion("speechBubbleRight");
            player = atlas.findRegion("gamePlayer");
            timer = atlas.findRegion("timer");

            //Otros
            roundGraySquare = atlas.findRegion("roundGraySquare");
            roundLightGrey = atlas.findRegion("roundLightGrey");
            smallBlackSquare = atlas.findRegion("smallBlackSquare");
        }

        //Menu
        public AtlasRegion getBackgroundMenu(){ return backgroundMenu; }
        public AtlasRegion getbInfo(){ return bInfo; }
        public AtlasRegion getbTwitter(){ return bTwitter; }
        public AtlasRegion getbGmail(){ return bGmail; }
        public AtlasRegion getbPlayStore(){ return bPlayStore; }
        //Buttons
        public AtlasRegion getGreyButtonDown(){ return greyButtonDown; }
        public AtlasRegion getGreyButtonUp(){ return greyButtonUp; }
        //Hall
        public AtlasRegion getNameplate(){ return nameplate; }
        //Users Hall
        public AtlasRegion getTopBorderUser(){ return topBorderUser; }
        public AtlasRegion getiEdit(){ return iEdit; }
        //Players Hall
        public AtlasRegion getBackgroundPlayer(){ return backgroundPlayer; }
        public AtlasRegion getiSendInvitation(){ return iSendInvitation; }
        public AtlasRegion getiLoad(){ return iLoad; }
        //Menu Hall
        public AtlasRegion getBackgroundMenuHall(){ return backgroundMenuHall; }
        public AtlasRegion getiExit(){ return iExit; }
        public AtlasRegion getiPlayersUp() { return iPlayersUp; }
        public AtlasRegion getiPlayersDown() { return iPlayersDown; }
        public AtlasRegion getiChatUp() { return iChatUp; }
        public AtlasRegion getiChatDown() { return iChatDown; }
        public AtlasRegion getiUserUp(){ return iUserUp; }
        public AtlasRegion getiUserDown(){ return iUserDown; }
        public AtlasRegion getiRankingUp() { return iRankingUp; }
        public AtlasRegion getiRankingDown() { return iRankingDown; }
        public AtlasRegion getiNewsDown() { return iNewsDown; }
        public AtlasRegion getiNewsUp() { return iNewsUp; }
        public AtlasRegion getiInfoMenu() { return iInfoMenu; }
        public AtlasRegion getLeftArrow(){ return leftArrow; }
        public AtlasRegion getRightArrow(){ return rightArrow; }

        //Iconos jugador
        public AtlasRegion getBackgroundPlayerName(){ return backgroundPlayerName; }
        public AtlasRegion getArPlayerHall(){ return arPlayerHall; }
        public AtlasRegion getArPlayerHall2(){ return arPlayerHall2; }
        public AtlasRegion getUserIcon(int index, int colorCode){
            if(index==0){
                if(colorCode==0) return getArPlayerHall();
                if(colorCode==1) return getArPlayerHall2();
            }
            return iUsers[index];
        }

        //Game
        public AtlasRegion getBackgroundGameButtons(){ return backgroundGameButtons; }
        public AtlasRegion getBlackboard(){ return blackboard; }
        public AtlasRegion getTablecloth(){ return tablecloth; }
        public AtlasRegion getbExitDown(){ return bExitDown; }
        public AtlasRegion getbExitUp(){ return bExitUp; }
        public AtlasRegion getbConfDown(){ return bConfDown; }
        public AtlasRegion getbConfUp(){ return bConfUp; }
        public AtlasRegion getbChatDown(){ return bChatDown; }
        public AtlasRegion getbChatUp(){ return bChatUp; }
        public AtlasRegion getDeck(){ return deck; }
        public AtlasRegion getSpeechBubbleLeft(){ return speechBubbleLeft; }
        public AtlasRegion getSpeechBubbleRight(){ return speechBubbleRight; }
        public AtlasRegion getPlayer(){ return player; }
        public AtlasRegion getTimer(){ return timer; }

        //Otros
        public AtlasRegion getRoundGraySquare(){ return roundGraySquare; }
        public AtlasRegion getRoundLightGrey(){ return roundLightGrey; }
        public AtlasRegion getSmallBlackSquare() { return smallBlackSquare; }
    }


    public class AssetFont implements Disposable{
        private static final String MAIANDRA_FONT = "fonts/maiandraGD.fnt";
        private static final String VERDANA_FONT = "fonts/verdana.fnt";
        private static final String VERDANA_SHADOW_FONT = "fonts/verdanaShadow.fnt";

        private final BitmapFont bfMaiandra40, bfMaiandra50, bfMaiandra54, bfMaiandra60, bfMaiandra65, bfMaiandra70, bfMaiandra80;
        private final BitmapFont bfVerdana50, bfVerdana55;

        private AssetFont(){
            bfMaiandra40 = createBitmapFont(MAIANDRA_FONT, 0.4f);
            bfMaiandra50 = createBitmapFont(MAIANDRA_FONT, 0.5f);
            bfMaiandra54 = createBitmapFont(MAIANDRA_FONT, 0.54f);
            bfMaiandra60 = createBitmapFont(MAIANDRA_FONT, 0.6f);
            bfMaiandra65 = createBitmapFont(MAIANDRA_FONT, 0.65f);
            bfMaiandra70 = createBitmapFont(MAIANDRA_FONT, 0.7f);
            bfMaiandra80 = createBitmapFont(MAIANDRA_FONT, 0.8f);

            bfVerdana50 = createBitmapFont(VERDANA_FONT, 0.5f);
            bfVerdana55 = createBitmapFont(VERDANA_FONT, 0.55f);
        }

        private BitmapFont createBitmapFont(String file,float scale){
            BitmapFont bitmapFont = new BitmapFont(Gdx.files.internal(file), false);
            bitmapFont.getData().markupEnabled = true;
            bitmapFont.getData().setScale(scale);
            // Enable linear texture filtering for smoother fonts
            bitmapFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            return bitmapFont;
        }

        @Override
        public void dispose() {
            aFont.bfMaiandra40.dispose();
            aFont.bfMaiandra50.dispose();
            aFont.bfMaiandra60.dispose();
            aFont.bfMaiandra65.dispose();
            aFont.bfMaiandra70.dispose();
            aFont.bfMaiandra80.dispose();

            aFont.bfVerdana50.dispose();
            aFont.bfVerdana55.dispose();
        }

        public BitmapFont getBfMaiandra40(){ return bfMaiandra40; }
        public BitmapFont getBfMaiandra50(){ return bfMaiandra50; }
        public BitmapFont getBfMaiandra54(){ return bfMaiandra54; }
        public BitmapFont getBfMaiandra60(){ return bfMaiandra60; }
        public BitmapFont getBfMaiandra65(){ return bfMaiandra65; }
        public BitmapFont getBfMaiandra70(){ return bfMaiandra70; }
        public BitmapFont getBfMaiandra80(){ return bfMaiandra80; }
        public BitmapFont getBfVerdana50(){ return bfVerdana50; }
        public BitmapFont getBfVerdana55(){ return bfVerdana55; }
    }
}
