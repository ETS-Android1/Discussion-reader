package n3vashis.example.discussrater;

import android.net.Uri;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

//Model class for books - MainActivity class is observer
public class Model extends Observable {
    private String title;
    public boolean buttonC = false; //has button been clicked
    public String base; //the image in base64 format
    public Uri image = null;
    public int id; // id of the book in the database

    public Model(String name,String b,int num){
        title = name;
        base = b;
        id = num;
    }

    public String getName() {
        return title;
    }

    public static ArrayList<Model> createContactsList(int numContacts) {
        ArrayList<Model> contacts = new ArrayList<Model>();

        for (int i = 0; i < numContacts; i++) {
            contacts.add(new Model("","",0));
        }

        return contacts;
    }

    public void buttonClicked(){
        buttonC = true;
        setChanged();
        notifyObservers();
    }

    public void initObservers()
    {
        setChanged();
        notifyObservers();
    }

    @Override
    public synchronized void addObserver(Observer o)
    {
        super.addObserver(o);
    }

    @Override
    public synchronized void deleteObservers()
    {
        super.deleteObservers();
    }


    @Override
    public void notifyObservers()
    {
        super.notifyObservers();
    }

    public void setImage(String b){
        base = b;
    }
}

