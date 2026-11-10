package com.sander.notepad.core;

import android.content.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class SanderNote {

    private static final String FILE_NAME = "sander_notes.txt";

    public static boolean save(Context context, String text) {
        try {
            File file = new File(context.getFilesDir(), FILE_NAME);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(text.getBytes());
            fos.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String load(Context context) {
        try {
            File file = new File(context.getFilesDir(), FILE_NAME);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                byte[] buffer = new byte[(int) file.length()];
                fis.read(buffer);
                fis.close();
                return new String(buffer);
            }
        } catch (Exception e) {}
        return "";
    }
}
