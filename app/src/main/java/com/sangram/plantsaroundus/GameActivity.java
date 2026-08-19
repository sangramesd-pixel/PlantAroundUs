package com.sangram.plantsaroundus;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Base64;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GameActivity extends Activity implements TextToSpeech.OnInitListener {
    private final String childName = "Aarav Damor";
    private final List<MainActivity.Section> sections = QuestionBank.create();
    private ScrollView scroll;
    private LinearLayout root;
    private TextToSpeech tts;
    private int sectionIndex = -1;
    private int questionIndex = 0;
    private int score = 0;
    private int maxScore = 0;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        tts = new TextToSpeech(this, this);
        maxScore = calcMax();
        scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(Color.rgb(247,250,252));
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(16), dp(16), dp(16), dp(24));
        scroll.addView(root);
        setContentView(scroll);
        showHome();
    }

    private void showHome() {
        root.removeAllViews();
        LinearLayout hero = card(Color.rgb(220,252,231));
        TextView t = title("🌱 Aarav Damor's Plant Game"); t.setGravity(Gravity.CENTER); hero.addView(t);
        TextView s = body("Plants Around Us • Class 2 • CBSE"); s.setGravity(Gravity.CENTER); hero.addView(s);
        ImageView photo = new ImageView(this);
        byte[] data = Base64.decode(AaravImage.BASE64, Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(data,0,data.length);
        photo.setImageBitmap(bmp); photo.setScaleType(ImageView.ScaleType.CENTER_CROP);
        photo.setBackground(round(Color.WHITE, Color.rgb(34,197,94), 999)); photo.setClipToOutline(true);
        LinearLayout.LayoutParams pp = new LinearLayout.LayoutParams(dp(170),dp(170)); pp.gravity=Gravity.CENTER; pp.setMargins(0,dp(12),0,dp(12)); photo.setLayoutParams(pp);
        hero.addView(photo);
        TextView p = body("Player: " + childName); p.setGravity(Gravity.CENTER); hero.addView(p);
        root.addView(hero);

        root.addView(sectionHeader("Game Sections"));
        String[] names={"1. MCQ Zone","2. True or False","3. Fill in the Blanks","4. Who Am I?","5. Match the Following","6. Picture & Thinking"};
        for(String n:names){ TextView row=body("• "+n); row.setPadding(dp(10),dp(7),dp(10),dp(7)); root.addView(row); }
        Button start=primary("▶ Start Game"); start.setOnClickListener(v->{sectionIndex=0;questionIndex=0;score=0;showSectionIntro();}); root.addView(start);
    }

    private void showSectionIntro(){
        root.removeAllViews();
        MainActivity.Section sec=sections.get(sectionIndex);
        root.addView(topBar(sec.emoji+" "+sec.title));
        LinearLayout c=card(Color.WHITE);
        c.addView(title(sec.emoji+" "+sec.title));
        c.addView(body(sec.subtitle));
        c.addView(body("Section "+(sectionIndex+1)+" of "+sections.size()));
        c.addView(body("Points in this section: "+sectionPoints(sec)));
        root.addView(c);
        Button b=primary("Start Section"); b.setOnClickListener(v->{questionIndex=0;showCurrent();}); root.addView(b);
    }

    private void showCurrent(){
        MainActivity.Section sec=sections.get(sectionIndex);
        if(!sec.matchQuestions.isEmpty()){
            if(questionIndex<sec.matchQuestions.size()) showMatch(sec,sec.matchQuestions.get(questionIndex)); else showSectionDone();
        }else{
            if(questionIndex<sec.questions.size()) showQuestion(sec,sec.questions.get(questionIndex)); else showSectionDone();
        }
    }

    private void showQuestion(MainActivity.Section sec, MainActivity.Q q){
        root.removeAllViews();
        root.addView(topBar(sec.emoji+" "+sec.title));
        root.addView(progress(questionIndex+1,sec.questions.size()));
        LinearLayout c=card(Color.WHITE);
        if(sectionIndex==5){ PlantGraphic g=new PlantGraphic(q.q); c.addView(g,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,dp(190))); }
        c.addView(title(q.q));
        Button speak=soft("🔊 Read Question"); speak.setOnClickListener(v->speak(q.q)); c.addView(speak);
        TextView feedback=body(""); feedback.setVisibility(View.GONE);
        Button next=primary("Next ➜"); next.setVisibility(View.GONE);
        List<Button> buttons=new ArrayList<>();
        for(int i=0;i<q.a.length;i++){
            final int idx=i; Button b=option(((char)('A'+i))+". "+q.a[i]); buttons.add(b);
            b.setOnClickListener(v->{
                for(Button x:buttons)x.setEnabled(false);
                if(idx==q.correct){score++; b.setBackground(round(Color.rgb(220,252,231),Color.rgb(34,197,94),18)); feedback.setText("✅ Correct!\n"+q.explain);}
                else{b.setBackground(round(Color.rgb(254,226,226),Color.rgb(239,68,68),18)); feedback.setText("🌼 Nice try! Correct answer: "+((char)('A'+q.correct))+". "+q.a[q.correct]+"\n"+q.explain);}
                feedback.setVisibility(View.VISIBLE); next.setVisibility(View.VISIBLE);
            });
            c.addView(b);
        }
        next.setOnClickListener(v->{questionIndex++;showCurrent();}); c.addView(feedback); c.addView(next); root.addView(c);
    }

    private void showMatch(MainActivity.Section sec, MainActivity.MatchQ mq){
        root.removeAllViews();
        root.addView(topBar(sec.emoji+" "+sec.title));
        root.addView(progress(questionIndex+1,sec.matchQuestions.size()));
        LinearLayout c=card(Color.WHITE);
        c.addView(title(mq.prompt));
        c.addView(body("Tap one item on the left, then tap its matching item on the right. A line will connect the pair."));
        MatchBoard board=new MatchBoard(mq); c.addView(board,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,dp(360)));
        TextView fb=body(""); fb.setVisibility(View.GONE);
        LinearLayout row=new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
        Button check=primary("Check"); Button reset=soft("Reset"); LinearLayout.LayoutParams half=new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,1f); half.setMargins(dp(4),dp(4),dp(4),dp(4)); check.setLayoutParams(half); reset.setLayoutParams(half); row.addView(check); row.addView(reset); c.addView(row);
        Button next=primary("Next ➜"); next.setVisibility(View.GONE); c.addView(fb); c.addView(next);
        reset.setOnClickListener(v->showMatch(sec,mq));
        check.setOnClickListener(v->{int correct=board.lockAndScore();score+=correct;fb.setText("✅ You matched "+correct+" out of "+mq.left.length+" correctly.");fb.setVisibility(View.VISIBLE);row.setVisibility(View.GONE);next.setVisibility(View.VISIBLE);});
        next.setOnClickListener(v->{questionIndex++;showCurrent();}); root.addView(c);
    }

    private void showSectionDone(){
        root.removeAllViews(); MainActivity.Section sec=sections.get(sectionIndex); LinearLayout c=card(Color.rgb(254,249,195));
        c.addView(title("🎉 "+sec.title+" Complete!")); c.addView(body("Well done, "+childName+"!")); c.addView(body("Current score: "+score+" / "+maxScore)); root.addView(c);
        Button b=primary(sectionIndex==sections.size()-1?"See Final Result":"Go to Next Section"); b.setOnClickListener(v->{sectionIndex++;questionIndex=0;if(sectionIndex<sections.size())showSectionIntro();else showFinal();}); root.addView(b);
    }

    private void showFinal(){
        root.removeAllViews(); int pct=(int)Math.round(score*100.0/Math.max(1,maxScore)); LinearLayout c=card(Color.rgb(224,231,255));
        c.addView(title("🏆 Game Complete!")); c.addView(body(childName+", your score is "+score+" out of "+maxScore+" ("+pct+"%)."));
        c.addView(body(pct>=85?"🌟 Excellent Plant Champion!":pct>=70?"🌱 Very good work!":pct>=50?"👍 Good job! Keep practicing.":"🌼 Nice effort! Play again and grow more.")); root.addView(c);
        Button b=primary("🔄 Play Again"); b.setOnClickListener(v->showHome()); root.addView(b);
    }

    class MatchBoard extends View {
        final MainActivity.MatchQ mq; final Paint line=new Paint(Paint.ANTI_ALIAS_FLAG); final Paint text=new Paint(Paint.ANTI_ALIAS_FLAG); final Map<Integer,Integer> map=new HashMap<>(); int selected=-1; boolean locked=false;
        MatchBoard(MainActivity.MatchQ q){super(GameActivity.this);mq=q;line.setStrokeWidth(dp(3));text.setTextSize(dp(16));text.setColor(Color.rgb(15,23,42));setBackground(round(Color.rgb(248,250,252),Color.rgb(226,232,240),20));setOnTouchListener((v,e)->{if(locked||e.getAction()!=0)return true;float x=e.getX(),y=e.getY();int row=Math.min(mq.left.length-1,Math.max(0,(int)(y/(getHeight()/(float)mq.left.length))));if(x<getWidth()/2f){selected=row;}else if(selected>=0){map.put(selected,row);selected=-1;invalidate();}return true;});}
        int lockAndScore(){locked=true;int c=0;for(int i=0;i<mq.left.length;i++){Integer r=map.get(i);if(r!=null&&r==mq.correctMap[i])c++;}invalidate();return c;}
        @Override protected void onDraw(Canvas c){super.onDraw(c);float w=getWidth(),h=getHeight(),rowH=h/mq.left.length; text.setTextAlign(Paint.Align.LEFT); for(int i=0;i<mq.left.length;i++){float y=i*rowH+rowH*.58f; text.setColor(selected==i?Color.rgb(180,83,9):Color.rgb(15,23,42));c.drawText(mq.left[i],dp(14),y,text);} text.setTextAlign(Paint.Align.RIGHT);text.setColor(Color.rgb(15,23,42));for(int i=0;i<mq.right.length;i++){float y=i*rowH+rowH*.58f;c.drawText(mq.right[i],w-dp(14),y,text);}for(Map.Entry<Integer,Integer>e:map.entrySet()){boolean ok=e.getValue()==mq.correctMap[e.getKey()];line.setColor(locked?(ok?Color.rgb(34,197,94):Color.rgb(239,68,68)):Color.rgb(59,130,246));float y1=e.getKey()*rowH+rowH*.5f,y2=e.getValue()*rowH+rowH*.5f;c.drawLine(w*.43f,y1,w*.57f,y2,line);c.drawCircle(w*.43f,y1,dp(4),line);c.drawCircle(w*.57f,y2,dp(4),line);}}
    }

    class PlantGraphic extends View {
        final String key; final Paint p=new Paint(Paint.ANTI_ALIAS_FLAG);
        PlantGraphic(String k){super(GameActivity.this);key=k.toLowerCase(Locale.ENGLISH);setBackground(round(Color.rgb(240,253,244),Color.rgb(187,247,208),22));}
        @Override protected void onDraw(Canvas c){super.onDraw(c);float w=getWidth(),h=getHeight();p.setStyle(Paint.Style.FILL);if(key.contains("rose"))rose(c,w,h);else if(key.contains("mint"))herb(c,w,h);else if(key.contains("pumpkin")||key.contains("soil"))creeper(c,w,h);else if(key.contains("money")||key.contains("fence")||key.contains("grape"))climber(c,w,h);else if(key.contains("lotus")||key.contains("water"))lotus(c,w,h);else tree(c,w,h);}
        void ground(Canvas c,float w,float h){p.setColor(Color.rgb(146,97,58));c.drawRect(0,h*.82f,w,h,p);} void tree(Canvas c,float w,float h){ground(c,w,h);p.setColor(Color.rgb(120,72,44));c.drawRect(w*.46f,h*.4f,w*.56f,h*.83f,p);p.setColor(Color.rgb(34,139,70));c.drawCircle(w*.4f,h*.38f,w*.16f,p);c.drawCircle(w*.57f,h*.33f,w*.18f,p);c.drawCircle(w*.5f,h*.23f,w*.17f,p);} void rose(Canvas c,float w,float h){ground(c,w,h);p.setColor(Color.rgb(34,139,70));p.setStrokeWidth(dp(5));c.drawLine(w*.5f,h*.8f,w*.5f,h*.3f,p);p.setColor(Color.rgb(239,68,68));for(int i=0;i<6;i++){double a=i*Math.PI/3;c.drawCircle((float)(w*.5+Math.cos(a)*w*.06),(float)(h*.28+Math.sin(a)*h*.055),dp(16),p);}p.setColor(Color.rgb(250,204,21));c.drawCircle(w*.5f,h*.28f,dp(9),p);} void herb(Canvas c,float w,float h){ground(c,w,h);p.setColor(Color.rgb(34,139,70));p.setStrokeWidth(dp(4));for(int i=-2;i<=2;i++){float x=w*.5f+i*w*.06f;c.drawLine(w*.5f,h*.8f,x,h*(.4f+Math.abs(i)*.03f),p);c.drawOval(x-dp(16),h*.5f,x+dp(5),h*.57f,p);}} void creeper(Canvas c,float w,float h){ground(c,w,h);p.setColor(Color.rgb(34,139,70));p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(dp(5));Path path=new Path();path.moveTo(w*.12f,h*.7f);path.cubicTo(w*.3f,h*.52f,w*.5f,h*.8f,w*.82f,h*.58f);c.drawPath(path,p);p.setStyle(Paint.Style.FILL);p.setColor(Color.rgb(249,115,22));c.drawCircle(w*.66f,h*.68f,dp(28),p);} void climber(Canvas c,float w,float h){ground(c,w,h);p.setColor(Color.rgb(120,72,44));p.setStrokeWidth(dp(5));for(int i=0;i<4;i++)c.drawLine(w*(.35f+i*.1f),h*.18f,w*(.35f+i*.1f),h*.82f,p);p.setColor(Color.rgb(34,139,70));p.setStyle(Paint.Style.STROKE);Path path=new Path();path.moveTo(w*.28f,h*.8f);path.cubicTo(w*.55f,h*.66f,w*.28f,h*.46f,w*.6f,h*.22f);c.drawPath(path,p);p.setStyle(Paint.Style.FILL);} void lotus(Canvas c,float w,float h){p.setColor(Color.rgb(147,197,253));c.drawRect(0,h*.55f,w,h,p);p.setColor(Color.rgb(244,114,182));for(int i=0;i<8;i++){double a=i*Math.PI/4;c.drawCircle((float)(w*.55+Math.cos(a)*w*.07),(float)(h*.45+Math.sin(a)*h*.06),dp(17),p);}p.setColor(Color.rgb(250,204,21));c.drawCircle(w*.55f,h*.45f,dp(9),p);}
    }

    private int calcMax(){int t=0;for(MainActivity.Section s:sections){if(s.matchQuestions.isEmpty())t+=s.questions.size();else for(MainActivity.MatchQ m:s.matchQuestions)t+=m.left.length;}return t;}
    private int sectionPoints(MainActivity.Section s){int t=0;if(s.matchQuestions.isEmpty())return s.questions.size();for(MainActivity.MatchQ m:s.matchQuestions)t+=m.left.length;return t;}
    private LinearLayout progress(int n,int total){LinearLayout c=card(Color.WHITE);c.setOrientation(LinearLayout.HORIZONTAL);TextView a=body("Question "+n+" of "+total);a.setLayoutParams(new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,1));TextView b=body("Score: "+score+" / "+maxScore);b.setGravity(Gravity.END);c.addView(a);c.addView(b);return c;}
    private TextView topBar(String txt){TextView v=body(txt+"     ⭐ "+score+"/"+maxScore);v.setTextSize(16);v.setTypeface(v.getTypeface(),1);v.setPadding(dp(12),dp(10),dp(12),dp(10));v.setBackground(round(Color.rgb(219,234,254),Color.rgb(191,219,254),999));return v;}
    private TextView sectionHeader(String x){TextView v=body(x);v.setTextSize(19);v.setTypeface(v.getTypeface(),1);v.setPadding(dp(4),dp(16),dp(4),dp(8));return v;}
    private LinearLayout card(int color){LinearLayout c=new LinearLayout(this);c.setOrientation(LinearLayout.VERTICAL);c.setPadding(dp(16),dp(16),dp(16),dp(16));c.setBackground(round(color,Color.rgb(226,232,240),24));LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);lp.setMargins(0,dp(8),0,dp(10));c.setLayoutParams(lp);return c;}
    private TextView title(String x){TextView v=new TextView(this);v.setText(x);v.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);v.setTextColor(Color.rgb(15,23,42));v.setPadding(0,0,0,dp(8));return v;}
    private TextView body(String x){TextView v=new TextView(this);v.setText(x);v.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);v.setTextColor(Color.rgb(51,65,85));v.setPadding(0,0,0,dp(8));return v;}
    private Button primary(String x){return button(x,Color.rgb(34,197,94),Color.WHITE);}
    private Button soft(String x){return button(x,Color.rgb(241,245,249),Color.rgb(30,41,59));}
    private Button option(String x){Button b=button(x,Color.rgb(250,250,250),Color.rgb(15,23,42));b.setGravity(Gravity.START|Gravity.CENTER_VERTICAL);return b;}
    private Button button(String x,int bg,int fg){Button b=new Button(this);b.setText(x);b.setAllCaps(false);b.setTextSize(17);b.setTextColor(fg);b.setPadding(dp(14),dp(13),dp(14),dp(13));b.setBackground(round(bg,Color.rgb(203,213,225),18));LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);lp.setMargins(dp(4),dp(6),dp(4),dp(6));b.setLayoutParams(lp);return b;}
    private GradientDrawable round(int fill,int stroke,int r){GradientDrawable g=new GradientDrawable();g.setColor(fill);g.setCornerRadius(dp(r));g.setStroke(dp(1),stroke);return g;}
    private int dp(int n){return(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,n,getResources().getDisplayMetrics());}
    private void speak(String x){if(tts!=null)tts.speak(x.replaceAll("[^\\x00-\\x7F]",""),TextToSpeech.QUEUE_FLUSH,null,"q");}
    @Override public void onInit(int status){if(status==TextToSpeech.SUCCESS)tts.setLanguage(Locale.ENGLISH);} @Override protected void onDestroy(){if(tts!=null){tts.stop();tts.shutdown();}super.onDestroy();}
}
