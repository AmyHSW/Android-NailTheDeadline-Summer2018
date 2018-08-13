package edu.neu.madcourse.shuwanhuang.nailthedeadline.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

import edu.neu.madcourse.shuwanhuang.nailthedeadline.object.Cat;
import edu.neu.madcourse.shuwanhuang.nailthedeadline.adapter.CatsImageAdapter;
import edu.neu.madcourse.shuwanhuang.nailthedeadline.R;
import edu.neu.madcourse.shuwanhuang.nailthedeadline.util.DatabaseUtil;

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
        Set<Cat> collectedCats = DatabaseUtil.readCollectedCatsFromDB(this);
        cats.addAll(collectedCats);
    }

    private void initGridView() {
        if (!cats.isEmpty()) {
            TextView noCatsTextView = findViewById(R.id.no_cats_text);
            noCatsTextView.setVisibility(View.INVISIBLE);
        }
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new CatsImageAdapter(this, cats));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Cat cat = cats.get(position);
                Toast.makeText(CatsActivity.this, "Cat #" + cat.getID(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
