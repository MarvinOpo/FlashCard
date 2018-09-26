package com.mvopo.flashcard.Interface;

import android.os.Bundle;
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

        void launchGameActivity();

        void moveFirstCardToLast();

        void openLinkIntent(String url);

        void setCharacters(ArrayList<String> characters);

        void setButtonVisibility(int visibility);

        boolean shouldShuffle();

        void startAutoFlash();
        void stopAutoFlash();

        void swipeTopCard();

        void showCreditsDialog();
    }

    public interface mainAction{

        ArrayList<String> getCharacters(String regex, int baseResourceID);
        String getPhonetics();

        SwipeFlingAdapterView.onFlingListener getFlingListener();

        void onOptionItemSelected(int itemID);

        CompoundButton.OnCheckedChangeListener getCheckChangeListener();
        SeekBar.OnSeekBarChangeListener getSeekBarChangeListener();
        View.OnClickListener getClickListener();
        View.OnClickListener getCreditLinkListener();

        void performNext();
        void performFlingTransition();
    }
}
