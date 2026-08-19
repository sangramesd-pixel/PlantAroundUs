package com.sangram.plantsaroundus;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener {
    static class Q {
        String q; String[] a; int correct; String explain;
        Q(String q, String[] a, int correct, String explain){this.q=q;this.a=a;this.correct=correct;this.explain=explain;}
    }

    private final List<Q> questions = new ArrayList<>();
    private int index = 0, score = 0;
    private TextView title, progress, question, feedback, scoreView;
    private final Button[] optionButtons = new Button[4];
    private Button nextButton, speakButton;
    private TextToSpeech tts;

    @Override public void onCreate(Bundle b){
        super.onCreate(b);
        tts = new TextToSpeech(this, this);
        loadQuestions();
        Collections.shuffle(questions);
        buildUi();
        showQuestion();
    }

    private void loadQuestions(){
        questions.add(new Q("🌳 Which of these is a big and strong plant?", new String[]{"Grass","Mango tree","Rose plant","Mint"},1,"A big plant with a hard woody stem is called a tree."));
        questions.add(new Q("🌱 Which part of a plant grows under the soil?", new String[]{"Flower","Leaf","Root","Fruit"},2,"Roots grow under the soil and absorb water."));
        questions.add(new Q("🍃 Which part of the plant usually makes food?", new String[]{"Leaf","Root","Flower","Fruit"},0,"Leaves use sunlight, air and water to make food."));
        questions.add(new Q("🌹 Which one is a shrub?", new String[]{"Banyan","Rose","Grass","Pumpkin"},1,"Rose is a shrub because it is small and bushy with woody stems."));
        questions.add(new Q("🌿 Which one is a herb?", new String[]{"Mango","Coconut","Mint","Rose"},2,"Mint is a herb with a soft green stem."));
        questions.add(new Q("🪴 Which plant needs support to grow upward?", new String[]{"Neem","Money plant","Coconut","Rose"},1,"Money plant is a climber and needs support."));
        questions.add(new Q("🎃 Which plant grows along the ground?", new String[]{"Mango","Rose","Pumpkin","Coconut"},2,"Pumpkin is a creeper with a weak stem."));
        questions.add(new Q("🥭 Which plant gives us mangoes?", new String[]{"Mango tree","Rose plant","Grass","Cactus"},0,"Mangoes grow on mango trees."));
        questions.add(new Q("🌸 Which part of a plant can become a fruit?", new String[]{"Root","Stem","Flower","Leaf"},2,"A flower can develop into a fruit."));
        questions.add(new Q("☀️ What does a plant need to grow?", new String[]{"Only water","Only sunlight","Only air","Air, water and sunlight"},3,"Plants need air, water and sunlight to grow well."));
        questions.add(new Q("🌵 Which plant usually grows in a desert?", new String[]{"Cactus","Paddy","Lotus","Mint"},0,"Cactus can store water and survive in deserts."));
        questions.add(new Q("🪷 Which plant grows in water?", new String[]{"Rose","Lotus","Neem","Cactus"},1,"Lotus is an aquatic plant."));
        questions.add(new Q("🌳 Which of these is a tree?", new String[]{"Rose","Tomato","Neem","Pumpkin"},2,"Neem is a tree with a strong woody trunk."));
        questions.add(new Q("🌿 Which plant type has a soft green stem?", new String[]{"Herb","Tree","Shrub","Creeper"},0,"Herbs usually have soft green stems."));
        questions.add(new Q("💧 What do roots take from the soil?", new String[]{"Sunlight","Water and minerals","Flowers","Fruit"},1,"Roots absorb water and minerals from soil."));
        questions.add(new Q("🌲 I am tall and have a thick hard trunk. Who am I?", new String[]{"Herb","Tree","Creeper","Climber"},1,"A tall plant with a thick trunk is a tree."));
        questions.add(new Q("🌿 I have a weak stem and climb with support. Who am I?", new String[]{"Tree","Shrub","Climber","Herb"},2,"Climbers use support to grow upward."));
        questions.add(new Q("🍉 I have a weak stem and spread on the ground. Who am I?", new String[]{"Creeper","Tree","Shrub","Herb"},0,"Creepers spread along the ground."));
        questions.add(new Q("✅ Which pair is correct?", new String[]{"Mango — Herb","Rose — Shrub","Pumpkin — Tree","Mint — Climber"},1,"Rose is correctly classified as a shrub."));
        questions.add(new Q("🐕 Which one is NOT a plant?", new String[]{"Tree","Rose","Dog","Grass"},2,"A dog is an animal, not a plant."));
    }

    private TextView tv(String text, int sp){
        TextView v=new TextView(this); v.setText(text); v.setTextSize(sp); v.setTextColor(Color.rgb(35,70,40)); v.setPadding(18,12,18,12); return v;
    }
    private Button btn(String text){
        Button b=new Button(this); b.setText(text); b.setTextSize(18); b.setAllCaps(false); b.setPadding(12,14,12,14); LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2); p.setMargins(8,8,8,8); b.setLayoutParams(p); return b;
    }

    private void buildUi(){
        ScrollView scroll=new ScrollView(this);
        LinearLayout root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(20,24,20,24); root.setBackgroundColor(Color.rgb(248,255,248));
        title=tv("🌱 Plants Around Us",28); title.setGravity(Gravity.CENTER); root.addView(title);
        TextView sub=tv("Class 2 • Fun Quiz",18); sub.setGravity(Gravity.CENTER); root.addView(sub);
        LinearLayout top=new LinearLayout(this); top.setOrientation(LinearLayout.HORIZONTAL); top.setGravity(Gravity.CENTER_VERTICAL);
        progress=tv("",16); scoreView=tv("",16); LinearLayout.LayoutParams half=new LinearLayout.LayoutParams(0,-2,1); progress.setLayoutParams(half); scoreView.setLayoutParams(half); scoreView.setGravity(Gravity.END); top.addView(progress); top.addView(scoreView); root.addView(top);
        question=tv("",24); question.setGravity(Gravity.CENTER); question.setPadding(16,28,16,24); root.addView(question);
        speakButton=btn("🔊 Read Question"); root.addView(speakButton); speakButton.setOnClickListener(v -> speak());
        for(int i=0;i<4;i++){ final int choice=i; optionButtons[i]=btn(""); optionButtons[i].setOnClickListener(v -> answer(choice)); root.addView(optionButtons[i]); }
        feedback=tv("",18); feedback.setGravity(Gravity.CENTER); feedback.setPadding(14,22,14,18); root.addView(feedback);
        nextButton=btn("Next Question ➜"); nextButton.setVisibility(View.GONE); nextButton.setOnClickListener(v -> { index++; if(index<questions.size()) showQuestion(); else showResult(); }); root.addView(nextButton);
        scroll.addView(root); setContentView(scroll);
    }

    private void showQuestion(){
        Q q=questions.get(index); progress.setText("Question " + (index+1) + " / " + questions.size()); scoreView.setText("⭐ Score: " + score); question.setText(q.q); feedback.setText(""); nextButton.setVisibility(View.GONE);
        for(int i=0;i<4;i++){ optionButtons[i].setText((char)('A'+i) + ". " + q.a[i]); optionButtons[i].setEnabled(true); }
    }

    private void answer(int choice){
        Q q=questions.get(index); for(Button b: optionButtons) b.setEnabled(false);
        if(choice==q.correct){ score++; feedback.setText("✅ Correct! Great job!\n" + q.explain); }
        else { feedback.setText("🌼 Nice try! The correct answer is " + (char)('A'+q.correct) + ". " + q.a[q.correct] + ".\n" + q.explain); }
        scoreView.setText("⭐ Score: " + score); nextButton.setVisibility(View.VISIBLE);
    }

    private void showResult(){
        question.setText("🏆 Quiz Complete!"); progress.setText("Finished"); scoreView.setText("⭐ Score: " + score + "/" + questions.size());
        String msg = score>=18 ? "🌟 Excellent! Plant Expert!" : score>=14 ? "🌱 Very Good!" : score>=10 ? "👍 Good! Keep learning!" : "🌼 Try again and grow your plant power!";
        feedback.setText(msg);
        for(Button b:optionButtons) b.setVisibility(View.GONE); speakButton.setVisibility(View.GONE); nextButton.setText("🔄 Play Again"); nextButton.setVisibility(View.VISIBLE); nextButton.setOnClickListener(v -> restart());
    }

    private void restart(){
        index=0; score=0; Collections.shuffle(questions); for(Button b:optionButtons){ b.setVisibility(View.VISIBLE); b.setEnabled(true);} speakButton.setVisibility(View.VISIBLE); nextButton.setText("Next Question ➜"); nextButton.setOnClickListener(v -> { index++; if(index<questions.size()) showQuestion(); else showResult(); }); showQuestion();
    }

    private void speak(){ if(tts!=null && index<questions.size()) tts.speak(questions.get(index).q.replaceAll("[^\\x00-\\x7F]", ""), TextToSpeech.QUEUE_FLUSH, null, "q"); }
    @Override public void onInit(int status){ if(status==TextToSpeech.SUCCESS) tts.setLanguage(Locale.ENGLISH); }
    @Override protected void onDestroy(){ if(tts!=null){tts.stop(); tts.shutdown();} super.onDestroy(); }
}
