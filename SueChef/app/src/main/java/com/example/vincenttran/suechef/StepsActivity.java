package com.example.vincenttran.suechef;

import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
import com.google.gson.JsonElement;
import java.util.Map;

public class StepsActivity extends AppCompatActivity implements RecognitionListener, AIListener{
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


        directions = getIntent().getStringArrayExtra("directions");
        stepNumber = 1;

        stepName = (TextView) findViewById(R.id.stepNumber);
        instruction = (TextView) findViewById(R.id.instruction);

        // Init assets for CMUSphinx
        try {
            Assets assets = new Assets(StepsActivity.this);
            File assetDir = assets.syncAssets();
            setupRecognizer(assetDir);              // Keyword Spotter init
        } catch (IOException e) {
            Log.e("voice_init", e.toString());
        }

        // Init TextToSpeech
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                setText();
            }
        });

        // Init API.ai
        final AIConfiguration config = new AIConfiguration(CLIENT_ACCESS_TOKEN,
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, config);
        aiService.setListener(this);

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

//        File commandGrammar = new File(assetsDir, "commands.gram");
        recognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);

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

    private void setText() {
        String step = getResources().getString(R.string.word_for_step) + stepNumber;
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
        if (hypothesis == null)
            return;

        String text = hypothesis.getHypstr();

        if (text.equals(KEYPHRASE)) {
            Toast.makeText(this, "I heard you!", Toast.LENGTH_SHORT).show();
        }

        // TODO: call api.ai api to start listening
        aiService.startListening();

    }
    /****************************************************************************************/




    /*********************************** API.AI METHODS *************************************/
    @Override
    public void onError(AIError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onListeningFinished() {

    }

    @Override
    public void onResult(AIResponse response) {
        Result result = response.getResult();

        // Get parameters
        String parameterString = "";
        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
            }
        }


        Toast.makeText(this, "Query:" + result.getResolvedQuery() +
                "\nAction: " + result.getAction() +
                "\nParameters: " + parameterString, Toast.LENGTH_SHORT).show();
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
}
