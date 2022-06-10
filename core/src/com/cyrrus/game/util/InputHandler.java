package com.cyrrus.game.util;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.cyrrus.game.screens.PlayScreen;

public class InputHandler implements InputProcessor {

    private PlayScreen screen;
    private Array<Integer> KeyPressRecords;

    public InputHandler(PlayScreen screen){

        KeyPressRecords = new Array<>();
        this.screen = screen;
    }

    public boolean keyPressed(int keycode){

        return KeyPressRecords.contains(keycode,true);
    }

    @Override
    public boolean keyDown(int keycode) {

        if (!KeyPressRecords.contains(keycode,true))KeyPressRecords.add(keycode);

        screen.KeyDownNotif(keycode);

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        if (KeyPressRecords.contains(keycode,true))KeyPressRecords.removeValue(keycode,true);

        screen.KeyUpNotif(keycode);

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
