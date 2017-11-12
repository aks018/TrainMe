package search.music.avita.trainme;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void newWorkout(View view) {
        Intent intent = new Intent(this, NewWorkout.class);
        startActivity(intent);
    }

    public void viewWorkout(View view) {
        Toast.makeText(getApplicationContext(),"Coming soon", Toast.LENGTH_LONG).show();
    }

    public void goToSettings(View view) {
        Toast.makeText(getApplicationContext(), "Version 2.2", Toast.LENGTH_LONG).show();

    }
}
