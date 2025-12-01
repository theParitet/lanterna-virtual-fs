package io.github.theparitet;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import java.util.concurrent.atomic.AtomicBoolean;

public class VimNavigation extends WindowListenerAdapter {
    @Override
    public void onInput(Window window, KeyStroke keyStroke, AtomicBoolean atomicBoolean) {
        if (keyStroke.getKeyType() != KeyType.Character) {
            return;
        }

        Interactable focus = window.getTextGUI().getFocusedInteractable();
        if (focus instanceof TextBox) {
            return;
        }

        Character c = keyStroke.getCharacter();
        KeyType direction = null;

        if (c != null) {
            switch (c) {
                case 'h': direction = KeyType.ArrowLeft; break;
                case 'j': direction = KeyType.ArrowDown; break;
                case 'k': direction = KeyType.ArrowUp; break;
                case 'l': direction = KeyType.ArrowRight; break;
            }
        }

        if (direction != null) {
            atomicBoolean.set(false);
            window.handleInput(new KeyStroke(direction));
        }
    }
}