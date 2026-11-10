package com.sander.notepad.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SanderUI extends LinearLayout {

    public Button btnWiki, btnSubdomain, btnGithub, btnNotepad, btnTetris, btnSafety, btnHelp, btnLangToggle;
    public Button btnEmail, btnDNS, btnWhois, btnIP, btnPhone;

    public SanderUI(Context context) {
        super(context);
        setOrientation(VERTICAL);
        setBackgroundColor(Color.BLACK);
        setPadding(20, 40, 20, 20); 

        TextView title = new TextView(context);
        title.setText("SANDER SUITE");
        title.setTextColor(Color.CYAN);
        title.setTextSize(24);
        title.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 0, 0, 40);
        addView(title);

        // BARIS 1
        LinearLayout row1 = new LinearLayout(context);
        row1.setOrientation(HORIZONTAL);
        btnWiki = createStyledButton(context, "WIKIPEDIA", Color.CYAN);
        btnSubdomain = createStyledButton(context, "SUBDOMAIN", Color.YELLOW);
        row1.addView(btnWiki, new LayoutParams(0, -2, 1f));
        row1.addView(btnSubdomain, new LayoutParams(0, -2, 1f));
        addView(row1);

        // BARIS 2
        LinearLayout row2 = new LinearLayout(context);
        row2.setOrientation(HORIZONTAL);
        btnGithub = createStyledButton(context, "GITHUB", Color.MAGENTA);
        btnNotepad = createStyledButton(context, "NOTEPAD", Color.GREEN);
        row2.addView(btnGithub, new LayoutParams(0, -2, 1f));
        row2.addView(btnNotepad, new LayoutParams(0, -2, 1f));
        addView(row2);

        // BARIS 3
        LinearLayout row3 = new LinearLayout(context);
        row3.setOrientation(HORIZONTAL);
        btnTetris = createStyledButton(context, "TETRIS", Color.RED);
        btnSafety = createStyledButton(context, "SAFETY", Color.LTGRAY);
        row3.addView(btnTetris, new LayoutParams(0, -2, 1f));
        row3.addView(btnSafety, new LayoutParams(0, -2, 1f));
        addView(row3);

        // BARIS 4
        LinearLayout row4 = new LinearLayout(context);
        row4.setOrientation(HORIZONTAL);
        btnHelp = createStyledButton(context, "HELP", Color.WHITE);
        btnLangToggle = createStyledButton(context, "ID/EN", Color.LTGRAY);
        row4.addView(btnHelp, new LayoutParams(0, -2, 1f));
        row4.addView(btnLangToggle, new LayoutParams(0, -2, 1f));
        addView(row4);

        // BARIS 5: OSINT
        LinearLayout row5 = new LinearLayout(context);
        row5.setOrientation(HORIZONTAL);
        btnEmail = createStyledButton(context, "EMAIL", Color.CYAN);
        btnDNS = createStyledButton(context, "DNS", Color.YELLOW);
        btnWhois = createStyledButton(context, "WHOIS", Color.MAGENTA);
        row5.addView(btnEmail, new LayoutParams(0, -2, 1f));
        row5.addView(btnDNS, new LayoutParams(0, -2, 1f));
        row5.addView(btnWhois, new LayoutParams(0, -2, 1f));
        addView(row5);

        // BARIS 6: OSINT
        LinearLayout row6 = new LinearLayout(context);
        row6.setOrientation(HORIZONTAL);
        btnIP = createStyledButton(context, "IP GEO", Color.GREEN);
        btnPhone = createStyledButton(context, "PHONE", Color.RED);
        row6.addView(btnIP, new LayoutParams(0, -2, 1f));
        row6.addView(btnPhone, new LayoutParams(0, -2, 1f));
        addView(row6);
    }

    private Button createStyledButton(Context context, String text, int textColor) {
        Button btn = new Button(context);
        btn.setText(text);
        btn.setTextColor(textColor);
        btn.setTextSize(11);
        btn.setTypeface(Typeface.MONOSPACE);
        btn.setAllCaps(false);
        btn.setPadding(0, 30, 0, 30);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(15f);
        drawable.setColor(Color.parseColor("#151515"));
        drawable.setStroke(3, textColor);
        btn.setBackground(drawable);
        return btn;
    }
}
