package com.alimama.moon.ui;

public interface IBottomNavFragment {
    String currFragmentTitle();

    void refresh();

    void willBeDisplayed();

    void willBeHidden();
}
