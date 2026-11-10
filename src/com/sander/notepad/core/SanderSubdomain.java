package com.sander.notepad.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SanderSubdomain {

    public interface Callback {
        void onResult(Set<String> subdomains, String error);
    }

    public static void execute(final String domain, final Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Set<String> results = new HashSet<>();
                    
                    // 1. COBA CRTSH DULU (Timeout 30 Detik)
                    results = fetchCrtSh(domain);
                    
                    // 2. KALAU CRTSH KOSONG, FALLBACK KE HACKERTARGET
                    if (results.isEmpty()) {
                        results = fetchHackerTarget(domain);
                    }

                    if (results.isEmpty()) {
                        callback.onResult(null, "Tidak ada subdomain ditemukan (semua API gagal/timeout).");
                    } else {
                        callback.onResult(results, null);
                    }

                } catch (Exception e) {
                    callback.onResult(null, e.toString());
                }
            }
        }).start();
    }

    private static Set<String> fetchCrtSh(String domain) {
        Set<String> subs = new HashSet<>();
        try {
            String urlStr = "https://crt.sh/?q=%25." + URLEncoder.encode(domain, "UTF-8") + "&output=json";
            String json = fetch(urlStr, 30000); // 30 Detik Timeout
            
            int idx = 0;
            while ((idx = json.indexOf("\"name_value\":\"", idx)) != -1) {
                int start = idx + 14;
                int end = json.indexOf("\"", start);
                if (end != -1) {
                    String rawValue = json.substring(start, end);
                    String[] parts = rawValue.split("\\\\n");
                    for (String part : parts) {
                        String sub = part.trim().toLowerCase();
                        if (sub.startsWith("*.")) sub = sub.substring(2);
                        if (!sub.isEmpty() && !sub.equals(domain)) subs.add(sub);
                    }
                    idx = end;
                } else break;
            }
        } catch (Exception e) {
            // Gagal, biarin kosong
        }
        return subs;
    }

    private static Set<String> fetchHackerTarget(String domain) {
        Set<String> subs = new HashSet<>();
        try {
            String urlStr = "https://api.hackertarget.com/hostsearch/?q=" + URLEncoder.encode(domain, "UTF-8");
            String text = fetch(urlStr, 10000); // 10 Detik Timeout
            
            if (text != null && !text.contains("error")) {
                String[] lines = text.split("\n");
                for (String line : lines) {
                    String[] parts = line.split(",");
                    if (parts.length > 0) {
                        String sub = parts[0].trim().toLowerCase();
                        if (!sub.isEmpty() && !sub.equals(domain)) subs.add(sub);
                    }
                }
            }
        } catch (Exception e) {
            // Gagal, biarin kosong
        }
        return subs;
    }

    private static String fetch(String urlStr, int timeout) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/120.0.0.0");
        conn.setConnectTimeout(timeout);
        conn.setReadTimeout(timeout);

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) sb.append(line).append("\n");
        reader.close();
        return sb.toString();
    }
}
