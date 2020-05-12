package n3vashis.example.discussrater;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;



public class MainActivity extends AppCompatActivity implements Observer {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private int numTitles = 0;
    private String name = "";
    ArrayList<Model> titles;
    TextView begText;
    ImageView begImage;
    DbClass db = new DbClass(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.rvContacts);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        titles = Model.createContactsList(numTitles);
        mAdapter = new TitleAdapter(titles);
        recyclerView.setAdapter(mAdapter);

        ArrayList<String> listBook =  db.loadExisting();
        System.out.println(listBook.size());

        begText = (TextView) findViewById(R.id.textView6);
        begImage = (ImageView) findViewById(R.id.imageView);

        if(listBook.size() > 0){
            begText.setVisibility(begText.GONE);
            begImage.setVisibility(begImage.GONE);
        }

        for (int i = 0; i < listBook.size(); i++) {
            //System.out.println("created");
            Model m = new Model((listBook.get(i)));
            m.addObserver(this);
            titles.add(m);
            numTitles++;
            mAdapter.notifyDataSetChanged();
        }


    }

    public void addBook(String s){
        String author = s + "a";
        db.insert(s, author, 0);
        Cursor rs = db.getData(numTitles);
        rs.moveToFirst();

        name = rs.getString(rs.getColumnIndex(DbClass.BOOKS_COLUMN_NAME));
        String phone = rs.getString(rs.getColumnIndex(DbClass.BOOKS_COLUMN_AUTHOR));

        System.out.println("name " + name);
    }

    public void updateVote(String s){

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.poll:
                startPoll();
                break;
            case R.id.load:
                onLoadPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    public void startPoll(){
        Intent i = new Intent(this, PollActivity.class);
        startActivity(i);
    }


    public void onLoadPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter book name");

        final View customLayout = getLayoutInflater().inflate(R.layout.custom_layout, null);
        builder.setView(customLayout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                EditText editText = customLayout.findViewById(R.id.editText);
                String s = editText.getText().toString();
                createNewBook(s);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void createNewBook(String s){
        numTitles++;
        Model m = new Model(s);
        m.addObserver(this);
        titles.add(m);
        mAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
        begText.setVisibility(begText.GONE);
        begImage.setVisibility(begImage.GONE);
        addBook(s);
    }

    @Override
    public void update(Observable o, Object arg) {
        Model m = (Model) o;
        System.out.println(m.buttonC);
        if(m.buttonC == true) {
            //System.out.println("here");
            Intent intent = new Intent(this, PopupActivity.class);
            intent.putExtra("title",name);
            startActivityForResult(intent,0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 0) {
            // Make sure the request was successful
            if(resultCode == Activity.RESULT_OK){
                //float result=data.getFloatExtra("result",0.0f);
                String stitle = data.getStringExtra("result");
                System.out.println("result id " + stitle);
            }
        }
    }

}
