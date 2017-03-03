package android.yzz.com.lianxinview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.yzz_list);
        List<String> list = new ArrayList<>();
        for (int i = 0; i <20 ; i++) {
            list.add("======="+i+"==========");
        }
        ArrayAdapter a = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,list);
        lv.setAdapter(a);
        a.notifyDataSetChanged();

    }
}
