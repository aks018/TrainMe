package search.music.avita.trainme;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.travijuu.numberpicker.library.NumberPicker;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;

import de.codecrafters.tableview.SortState;
import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.SwipeToRefreshListener;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.providers.SortStateViewProvider;
import de.codecrafters.tableview.toolkit.EndlessOnScrollListener;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;

import static android.content.ContentValues.TAG;

public class NewWorkout extends Activity implements TextToSpeech.OnInitListener {
    String TAG = "NewWorkoutTag";
    //private static String[][] DATA_TO_SHOW = {};
    private String[] TABLE_HEADERS = {"Activity", "Duration (minutes)", "Workout?"};
    private static ArrayList<Workout> DATA_TO_SHOW;

    private static int totalWorkOutTimeSeconds;

    private AlertDialog alertDialog;

    private TextToSpeech tts;


    CountDownTimer countDownTimer;
    static boolean stop = false;
    Vibrator v;

    EditText newWorkout;
    NumberPicker numberPicker;
    TextView totalWorkout;

    ListView workouts;
    WorkoutAdapter workoutAdapter;
    static boolean[] isCheckedArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_workout);

        workouts = (ListView) findViewById(R.id.tableView);


        if(DATA_TO_SHOW==null) {
            WorkoutData workoutData = new WorkoutData();
            DATA_TO_SHOW = workoutData.returnWorkoutInitalData();
            isCheckedArray = new boolean[DATA_TO_SHOW.size()];
            totalWorkOutTimeSeconds = 0;
        }

        workoutAdapter = new WorkoutAdapter(this, DATA_TO_SHOW, isCheckedArray);
        workouts.setAdapter(workoutAdapter);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        tts = new TextToSpeech(this, this);


        //tableView.setEmptyDataIndicatorView(findViewById(R.id.empty_data_indicator));
        newWorkout = (EditText) findViewById(R.id.workoutEditText);
        newWorkout.setImeOptions(EditorInfo.IME_ACTION_DONE);

        numberPicker = (NumberPicker) findViewById(R.id.number_picker);
        numberPicker.setMin(15);
        numberPicker.setMax(60 * 480);
        numberPicker.setValue(30);
        totalWorkout = (TextView) findViewById(R.id.workoutTime);
        totalWorkout.setText("Total Workout Time: \n" + Integer.toString(totalWorkOutTimeSeconds) + " Seconds");


        workouts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                if (isCheckedArray[position] != true) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(NewWorkout.this, R.style.myDialog));
                    builder.setCancelable(false);
                    Workout workout = DATA_TO_SHOW.get(position);
                    builder.setMessage("Enter New Duration For " + workout.getActivityName());
                    LayoutInflater inflater = NewWorkout.this.getLayoutInflater();
                    //this is what I did to added the layout to the alert dialog
                    View layout = inflater.inflate(R.layout.activity_edit_duration, null);
                    builder.setView(layout);

                    final EditText timeRemaining = (EditText) layout.findViewById(R.id.editTextDuration);
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (timeRemaining.getText().equals("")) {
                                Toast.makeText(NewWorkout.this, "Please enter a valid value", Toast.LENGTH_LONG).show();
                                return;
                            } else {
                                Workout workout = DATA_TO_SHOW.get(position);
                                workout.setActivityDuration(Integer.parseInt(timeRemaining.getText().toString()));
                                DATA_TO_SHOW.set(position, workout);
                                workoutAdapter.notifyDataSetChanged();
                            }
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    final AlertDialog dialog = builder.create();
                    dialog.show();

                } else {
                    Toast.makeText(NewWorkout.this, "Please Uncheck Workout Before Editing Duration Time", Toast.LENGTH_LONG).show();
                }
            }

        });
        workoutAdapter.mListener = new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "IN CHECKED CHANGED");
                int index = (Integer) buttonView.getTag();
                Log.d(TAG, "Index: " + Integer.toString(index));
                Workout workout = DATA_TO_SHOW.get(index);
                isCheckedArray[(Integer) buttonView.getTag()] = isChecked;
                if (isChecked) {
                    totalWorkOutTimeSeconds += workout.getActivityDuration();
                    totalWorkout.setText("Total Workout Time: \n" + Integer.toString(totalWorkOutTimeSeconds) + " Seconds");

                } else {
                    totalWorkOutTimeSeconds -= workout.getActivityDuration();
                    totalWorkout.setText("Total Workout Time: \n" + Integer.toString(totalWorkOutTimeSeconds) + " Seconds");

                }
            }
        };
    }

    private void closeTTS() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        closeTTS();
        super.onDestroy();
    }

    public void addActivityToWorkout(View view) {
        Toast.makeText(NewWorkout.this, "Unable to add workout now.", Toast.LENGTH_LONG).show();
        /*if (newWorkout.getText().toString().equals("")) {

            Toast.makeText(getApplicationContext(), "Please Enter A Valid Activity Name", Toast.LENGTH_LONG).show();
            return;
        } else if (numberPicker.getValue() == 0) {
            Toast.makeText(getApplicationContext(), "Please Enter A Valid Activity Duration", Toast.LENGTH_LONG).show();
            return;

        }

        Workout workout = new Workout(newWorkout.getText().toString(), numberPicker.getValue());

        newWorkout.setText("");
        DATA_TO_SHOW.add(workout);
        totalWorkOutTimeSeconds += numberPicker.getValue();

        totalWorkout.setText("Total Workout Time: \n" + Integer.toString(totalWorkOutTimeSeconds) + " Seconds");

        workoutAdapter.notifyDataSetChanged();
        Log.d(TAG, DATA_TO_SHOW.toString());*/


    }


    public void beginWorkout(View view) {
        if (DATA_TO_SHOW.size() <= 0) {
            Log.d(TAG, "NO ENTRIES");
            Toast.makeText(getApplicationContext(), "Please Create a Workout First", Toast.LENGTH_LONG).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog))
                .setTitle("New Workout");
        final FrameLayout frameView = new FrameLayout(NewWorkout.this);
        builder.setView(frameView);

        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        LayoutInflater inflater = alertDialog.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.workout, frameView);
        final TextView timeRemaining = (TextView) dialoglayout.findViewById(R.id.timeRemaining);
        loopThroughActivities(timeRemaining, 0, alertDialog);
        alertDialog.show();
    }

    private void tenSecondsLeft(int seconds) {
        tts.speak(Integer.toString(seconds), TextToSpeech.QUEUE_FLUSH, null, null);

    }

    public void printArray() {
        for (int i = 0; i < isCheckedArray.length; i++) {
            Log.d(TAG, Boolean.toString(isCheckedArray[i]));
        }
    }

    private void speakOutActivity(String activitySpeak) {

        if (tts.speak("Workout Activity: " + activitySpeak + " Has Started", TextToSpeech.QUEUE_FLUSH, null, null) < 0) {
            Log.d(TAG, "ERROR IN SPEAK");
        }

    }

    private void loopThroughActivities(final TextView timeRemaining, final int index, final AlertDialog alertDialog) {

        printArray();
        final int total_seconds = getTotalMinutes(DATA_TO_SHOW, index);
        final String activity = getActivity(DATA_TO_SHOW, index);
        v.vibrate(150);
        if (isCheckedArray[index] == true) {
            speakOutActivity(activity);
            Log.d(TAG, "Index: " + Integer.toString(index));
            Log.d(TAG, Boolean.toString(isCheckedArray[index]));
            countDownTimer = new CountDownTimer(total_seconds * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    if (stop == false) {
                        //currentActivity.setText("Current Activity: " + currentActivity.getText().toString());
                        if (millisUntilFinished <= 10000) {
                            //Log.d(TAG, Long.toString(millisUntilFinished));
                            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            v.vibrate(150);
                            tenSecondsLeft((int) millisUntilFinished / 1000);
                            timeRemaining.setTextColor(Color.parseColor("#FF0000"));
                        }
                        timeRemaining.setText("Current Activity: " + activity + "\nSeconds Remaining: " + millisUntilFinished / 1000);
                    } else {
                        countDownTimer.cancel();
                    }
                }

                public void onFinish() {
                    timeRemaining.setText("done!");
                    int value = index;
                    value++;
                    if (value >= DATA_TO_SHOW.size()) {
                        if (stop == false) {

                            alertDialog.dismiss();

                            //Intent intent = getIntent();
                        }
                    } else {
                        if (stop == false) {
                            final int sendValue = value;
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    timeRemaining.setTextColor(Color.parseColor("#348fc3"));
                                    loopThroughActivities(timeRemaining, sendValue, alertDialog);
                                }
                            }, 7500);

                        }
                    }
                }
            }.start();
        } else {
            timeRemaining.setTextColor(Color.parseColor("#348fc3"));
            int increaseIndex = index;
            increaseIndex++;
            if (increaseIndex < DATA_TO_SHOW.size()) {
                loopThroughActivities(timeRemaining, increaseIndex, alertDialog);
            }
        }


    }

    public void endWorkout(View view) {

        alertDialog.dismiss();

        stop = true;

        //Intent intent = getIntent();
        //startActivity(intent);

    }

    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                //speakOut();
                Log.d(TAG, "Success");
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    private String getActivity(ArrayList<Workout> totalAmount, int index) {
        Workout value = totalAmount.get(index);


        return value.getActivityName();
    }

    private int getTotalMinutes(ArrayList<Workout> totalAmount, int index) {

        Workout value = totalAmount.get(index);
        return value.getActivityDuration();

    }



}
