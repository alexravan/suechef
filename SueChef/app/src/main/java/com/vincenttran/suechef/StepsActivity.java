package com.vincenttran.suechef;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;

import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;
import ai.api.AIConfiguration;
import ai.api.AIListener;
import ai.api.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.JsonElement;
import java.util.Map;

public class StepsActivity extends AppCompatActivity implements RecognitionListener, AIListener{
    private static final int RECORD_AUDIO_REQUEST_CODE = 1337;
    private TextView stepName;
    private TextView instruction;
    private String[] directions;
    private int stepNumber;
    private TextToSpeech tts;
    private SpeechRecognizer recognizer;
    private static final String KWS_SEARCH = "wakeup";      // Name of search
    private static final String KEYPHRASE = "okay sue chef";
    private AIService aiService;
    private String CLIENT_ACCESS_TOKEN = "401423411e744fd582341c09f066b8e0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);  // Keep screen on

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Sue Chef");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        /***** Check for Permissions *****/
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(StepsActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_REQUEST_CODE);
        } else {
            enableMicrophoneFeatures();
        }
        /*********************************/


        directions = getIntent().getStringArrayExtra("directions");
        stepNumber = 1;

        stepName = (TextView) findViewById(R.id.stepNumber);
        instruction = (TextView) findViewById(R.id.instruction);

        // Make screen swipe-able (For next, repeat, previous)
        findViewById(R.id.activity_steps).setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft() {
                changeStep(1);
            }

            @Override
            public void onSwipeRight() {
                changeStep(2);
            }

            // TODO: make swipe down repeat
        });

        // Init TextToSpeech
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                setText();
            }
        });



    }

    // User reopens app, should start keyword search
    @Override
    protected void onResume() {
        super.onResume();
//        enableMicrophoneFeatures();
    }

    // Called when permissions are granted
    private void enableMicrophoneFeatures() {
        // Init assets for CMUSphinx
        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    Assets assets = new Assets(StepsActivity.this);
                    File assetDir = assets.syncAssets();
                    setupRecognizer(assetDir);
                } catch (IOException e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception result) {
                if (result != null) {
                    Toast.makeText(StepsActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                }
                else {
                    search(KWS_SEARCH);
                }
            }
        }.execute();

        // Init API.ai
        final AIConfiguration config = new AIConfiguration(CLIENT_ACCESS_TOKEN,
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, config);
        aiService.setListener(this);

        // Set up FAB onclick
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recognizer.stop();
                aiService.startListening();
            }
        });
    }


    // User minimizes app, should stop keyword search
    @Override
    protected void onPause() {
//        recognizer.stop();
//        recognizer.cancel();
//        recognizer = null;
//        tts.stop();
        super.onPause();
    }

    // App is closed, recognizer should be deleted
    @Override
    protected void onDestroy() {
        super.onDestroy();

        recognizer.shutdown();
    }

    private void search(String search) {
        recognizer.stop();

        if (search.equals(KWS_SEARCH)) {
            recognizer.startListening(KWS_SEARCH);
        }
    }

    private void setupRecognizer(File assetsDir) throws IOException {
        recognizer = defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))       // TODO: Do we need this??

                // Threshold to tune for keyphrase to balance between false alarms and misses
                .setKeywordThreshold(1e-45f)

                // Use context-independent phonetic search, context-dependent is too slow for mobile
                .setBoolean("-allphone_ci", true)

                .getRecognizer();
        recognizer.addListener(this);

        recognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);

    }


    // 1 -> Next
    // 2 -> Previous
    // Other -> Repeat
    private void changeStep(int code) {
        switch (code) {
            case 1:             // Next
                stepNumber++;
                if (stepNumber > directions.length) stepNumber = directions.length;
                YoYo.with(Techniques.SlideInRight)
                        .duration(300)
                        .playOn(instruction);
                break;
            case 2:             // Back
                stepNumber--;
                if (stepNumber == 0) stepNumber = 1;
                YoYo.with(Techniques.SlideInLeft)
                        .duration(300)
                        .playOn(instruction);
        }
        setText();
    }

    private void setText() {
        String step = getResources().getString(R.string.word_for_step) + " " + stepNumber;
        stepName.setText(step);
        String direction = directions[stepNumber - 1];
        instruction.setText(direction);

        tts.speak(direction, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    /********************************* CMUSPHINX METHODS ***********************************/
    @Override
    public void onEndOfSpeech() {

    }
    @Override
    public void onBeginningOfSpeech() {
    }
    @Override
    public void onResult(Hypothesis hypothesis) {

    }
    @Override
    public void onTimeout() {

    }
    @Override
    public void onError(Exception e) {
        Log.e("Recognizer error", e.toString());
    }

    // Method in which keyword-spotting can react
    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null) return;


        String text = hypothesis.getHypstr();

        if (text.equals(KEYPHRASE)) {
            recognizer.stop();                  // Stop listening for keyword
            aiService.startListening();         // Start api.ai listener
        }


    }
    /****************************************************************************************/




    /*********************************** API.AI METHODS *************************************/
    @Override
    public void onError(AIError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
        aiService.stopListening();
        recognizer.startListening(KWS_SEARCH);
    }
    @Override
    public void onListeningFinished() {

    }


    @Override
    public void onResult(AIResponse response) {
        aiService.stopListening();
        recognizer.startListening(KWS_SEARCH);

        Result result = response.getResult();

        if (result.getAction().equals("changeStep")) {
            // Get parameters
            if (result.getParameters() != null && !result.getParameters().isEmpty()) {
                for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                    String command = entry.getValue().getAsString();
                    if ("Next".equals(command)) {
                        changeStep(1);
                        break;
                    } else if (command.equals("Previous")) {
                        changeStep(2);
                        break;
                    } else if (command.equals("Repeat")) {
                        changeStep(3);
                        break;
                    }
                }
            }
            else {                // Command unknown
                tts.speak(result.getFulfillment().getSpeech(), TextToSpeech.QUEUE_ADD, null, null);
            }
        }
    }

    @Override
    public void onListeningStarted() {
    }
    @Override
    public void onListeningCanceled() {

    }
    @Override
    public void onAudioLevel(float level) {

    }
    /****************************************************************************************/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RECORD_AUDIO_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    enableMicrophoneFeatures();
                }
            }
        }
    }
}
