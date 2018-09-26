package com.mvopo.flashcard.Interface;

import android.speech.RecognitionListener;
import android.view.View;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

public class PracticeContract {

    public interface practiceView{
        String getTopCard();

        void initAdapter(int layoutId);

        int getPermissionState();

        void moveFirstCardToLast();

        void onStartViewUpdate();
        void onStopViewUpdate();

        void requestPermission();

        void restartSpeechRecognizer();

        void setButtonVisibility(int visibility);

        void startSpeechRecognizer();
        void stopSpeechRecognizer();
        void setSpeechText(String text);

        void swipeTopCard();

        boolean shouldShowPermission();
        void showPermissionDialog();
    }

    public interface practiceAction{
        View.OnClickListener getClickListener();
        SwipeFlingAdapterView.onFlingListener getFlingListener();
        RecognitionListener getSpeechListener();

        void checkPermission();
        void onRequestPermissionResult(int requestCode, String permissions[], int[] grantResults);
    }
}
