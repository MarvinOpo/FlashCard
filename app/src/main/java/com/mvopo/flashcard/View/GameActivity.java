package com.mvopo.flashcard.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.mvopo.flashcard.R;
import com.mvopo.flashcard.View.Fragments.GameSettingFragment;

public class GameActivity extends AppCompatActivity{

    FragmentManager fm;
    FragmentTransaction ft;

    GameSettingFragment gst = new GameSettingFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, gst).commit();
    }
}
