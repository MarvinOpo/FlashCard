package com.mvopo.flashcard.Presenter;

import android.view.View;

import com.mvopo.flashcard.Interface.GameSettingContract;
import com.mvopo.flashcard.R;

public class GameSettingPresenter implements GameSettingContract.settingAction{
    GameSettingContract.settingView settingView;

    public GameSettingPresenter(GameSettingContract.settingView settingView) {
        this.settingView = settingView;
    }

    @Override
    public View.OnClickListener getButtonClickListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mode = "";
                boolean showGameInfo = false;
                switch (view.getId()){
                    case R.id.setting_practice_btn:
                        mode = "Practice";
                        break;
                    case R.id.setting_multi_btn:
                        mode = "Multiplayer";
                        break;
                    case R.id.setting_back_btn:
                        mode = "";
                        showGameInfo = true;
                        break;
                }

                settingView.setGameMode(mode);

                if(showGameInfo){
                    settingView.setGameModeVisibility(View.GONE);
                    settingView.setGameInfoVisibility(View.VISIBLE);
                }else{
                    settingView.setGameModeVisibility(View.VISIBLE);
                    settingView.setGameInfoVisibility(View.GONE);
                }

            }
        };

        return listener;
    }

}
