package hk.edu.polyu.eie.eie3109.todolist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String PREFERENCE_PACKAGE = "hk.edu.polyu.eie.eie3109.eie3109";
    public static final String PREFERENCE_NAME = "MyProfile";
    public static int MODE = Context.MODE_PRIVATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView TVName = (TextView) findViewById(R.id.TVName);
        Button BNList = (Button) findViewById(R.id.BNList);
        Button BNPhone = (Button) findViewById(R.id.BNPhone);

        Context c = null;
        try {
            c = this.createPackageContext(PREFERENCE_PACKAGE, CONTEXT_IGNORE_SECURITY);
            SharedPreferences sharedPreferences = c.getSharedPreferences(PREFERENCE_NAME, MODE);
            String name = sharedPreferences.getString("Name", "Default Name");
            String item = sharedPreferences.getString("item_0", "Empty");
            if (item.equals("Empty")) {
                sharedPreferences.edit().putString("item_0", "Meet with Professor").apply();
                sharedPreferences.edit().putInt("size", 1).apply();
            }
//            sharedPreferences.edit().putString("Name" , "Sam").apply();
//            sharedPreferences.edit().putString("item_0" , "Meet with Professor").apply();
//            sharedPreferences.edit().clear().commit();
            if (TVName != null) {
                TVName.setText(name);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }



        if (BNList != null) {
            BNList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, ListActivity.class));
                    MainActivity.this.finish();
                }
            });
        }
        if (BNPhone != null) {
            BNPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, PhoneActivity.class));
                    MainActivity.this.finish();
                }
            });
        }
    }
}
