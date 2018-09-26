package com.mvopo.flashcard.Presenter;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.mvopo.flashcard.Interface.PracticeContract;
import com.mvopo.flashcard.Model.Constants;

import java.util.ArrayList;

public class PracticePresenter implements PracticeContract.practiceAction{

    final String TAG = "PracticePresenter";
    PracticeContract.practiceView practiceView;

    public PracticePresenter(PracticeContract.practiceView practiceView) {
        this.practiceView = practiceView;
    }

    @Override
    public View.OnClickListener getClickListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view instanceof Button){
                    Button button = (Button) view;
                    String action = button.getText().toString();

                    switch (action){
                        case "START":
                            practiceView.startSpeechRecognizer();
                            practiceView.onStartViewUpdate();
                            break;
                        case "STOP":
                            practiceView.stopSpeechRecognizer();
                            practiceView.onStopViewUpdate();
                            break;
                    }
                }
            }
        };

        return listener;
    }

    @Override
    public SwipeFlingAdapterView.onFlingListener getFlingListener() {
        SwipeFlingAdapterView.onFlingListener listener = new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                practiceView.moveFirstCardToLast();
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
    public RecognitionListener getSpeechListener() {
        RecognitionListener listener = new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
                Log.e(TAG, "Ready to listen");
            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {
                Log.e(TAG, "Error received");
                practiceView.restartSpeechRecognizer();
            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> results = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String topResult = results.get(0).toUpperCase();
                String topCard = practiceView.getTopCard().toUpperCase();

                if (topResult.contains(topCard) || results.contains(topCard.toLowerCase())){
                    practiceView.setSpeechText(topCard);
                    practiceView.swipeTopCard();
                } else{
                    practiceView.setSpeechText(topResult);
                }

                practiceView.restartSpeechRecognizer();
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        };

        return listener;
    }

    @Override
    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (practiceView.getPermissionState() != PackageManager.PERMISSION_GRANTED) {
                if (practiceView.shouldShowPermission()) {
                    practiceView.showPermissionDialog();
                    return;
                }

                practiceView.requestPermission();
                return;
            }
        }

        practiceView.setButtonVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Constants.record_audio_code: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    practiceView.setButtonVisibility(View.VISIBLE);
                } else {
                    practiceView.showPermissionDialog();
                }
            }
        }
    }

}
