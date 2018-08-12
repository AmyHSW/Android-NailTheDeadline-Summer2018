package edu.neu.madcourse.shuwanhuang.nailthedeadline;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class CatsActivity extends AppCompatActivity {

    private ArrayList<Cat> cats = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cats);

        initCatsList();
        initGridView();
    }

    private void initCatsList() {
        // TODO get cats from database, only add cats whose id != 0
        cats.add(new Cat(1));
        cats.add(new Cat(2));
    }

    private void initGridView() {
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new CatsImageAdapter(this, cats));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(CatsActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
