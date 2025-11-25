package com.tfdevelopment.pochaonlinetfd.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tfdevelopment.pochaonlinetfd.utils.assets.AssetLoader;

public class StyleConfigurator {
    public static Label.LabelStyle getLS_Maiandra50(){
        Label.LabelStyle lsDSmall = new Label.LabelStyle(AssetLoader.aLoader.aFont.getBfMaiandra50(), Color.WHITE);
        return lsDSmall;
    }

    public static Label.LabelStyle getLS_Maiandra54(){
        Label.LabelStyle lsDSmall = new Label.LabelStyle(AssetLoader.aLoader.aFont.getBfMaiandra54(), Color.WHITE);
        return lsDSmall;
    }

    public static Label.LabelStyle getLS_Maiandra60(){
        Label.LabelStyle labelStyle = new Label.LabelStyle(AssetLoader.aLoader.aFont.getBfMaiandra60(), Color.WHITE);
        return labelStyle;
    }

    public static Label.LabelStyle getLS_Maiandra65(){
        Label.LabelStyle labelStyle = new Label.LabelStyle(AssetLoader.aLoader.aFont.getBfMaiandra65(), Color.WHITE);
        return labelStyle;
    }

    public static Label.LabelStyle getLS_Maiandra70(){
        Label.LabelStyle lsTitleNews = new Label.LabelStyle(AssetLoader.aLoader.aFont.getBfMaiandra70(), Color.WHITE);
        return lsTitleNews;
    }

    public static Label.LabelStyle getLS_Verdana55(){
        Label.LabelStyle labelStyle = new Label.LabelStyle(AssetLoader.aLoader.aFont.getBfVerdana55(), Color.WHITE);
        return labelStyle;
    }

    public static Label.LabelStyle getLS_Information(){
        Label.LabelStyle labelStyle = new Label.LabelStyle(AssetLoader.aLoader.aFont.getBfMaiandra65(), Color.WHITE);
        labelStyle.background = new TextureRegionDrawable(AssetLoader.aLoader.aImage.getSmallBlackSquare());
        return labelStyle;
    }

    public static TextButton.TextButtonStyle getTBS_Maiandra50(Drawable drawable){
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(drawable, drawable, drawable, AssetLoader.aLoader.aFont.getBfMaiandra50());
        return textButtonStyle;
    }

    public static TextButton.TextButtonStyle getTBS_Grey() {
        TextureAtlas.AtlasRegion arUp = AssetLoader.aLoader.aImage.getGreyButtonUp();
        TextureAtlas.AtlasRegion arDown = AssetLoader.aLoader.aImage.getGreyButtonDown();
        Drawable dUp = new TextureRegionDrawable(arUp);
        Drawable dDown = new TextureRegionDrawable(arDown);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(dUp, dDown, dUp, AssetLoader.aLoader.aFont.getBfMaiandra60());
        return textButtonStyle;
    }

    public static TextButton.TextButtonStyle getTBS_RoundButton50(Skin skin){
        TextButton.TextButtonStyle tbsMenu_Option = new TextButton.TextButtonStyle(skin.getDrawable("round-button-down"),
                skin.getDrawable("round-button"), null, AssetLoader.aLoader.aFont.getBfMaiandra50());
        tbsMenu_Option.downFontColor = new Color(1, 0.2f, 1,1);
        tbsMenu_Option.disabledFontColor = new Color(0.6f,0.6f,0.6f,1);
        tbsMenu_Option.disabled = skin.getDrawable("round-button");

        return tbsMenu_Option;
    }

    public static TextButton.TextButtonStyle getTBS_RoundButton60(Skin skin){
        TextButton.TextButtonStyle tbsMenu_Option = new TextButton.TextButtonStyle(skin.getDrawable("round-button"),
                skin.getDrawable("round-button-down"), null, AssetLoader.aLoader.aFont.getBfMaiandra60());
        tbsMenu_Option.downFontColor = new Color(Color.GRAY);

        return tbsMenu_Option;
    }

    public static TextButton.TextButtonStyle getTBS_RoundButton65(Skin skin){
        TextButton.TextButtonStyle tbsMenu_Option = new TextButton.TextButtonStyle(skin.getDrawable("round-button"),
                skin.getDrawable("round-button-down"), null, AssetLoader.aLoader.aFont.getBfMaiandra65());
        tbsMenu_Option.downFontColor = new Color(Color.GRAY);

        return tbsMenu_Option;
    }

    public static TextButton.TextButtonStyle getTBS_RoundButton(Skin skin){
        TextButton.TextButtonStyle tbsMenu_Option = new TextButton.TextButtonStyle(skin.getDrawable("round-button"),
                skin.getDrawable("round-button-down"), null, AssetLoader.aLoader.aFont.getBfMaiandra50());
        tbsMenu_Option.downFontColor = new Color(Color.GRAY);

        return tbsMenu_Option;
    }

    public static TextButton.TextButtonStyle getTBS_MenuHall(Skin skin){
        Drawable dUp = new TextureRegionDrawable(AssetLoader.aLoader.aImage.getiInfoMenu());
        TextButton.TextButtonStyle tbsMenuHall = new TextButton.TextButtonStyle(dUp, dUp, null, AssetLoader.aLoader.aFont.getBfMaiandra65());
        return tbsMenuHall;
    }

    public static TextButton.TextButtonStyle getTBS_Maiandra65(Skin skin){
        TextButton.TextButtonStyle tbsDCustom = new TextButton.TextButtonStyle(skin.getDrawable("button"),
                skin.getDrawable("button-down"), null,
                AssetLoader.aLoader.aFont.getBfMaiandra65());
        tbsDCustom.downFontColor = new Color(1, 0.2f, 1,1);
        tbsDCustom.disabledFontColor = new Color(0.6f,0.6f,0.6f,1);
        tbsDCustom.disabled = skin.getDrawable("button-down");

        return tbsDCustom;
    }

    public static Window.WindowStyle getWS_Maiandra80(Skin skin){
        Window.WindowStyle wsCustom = new Window.WindowStyle(AssetLoader.aLoader.aFont.getBfMaiandra80(), Color.WHITE, skin.getDrawable("window"));
        return wsCustom;
    }
    public static Window.WindowStyle getWS_GameButtons(){
        int size = 10;
        NinePatch ninePatch = new NinePatch(AssetLoader.aLoader.aImage.getBackgroundGameButtons(), size, size, size, size);
        NinePatchDrawable ninePatchDrawable = new NinePatchDrawable(ninePatch);

        Window.WindowStyle wsCustom = new Window.WindowStyle(AssetLoader.aLoader.aFont.getBfVerdana50(), Color.WHITE, ninePatchDrawable);
        return wsCustom;
    }
    public static Window.WindowStyle getWS_WSettings(){
        int size = 10;
        NinePatch ninePatch = new NinePatch(AssetLoader.aLoader.aImage.getBackgroundGameButtons(), size, size, size, size);
        NinePatchDrawable ninePatchDrawable = new NinePatchDrawable(ninePatch);

        Window.WindowStyle windowStyle = new Window.WindowStyle(AssetLoader.aLoader.aFont.getBfMaiandra60(), Color.WHITE, ninePatchDrawable);
        return windowStyle;
    }
    public static Window.WindowStyle getWS_Default(Skin skin){
        Window.WindowStyle wsDefault = new Window.WindowStyle(AssetLoader.aLoader.aFont.getBfMaiandra80(), Color.WHITE, skin.getDrawable("window"));
        return wsDefault;
    }

    public static CheckBox.CheckBoxStyle getCBS_Default(Skin skin){
        CheckBox.CheckBoxStyle cbsShowWindowWelcome = new CheckBox.CheckBoxStyle(skin.getDrawable("check-off"),
                skin.getDrawable("check-on"),AssetLoader.aLoader.aFont.getBfMaiandra50(),Color.WHITE);
        return cbsShowWindowWelcome;
    }

    public static TextField.TextFieldStyle getTFS_Login(Skin skin){
        TextField.TextFieldStyle tfsCustom = new TextField.TextFieldStyle(AssetLoader.aLoader.aFont.getBfMaiandra60(),
                Color.BLACK, skin.getDrawable("cursor"),skin.getDrawable("text-selection"), skin.getDrawable("textfield"));
        return tfsCustom;
    }

    public static CheckBox.CheckBoxStyle getCBS_ShowWindowWelcome(Skin skin){
        CheckBox.CheckBoxStyle cbsShowWindowWelcome = new CheckBox.CheckBoxStyle(skin.getDrawable("check-off"),skin.getDrawable("check-on"),
                AssetLoader.aLoader.aFont.getBfMaiandra60(),Color.WHITE);
        return cbsShowWindowWelcome;
    }

    public static List.ListStyle getLS_Chat(Skin skin){
        List.ListStyle lsChat = new List.ListStyle(AssetLoader.aLoader.aFont.getBfMaiandra50(), Color.WHITE, Color.WHITE, skin.getDrawable("list"));
        return lsChat;
    }

    public static TextField.TextFieldStyle getTFS_Custom(Skin skin){
        TextField.TextFieldStyle tfsCustom = new TextField.TextFieldStyle(AssetLoader.aLoader.aFont.getBfMaiandra65(),
                Color.BLACK, skin.getDrawable("cursor"),skin.getDrawable("text-selection"), skin.getDrawable("textfield"));
        return tfsCustom;
    }

    public static TextButton.TextButtonStyle getTBS_Decision(Skin skin){
        TextButton.TextButtonStyle tbsDecision = new TextButton.TextButtonStyle(skin.getDrawable("button"),
                skin.getDrawable("button-down"), null, AssetLoader.aLoader.aFont.getBfMaiandra50());
        tbsDecision.downFontColor = new Color(1, 0.2f, 1,1);
        return tbsDecision;
    }

    public static List.ListStyle getLS_HallPlayers(Skin skin){
        List.ListStyle lsHallPlayers = new List.ListStyle(AssetLoader.aLoader.aFont.getBfMaiandra60(), Color.PURPLE, Color.WHITE, skin.getDrawable("list"));
        return lsHallPlayers;
    }

    public static SelectBox.SelectBoxStyle getSBS_RankingType(Skin skin){
        SelectBox.SelectBoxStyle sbsRankingType = new SelectBox.SelectBoxStyle(AssetLoader.aLoader.aFont.getBfVerdana50(),
                Color.WHITE, skin.getDrawable("select-box"), getSPS_WithoutBackground(), getLS_Chat(skin));
        sbsRankingType.listStyle.background = skin.getDrawable("list");
        sbsRankingType.listStyle.fontColorSelected = Color.GOLD;
        return sbsRankingType;
    }

    public static ScrollPane.ScrollPaneStyle getSPS_WithoutBackground(){
        ScrollPane.ScrollPaneStyle spsWithoutBackground = new ScrollPane.ScrollPaneStyle(null,null,null,null,null);
        return spsWithoutBackground;
    }

    public static SelectBox.SelectBoxStyle getSBS_Ranking(Skin skin){
        SelectBox.SelectBoxStyle selectBoxStyle = new SelectBox.SelectBoxStyle(AssetLoader.aLoader.aFont.getBfVerdana50(), Color.WHITE,
                skin.getDrawable("select-box"), getSPS_WithoutBackground(),
                new List.ListStyle(AssetLoader.aLoader.aFont.getBfVerdana50(), Color.WHITE, Color.WHITE, skin.getDrawable("list")));
        selectBoxStyle.listStyle.background = skin.getDrawable("list");
        selectBoxStyle.listStyle.fontColorSelected = Color.GREEN;
        return selectBoxStyle;
    }

    public static SelectBox.SelectBoxStyle getSBS_LevelIA(Skin skin){
        SelectBox.SelectBoxStyle sbsLevelIA = new SelectBox.SelectBoxStyle(
                AssetLoader.aLoader.aFont.getBfVerdana50(), Color.WHITE,
                skin.getDrawable("select-box"), getSPS_WithoutBackground(), getLS_Chat(skin));
        sbsLevelIA.listStyle.background = skin.getDrawable("list");
        sbsLevelIA.listStyle.fontColorSelected = new Color(1, 0.2f, 1,1);
        return sbsLevelIA;
    }
}
