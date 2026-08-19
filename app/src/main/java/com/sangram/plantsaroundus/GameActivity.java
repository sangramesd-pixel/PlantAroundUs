package com.sangram.plantsaroundus;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GameActivity extends Activity implements TextToSpeech.OnInitListener {
    private final String[] heroNames = {"Spider-Man", "Hulk", "Thor", "Black Panther", "Captain America", "Iron Man"};
    private final List<MainActivity.Section> sections = new ArrayList<>();
    private final List<Integer> sectionPercentages = new ArrayList<>();
    private ScrollView scroll;
    private LinearLayout root;
    private TextToSpeech tts;
    private ToneGenerator tones;
    private SharedPreferences prefs;
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
        prefs = getSharedPreferences("evs_dashboard", MODE_PRIVATE);

        scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(Color.rgb(248,250,252));
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(16), dp(18), dp(16), dp(28));
        scroll.addView(root);
        setContentView(scroll);
        showWelcome();
    }

    private void showWelcome() {
        root.removeAllViews();
        LinearLayout hero = card(Color.rgb(236,253,245));
        TextView t = title("🌱 Aarav's EVS Adventure");
        t.setGravity(Gravity.CENTER);
        hero.addView(t);
        TextView sub = body("Food & Healthy Eating + Plants Around Us • Class 2");
        sub.setGravity(Gravity.CENTER);
        hero.addView(sub);
        hero.addView(aaravPhoto(dp(200), dp(200)));
        TextView bank = body("🎯 Large offline question bank: " + QuestionBank.totalQuestionUnits() + "+ practice items");
        bank.setGravity(Gravity.CENTER);
        bank.setTypeface(bank.getTypeface(), 1);
        hero.addView(bank);
        root.addView(hero);

        LinearLayout login = card(Color.WHITE);
        login.addView(sectionHeader("👋 New Player"));
        login.addView(body("Enter a player name. Every new game gets a fresh randomized question sequence."));
        EditText name = new EditText(this);
        name.setHint("Player name");
        name.setTextSize(19);
        name.setSingleLine(true);
        name.setImeOptions(EditorInfo.IME_ACTION_DONE);
        name.setPadding(dp(14),dp(12),dp(14),dp(12));
        name.setBackground(round(Color.rgb(248,250,252),Color.rgb(203,213,225),16));
        LinearLayout.LayoutParams np = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        np.setMargins(0,dp(10),0,dp(12));
        name.setLayoutParams(np);
        login.addView(name);

        Button start = primary3d("▶ Start Randomized Game");
        start.setOnClickListener(v -> {
            String n = cleanName(name.getText().toString());
            if (n.length() == 0) {
                Toast.makeText(this,"Please enter a player name.",Toast.LENGTH_SHORT).show();
                return;
            }
            startNewSession(n);
        });
        login.addView(start);

        Button dash = soft3d("📊 Open Score Dashboard");
        dash.setOnClickListener(v -> showDashboard());
        login.addView(dash);
        root.addView(login);
    }

    private void startNewSession(String name) {
        playerName = name;
        sections.clear();
        sections.addAll(QuestionBank.createSession());
        sectionPercentages.clear();
        score = 0;
        sectionIndex = 0;
        questionIndex = 0;
        sectionStartScore = 0;
        maxScore = calcMaxPoints();
        showSectionIntro();
    }

    private ImageView aaravPhoto(int width, int height) {
        ImageView img = new ImageView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width,height);
        lp.gravity = Gravity.CENTER;
        lp.setMargins(0,dp(12),0,dp(12));
        img.setLayoutParams(lp);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img.setPadding(dp(4),dp(4),dp(4),dp(4));
        img.setBackground(round(Color.WHITE,Color.rgb(34,197,94),22));
        img.setImageResource(R.drawable.aarav_photo);
        return img;
    }

    private void showSectionIntro() {
        root.removeAllViews();
        MainActivity.Section sec = sections.get(sectionIndex);
        root.addView(topBar(sec.emoji + " " + sec.title));
        LinearLayout c = card(Color.WHITE);
        c.addView(title(sec.emoji + " " + sec.title));
        c.addView(body(sec.subtitle));
        c.addView(body("Section " + (sectionIndex + 1) + " of " + sections.size()));
        c.addView(body("Maximum section points: " + sectionPoints(sec)));
        TextView reward = body("🦸 Score 80% or more to unlock: " + heroNames[sectionIndex]);
        reward.setTypeface(reward.getTypeface(),1);
        reward.setGravity(Gravity.CENTER);
        c.addView(reward);
        root.addView(c);
        Button profile = soft3d("👤 Player Profile / Dashboard");
        profile.setOnClickListener(v -> showProfileDialog());
        root.addView(profile);
        Button start = primary3d("Start Section ➜");
        start.setOnClickListener(v -> {
            questionIndex = 0;
            sectionStartScore = score;
            showCurrent();
        });
        root.addView(start);
    }

    private void showProfileDialog() {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(18),dp(8),dp(18),dp(8));
        box.addView(aaravPhoto(dp(145),dp(145)));
        TextView p = body("Player: " + playerName + "\nCurrent score: " + score + " / " + maxScore + "\nQuestions are randomized for this session.");
        p.setGravity(Gravity.CENTER);
        box.addView(p);
        new AlertDialog.Builder(this)
                .setTitle("🌟 Player Profile")
                .setView(box)
                .setPositiveButton("Dashboard",(d,w)->showDashboard())
                .setNegativeButton("Close",null)
                .show();
    }

    private void showCurrent() {
        MainActivity.Section sec = sections.get(sectionIndex);
        if (!sec.matchQuestions.isEmpty()) {
            if (questionIndex < sec.matchQuestions.size()) showMatch(sec,sec.matchQuestions.get(questionIndex));
            else showSectionDone();
        } else {
            if (questionIndex < sec.questions.size()) showQuestion(sec,sec.questions.get(questionIndex));
            else showSectionDone();
        }
    }

    private void showQuestion(MainActivity.Section sec, MainActivity.Q q) {
        root.removeAllViews();
        root.addView(topBar(sec.emoji + " " + sec.title));
        root.addView(progress(questionIndex + 1,sec.questions.size()));
        LinearLayout c = card(Color.WHITE);
        if (sectionIndex == 5) c.addView(new TopicGraphic(q.q),new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,dp(175)));
        c.addView(title(q.q));
        TextView point = body("⭐ Correct answer = +10 points");
        point.setGravity(Gravity.CENTER);
        point.setTypeface(point.getTypeface(),1);
        c.addView(point);
        Button speak = soft3d("🔊 Read Question - Indian English");
        speak.setOnClickListener(v -> speak(q.q));
        c.addView(speak);

        TextView feedback = body("");
        feedback.setVisibility(View.GONE);
        feedback.setGravity(Gravity.CENTER);
        Button next = primary3d("Next ➜");
        next.setVisibility(View.GONE);
        List<Button> buttons = new ArrayList<>();
        for (int i = 0; i < q.a.length; i++) {
            final int idx = i;
            Button b = option3d(((char)('A'+i)) + ". " + q.a[i]);
            buttons.add(b);
            b.setOnClickListener(v -> {
                for (Button x : buttons) x.setEnabled(false);
                if (idx == q.correct) {
                    score += 10;
                    playCorrect();
                    b.setBackground(round(Color.rgb(220,252,231),Color.rgb(34,197,94),16));
                    feedback.setText("✅ Correct! +10 points\n" + q.explain);
                } else {
                    playWrong();
                    b.setBackground(round(Color.rgb(254,226,226),Color.rgb(239,68,68),16));
                    feedback.setText("🌼 Nice try! +0 points\nCorrect answer: " + ((char)('A'+q.correct)) + ". " + q.a[q.correct] + "\n" + q.explain);
                }
                feedback.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);
            });
            c.addView(b);
        }
        next.setOnClickListener(v -> { questionIndex++; showCurrent(); });
        c.addView(feedback);
        c.addView(next);
        root.addView(c);
    }

    private void showMatch(MainActivity.Section sec, MainActivity.MatchQ mq) {
        root.removeAllViews();
        root.addView(topBar(sec.emoji + " " + sec.title));
        root.addView(progress(questionIndex + 1,sec.matchQuestions.size()));
        LinearLayout c = card(Color.WHITE);
        c.addView(title(mq.prompt));
        c.addView(body("Tap one item on the left, then its matching item on the right. Each correct pair = +10 points."));
        MatchBoard board = new MatchBoard(mq);
        c.addView(board,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,dp(370)));
        TextView fb = body("");
        fb.setVisibility(View.GONE);
        fb.setGravity(Gravity.CENTER);
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        Button check = primary3d("✓ Check");
        Button reset = soft3d("↻ Reset");
        LinearLayout.LayoutParams half = new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,1f);
        half.setMargins(dp(4),dp(5),dp(4),dp(5));
        check.setLayoutParams(half); reset.setLayoutParams(half);
        row.addView(check); row.addView(reset); c.addView(row);
        Button next = primary3d("Next ➜"); next.setVisibility(View.GONE); c.addView(fb); c.addView(next);
        reset.setOnClickListener(v -> showMatch(sec,mq));
        check.setOnClickListener(v -> {
            int correct = board.lockAndScore();
            int gained = correct * 10;
            score += gained;
            if (correct == mq.left.length) playCorrect(); else playWrong();
            fb.setText("Correct pairs: " + correct + " / " + mq.left.length + "\n⭐ +" + gained + " points");
            fb.setVisibility(View.VISIBLE); row.setVisibility(View.GONE); next.setVisibility(View.VISIBLE);
        });
        next.setOnClickListener(v -> { questionIndex++; showCurrent(); });
        root.addView(c);
    }

    private void showSectionDone() {
        root.removeAllViews();
        MainActivity.Section sec = sections.get(sectionIndex);
        int possible = sectionPoints(sec);
        int earned = score - sectionStartScore;
        int pct = possible == 0 ? 0 : (int)Math.round(earned * 100.0 / possible);
        sectionPercentages.add(pct);
        LinearLayout c = card(pct >= 80 ? Color.rgb(254,249,195) : Color.rgb(239,246,255));
        c.addView(title("🎉 Section Complete!"));
        c.addView(body(playerName + " scored " + earned + " / " + possible + " points (" + pct + "%)."));
        TextView reward = body(pct >= 80 ? "🦸 Fantastic! Now you are " + heroNames[sectionIndex] + "!" : "🌱 Reach 80% next time to unlock " + heroNames[sectionIndex] + ".");
        reward.setTextSize(22); reward.setTypeface(reward.getTypeface(),1); reward.setGravity(Gravity.CENTER); c.addView(reward);
        root.addView(c);
        Button next = primary3d(sectionIndex == sections.size()-1 ? "See Final Result 🏆" : "Next Section ➜");
        next.setOnClickListener(v -> {
            sectionIndex++;
            questionIndex = 0;
            if (sectionIndex < sections.size()) showSectionIntro(); else showFinal();
        });
        root.addView(next);
    }

    private void showFinal() {
        int pct = (int)Math.round(score * 100.0 / Math.max(1,maxScore));
        saveAttempt(pct);
        root.removeAllViews();
        LinearLayout c = card(Color.rgb(238,242,255));
        c.addView(aaravPhoto(dp(145),dp(145)));
        TextView t = title("🏆 EVS Adventure Complete!"); t.setGravity(Gravity.CENTER); c.addView(t);
        TextView r = body(playerName + ", your final score is\n" + score + " / " + maxScore + " points (" + pct + "%).");
        r.setGravity(Gravity.CENTER); r.setTextSize(21); r.setTypeface(r.getTypeface(),1); c.addView(r);
        c.addView(body(pct >= 85 ? "🌟 Excellent! EVS Superhero!" : pct >= 70 ? "🌿 Very good! EVS Explorer!" : "🌱 Keep playing - every new game gives different questions."));
        root.addView(c);
        Button dash = soft3d("📊 View Saved Dashboard"); dash.setOnClickListener(v -> showDashboard()); root.addView(dash);
        Button again = primary3d("🔄 New Randomized Game"); again.setOnClickListener(v -> showWelcome()); root.addView(again);
    }

    private void saveAttempt(int pct) {
        StringBuilder sec = new StringBuilder();
        for (int i = 0; i < sectionPercentages.size(); i++) {
            if (i > 0) sec.append(",");
            sec.append(sectionPercentages.get(i));
        }
        String time = new SimpleDateFormat("dd MMM yyyy, hh:mm a",Locale.ENGLISH).format(new Date());
        String rec = safe(playerName) + "|" + score + "|" + maxScore + "|" + pct + "|" + safe(time) + "|" + sec;
        String old = prefs.getString("records","");
        String all = rec + (old.length() == 0 ? "" : "\n" + old);
        String[] lines = all.split("\\n");
        StringBuilder keep = new StringBuilder();
        for (int i = 0; i < Math.min(lines.length,100); i++) {
            if (i > 0) keep.append("\n");
            keep.append(lines[i]);
        }
        prefs.edit().putString("records",keep.toString()).apply();
    }

    private void showDashboard() {
        root.removeAllViews();
        root.addView(topBar("📊 Score Dashboard"));
        String data = prefs.getString("records","");
        if (data.trim().length() == 0) {
            LinearLayout empty = card(Color.WHITE);
            empty.addView(title("No saved scores yet"));
            empty.addView(body("Complete a full game and the player's score will be saved here automatically."));
            root.addView(empty);
        } else {
            String[] lines = data.split("\\n");
            Map<String,int[]> stats = new LinkedHashMap<>();
            for (String line : lines) {
                String[] p = line.split("\\|",-1);
                if (p.length < 5) continue;
                String name = p[0];
                int pct = num(p[3]);
                int[] st = stats.get(name);
                if (st == null) st = new int[]{0,0};
                st[0]++;
                if (pct > st[1]) st[1] = pct;
                stats.put(name,st);
            }
            LinearLayout summary = card(Color.rgb(236,253,245));
            summary.addView(title("🏅 Player Summary"));
            for (Map.Entry<String,int[]> e : stats.entrySet()) summary.addView(body(e.getKey() + "  •  Attempts: " + e.getValue()[0] + "  •  Best: " + e.getValue()[1] + "%"));
            root.addView(summary);

            root.addView(sectionHeader("Recent Attempts"));
            for (int i = 0; i < Math.min(lines.length,20); i++) {
                String[] p = lines[i].split("\\|",-1);
                if (p.length < 5) continue;
                LinearLayout rec = card(Color.WHITE);
                rec.addView(body("👤 " + p[0] + "     ⭐ " + p[1] + "/" + p[2] + " (" + p[3] + "%)"));
                rec.addView(body("🕒 " + p[4]));
                if (p.length > 5 && p[5].length() > 0) rec.addView(body("Sections: " + p[5].replace(",","% • ") + "%"));
                root.addView(rec);
            }
        }
        Button clear = soft3d("🗑 Clear Dashboard");
        clear.setOnClickListener(v -> new AlertDialog.Builder(this).setTitle("Clear all scores?").setMessage("This will remove all saved player results from this phone.").setPositiveButton("Clear",(d,w)->{prefs.edit().remove("records").apply();showDashboard();}).setNegativeButton("Cancel",null).show());
        root.addView(clear);
        Button home = primary3d("← Back to New Player"); home.setOnClickListener(v -> showWelcome()); root.addView(home);
    }

    private int num(String s) { try { return Integer.parseInt(s); } catch(Exception e) { return 0; } }
    private String safe(String s) { return s.replace("|"," ").replace("\n"," ").trim(); }
    private String cleanName(String s) { return safe(s); }

    private void speak(String text) {
        if (tts != null) tts.speak(text,TextToSpeech.QUEUE_FLUSH,null,"evs_question");
    }
    private void playCorrect() { if (tones != null) tones.startTone(ToneGenerator.TONE_PROP_ACK,150); }
    private void playWrong() { if (tones != null) tones.startTone(ToneGenerator.TONE_PROP_NACK,170); }

    @Override public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS && tts != null) {
            Locale indian = new Locale("en","IN");
            int r = tts.setLanguage(indian);
            if (r == TextToSpeech.LANG_MISSING_DATA || r == TextToSpeech.LANG_NOT_SUPPORTED) tts.setLanguage(Locale.ENGLISH);
            tts.setSpeechRate(0.88f);
            tts.setPitch(1.0f);
        }
    }

    @Override protected void onDestroy() {
        if (tts != null) { tts.stop(); tts.shutdown(); }
        if (tones != null) tones.release();
        super.onDestroy();
    }

    class MatchBoard extends View {
        final MainActivity.MatchQ mq;
        final Paint line = new Paint(Paint.ANTI_ALIAS_FLAG);
        final Paint text = new Paint(Paint.ANTI_ALIAS_FLAG);
        final Map<Integer,Integer> map = new HashMap<>();
        int selected = -1;
        boolean locked = false;
        MatchBoard(MainActivity.MatchQ q) {
            super(GameActivity.this); mq=q; line.setStrokeWidth(dp(3)); text.setTextSize(dp(15)); text.setColor(Color.rgb(15,23,42));
            setBackground(round(Color.rgb(248,250,252),Color.rgb(203,213,225),18));
            setOnTouchListener((v,e)->{
                if (locked || e.getAction()!=MotionEvent.ACTION_DOWN) return true;
                int rows=Math.max(mq.left.length,mq.right.length);
                int row=Math.min(rows-1,Math.max(0,(int)(e.getY()/(getHeight()/(float)rows))));
                if (e.getX()<getWidth()/2f && row<mq.left.length) selected=row;
                else if (selected>=0 && row<mq.right.length) { removeRight(row); map.put(selected,row); selected=-1; }
                invalidate(); return true;
            });
        }
        private void removeRight(int right) { Integer key=null; for(Map.Entry<Integer,Integer> e:map.entrySet()) if(e.getValue()==right) key=e.getKey(); if(key!=null) map.remove(key); }
        int lockAndScore(){locked=true;int c=0;for(int i=0;i<mq.left.length;i++){Integer r=map.get(i);if(r!=null&&r==mq.correctMap[i])c++;}invalidate();return c;}
        @Override protected void onDraw(Canvas c){super.onDraw(c);float w=getWidth(),h=getHeight();int rows=Math.max(mq.left.length,mq.right.length);float rh=h/rows;text.setTextAlign(Paint.Align.LEFT);for(int i=0;i<mq.left.length;i++){float y=i*rh+rh*.58f;text.setColor(i==selected?Color.rgb(180,83,9):Color.rgb(15,23,42));c.drawText(mq.left[i],dp(12),y,text);}text.setTextAlign(Paint.Align.RIGHT);text.setColor(Color.rgb(15,23,42));for(int i=0;i<mq.right.length;i++){float y=i*rh+rh*.58f;c.drawText(mq.right[i],w-dp(12),y,text);}for(Map.Entry<Integer,Integer> e:map.entrySet()){boolean ok=e.getValue()==mq.correctMap[e.getKey()];line.setColor(locked?(ok?Color.rgb(34,197,94):Color.rgb(239,68,68)):Color.rgb(59,130,246));float y1=e.getKey()*rh+rh*.5f,y2=e.getValue()*rh+rh*.5f;c.drawLine(w*.43f,y1,w*.57f,y2,line);c.drawCircle(w*.43f,y1,dp(4),line);c.drawCircle(w*.57f,y2,dp(4),line);}}
    }

    class TopicGraphic extends View {
        final String key; final Paint p=new Paint(Paint.ANTI_ALIAS_FLAG);
        TopicGraphic(String k){super(GameActivity.this);key=k.toLowerCase(Locale.ENGLISH);setBackground(round(Color.rgb(240,253,244),Color.rgb(187,247,208),20));}
        @Override protected void onDraw(Canvas c){super.onDraw(c);float w=getWidth(),h=getHeight();if(key.contains("milk")||key.contains("paneer"))milk(c,w,h);else if(key.contains("food")||key.contains("meal")||key.contains("lunch")||key.contains("roti")||key.contains("orange"))plate(c,w,h);else if(key.contains("rose"))rose(c,w,h);else if(key.contains("mint")||key.contains("spinach"))herb(c,w,h);else if(key.contains("pumpkin")||key.contains("watermelon")||key.contains("ground"))creeper(c,w,h);else if(key.contains("money")||key.contains("fence")||key.contains("climb")||key.contains("grape"))climber(c,w,h);else if(key.contains("lotus")||key.contains("pond")||key.contains("water"))lotus(c,w,h);else if(key.contains("cactus")||key.contains("dry"))cactus(c,w,h);else tree(c,w,h);}
        void ground(Canvas c,float w,float h){p.setStyle(Paint.Style.FILL);p.setColor(Color.rgb(146,97,58));c.drawRect(0,h*.82f,w,h,p);}
        void tree(Canvas c,float w,float h){ground(c,w,h);p.setColor(Color.rgb(120,72,44));c.drawRect(w*.46f,h*.4f,w*.56f,h*.83f,p);p.setColor(Color.rgb(34,139,70));c.drawCircle(w*.4f,h*.38f,w*.16f,p);c.drawCircle(w*.57f,h*.33f,w*.18f,p);c.drawCircle(w*.5f,h*.23f,w*.17f,p);}
        void rose(Canvas c,float w,float h){ground(c,w,h);p.setColor(Color.rgb(34,139,70));p.setStrokeWidth(dp(5));c.drawLine(w*.5f,h*.8f,w*.5f,h*.3f,p);p.setColor(Color.rgb(239,68,68));for(int i=0;i<6;i++){double a=i*Math.PI/3;c.drawCircle((float)(w*.5+Math.cos(a)*w*.06),(float)(h*.28+Math.sin(a)*h*.055),dp(16),p);}p.setColor(Color.rgb(250,204,21));c.drawCircle(w*.5f,h*.28f,dp(9),p);}
        void herb(Canvas c,float w,float h){ground(c,w,h);p.setColor(Color.rgb(34,139,70));p.setStrokeWidth(dp(4));for(int i=-2;i<=2;i++){float x=w*.5f+i*w*.06f;c.drawLine(w*.5f,h*.8f,x,h*(.4f+Math.abs(i)*.03f),p);c.drawOval(x-dp(16),h*.5f,x+dp(5),h*.57f,p);}}
        void creeper(Canvas c,float w,float h){ground(c,w,h);p.setColor(Color.rgb(34,139,70));p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(dp(5));Path path=new Path();path.moveTo(w*.12f,h*.7f);path.cubicTo(w*.3f,h*.52f,w*.5f,h*.8f,w*.82f,h*.58f);c.drawPath(path,p);p.setStyle(Paint.Style.FILL);p.setColor(Color.rgb(249,115,22));c.drawCircle(w*.66f,h*.68f,dp(28),p);}
        void climber(Canvas c,float w,float h){ground(c,w,h);p.setColor(Color.rgb(120,72,44));p.setStrokeWidth(dp(5));for(int i=0;i<4;i++)c.drawLine(w*(.35f+i*.1f),h*.18f,w*(.35f+i*.1f),h*.82f,p);p.setColor(Color.rgb(34,139,70));p.setStyle(Paint.Style.STROKE);Path path=new Path();path.moveTo(w*.28f,h*.8f);path.cubicTo(w*.55f,h*.66f,w*.28f,h*.46f,w*.6f,h*.22f);c.drawPath(path,p);p.setStyle(Paint.Style.FILL);}
        void lotus(Canvas c,float w,float h){p.setStyle(Paint.Style.FILL);p.setColor(Color.rgb(147,197,253));c.drawRect(0,h*.55f,w,h,p);p.setColor(Color.rgb(244,114,182));for(int i=0;i<8;i++){double a=i*Math.PI/4;c.drawCircle((float)(w*.55+Math.cos(a)*w*.07),(float)(h*.45+Math.sin(a)*h*.06),dp(17),p);}p.setColor(Color.rgb(250,204,21));c.drawCircle(w*.55f,h*.45f,dp(9),p);}
        void cactus(Canvas c,float w,float h){ground(c,w,h);p.setColor(Color.rgb(34,139,70));c.drawRoundRect(w*.44f,h*.2f,w*.57f,h*.82f,dp(18),dp(18),p);c.drawRoundRect(w*.31f,h*.42f,w*.48f,h*.52f,dp(14),dp(14),p);c.drawRoundRect(w*.54f,h*.34f,w*.7f,h*.44f,dp(14),dp(14),p);}
        void plate(Canvas c,float w,float h){p.setStyle(Paint.Style.FILL);p.setColor(Color.WHITE);c.drawCircle(w*.5f,h*.5f,Math.min(w,h)*.32f,p);p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(dp(4));p.setColor(Color.rgb(148,163,184));c.drawCircle(w*.5f,h*.5f,Math.min(w,h)*.32f,p);p.setStyle(Paint.Style.FILL);p.setColor(Color.rgb(249,115,22));c.drawCircle(w*.43f,h*.47f,dp(22),p);p.setColor(Color.rgb(34,197,94));c.drawCircle(w*.58f,h*.43f,dp(20),p);p.setColor(Color.rgb(234,179,8));c.drawRect(w*.39f,h*.61f,w*.64f,h*.67f,p);}
        void milk(Canvas c,float w,float h){p.setStyle(Paint.Style.FILL);p.setColor(Color.rgb(219,234,254));c.drawRoundRect(w*.38f,h*.25f,w*.62f,h*.8f,dp(12),dp(12),p);p.setColor(Color.WHITE);c.drawRect(w*.4f,h*.38f,w*.6f,h*.76f,p);p.setColor(Color.rgb(59,130,246));c.drawRect(w*.43f,h*.2f,w*.57f,h*.3f,p);}
    }

    private int calcMaxPoints(){int t=0;for(MainActivity.Section s:sections)t+=sectionPoints(s);return t;}
    private int sectionPoints(MainActivity.Section s){if(s.matchQuestions.isEmpty())return s.questions.size()*10;int t=0;for(MainActivity.MatchQ m:s.matchQuestions)t+=m.left.length*10;return t;}
    private LinearLayout progress(int n,int total){LinearLayout c=card(Color.WHITE);c.setOrientation(LinearLayout.HORIZONTAL);TextView a=body("Question "+n+" of "+total);a.setLayoutParams(new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,1));TextView b=body("⭐ "+score+" pts");b.setGravity(Gravity.END);c.addView(a);c.addView(b);return c;}
    private TextView topBar(String txt){TextView v=body(txt+"     ⭐ "+score+" pts");v.setTextSize(16);v.setTypeface(v.getTypeface(),1);v.setPadding(dp(12),dp(10),dp(12),dp(10));v.setBackground(round(Color.rgb(226,232,240),Color.rgb(203,213,225),14));LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);lp.setMargins(0,0,0,dp(10));v.setLayoutParams(lp);return v;}
    private TextView title(String text){TextView v=new TextView(this);v.setText(text);v.setTextSize(24);v.setTextColor(Color.rgb(15,23,42));v.setTypeface(v.getTypeface(),1);v.setPadding(dp(10),dp(12),dp(10),dp(12));return v;}
    private TextView sectionHeader(String text){TextView v=title(text);v.setTextSize(21);return v;}
    private TextView body(String text){TextView v=new TextView(this);v.setText(text);v.setTextSize(17);v.setTextColor(Color.rgb(51,65,85));v.setPadding(dp(10),dp(7),dp(10),dp(7));return v;}
    private LinearLayout card(int color){LinearLayout c=new LinearLayout(this);c.setOrientation(LinearLayout.VERTICAL);c.setPadding(dp(14),dp(14),dp(14),dp(14));c.setBackground(round(color,Color.rgb(226,232,240),22));c.setElevation(dp(2));LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);lp.setMargins(0,dp(6),0,dp(12));c.setLayoutParams(lp);return c;}
    private Button option3d(String text){Button b=base3d(text,Color.WHITE,Color.rgb(148,163,184),Color.rgb(241,245,249));b.setTextColor(Color.rgb(15,23,42));b.setGravity(Gravity.START|Gravity.CENTER_VERTICAL);return b;}
    private Button primary3d(String text){Button b=base3d(text,Color.rgb(34,197,94),Color.rgb(21,128,61),Color.rgb(22,163,74));b.setTextColor(Color.WHITE);b.setTypeface(b.getTypeface(),1);return b;}
    private Button soft3d(String text){Button b=base3d(text,Color.rgb(239,246,255),Color.rgb(147,197,253),Color.rgb(219,234,254));b.setTextColor(Color.rgb(30,64,175));return b;}
    private Button base3d(String text,int normal,int border,int pressed){Button b=new Button(this);b.setText(text);b.setTextSize(17);b.setAllCaps(false);b.setPadding(dp(14),dp(13),dp(14),dp(13));StateListDrawable states=new StateListDrawable();states.addState(new int[]{android.R.attr.state_pressed},round(pressed,border,16));states.addState(new int[]{},round(normal,border,16));b.setBackground(states);b.setElevation(dp(6));LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);lp.setMargins(dp(2),dp(7),dp(2),dp(7));b.setLayoutParams(lp);b.setOnTouchListener((v,e)->{if(e.getAction()==MotionEvent.ACTION_DOWN){v.setTranslationY(dp(2));v.setElevation(dp(2));}else if(e.getAction()==MotionEvent.ACTION_UP||e.getAction()==MotionEvent.ACTION_CANCEL){v.setTranslationY(0);v.setElevation(dp(6));}return false;});return b;}
    private GradientDrawable round(int fill,int stroke,int radius){GradientDrawable g=new GradientDrawable();g.setColor(fill);g.setCornerRadius(dp(radius));g.setStroke(dp(1),stroke);return g;}
    private int dp(int v){return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,v,getResources().getDisplayMetrics());}
}
