package edu.csulb.android.notetakingapplication;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    DatabaseOperations dop;
    String[] photo_names;
    ImageView img;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        img = (ImageView) findViewById(R.id.temp_img);
        img.setVisibility(View.INVISIBLE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                     //   .setAction("Action", null).show();
                Intent i = new Intent(MainActivity.this,AddPhotoActivity.class);
                Bundle status = new Bundle();
                status.putInt("status",1);
                i.putExtras(status);
                startActivity(i);

            }
        });
        dop = new DatabaseOperations(this);
        long count = dop.getPhotoCount(dop);
        Log.v("count value ", "count" + count);
        final String[] photo_names = new String[(int)count];
        if(count > 0) {
            Cursor c = dop.getPhotoCaption(dop);
            for(int i=0;i<=count-1;i++){
                if(c.moveToNext()){
                    photo_names[i] = c.getString(0);
                }
            }
            Log.v("count value ", "count" + count);

            ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, photo_names);
            listView = (ListView) findViewById(R.id.activity_photo);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   // Toast.makeText(getBaseContext(),"Clicked",Toast.LENGTH_LONG).show();
                   // Log.v("Clicked","Clicked "+position);
                    String photo_no=null;
                    position+=1;
                    Cursor cu = dop.getPhotoNumber(dop,photo_names[position-1]);
                    while(cu.moveToNext()){
                        photo_no = cu.getString(0);
                    }


                    String caption = null;
                    String path=null;
                    Cursor cr=  dop.getPhotoInformation(dop,photo_no);
                    while(cr.moveToNext()){
                        caption= cr.getString(0);
                        path = cr.getString(1);
                    }
                    Log.v("Caption and Path" ,"Cap:"+caption+" Path: "+path);
                    Intent displayPhoto = new Intent(MainActivity.this,PhotoActivity2.class);
                    Bundle photoBundle = new Bundle();
                    photoBundle.putString("caption",caption);
                    photoBundle.putString("path",path);
                    photoBundle.putString("photoNo",photo_no);
                    displayPhoto.putExtras(photoBundle);
                    startActivity(displayPhoto);
                }
            });
        }
        else{
            img.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(Intent.ACTION_DELETE);
                    intent.setData(Uri.parse("package:edu.csulb.android.notetakingapplication"));
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
