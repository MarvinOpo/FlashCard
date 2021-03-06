package com.mvopo.flashcard.Presenter;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.mvopo.flashcard.Interface.MainActivityContract;
import com.mvopo.flashcard.Model.Constants;
import com.mvopo.flashcard.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class MainActivityPresenter implements MainActivityContract.mainAction {

    final String TAG = "MainPresenter";

    ArrayList<String> presenterChars = new ArrayList<>();
    ArrayList<String> tempCharHolder = new ArrayList<>();
    MainActivityContract.mainView mainView;

    Handler handler = new Handler();
    boolean performAuto = false;

    int menuID = -1;

    public MainActivityPresenter(MainActivityContract.mainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public ArrayList<String> getCharacters(String regex, int baseResourceID) {
        String baseResource;

        presenterChars.clear();
        tempCharHolder.clear();

        if (baseResourceID == -1) baseResource = getPhonetics();
        else baseResource = mainView.getBaseResource(baseResourceID);

        presenterChars.addAll(new ArrayList<>(Arrays.asList(baseResource.split(regex))));
        tempCharHolder.addAll(presenterChars);

        if (mainView.shouldShuffle()) Collections.shuffle(presenterChars);
        return presenterChars;
    }

    @Override
    public String getPhonetics() {
        String consonants = mainView.getBaseResource(R.string.consonants);
        String vowels = mainView.getBaseResource(R.string.vowels);
        String phonetics = vowels.replaceAll(Constants.regexPerChar, " ").toUpperCase();

        for (int i = 0; i < consonants.length(); i++) {
            for (int j = 0; j < vowels.length(); j++) {
                phonetics = phonetics + "" + consonants.charAt(i) + "" + vowels.charAt(j) + " ";
            }
        }

        return phonetics;
    }

    @Override
    public SwipeFlingAdapterView.onFlingListener getFlingListener() {
        SwipeFlingAdapterView.onFlingListener listener = new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                mainView.moveFirstCardToLast();
            }

            @Override
            public void onLeftCardExit(Object o) {

            }

            @Override
            public void onRightCardExit(Object o) {

            }

            @Override
            public void onAdapterAboutToEmpty(int i) {

            }

            @Override
            public void onScroll(float v) {

            }
        };

        return listener;
    }

    @Override
    public void onOptionItemSelected(int itemID) {
        menuID = itemID;
        String regex = Constants.regexWhiteSpace;
        int baseResourceID = -1;
        int layout = R.layout.letter_num_layout;

        switch (itemID) {
            case R.id.letters_num:
                regex = Constants.regexPerChar;
                baseResourceID = R.string.letters_num;
                break;
            case R.id.words_3_letter:
                baseResourceID = R.string.word_3_letters;
                break;
            case R.id.words_5_letter:
                baseResourceID = R.string.word_5_letters;
                layout = R.layout.lengthy_words_layout;
                break;
            case R.id.words_8_letter:
                baseResourceID = R.string.word_8_letters;
                layout = R.layout.lengthy_words_layout;
                break;
            case R.id.mini_game:
                baseResourceID = R.string.word_8_letters;

                presenterChars = getCharacters(regex, baseResourceID);
                mainView.setCharacters(presenterChars);

                mainView.launchGameActivity();
                return;
            case R.id.credits:
                mainView.showCreditsDialog();
                return;
        }

        presenterChars = getCharacters(regex, baseResourceID);
        presenterChars.add(0, mainView.getTopCard());

        mainView.setCharacters(presenterChars);

        mainView.initAdapter(layout);
        performFlingTransition();
    }

    @Override
    public CompoundButton.OnCheckedChangeListener getCheckChangeListener() {
        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                int id = compoundButton.getId();

                switch (id) {
                    case R.id.auto_cb:
                        if (isChecked) {
                            mainView.setButtonVisibility(View.VISIBLE);
                            break;
                        }

                        mainView.setButtonVisibility(View.INVISIBLE);
                        performAuto = false;

                        break;
                    case R.id.shuffle_cb:
                        String topChar = mainView.getTopCard();
                        if (isChecked) {
                            Collections.shuffle(presenterChars);
                            presenterChars.add(0, topChar);

                            mainView.setCharacters(presenterChars);
                            performFlingTransition();
                            break;
                        }

                        tempCharHolder.add(0, topChar);
                        mainView.setCharacters(tempCharHolder);
                        tempCharHolder.remove(0);

                        performFlingTransition();
                        break;
                }
            }
        };

        return listener;
    }

    @Override
    public SeekBar.OnSeekBarChangeListener getSeekBarChangeListener() {
        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                handler.removeCallbacksAndMessages(null);
                performNext();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };

        return listener;
    }

    @Override
    public View.OnClickListener getClickListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                Button button = (Button) view;

                switch (id) {
                    case R.id.start_btn:
                        String buttonAction = button.getText().toString();

                        if (buttonAction.equalsIgnoreCase("START")) {
                            mainView.startAutoFlash();

                            performAuto = true;
                            performNext();
                        } else {
                            performFlingTransition();
                        }
                        break;
                }
            }
        };

        return listener;
    }

    @Override
    public View.OnClickListener getCreditLinkListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.swipe_card_link:
                        mainView.openLinkIntent("https://github.com/Diolor/Swipecards");
                        break;
                    case R.id.icon8_link:
                        mainView.openLinkIntent("https://icons8.com/");
                        break;
                }
            }
        };

        return listener;
    }

    @Override
    public void performNext() {
        if (performAuto) {
            int speed = 5000 / (mainView.getSeekBarProgress() + 1);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mainView.swipeTopCard();
                    performNext();
                }
            }, speed);
        }
    }

    @Override
    public void performFlingTransition() {
        if (performAuto) {
            handler.removeCallbacksAndMessages(null);
            mainView.stopAutoFlash();
            performAuto = false;
        }

        mainView.swipeTopCard();
    }
}
