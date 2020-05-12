package n3vashis.example.discussrater;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class PollActivity extends AppCompatActivity {
    private static int RESULT_LOAD_IMAGE = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poll_layouts);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(this));

        Button vone = (Button) findViewById(R.id.vone);
        Button vtwo = (Button) findViewById(R.id.vtwo);
        Button vthree = (Button) findViewById(R.id.vthree);

        vone.setEnabled(true);
        vone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ;
            }
        });

    }


    }

