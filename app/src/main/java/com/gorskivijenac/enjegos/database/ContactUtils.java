package com.gorskivijenac.enjegos.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

public class ContactUtils {
    @SuppressLint("Range")
    public static List<Contact> getContacts(Context context) {
        List<Contact> contacts = new ArrayList<>();

        String[] projection = new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME};

        Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                projection, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String phoneNumber = "";

                Cursor phoneCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{String.valueOf(id)}, null);

                if (phoneCursor != null && phoneCursor.getCount() > 0) {
                    phoneCursor.moveToFirst();
                    phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    phoneCursor.close();
                }

                Contact contact = new Contact(id, name, phoneNumber);
                contacts.add(contact);
            }
            cursor.close();
        }

        return contacts;
    }


}
