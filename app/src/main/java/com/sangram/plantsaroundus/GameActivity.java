package com.sangram.plantsaroundus;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Base64;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GameActivity extends Activity implements TextToSpeech.OnInitListener {
    private final List<MainActivity.Section> sections = QuestionBank.create();
    private final String[] heroNames = {"Spider-Man", "Hulk", "Thor", "Black Panther", "Captain America", "Iron Man"};
    private ScrollView scroll;
    private LinearLayout root;
    private TextToSpeech tts;
    private ToneGenerator tones;
    private Bitmap aaravBitmap;
    private String playerName = "";
    private int sectionIndex = 0;
    private int questionIndex = 0;
    private int score = 0;
    private int maxScore = 0;
    private int sectionStartScore = 0;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        tts = new TextToSpeech(this, this);
        tones = new ToneGenerator(AudioManager.STREAM_MUSIC, 28);
        aaravBitmap = decodeAarav();
        maxScore = calcMaxPoints();

        scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(Color.rgb(248, 250, 252));
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(16), dp(18), dp(16), dp(28));
        scroll.addView(root);
        setContentView(scroll);
        showWelcome();
    }

    private Bitmap decodeAarav() {
        try {
            byte[] data = Base64.decode(AaravImage.BASE64, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(data, 0, data.length);
        } catch (Exception e) {
            return null;
        }
    }

    private void showWelcome() {
        root.removeAllViews();
        LinearLayout hero = card(Color.rgb(236, 253, 245));
        TextView title = title("🌱 Plants Around Us");
        title.setGravity(Gravity.CENTER);
        hero.addView(title);
        TextView sub = body("Fun Learning Game • Class 2");
        sub.setGravity(Gravity.CENTER);
        hero.addView(sub);
        hero.addView(photoView(dp(190), dp(190)));
        TextView buddy = body("Game buddy: Aarav 🌿");
        buddy.setGravity(Gravity.CENTER);
        buddy.setTypeface(buddy.getTypeface(), 1);
        hero.addView(buddy);
        root.addView(hero);

        LinearLayout login = card(Color.WHITE);
        login.addView(sectionHeader("👋 Welcome, new player!"));
        login.addView(body("Enter your name before starting the game."));
        EditText name = new EditText(this);
        name.setHint("Your name");
        name.setTextSize(19);
        name.setSingleLine(true);
        name.setImeOptions(EditorInfo.IME_ACTION_DONE);
        name.setPadding(dp(14), dp(12), dp(14), dp(12));
        name.setBackground(round(Color.rgb(248,250,252), Color.rgb(203,213,225), 16));
        LinearLayout.LayoutParams np = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        np.setMargins(0, dp(10), 0, dp(12));
        name.setLayoutParams(np);
        login.addView(name);
        Button start = primary3d("▶ Start My Plant Adventure");
        start.setOnClickListener(v -> {
            String n = name.getText().toString().trim();
            if (n.isEmpty()) {
                Toast.makeText(this, "Please enter your name.", Toast.LENGTH_SHORT).show();
                return;
            }
            playerName = n;
            score = 0;
            sectionIndex = 0;
            questionIndex = 0;
            sectionStartScore = 0;
            showSectionIntro();
        });
        login.addView(start);
        root.addView(login);
    }

    private ImageView photoView(int width, int height) {
        ImageView photo = new ImageView(this);
        LinearLayout.LayoutParams pp = new LinearLayout.LayoutParams(width, height);
        pp.gravity = Gravity.CENTER;
        pp.setMargins(0, dp(14), 0, dp(10));
        photo.setLayoutParams(pp);
        photo.setPadding(dp(4), dp(4), dp(4), dp(4));
        photo.setBackground(round(Color.WHITE, Color.rgb(34,197,94), 24));
        photo.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (aaravBitmap != null) photo.setImageBitmap(aaravBitmap);
        else photo.setImageResource(android.R.drawable.ic_menu_gallery);
        return photo;
    }

    private void showSectionIntro() {
        root.removeAllViews();
        MainActivity.Section sec = sections.get(sectionIndex);
        root.addView(topBar(sec.emoji + " " + sec.title));

        LinearLayout c = card(Color.WHITE);
        c.addView(title(sec.emoji + " " + sec.title));
        c.addView(body(sec.subtitle));
        c.addView(body("Section " + (sectionIndex + 1) + " of " + sections.size()));
        c.addView(body("Maximum points: " + sectionPoints(sec) + " points"));
        TextView heroTip = body("Score 80% or more to unlock: " + heroNames[sectionIndex] + " 🦸");
        heroTip.setTypeface(heroTip.getTypeface(), 1);
        c.addView(heroTip);
        root.addView(c);

        Button profile = soft3d("👤 View Player Profile");
        profile.setOnClickListener(v -> showProfileDialog());
        root.addView(profile);

        Button b = primary3d("Start Section ➜");
        b.setOnClickListener(v -> {
            questionIndex = 0;
            sectionStartScore = score;
            showCurrent();
        });
        root.addView(b);
    }

    private void showProfileDialog() {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(20), dp(10), dp(20), dp(10));
        box.addView(photoView(dp(150), dp(150)));
        TextView p = body("Player: " + playerName + "\nCurrent score: " + score + " / " + maxScore + "\nPlant-game buddy: Aarav");
        p.setGravity(Gravity.CENTER);
        box.addView(p);
        new AlertDialog.Builder(this)
                .setTitle("🌟 Player Profile")
                .setView(box)
                .setPositiveButton("Close", null)
                .show();
    }

    private void showCurrent() {
        MainActivity.Section sec = sections.get(sectionIndex);
        if (!sec.matchQuestions.isEmpty()) {
            if (questionIndex < sec.matchQuestions.size()) showMatch(sec, sec.matchQuestions.get(questionIndex));
            else showSectionDone();
        } else {
            if (questionIndex < sec.questions.size()) showQuestion(sec, sec.questions.get(questionIndex));
            else showSectionDone();
        }
    }

    private void showQuestion(MainActivity.Section sec, MainActivity.Q q) {
        root.removeAllViews();
        root.addView(topBar(sec.emoji + " " + sec.title));
        root.addView(progress(questionIndex + 1, sec.questions.size()));

        LinearLayout c = card(Color.WHITE);
        if (sectionIndex == 5) {
            PlantGraphic g = new PlantGraphic(q.q);
            c.addView(g, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(180)));
        }
        c.addView(title(q.q));
        TextView pointLabel = body("⭐ Correct answer = +10 points");
        pointLabel.setGravity(Gravity.CENTER);
        pointLabel.setTypeface(pointLabel.getTypeface(), 1);
        c.addView(pointLabel);

        Button speak = soft3d("🔊 Read Question (Indian English)");
        speak.setOnClickListener(v -> speak(q.q));
        c.addView(speak);

        TextView feedback = body("");
        feedback.setGravity(Gravity.CENTER);
        feedback.setVisibility(View.GONE);
        Button next = primary3d("Next ➜");
        next.setVisibility(View.GONE);
        List<Button> buttons = new ArrayList<>();

        for (int i = 0; i < q.a.length; i++) {
            final int idx = i;
            Button b = option3d(((char) ('A' + i)) + ". " + q.a[i]);
            buttons.add(b);
            b.setOnClickListener(v -> {
                for (Button x : buttons) x.setEnabled(false);
                if (idx == q.correct) {
                    score += 10;
                    playCorrect();
                    b.setBackground(round(Color.rgb(220,252,231), Color.rgb(34,197,94), 16));
                    feedback.setText("✅ Correct!  +10 points\n" + q.explain);
                } else {
                    playWrong();
                    b.setBackground(round(Color.rgb(254,226,226), Color.rgb(239,68,68), 16));
                    feedback.setText("🌼 Nice try!  +0 points\nCorrect answer: " + ((char) ('A' + q.correct)) + ". " + q.a[q.correct] + "\n" + q.explain);
                }
                feedback.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);
            });
            c.addView(b);
        }

        next.setOnClickListener(v -> {
            questionIndex++;
            showCurrent();
        });
        c.addView(feedback);
        c.addView(next);
        root.addView(c);
    }

    private void showMatch(MainActivity.Section sec, MainActivity.MatchQ mq) {
        root.removeAllViews();
        root.addView(topBar(sec.emoji + " " + sec.title));
        root.addView(progress(questionIndex + 1, sec.matchQuestions.size()));

        LinearLayout c = card(Color.WHITE);
        c.addView(title(mq.prompt));
        c.addView(body("Tap an item on the left, then tap its match on the right. Each correct pair gives +10 points."));
        MatchBoard board = new MatchBoard(mq);
        c.addView(board, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(360)));

        TextView fb = body("");
        fb.setVisibility(View.GONE);
        fb.setGravity(Gravity.CENTER);
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        Button check = primary3d("✓ Check");
        Button reset = soft3d("↻ Reset");
        LinearLayout.LayoutParams half = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        half.setMargins(dp(4), dp(6), dp(4), dp(6));
        check.setLayoutParams(half);
        reset.setLayoutParams(half);
        row.addView(check);
        row.addView(reset);
        c.addView(row);

        Button next = primary3d("Next ➜");
        next.setVisibility(View.GONE);
        c.addView(fb);
        c.addView(next);

        reset.setOnClickListener(v -> showMatch(sec, mq));
        check.setOnClickListener(v -> {
            int correct = board.lockAndScore();
            int gained = correct * 10;
            score += gained;
            if (correct == mq.left.length) playCorrect();
            else playWrong();
            fb.setText("✅ Correct pairs: " + correct + " / " + mq.left.length + "\n⭐ Points earned: +" + gained);
            fb.setVisibility(View.VISIBLE);
            row.setVisibility(View.GONE);
            next.setVisibility(View.VISIBLE);
        });
        next.setOnClickListener(v -> {
            questionIndex++;
            showCurrent();
        });
        root.addView(c);
    }

    private void showSectionDone() {
        root.removeAllViews();
        MainActivity.Section sec = sections.get(sectionIndex);
        int possible = sectionPoints(sec);
        int earned = score - sectionStartScore;
        int pct = possible == 0 ? 0 : (int) Math.round(earned * 100.0 / possible);

        LinearLayout c = card(pct >= 80 ? Color.rgb(254,249,195) : Color.rgb(239,246,255));
        c.addView(title("🎉 " + sec.title + " Complete!"));
        c.addView(body(playerName + ", you earned " + earned + " / " + possible + " points in this section."));
        c.addView(body("Section score: " + pct + "%"));

        TextView hero = body(pct >= 80
                ? "🦸 Amazing! Now you are " + heroNames[sectionIndex] + "!"
                : "🌱 Good effort! Reach 80% next time to become " + heroNames[sectionIndex] + ".");
        hero.setTextSize(22);
        hero.setTypeface(hero.getTypeface(), 1);
        hero.setGravity(Gravity.CENTER);
        hero.setPadding(dp(10), dp(16), dp(10), dp(16));
        c.addView(hero);
        root.addView(c);

        Button b = primary3d(sectionIndex == sections.size() - 1 ? "See Final Result 🏆" : "Go to Next Section ➜");
        b.setOnClickListener(v -> {
            sectionIndex++;
            questionIndex = 0;
            if (sectionIndex < sections.size()) showSectionIntro();
            else showFinal();
        });
        root.addView(b);
    }

    private void showFinal() {
        root.removeAllViews();
        int pct = (int) Math.round(score * 100.0 / Math.max(1, maxScore));
        LinearLayout c = card(Color.rgb(238,242,255));
        c.addView(photoView(dp(140), dp(140)));
        c.addView(title("🏆 Plant Adventure Complete!"));
        TextView r = body(playerName + ", your final score is\n" + score + " / " + maxScore + " points (" + pct + "%).");
        r.setGravity(Gravity.CENTER);
        r.setTextSize(21);
        c.addView(r);
        c.addView(body(pct >= 85 ? "🌟 Superb! You are a Plant Superhero!" : pct >= 70 ? "🌿 Very good! You are a Plant Explorer!" : "🌱 Keep playing and grow your plant power!"));
        root.addView(c);
        Button again = primary3d("🔄 New Player / Play Again");
        again.setOnClickListener(v -> showWelcome());
        root.addView(again);
    }

    private void speak(String text) {
        if (tts == null) return;
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "plant_question");
    }

    private void playCorrect() {
        if (tones != null) tones.startTone(ToneGenerator.TONE_PROP_ACK, 150);
    }

    private void playWrong() {
        if (tones != null) tones.startTone(ToneGenerator.TONE_PROP_NACK, 170);
    }

    @Override public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS && tts != null) {
            Locale indianEnglish = new Locale("en", "IN");
            int result = tts.setLanguage(indianEnglish);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                tts.setLanguage(Locale.ENGLISH);
            }
            tts.setSpeechRate(0.88f);
            tts.setPitch(1.0f);
        }
    }

    @Override protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        if (tones != null) tones.release();
        super.onDestroy();
    }

    class MatchBoard extends View {
        final MainActivity.MatchQ mq;
        final Paint line = new Paint(Paint.ANTI_ALIAS_FLAG);
        final Paint text = new Paint(Paint.ANTI_ALIAS_FLAG);
        final Map<Integer, Integer> map = new HashMap<>();
        int selected = -1;
        boolean locked = false;

        MatchBoard(MainActivity.MatchQ q) {
            super(GameActivity.this);
            mq = q;
            line.setStrokeWidth(dp(3));
            text.setTextSize(dp(16));
            text.setColor(Color.rgb(15,23,42));
            setBackground(round(Color.rgb(248,250,252), Color.rgb(203,213,225), 18));
            setOnTouchListener((v, e) -> {
                if (locked || e.getAction() != MotionEvent.ACTION_DOWN) return true;
                float x = e.getX(), y = e.getY();
                int rows = Math.max(mq.left.length, mq.right.length);
                int row = Math.min(rows - 1, Math.max(0, (int) (y / (getHeight() / (float) rows))));
                if (x < getWidth() / 2f && row < mq.left.length) selected = row;
                else if (selected >= 0 && row < mq.right.length) {
                    map.put(selected, row);
                    selected = -1;
                }
                invalidate();
                return true;
            });
        }

        int lockAndScore() {
            locked = true;
            int c = 0;
            for (int i = 0; i < mq.left.length; i++) {
                Integer r = map.get(i);
                if (r != null && r == mq.correctMap[i]) c++;
            }
            invalidate();
            return c;
        }

        @Override protected void onDraw(Canvas c) {
            super.onDraw(c);
            float w = getWidth(), h = getHeight();
            int rows = Math.max(mq.left.length, mq.right.length);
            float rowH = h / rows;
            text.setTextAlign(Paint.Align.LEFT);
            for (int i = 0; i < mq.left.length; i++) {
                float y = i * rowH + rowH * .58f;
                text.setColor(selected == i ? Color.rgb(180,83,9) : Color.rgb(15,23,42));
                c.drawText(mq.left[i], dp(14), y, text);
            }
            text.setTextAlign(Paint.Align.RIGHT);
            text.setColor(Color.rgb(15,23,42));
            for (int i = 0; i < mq.right.length; i++) {
                float y = i * rowH + rowH * .58f;
                c.drawText(mq.right[i], w - dp(14), y, text);
            }
            for (Map.Entry<Integer, Integer> e : map.entrySet()) {
                boolean ok = e.getValue() == mq.correctMap[e.getKey()];
                line.setColor(locked ? (ok ? Color.rgb(34,197,94) : Color.rgb(239,68,68)) : Color.rgb(59,130,246));
                float y1 = e.getKey() * rowH + rowH * .5f;
                float y2 = e.getValue() * rowH + rowH * .5f;
                c.drawLine(w * .43f, y1, w * .57f, y2, line);
                c.drawCircle(w * .43f, y1, dp(4), line);
                c.drawCircle(w * .57f, y2, dp(4), line);
            }
        }
    }

    class PlantGraphic extends View {
        final String key;
        final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        PlantGraphic(String k) {
            super(GameActivity.this);
            key = k.toLowerCase(Locale.ENGLISH);
            setBackground(round(Color.rgb(240,253,244), Color.rgb(187,247,208), 20));
        }
        @Override protected void onDraw(Canvas c) {
            super.onDraw(c);
            float w = getWidth(), h = getHeight();
            if (key.contains("rose")) rose(c,w,h);
            else if (key.contains("mint")) herb(c,w,h);
            else if (key.contains("pumpkin") || key.contains("soil")) creeper(c,w,h);
            else if (key.contains("money") || key.contains("fence") || key.contains("grape")) climber(c,w,h);
            else if (key.contains("lotus") || key.contains("water")) lotus(c,w,h);
            else tree(c,w,h);
        }
        void ground(Canvas c,float w,float h){p.setStyle(Paint.Style.FILL);p.setColor(Color.rgb(146,97,58));c.drawRect(0,h*.82f,w,h,p);}
        void tree(Canvas c,float w,float h){ground(c,w,h);p.setColor(Color.rgb(120,72,44));c.drawRect(w*.46f,h*.4f,w*.56f,h*.83f,p);p.setColor(Color.rgb(34,139,70));c.drawCircle(w*.4f,h*.38f,w*.16f,p);c.drawCircle(w*.57f,h*.33f,w*.18f,p);c.drawCircle(w*.5f,h*.23f,w*.17f,p);}
        void rose(Canvas c,float w,float h){ground(c,w,h);p.setColor(Color.rgb(34,139,70));p.setStrokeWidth(dp(5));c.drawLine(w*.5f,h*.8f,w*.5f,h*.3f,p);p.setColor(Color.rgb(239,68,68));for(int i=0;i<6;i++){double a=i*Math.PI/3;c.drawCircle((float)(w*.5+Math.cos(a)*w*.06),(float)(h*.28+Math.sin(a)*h*.055),dp(16),p);}p.setColor(Color.rgb(250,204,21));c.drawCircle(w*.5f,h*.28f,dp(9),p);}
        void herb(Canvas c,float w,float h){ground(c,w,h);p.setColor(Color.rgb(34,139,70));p.setStrokeWidth(dp(4));for(int i=-2;i<=2;i++){float x=w*.5f+i*w*.06f;c.drawLine(w*.5f,h*.8f,x,h*(.4f+Math.abs(i)*.03f),p);c.drawOval(x-dp(16),h*.5f,x+dp(5),h*.57f,p);}}
        void creeper(Canvas c,float w,float h){ground(c,w,h);p.setColor(Color.rgb(34,139,70));p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(dp(5));Path path=new Path();path.moveTo(w*.12f,h*.7f);path.cubicTo(w*.3f,h*.52f,w*.5f,h*.8f,w*.82f,h*.58f);c.drawPath(path,p);p.setStyle(Paint.Style.FILL);p.setColor(Color.rgb(249,115,22));c.drawCircle(w*.66f,h*.68f,dp(28),p);}
        void climber(Canvas c,float w,float h){ground(c,w,h);p.setColor(Color.rgb(120,72,44));p.setStrokeWidth(dp(5));for(int i=0;i<4;i++)c.drawLine(w*(.35f+i*.1f),h*.18f,w*(.35f+i*.1f),h*.82f,p);p.setColor(Color.rgb(34,139,70));p.setStyle(Paint.Style.STROKE);Path path=new Path();path.moveTo(w*.28f,h*.8f);path.cubicTo(w*.55f,h*.66f,w*.28f,h*.46f,w*.6f,h*.22f);c.drawPath(path,p);p.setStyle(Paint.Style.FILL);}
        void lotus(Canvas c,float w,float h){p.setColor(Color.rgb(147,197,253));c.drawRect(0,h*.55f,w,h,p);p.setColor(Color.rgb(244,114,182));for(int i=0;i<8;i++){double a=i*Math.PI/4;c.drawCircle((float)(w*.55+Math.cos(a)*w*.07),(float)(h*.45+Math.sin(a)*h*.06),dp(17),p);}p.setColor(Color.rgb(250,204,21));c.drawCircle(w*.55f,h*.45f,dp(9),p);}
    }

    private int calcMaxPoints() {
        int t = 0;
        for (MainActivity.Section s : sections) {
            if (s.matchQuestions.isEmpty()) t += s.questions.size() * 10;
            else for (MainActivity.MatchQ m : s.matchQuestions) t += m.left.length * 10;
        }
        return t;
    }

    private int sectionPoints(MainActivity.Section s) {
        int t = 0;
        if (s.matchQuestions.isEmpty()) return s.questions.size() * 10;
        for (MainActivity.MatchQ m : s.matchQuestions) t += m.left.length * 10;
        return t;
    }

    private LinearLayout progress(int n, int total) {
        LinearLayout c = card(Color.WHITE);
        c.setOrientation(LinearLayout.HORIZONTAL);
        TextView a = body("Question " + n + " of " + total);
        a.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        TextView b = body("⭐ " + score + " pts");
        b.setGravity(Gravity.END);
        c.addView(a);
        c.addView(b);
        return c;
    }

    private TextView topBar(String txt) {
        TextView v = body(txt + "     ⭐ " + score + " pts");
        v.setTextSize(16);
        v.setTypeface(v.getTypeface(), 1);
        v.setPadding(dp(12), dp(10), dp(12), dp(10));
        v.setBackground(round(Color.rgb(226,232,240), Color.rgb(203,213,225), 14));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, dp(10));
        v.setLayoutParams(lp);
        v.setOnClickListener(x -> showProfileDialog());
        return v;
    }

    private TextView title(String text) {
        TextView v = new TextView(this);
        v.setText(text);
        v.setTextSize(24);
        v.setTextColor(Color.rgb(15,23,42));
        v.setTypeface(v.getTypeface(), 1);
        v.setPadding(dp(10), dp(12), dp(10), dp(12));
        return v;
    }

    private TextView sectionHeader(String text) {
        TextView v = title(text);
        v.setTextSize(21);
        return v;
    }

    private TextView body(String text) {
        TextView v = new TextView(this);
        v.setText(text);
        v.setTextSize(17);
        v.setTextColor(Color.rgb(51,65,85));
        v.setPadding(dp(10), dp(7), dp(10), dp(7));
        return v;
    }

    private LinearLayout card(int color) {
        LinearLayout c = new LinearLayout(this);
        c.setOrientation(LinearLayout.VERTICAL);
        c.setPadding(dp(14), dp(14), dp(14), dp(14));
        c.setBackground(round(color, Color.rgb(226,232,240), 22));
        c.setElevation(dp(2));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, dp(6), 0, dp(12));
        c.setLayoutParams(lp);
        return c;
    }

    private Button option3d(String text) {
        Button b = base3d(text, Color.WHITE, Color.rgb(148,163,184), Color.rgb(241,245,249));
        b.setTextColor(Color.rgb(15,23,42));
        b.setTextSize(18);
        b.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        return b;
    }

    private Button primary3d(String text) {
        Button b = base3d(text, Color.rgb(34,197,94), Color.rgb(21,128,61), Color.rgb(22,163,74));
        b.setTextColor(Color.WHITE);
        b.setTypeface(b.getTypeface(), 1);
        return b;
    }

    private Button soft3d(String text) {
        Button b = base3d(text, Color.rgb(239,246,255), Color.rgb(147,197,253), Color.rgb(219,234,254));
        b.setTextColor(Color.rgb(30,64,175));
        return b;
    }

    private Button base3d(String text, int normalColor, int borderColor, int pressedColor) {
        Button b = new Button(this);
        b.setText(text);
        b.setTextSize(17);
        b.setAllCaps(false);
        b.setPadding(dp(14), dp(13), dp(14), dp(13));
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed}, round(pressedColor, borderColor, 16));
        states.addState(new int[]{}, round(normalColor, borderColor, 16));
        b.setBackground(states);
        b.setElevation(dp(6));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(dp(2), dp(7), dp(2), dp(7));
        b.setLayoutParams(lp);
        b.setOnTouchListener((v, e) -> {
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                v.setTranslationY(dp(2));
                v.setElevation(dp(2));
            } else if (e.getAction() == MotionEvent.ACTION_UP || e.getAction() == MotionEvent.ACTION_CANCEL) {
                v.setTranslationY(0);
                v.setElevation(dp(6));
            }
            return false;
        });
        return b;
    }

    private GradientDrawable round(int fill, int stroke, int radiusDp) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(fill);
        g.setCornerRadius(dp(radiusDp));
        g.setStroke(dp(1), stroke);
        return g;
    }

    private int dp(int v) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, getResources().getDisplayMetrics());
    }
}
