package hk.edu.polyu.eie.eie3109.todolist;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

public class ListActivity extends AppCompatActivity {

    private ListView myToDoList;
    private ArrayList myArrayList;
    private ArrayAdapter<String> myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        TextView TVMessage = (TextView) findViewById(R.id.TVMessage);
        Button BNBack = (Button) findViewById(R.id.BNBack);

        myArrayList = new ArrayList();
//        for (int i = 0; i < 10; i++) {
//            myStringList[i] = "Empty " + i;
//        }
//        myArrayList.add("Return books to Library");
//        myArrayList.add("Meeting with Advisor");

        myToDoList = (ListView) findViewById(R.id.LVList);
//        myAdapter = new ArrayAdapter<String>(this, R.layout.list_item, myArrayList);
//        myToDoList.setAdapter(myAdapter);

        Context c = null;
        try {
            c = this.createPackageContext(MainActivity.PREFERENCE_PACKAGE, CONTEXT_IGNORE_SECURITY);
            SharedPreferences sharedPreferences = c.getSharedPreferences(MainActivity.PREFERENCE_NAME, MainActivity.MODE);
            String name = sharedPreferences.getString("Name", "Default Name");
            if (TVMessage != null) {
                TVMessage.setText("Hi! " + name);
            }

            myArrayList.clear();
            int size = sharedPreferences.getInt("size", 0);
            for(int i=0;i<size;i++)
            {
                myArrayList.add(sharedPreferences.getString("item_" + i, null));
            }
            Collections.sort(myArrayList, new Comparator<String>()
            {
                @Override
                public int compare(String text1, String text2)
                {
                    return text1.compareToIgnoreCase(text2);
                }
            });

            myAdapter = new ArrayAdapter<String>(this, R.layout.list_item, myArrayList);
            myToDoList.setAdapter(myAdapter);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (BNBack != null) {
            BNBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ListActivity.this, MainActivity.class));
                    ListActivity.this.finish();
                    Context c = null;
                    try {
                        c = createPackageContext(MainActivity.PREFERENCE_PACKAGE, CONTEXT_IGNORE_SECURITY);
                        SharedPreferences sharedPreferences = c.getSharedPreferences(MainActivity.PREFERENCE_NAME, MainActivity.MODE);
                        sharedPreferences.edit().putInt("size", myArrayList.size()).apply();
                        for(int i=0;i<myArrayList.size();i++) {
                            sharedPreferences.edit().remove("item_" + i).apply();
                            sharedPreferences.edit().putString("item_" + i, myArrayList.get(i).toString()).apply();
                        }
                    }catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        myToDoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int item_no = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
                ListView options = new ListView(ListActivity.this);
                options.setAdapter(new ArrayAdapter<String>(ListActivity.this, R.layout.list_item, new String[] {"Add above", "Add below", "Edit", "Remove"}));
                builder.setView(options);

                final Dialog dialog = builder.create();
                dialog.show();

                options.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0 || position == 1) {
                            final Dialog dialogForm = new Dialog(ListActivity.this);
                            dialogForm.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialogForm.setContentView(R.layout.form_operation);
                            TextView TVTitle = (TextView) dialogForm.findViewById(R.id.TVTitle);
                            final EditText ETText = (EditText) dialogForm.findViewById(R.id.ETText);
                            Button BNSubmit = (Button) dialogForm.findViewById(R.id.BNSubmit);
                            if (TVTitle != null) {
                                TVTitle.setText("Add");
                            }
                            if (BNSubmit != null) {
                                final int pos = position;
                                BNSubmit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        myArrayList.add(item_no+pos, ETText.getText().toString());
                                        myAdapter.notifyDataSetChanged();
                                        dialogForm.dismiss();
                                    }
                                });
                            }
                            dialogForm.show();
                        }
                        if (position == 2) {
                            final Dialog dialogForm = new Dialog(ListActivity.this);
                            dialogForm.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialogForm.setContentView(R.layout.form_operation);
                            TextView TVTitle = (TextView) dialogForm.findViewById(R.id.TVTitle);
                            final EditText ETText = (EditText) dialogForm.findViewById(R.id.ETText);
                            Button BNSubmit = (Button) dialogForm.findViewById(R.id.BNSubmit);
                            if (TVTitle != null) {
                                TVTitle.setText("Edit");
                            }
                            if (ETText != null) {
                                ETText.setText(myArrayList.get(item_no).toString());
                            }
                            if (BNSubmit != null) {
                                BNSubmit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        myArrayList.set(item_no, ETText.getText().toString());
                                        myAdapter.notifyDataSetChanged();
                                        dialogForm.dismiss();
                                    }
                                });
                            }
                            dialogForm.show();
                        }
                        if (position == 3) {
                            myArrayList.remove(item_no);
                            myAdapter.notifyDataSetChanged();
                        }


                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Item "+Integer.toString(item_no)+" "+parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
