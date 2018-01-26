package hk.edu.polyu.eie.eie3109.todolist;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.Collections;

public class PhoneActivity extends AppCompatActivity {
    private ListView myPhoneList;
    private SimpleCursorAdapter myCursorAdapter;
    private String[] nameList = new String[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        Button BNBack2 = (Button) findViewById(R.id.BNBack2);
        myPhoneList = (ListView) findViewById(R.id.LVPhoneList);
        showContacts();

        if (BNBack2 != null) {
            BNBack2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(PhoneActivity.this, MainActivity.class));
                    PhoneActivity.this.finish();
                }
            });
        }

        myPhoneList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = nameList[position];
                String phone = "";
                String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" like'%" + name +"%'";
                Cursor c = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER}, selection, null, null);
                if (c.moveToFirst()) {
                    phone = c.getString(0);
                }
                c.close();
//                try {
//                    while (contactLookupCursor.moveToNext()) {
////                        contactName = contactLookupCursor.getString(contactLookupCursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME));
//                        Toast.makeText(getApplicationContext(), contactLookupCursor.getString(contactLookupCursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup.NUMBER)), Toast.LENGTH_SHORT).show();
////                        contactNumber = contactLookupCursor.getString(contactLookupCursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup.NUMBER));
//                    }
//                } finally {
//                    contactLookupCursor.close();
//                }
                Toast.makeText(getApplicationContext(), phone, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.READ_CONTACTS}, 100);
        } else {
            final ContentResolver cr = getContentResolver();
            Cursor c = cr.query(ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME}, null, null, null);
            Cursor names = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME}, null, null, null);
            int index = 0;
            while (names.moveToNext()) {
                nameList[index] = names.getString(names.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                index++;
            }
//            while (c.moveToNext()) {
//                c.getString(0);
//            }
            myCursorAdapter = new SimpleCursorAdapter(this, R.layout.list_item, c, new String[]{ContactsContract.Contacts.DISPLAY_NAME}, new int[]{R.id.TVRow}, 0);
            myPhoneList.setAdapter(myCursorAdapter);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showContacts();
            } else {
                Toast.makeText(this, "Until you grant the permission, we cannot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
