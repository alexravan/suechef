package com.example.vincenttran.suechef;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class StepsActivity extends AppCompatActivity {
    TextView stepName;
    TextView instruction;
    String[] directions;
    int stepNumber;
    TextToSpeech tts;
    private boolean ttsIsReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

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

    public void nextStep(View view) {
        stepNumber++;
        if (stepNumber > directions.length) stepNumber = directions.length;
        setText();
    }

    public void prevStep(View view) {
        stepNumber--;
        if (stepNumber == 0) stepNumber = 1;
        setText();
    }

    public void setText() {
        stepName.setText("Step " + stepNumber);
        String direction = directions[stepNumber - 1];
        instruction.setText(direction);

        tts.speak(direction, TextToSpeech.QUEUE_FLUSH, null);

    }

}
