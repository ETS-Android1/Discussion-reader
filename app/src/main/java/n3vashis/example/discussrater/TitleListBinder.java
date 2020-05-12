package n3vashis.example.discussrater;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TitleListBinder extends AppCompatActivity {

    ArrayList<Model> titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.rvContacts);

        titles = Model.createContactsList(20);


        TitleAdapter adapter = new TitleAdapter(titles);
        rvContacts.setAdapter(adapter);
        rvContacts.setLayoutManager(new LinearLayoutManager(this));

    }
}
