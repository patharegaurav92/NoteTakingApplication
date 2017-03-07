package edu.csulb.android.notetakingapplication;

import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class PhotoActivity extends AppCompatActivity {
DatabaseOperations dop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        dop = new DatabaseOperations(this);
        populateListView();

    }
    public void populateListView(){
        Cursor cursor = dop.getInformation(dop);
        String []fromFieldNames = new String[]{TableData.TableInfo.PHOTO_CAPTION, TableData.TableInfo.PHOTO_PATH,TableData.TableInfo.PHOTO_NUMBER};
        int []toTextView = new int [] {R.id.caption_txt,R.id.path_txt,R.id.id_txt};
        SimpleCursorAdapter simpleCursorAdapter;
        simpleCursorAdapter= new SimpleCursorAdapter(getBaseContext(),R.layout.item_layout,cursor,fromFieldNames,toTextView,0);
        ListView listView = (ListView) findViewById(R.id.activity_photo);
        listView.setAdapter(simpleCursorAdapter);
    }
}
