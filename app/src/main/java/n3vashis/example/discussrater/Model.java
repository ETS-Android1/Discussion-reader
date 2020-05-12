package n3vashis.example.discussrater;

import android.widget.Button;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Model extends Observable {
    private String title;
    public boolean buttonC = false;
    public Model(String name){
        title = name;

    }

    public String getName() {
        return title;
    }

    public static ArrayList<Model> createContactsList(int numContacts) {
        ArrayList<Model> contacts = new ArrayList<Model>();

        for (int i = 0; i < numContacts; i++) {
            contacts.add(new Model("Person" + i));
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

}

