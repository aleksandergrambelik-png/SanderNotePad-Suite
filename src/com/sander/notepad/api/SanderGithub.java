package com.sander.notepad.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SanderGithub {

    public static class RepoInfo {
        public String name;
        public String description;
        public String url;
        
        public RepoInfo(String name, String description, String url) {
            this.name = name;
            this.description = description;
            this.url = url;
        }
    }

    public interface Callback {
        void onResult(List<RepoInfo> repos, String error);
    }

    public static void searchRepo(final String query, final Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Ambil 5 repo teratas
                    String urlStr = "https://api.github.com/search/repositories?q=" 
                            + URLEncoder.encode(query, "UTF-8") + "&per_page=5";
                    
                    URL url = new URL(urlStr);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                    conn.setRequestProperty("Accept", "application/vnd.github.v3+json");
                    conn.setConnectTimeout(15000);
                    conn.setReadTimeout(15000);

                    int responseCode = conn.getResponseCode();
                    if (responseCode == 403) {
                        callback.onResult(null, "Rate limit exceeded (60 req/jam). Coba lagi nanti.");
                        return;
                    }

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) sb.append(line);
                    reader.close();

                    String json = sb.toString();
                    List<RepoInfo> repos = new ArrayList<>();

                    // Parsing manual: cari "full_name", "description", "html_url"
                    int idx = 0;
                    while ((idx = json.indexOf("\"full_name\":\"", idx)) != -1) {
                        int start = idx + 13;
                        int end = json.indexOf("\"", start);
                        String name = json.substring(start, end);

                        // Cari deskripsi di sekitar sini
                        String desc = "";
                        int descIdx = json.indexOf("\"description\":", end);
                        if (descIdx != -1 && descIdx < end + 500) { // Pastiin masih di objek yang sama
                            int descStart = json.indexOf("\"", descIdx + 14);
                            if (descStart != -1) {
                                int descEnd = json.indexOf("\"", descStart + 1);
                                if (descEnd != -1) {
                                    desc = json.substring(descStart + 1, descEnd);
                                }
                            }
                        }

                        // Cari URL
                        String repoUrl = "";
                        int urlIdx = json.indexOf("\"html_url\":\"", end);
                        if (urlIdx != -1 && urlIdx < end + 1000) {
                            int urlStart = urlIdx + 12;
                            int urlEnd = json.indexOf("\"", urlStart);
                            if (urlEnd != -1) {
                                repoUrl = json.substring(urlStart, urlEnd);
                            }
                        }

                        repos.add(new RepoInfo(name, desc, repoUrl));
                        idx = end;
                        
                        if (repos.size() >= 5) break; // Maksimal 5
                    }

                    if (repos.isEmpty()) {
                        callback.onResult(null, "Repo tidak ditemukan.");
                    } else {
                        callback.onResult(repos, null);
                    }

                } catch (Exception e) {
                    callback.onResult(null, e.toString());
                }
            }
        }).start();
    }
}
