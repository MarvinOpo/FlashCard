package com.mvopo.flashcard.View.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.mvopo.flashcard.Interface.PracticeContract;
import com.mvopo.flashcard.Model.Constants;
import com.mvopo.flashcard.Presenter.PracticePresenter;
import com.mvopo.flashcard.R;

import java.util.ArrayList;
import java.util.Collections;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

public class PracticeFragment extends Fragment implements PracticeContract.practiceView,
        ActivityCompat.OnRequestPermissionsResultCallback{

    PracticeContract.practiceAction mPresenter;

    ArrayList<String> characters = new ArrayList<>();
    ArrayAdapter adapter;

    SwipeFlingAdapterView flingContainer;

    Button btnStart;
    TextView tvSpeechText;

    SpeechRecognizer speechRecognizer;
    Intent speechRecognizerIntent;
    boolean isListening = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_practice, container, false);
        characters.addAll(getArguments().getStringArrayList("characters"));
        Collections.shuffle(characters);

        mPresenter = new PracticePresenter(this);

        flingContainer = view.findViewById(R.id.fling_view);

        btnStart = view.findViewById(R.id.start_btn);
        tvSpeechText = view.findViewById(R.id.detected_text);

        initAdapter(R.layout.lengthy_words_layout);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        flingContainer.setFlingListener(mPresenter.getFlingListener());
        btnStart.setOnClickListener(mPresenter.getClickListener());

        mPresenter.checkPermission();
    }

    @Override
    public String getTopCard() {
        return characters.get(0);
    }

    @Override
    public void initAdapter(int layoutId) {
        adapter = new ArrayAdapter<>(getContext(), layoutId, R.id.char_txtview, characters);
        flingContainer.setAdapter(adapter);
    }

    @Override
    public int getPermissionState() {
        return checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO);
    }

    @Override
    public void moveFirstCardToLast() {
        characters.add(characters.remove(0));
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onStartViewUpdate() {
        Toast.makeText(getContext(), "Detection Started.", Toast.LENGTH_SHORT).show();
        btnStart.setText("STOP");
        btnStart.setBackgroundColor(getResources().getColor(R.color.colorStop));
    }

    @Override
    public void onStopViewUpdate() {
        Toast.makeText(getContext(), "Detection Stopped.", Toast.LENGTH_SHORT).show();
        btnStart.setText("START");
        btnStart.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void requestPermission() {
        requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, Constants.record_audio_code);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        mPresenter.onRequestPermissionResult(requestCode, permissions, grantResults);
    }

    @Override
    public void restartSpeechRecognizer() {
        if(isListening) {
            isListening = false;
            speechRecognizer.destroy();
        }

        startSpeechRecognizer();
    }

    @Override
    public void setButtonVisibility(int visibility) {
        btnStart.setVisibility(visibility);
    }

    @Override
    public void startSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                getActivity().getPackageName());

        speechRecognizer.setRecognitionListener(mPresenter.getSpeechListener());

        if (!isListening) {
            speechRecognizer.startListening(speechRecognizerIntent);
            isListening = true;
        }
    }

    @Override
    public void stopSpeechRecognizer() {
        isListening = false;
        speechRecognizer.destroy();
    }

    @Override
    public void setSpeechText(String text) {
        tvSpeechText.setText(text);
    }

    @Override
    public void swipeTopCard() {
        flingContainer.getTopCardListener().selectRight();
    }

    @Override
    public boolean shouldShowPermission() {
        return shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO);
    }

    @Override
    public void showPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Please allow all permissions required for this app." +
                "\n\nMicrophone - Listen to audio, to translate speech into text.");
        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, Constants.microphone_code);
            }
        });
        builder.setNegativeButton("Not now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((Activity) getContext()).finish();
            }
        });

        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Constants.microphone_code:
                mPresenter.checkPermission();
                break;
        }
    }
}
