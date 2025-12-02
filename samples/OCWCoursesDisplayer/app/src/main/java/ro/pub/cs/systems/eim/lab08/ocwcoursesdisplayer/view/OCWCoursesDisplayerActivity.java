package ro.pub.cs.systems.eim.lab08.ocwcoursesdisplayer.view;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import ro.pub.cs.systems.eim.lab08.ocwcoursesdisplayer.R;
import ro.pub.cs.systems.eim.lab08.ocwcoursesdisplayer.network.OCWCoursesDisplayerAsyncTask;





public class OCWCoursesDisplayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocw_courses_displayer);

        TextView ocwCoursesDisplayerTextView = findViewById(R.id.ocw_courses_displayer_text_view);
        Button displayOcwCoursesButton = findViewById(R.id.display_ocw_courses_button);

        displayOcwCoursesButton.setOnClickListener(
                (view) -> new OCWCoursesDisplayerAsyncTask(ocwCoursesDisplayerTextView).execute()
        );
    }
}
