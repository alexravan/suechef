package com.example.vincenttran.suechef;

import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class StepsActivity extends AppCompatActivity {
    TextView stepName;
    TextView instruction;
    String[] directions;
    int stepNumber;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Sue Chef");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        directions = getIntent().getStringArrayExtra("directions");
        stepNumber = 1;

        stepName = (TextView) findViewById(R.id.stepNumber);
        instruction = (TextView) findViewById(R.id.instruction);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                setText();
            }
        });



    }

    public void changeStep(View view) {
        switch (view.getId()) {
            case R.id.nextButton:
                stepNumber++;
                if (stepNumber > directions.length) stepNumber = directions.length;
                break;
            case R.id.backButton:
                stepNumber--;
                if (stepNumber == 0) stepNumber = 1;
                break;
        }
        setText();
    }

    public void setText() {
        stepName.setText("Step " + stepNumber);
        String direction = directions[stepNumber - 1];
        instruction.setText(direction);

        tts.speak(direction, TextToSpeech.QUEUE_FLUSH, null);

    }

}
