package com.mvopo.flashcard.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.mvopo.flashcard.Interface.MainActivityContract;
import com.mvopo.flashcard.Model.Constants;
import com.mvopo.flashcard.Presenter.MainActivityPresenter;
import com.mvopo.flashcard.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainActivityContract.mainView {

    MainActivityContract.mainAction mPresenter;

    ArrayList<String> characters = new ArrayList<>();
    ArrayAdapter adapter;

    SwipeFlingAdapterView flingContainer;
    LinearLayout settingContainer;

    SeekBar seekBar;
    CheckBox cbAuto, cbShuffle;
    TextView tvSpeechText;
    Button btnStart;

    CompoundButton.OnCheckedChangeListener onCheckedChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresenter = new MainActivityPresenter(this);

        flingContainer = findViewById(R.id.fling_view);
        settingContainer = findViewById(R.id.setting_container);
        seekBar = findViewById(R.id.speed_seekbar);
        cbAuto = findViewById(R.id.auto_cb);
        cbShuffle = findViewById(R.id.shuffle_cb);
        tvSpeechText = findViewById(R.id.detected_text);
        btnStart = findViewById(R.id.start_btn);

        characters.addAll(mPresenter.getCharacters(Constants.regexPerChar, R.string.letters_num));
        initAdapter(R.layout.letter_num_layout);
    }

    @Override
    protected void onStart() {
        super.onStart();

        flingContainer.setFlingListener(mPresenter.getFlingListener());

        onCheckedChangeListener = mPresenter.getCheckChangeListener();

        cbAuto.setOnCheckedChangeListener(onCheckedChangeListener);
        cbShuffle.setOnCheckedChangeListener(onCheckedChangeListener);

        btnStart.setOnClickListener(mPresenter.getClickListener());
        seekBar.setOnSeekBarChangeListener(mPresenter.getSeekBarChangeListener());
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit app?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mPresenter.onOptionItemSelected(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public String getBaseResource(int stringResourceID) {
        String resource = getResources().getString(stringResourceID).toUpperCase();
        return resource;
    }

    @Override
    public String getTopCard() {
        return characters.get(0);
    }

    @Override
    public int getSeekBarProgress() {
        return seekBar.getProgress();
    }

    @Override
    public void initAdapter(int layoutId) {
        adapter = new ArrayAdapter<>(this, layoutId, R.id.char_txtview, characters);
        flingContainer.setAdapter(adapter);
    }

    @Override
    public void launchGameActivity() {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putStringArrayListExtra("characters", characters);
        startActivity(intent);
        finish();
    }


    @Override
    public void moveFirstCardToLast() {
        characters.add(characters.remove(0));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void openLinkIntent(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void setCharacters(ArrayList<String> characters) {
        this.characters.clear();
        this.characters.addAll(characters);
    }

    @Override
    public void setButtonVisibility(int visibility) {
        btnStart.setText("START");
        btnStart.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        btnStart.setVisibility(visibility);
    }

    @Override
    public void startAutoFlash() {
        Toast.makeText(MainActivity.this, "Flashing Started.", Toast.LENGTH_SHORT).show();
        btnStart.setText("STOP");
        btnStart.setBackgroundColor(getResources().getColor(R.color.colorStop));
    }

    @Override
    public void stopAutoFlash() {
        Toast.makeText(MainActivity.this, "Flashing Stopped.", Toast.LENGTH_SHORT).show();
        btnStart.setText("START");
        btnStart.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void swipeTopCard() {
        flingContainer.getTopCardListener().selectRight();
    }

    @Override
    public void showCreditsDialog() {
        View view = getLayoutInflater().inflate(R.layout.credits_dialog, null);

        TextView swipeCardLink = view.findViewById(R.id.swipe_card_link);
        TextView icons8Link = view.findViewById(R.id.icon8_link);

        swipeCardLink.setOnClickListener(mPresenter.getCreditLinkListener());
        icons8Link.setOnClickListener(mPresenter.getCreditLinkListener());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.show();
    }

    @Override
    public boolean shouldShuffle() {
        return cbShuffle.isChecked();
    }
}
