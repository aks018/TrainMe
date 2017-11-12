package search.music.avita.trainme;

import android.widget.CheckBox;

import java.util.ArrayList;

/**
 * Created by avita on 11/5/2017.
 */

public class WorkoutData {

    public ArrayList<Workout> returnWorkoutInitalData()
    {
        ArrayList<Workout> values = new ArrayList<>();
        Workout w1 = new Workout("Push Ups", 30);
        Workout w2 = new Workout("Sit Ups", 30);
        Workout w3 = new Workout("Planks", 30);
        Workout w4 = new Workout("Sprints", 30);
        Workout w5 = new Workout("Pilates", 30);
        Workout w6 = new Workout("Burpees", 30);
        Workout w7 = new Workout("Jumping Jacks", 30);
        Workout w8 = new Workout("Jump Rope", 30);
        Workout w9 = new Workout("Spider Lunges", 30);
        Workout w10 = new Workout("Jumping Lunges", 30);

        values.add(w1);
        values.add(w2);
        values.add(w3);
        values.add(w4);
        values.add(w5);
        values.add(w6);
        values.add(w7);
        values.add(w8);
        values.add(w9);
        values.add(w10);
        return values;
    }
}
