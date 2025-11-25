package com.tfdevelopment.pochaonlinetfd.screen.game.gui.windows;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.tfdevelopment.pochaonlinetfd.screen.game.gui.GUIBuilder;

public class GUIBuilder_Windows {
    private static final String TAG = GUIBuilder_Windows.class.getName();

    private GUIWindows_Connection guiW_Connection;
    private GUIWindows_Reload guiW_Reload;
    private GUIWindows_Notice guiW_Notice;
    private GUIWindows_Error guiW_Error;
    private GUIWindows_Invitation guiW_Invitation;

    public GUIBuilder_Windows(GUIBuilder guiBuilder) {
        this.guiW_Connection = new GUIWindows_Connection(guiBuilder, this);
        this.guiW_Reload = new GUIWindows_Reload(guiBuilder, this);
        this.guiW_Notice = new GUIWindows_Notice(guiBuilder, this);
        this.guiW_Error = new GUIWindows_Error(guiBuilder, this);
        this.guiW_Invitation = new GUIWindows_Invitation(guiBuilder, this);
    }

    public void render(float delta){

    }

    public Stack createStackWindows() {
        Stack stack = new Stack();
        stack.add(guiW_Invitation.buildWindowInvitation());
        stack.add(guiW_Connection.buildPlayerReconnectedWindow());
        stack.add(guiW_Reload.buildLoginReloadWindow());
        stack.add(guiW_Connection.buildServerDisconnetiondWindow());
        stack.add(guiW_Notice.buildWindowNotice());
        stack.add(guiW_Error.buildErrorWindow());
        return stack;
    }

    void setStyleWindow(Window window){
        window.setColor(new Color(0/255f,0/255f,253f/255f,0.95f));
        window.getTitleLabel().setAlignment(Align.center);
    }

    public GUIWindows_Connection getGuiW_Connection(){ return guiW_Connection; }
    public GUIWindows_Reload getGuiW_Reload(){ return guiW_Reload; }
    public GUIWindows_Notice getGuiW_Notice(){ return guiW_Notice; }
    public GUIWindows_Error getGuiW_Error(){ return guiW_Error; }
    public GUIWindows_Invitation getGuiW_Invitation(){ return guiW_Invitation; }
}
