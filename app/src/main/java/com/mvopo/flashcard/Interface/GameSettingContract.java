package com.mvopo.flashcard.Interface;

import android.view.View;

public class GameSettingContract {

    public interface settingView{
        void setGameMode(String mode);

        void setGameModeVisibility(int visibility);
        void setGameInfoVisibility(int visibility);
    }

    public interface settingAction{
        View.OnClickListener getButtonClickListener();
    }
}
