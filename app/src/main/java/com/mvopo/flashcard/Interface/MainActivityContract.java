package com.mvopo.flashcard.Interface;

import android.speech.RecognitionListener;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;

public class MainActivityContract {

    public interface mainView{
        String getBaseResource(int stringResourceID);
        String getTopCard();
        int getSeekBarProgress();

        void initAdapter(int layoutId);

        void startSpeechRecognizer();
        void restartSpeechRecognizer();

        void moveFirstCardToLast();
        void setCharacters(ArrayList<String> characters);

        void setButtonVisibility(int visibility);
        void setSettingVisibility(int visibility);
        void setSpeechTextVisibility(int visibility);
        void setSpeechText(String text);

        boolean shouldShuffle();

        void startAutoFlash();
        void stopAutoFlash();

        void swipeTopCard();
    }

    public interface mainAction{

        ArrayList<String> getCharacters(String regex, int baseResourceID);
        ArrayList<String> getRandomCharacters(int size);
        String getPhonetics();

        SwipeFlingAdapterView.onFlingListener getFlingListener();

        void onOptionItemSelected(int itemID);

        CompoundButton.OnCheckedChangeListener getCheckChangeListener();
        SeekBar.OnSeekBarChangeListener getSeekBarChangeListener();
        View.OnClickListener getClickListener();
        RecognitionListener getSpeechListener();

        void performNext();
        void performFlingTransition();
    }
}
