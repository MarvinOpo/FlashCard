package com.mvopo.flashcard.View.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mvopo.flashcard.Interface.GameSettingContract;
import com.mvopo.flashcard.Presenter.GameSettingPresenter;
import com.mvopo.flashcard.R;

public class GameSettingFragment extends Fragment implements GameSettingContract.settingView{

    GameSettingContract.settingAction mPresenter;

    CardView cvGameMode, cvGameInfo;
    Button btnPractice, btnMultiPlayer, btnBack;

    View.OnClickListener buttonListener;

    String gameMode = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_setting, container, false);

        mPresenter = new GameSettingPresenter(this);

        buttonListener = mPresenter.getButtonClickListener();

        cvGameMode = view.findViewById(R.id.card_game_mode);
        cvGameInfo = view.findViewById(R.id.card_game_info);

        btnPractice = view.findViewById(R.id.setting_practice_btn);
        btnMultiPlayer = view.findViewById(R.id.setting_multi_btn);
        btnBack = view.findViewById(R.id.setting_back_btn);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        btnPractice.setOnClickListener(buttonListener);
        btnMultiPlayer.setOnClickListener(buttonListener);
        btnBack.setOnClickListener(buttonListener);
    }

    @Override
    public void setGameMode(String mode) {
        gameMode = mode;
    }

    @Override
    public void setGameModeVisibility(int visibility) {
        cvGameMode.setVisibility(visibility);
    }

    @Override
    public void setGameInfoVisibility(int visibility) {
        cvGameInfo.setVisibility(visibility);
    }
}
