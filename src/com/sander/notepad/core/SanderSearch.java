package com.sander.notepad.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SanderSearch {

    public interface Callback {
        void onResult(String title, String extract, String link, String error);
    }

    public static void execute(final String query, final boolean isEnglish, final Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String lang = isEnglish ? "en" : "id";
                    
                    // 1. Cari judul
                    String searchUrl = "https://" + lang + ".wikipedia.org/w/api.php?action=query&list=search&srsearch=" 
                            + URLEncoder.encode(query, "UTF-8") + "&format=json&utf8=1";
                    String searchJson = fetch(searchUrl);

                    String title = "";
                    int titleIdx = searchJson.indexOf("\"title\":\"");
                    if (titleIdx != -1) {
                        int start = titleIdx + 9;
                        int end = searchJson.indexOf("\"", start);
                        if (end != -1) title = searchJson.substring(start, end);
                    }

                    if (title.isEmpty()) {
                        callback.onResult(null, null, null, "Artikel tidak ditemukan / Article not found.");
                        return;
                    }

                    // 2. Ambil deskripsi panjang
                    String summaryUrl = "https://" + lang + ".wikipedia.org/api/rest_v1/page/summary/" 
                            + URLEncoder.encode(title.replace(" ", "_"), "UTF-8");
                    String summaryJson = fetch(summaryUrl);

                    String extract = "";
                    int extractIdx = summaryJson.indexOf("\"extract\":\"");
                    if (extractIdx != -1) {
                        int start = extractIdx + 11;
                        int end = summaryJson.indexOf("\",", start);
                        if (end != -1) {
                            extract = summaryJson.substring(start, end);
                            extract = extract.replace("\\n", "\n").replace("\\\"", "\"");
                        }
                    }

                    if (extract.isEmpty()) extract = "Tidak ada deskripsi.";

                    String link = "https://" + lang + ".wikipedia.org/wiki/" + title.replace(" ", "_");

                    callback.onResult(title, extract, link, null);

                } catch (Exception e) {
                    callback.onResult(null, null, null, e.toString());
                }
            }
        }).start();
    }

    private static String fetch(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/120.0.0.0");
        conn.setConnectTimeout(15000);
        conn.setReadTimeout(15000);

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) sb.append(line).append("\n");
        reader.close();
        return sb.toString();
    }
}
