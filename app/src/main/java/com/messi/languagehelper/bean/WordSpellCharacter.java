package com.messi.languagehelper.bean;

/**
 * Created by luli on 26/05/2017.
 */

public class WordSpellCharacter {

    private Character character;

    private boolean isSelected;

    public WordSpellCharacter(Character character){
        this.character = character;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
