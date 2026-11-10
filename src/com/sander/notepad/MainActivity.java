package com.sander.notepad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;
import android.graphics.Typeface;

import com.sander.notepad.ui.SanderUI;
import com.sander.notepad.game.SanderTetris;
import com.sander.notepad.core.SanderSearch;
import com.sander.notepad.core.SanderSubdomain;
import com.sander.notepad.core.SanderNote;
import com.sander.notepad.api.SanderGithub;
import com.sander.notepad.security.SanderKeyLoader;
import com.sander.notepad.security.SanderSafety;

import java.util.List;
import java.util.Set;

public class MainActivity extends Activity {

    boolean isEnglish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!SanderSafety.verifySignature(this)) {
            finish();
            return;
        }

        if (!SanderKeyLoader.isPinSet(this)) {
            showSetupPinDialog();
        } else {
            showUnlockPinDialog();
        }
    }

    private void showSetupPinDialog() {
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        input.setHint("Bikin PIN 4 Angka");
        input.setTextColor(0xFF00FF00);
        input.setBackgroundColor(0xFF111111);

        new AlertDialog.Builder(this)
            .setTitle("BUAT PIN BARU")
            .setView(input)
            .setCancelable(false)
            .setPositiveButton("SIMPAN", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String pin = input.getText().toString();
                    if (pin.length() == 4) {
                        SanderKeyLoader.savePin(MainActivity.this, pin);
                        showHomeScreen();
                    } else {
                        Toast.makeText(MainActivity.this, "Harus 4 angka!", Toast.LENGTH_SHORT).show();
                        showSetupPinDialog();
                    }
                }
            }).show();
    }

    private void showUnlockPinDialog() {
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        input.setHint("Masukkan PIN");
        input.setTextColor(0xFF00FF00);
        input.setBackgroundColor(0xFF111111);

        new AlertDialog.Builder(this)
            .setTitle("SANDER LOCK")
            .setView(input)
            .setCancelable(false)
            .setPositiveButton("BUKA", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (SanderKeyLoader.checkPin(MainActivity.this, input.getText().toString())) {
                        showHomeScreen();
                    } else {
                        finish();
                    }
                }
            }).show();
    }

    private void showHomeScreen() {
        SanderUI ui = new SanderUI(this);
        setContentView(ui);

ui.btnEmail.setOnClickListener(new View.OnClickListener() {
    @Override public void onClick(View v) { showOsintScreen("EMAIL RECON", "Email...", "sander_email", 1); }
});
ui.btnDNS.setOnClickListener(new View.OnClickListener() {
    @Override public void onClick(View v) { showOsintScreen("DNS LOOKUP", "Domain...", "sander_dns", 2); }
});
ui.btnWhois.setOnClickListener(new View.OnClickListener() {
    @Override public void onClick(View v) { showOsintScreen("WHOIS LOOKUP", "Domain...", "sander_whois", 3); }
});
ui.btnIP.setOnClickListener(new View.OnClickListener() {
    @Override public void onClick(View v) { showOsintScreen("IP GEOLOCATION", "IP Address...", "sander_ip", 4); }
});
ui.btnPhone.setOnClickListener(new View.OnClickListener() {
    @Override public void onClick(View v) { showOsintScreen("PHONE INFO", "Nomor HP...", "sander_phone", 5); }
});
        ui.btnWiki.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { showWikiScreen(); }
        });
        ui.btnSubdomain.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { showSubdomainScreen(); }
        });
        ui.btnGithub.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { showGithubScreen(); }
        });
        ui.btnNotepad.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { showNotepadScreen(); }
        });
        ui.btnTetris.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { showTetrisScreen(); }
        });
        ui.btnSafety.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { showSafetyDialog(); }
        });
        ui.btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { showHelpDialog(); }
        });
        ui.btnLangToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEnglish = !isEnglish;
                Toast.makeText(MainActivity.this, isEnglish ? "EN" : "ID", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showWikiScreen() {
        LinearLayout layout = createBaseLayout();
        final TextView res = createOutputView();

        final EditText input = new EditText(this);
        input.setHint("Cari Wikipedia...");
        input.setTextColor(Color.CYAN);
        input.setBackgroundColor(0xFF222222);
        layout.addView(input);

        Button btnSearch = new Button(this);
        btnSearch.setText("SEARCH");
        layout.addView(btnSearch);
        layout.addView(createBackButton());
        layout.addView(res, new LinearLayout.LayoutParams(-1, 0, 1f));
        setContentView(layout);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                res.setText("Searching...");
                SanderSearch.execute(input.getText().toString(), isEnglish, new SanderSearch.Callback() {
                    @Override
                    public void onResult(String t, String e, String l, String err) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                res.setText(err != null ? err : e + "\n\nLink: " + l);
                            }
                        });
                    }
                });
            }
        });
    }

    private void showSubdomainScreen() {
        LinearLayout layout = createBaseLayout();
        final TextView res = createOutputView();
        final EditText input = new EditText(this);
        input.setHint("Domain (example.com)...");
        input.setTextColor(Color.YELLOW);
        input.setBackgroundColor(0xFF222222);
        layout.addView(input);

        Button btnScan = new Button(this);
        btnScan.setText("SCAN");
        layout.addView(btnScan);
        layout.addView(createBackButton());
        layout.addView(res, new LinearLayout.LayoutParams(-1, 0, 1f));
        setContentView(layout);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                res.setText("Scanning...");
                SanderSubdomain.execute(input.getText().toString(), new SanderSubdomain.Callback() {
                    @Override
                    public void onResult(Set<String> subs, String err) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (err != null) { res.setText(err); return; }
                                StringBuilder sb = new StringBuilder("FOUND ("+subs.size()+"):\n\n");
                                for (String s : subs) sb.append("- ").append(s).append("\n");
                                res.setText(sb.toString());
                            }
                        });
                    }
                });
            }
        });
    }

    private void showGithubScreen() {
        LinearLayout layout = createBaseLayout();
        final TextView res = createOutputView();
        final EditText input = new EditText(this);
        input.setHint("Repo / Keyword...");
        input.setTextColor(Color.MAGENTA);
        input.setBackgroundColor(0xFF222222);
        layout.addView(input);

        Button btnSearch = new Button(this);
        btnSearch.setText("SEARCH");
        layout.addView(btnSearch);
        layout.addView(createBackButton());
        layout.addView(res, new LinearLayout.LayoutParams(-1, 0, 1f));
        setContentView(layout);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                res.setText("Searching...");
                SanderGithub.searchRepo(input.getText().toString(), new SanderGithub.Callback() {
                    @Override
                    public void onResult(List<SanderGithub.RepoInfo> repos, String err) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (err != null) { res.setText(err); return; }
                                StringBuilder sb = new StringBuilder("GITHUB ("+repos.size()+"):\n\n");
                                for (SanderGithub.RepoInfo r : repos) {
                                    sb.append("📦 ").append(r.name).append("\n   ").append(r.description).append("\n   ").append(r.url).append("\n\n");
                                }
                                res.setText(sb.toString());
                            }
                        });
                    }
                });
            }
        });
    }

    private void showNotepadScreen() {
        LinearLayout layout = createBaseLayout();
        final EditText note = new EditText(this);
        note.setHint("Tulis catatan...");
note.setTextColor(Color.CYAN);
        note.setBackgroundColor(0xFF111111);
        note.setMinLines(10);
        note.setGravity(android.view.Gravity.TOP);
        note.setText(SanderNote.load(this));
        layout.addView(note, new LinearLayout.LayoutParams(-1, 0, 1f));

        LinearLayout btnRow = new LinearLayout(this);
        btnRow.setOrientation(LinearLayout.HORIZONTAL);
        Button save = new Button(this);
        save.setText("SAVE");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SanderNote.save(MainActivity.this, note.getText().toString());
                Toast.makeText(MainActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
            }
        });
        Button back = createBackButton();
        btnRow.addView(save, new LinearLayout.LayoutParams(0, -2, 1f));
        btnRow.addView(back, new LinearLayout.LayoutParams(0, -2, 1f));
        layout.addView(btnRow);
        setContentView(layout);
    }

    private void showTetrisScreen() {
        LinearLayout layout = createBaseLayout();
        final SanderTetris game = new SanderTetris(this);
        layout.addView(game, new LinearLayout.LayoutParams(-1, 0, 1f));

        LinearLayout controls = new LinearLayout(this);
        controls.setOrientation(LinearLayout.HORIZONTAL);
        
        Button left = new Button(this);
        left.setText("◀");
        left.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { game.moveLeft(); }
        });
        
        Button rotate = new Button(this);
        rotate.setText("↻");
        rotate.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { game.rotate(); }
        });
        
        Button right = new Button(this);
        right.setText("▶");
        right.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { game.moveRight(); }
        });
        
        controls.addView(left, new LinearLayout.LayoutParams(0, -2, 1f));
        controls.addView(rotate, new LinearLayout.LayoutParams(0, -2, 1f));
        controls.addView(right, new LinearLayout.LayoutParams(0, -2, 1f));
        
        layout.addView(controls);
        layout.addView(createBackButton());
        setContentView(layout);
        game.resume();
    }

private void showOsintScreen(String title, String hint, String type, int mode) {
    LinearLayout layout = createBaseLayout();
    final TextView res = createOutputView();
    final EditText input = new EditText(this);
    input.setHint(hint);
    input.setTextColor(Color.CYAN);
    input.setBackgroundColor(0xFF222222);
    layout.addView(input);
    
    Button btnRun = new Button(this);
    btnRun.setText("RUN");
    layout.addView(btnRun);
    layout.addView(createBackButton());
    layout.addView(res, new LinearLayout.LayoutParams(-1, 0, 1f));
    setContentView(layout);

    btnRun.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String target = input.getText().toString().trim();
            if (target.isEmpty()) return;
            res.setText("Scanning...");
            new Thread(new Runnable() {
                @Override
                public void run() {
String output = "";
if (mode == 1) output = com.sander.notepad.osint.SanderEmail.check(target);
else if (mode == 2) output = com.sander.notepad.osint.SanderDNS.lookup(target);
else if (mode == 3) output = com.sander.notepad.osint.SanderWhois.lookup(target);
else if (mode == 4) output = com.sander.notepad.osint.SanderIP.lookup(target);
else if (mode == 5) output = com.sander.notepad.osint.SanderPhone.lookup(target);

// FORMAT JSON-NYA
output = com.sander.notepad.osint.JsonFormatter.format(output);
                    final String finalOutput = output;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            res.setText(finalOutput);
                        }
                    });
                }
            }).start();
        }
    });
}

    private LinearLayout createBaseLayout() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(Color.BLACK);
        layout.setPadding(20, 40, 20, 20);
        return layout;
    }

    private TextView createOutputView() {
        TextView tv = new TextView(this);
        tv.setTextColor(Color.WHITE);
        tv.setTypeface(Typeface.MONOSPACE);
        tv.setPadding(0, 20, 0, 20);
        tv.setTextIsSelectable(true);
        return tv;
    }

    private Button createBackButton() {
        Button btn = new Button(this);
        btn.setText("[ BACK ]");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { showHomeScreen(); }
        });
        return btn;
    }

    private void showSafetyDialog() {
        new AlertDialog.Builder(this)
            .setTitle("SAFETY")
            .setMessage(SanderSafety.getSafetyInfo(this))
            .setPositiveButton("OK", null).show();
    }

    private void showHelpDialog() {
        new AlertDialog.Builder(this)
            .setTitle("HELP")
            .setMessage("SANDER SUITE v2.0\n\nPilih menu di Home.\nGunakan tombol [ BACK ] untuk kembali.")
            .setPositiveButton("OK", null).show();
    }
}
