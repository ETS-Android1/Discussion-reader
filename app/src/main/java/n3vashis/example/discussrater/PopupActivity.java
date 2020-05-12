package n3vashis.example.discussrater;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class PopupActivity extends AppCompatActivity  {

    Model m;
    SharedPreferences sharedpreferences;
    TextView name;
    TextView author;
    TextView link;
    RatingBar mBar;
    public static final String mypreference = "mypref";
    public static final String Name = "nameKey";
    public static final String Author = "authorKey";
    public static final String Link = "linkKey";
    public static final String Rating = "ratingKey";

    public static float rating = 0.0f;
    private static int RESULT_LOAD_IMAGE = 1;
    private String data = "";
    Button b2;
    Button b3;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity);

        b2 = (Button) findViewById(R.id.button4);

        name = (TextView) findViewById(R.id.editTextPhone);
        author= (TextView) findViewById(R.id.editTextStreet);
        link = (TextView) findViewById(R.id.editTextEmail);

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        Bundle extraInfo = getIntent().getExtras();
        data = extraInfo.getString("title");
        name.setText(data);

        loadData();

        mBar = (RatingBar) findViewById(R.id.ratingBar);
        mBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rate,
                                        boolean fromUser) {
                System.out.println("The rating is " + rate);
                rating = rate;
            }
        });

        b2.setEnabled(true);
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String n = name.getText().toString();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Name, n);
                editor.commit();

                String a = author.getText().toString();
                editor.putString(Author, a);
                editor.commit();

                String l = link.getText().toString();
                editor.putString(Link, l);
                editor.commit();

                editor.putFloat(Rating, rating);
                editor.commit();

                /*Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);*/
            }
        });

    }

    public void loadData(){
        name = (TextView) findViewById(R.id.editTextPhone);

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        name.setText(sharedpreferences.getString(Name, data));
        //set author
        author = (TextView) findViewById(R.id.editTextStreet);

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        author.setText(sharedpreferences.getString(Author, ""));
        //set link
        link = (TextView) findViewById(R.id.editTextEmail);

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        link.setText(sharedpreferences.getString(Link, ""));

        mBar = (RatingBar) findViewById(R.id.ratingBar);

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        mBar.setRating(sharedpreferences.getFloat(Rating, 0.0f));
    }


    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",name.getText().toString());
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.imageView6);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
    }
}

