    package edu.csulb.android.notetakingapplication;

    import android.content.Intent;
    import android.database.Cursor;
    import android.net.Uri;
    import android.os.Bundle;
    import android.support.design.widget.FloatingActionButton;
    import android.support.v7.app.AppCompatActivity;
    import android.support.v7.widget.Toolbar;
    import android.util.Log;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.AdapterView;
    import android.widget.ImageView;
    import android.widget.ListView;

    import java.util.ArrayList;

    public class ListActivity extends AppCompatActivity {
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
                    Intent i = new Intent(ListActivity.this,AddPhotoActivity.class);
                    Bundle status = new Bundle();
                    status.putInt("status",1);
                    i.putExtras(status);
                    startActivity(i);

                }
            });
            dop = new DatabaseOperations(this);
            long count = dop.getPhotoCount(dop);
            Log.v("count value ", "count" + count);
            final ArrayList<String> photo_names = new ArrayList<String>();
            if(count > 0) {
                Cursor c = dop.getPhotoCaption(dop);
                for(int i=0;i<=count-1;i++){
                    if(c.moveToNext()){

                        photo_names.add(c.getString(0));
                    }
                }
                Log.v("count value ", "count" + count);

                PhotoAdapter adapter = new PhotoAdapter(this,photo_names);


                listView = (ListView) findViewById(R.id.activity_photo);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String photo_no=null;
                        position+=1;
                        Cursor cu = dop.getPhotoNumber(dop,photo_names.get(position-1));
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
                        Intent displayPhoto = new Intent(ListActivity.this,PhotoActivity2.class);
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
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
           int id = item.getItemId();
            if (id == R.id.action_settings) {
                Intent intent = new Intent(Intent.ACTION_DELETE);
                intent.setData(Uri.parse("package:edu.csulb.android.notetakingapplication"));
                startActivity(intent);
            }

            return super.onOptionsItemSelected(item);
        }
    }
