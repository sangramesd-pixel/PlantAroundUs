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
    private TextView progress, question, feedback, scoreView;
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

    private void add(String q, String a, String b, String c, String d, int correct, String explain){
        questions.add(new Q(q,new String[]{a,b,c,d},correct,explain));
    }
    private void tf(String q, boolean answer, String explain){
        questions.add(new Q(q,new String[]{"True","False","",""},answer?0:1,explain));
    }

    private void loadQuestions(){
        // A. MCQs 1-15
        add("🌳 1. Which of these is a tree?","Rose","Mango","Mint","Pumpkin",1,"Mango is a tree with a strong woody trunk.");
        add("🌹 2. Which of these is a shrub?","Banyan","Rose","Grass","Money plant",1,"Rose is a small bushy woody plant called a shrub.");
        add("🌿 3. Which of these is a herb?","Mint","Mango","Neem","Banyan",0,"Mint is a small plant with a soft green stem.");
        add("🍇 4. Which plant needs support to grow?","Mango","Grapevine","Neem","Banyan",1,"Grapevine is a climber and needs support.");
        add("🎃 5. Which plant spreads along the ground?","Pumpkin","Rose","Mango","Tulsi",0,"Pumpkin is a creeper with a weak stem.");
        add("🌲 6. Which plant has a thick and hard stem called a trunk?","Tree","Herb","Creeper","Climber",0,"Trees have thick, hard woody trunks.");
        add("🌿 7. Which plant has a soft green stem?","Herb","Tree","Shrub","None",0,"Herbs usually have soft green stems.");
        add("🍉 8. Which of these is a creeper?","Watermelon","Grapevine","Rose","Neem",0,"Watermelon spreads along the ground and is a creeper.");
        add("🪴 9. Which of these is a climber?","Money plant","Mango","Grass","Pumpkin",0,"Money plant uses support to climb upward.");
        add("🌳 10. Which is the biggest type of plant?","Herb","Shrub","Tree","Creeper",2,"Trees are generally the largest type of plant.");
        add("🥭 11. Which plant gives us mangoes?","Mango tree","Rose plant","Mint plant","Money plant",0,"Mangoes grow on mango trees.");
        add("🌳 12. Which plant can give us shade?","Big tree","Grass","Creeper","Small herb",0,"The wide branches and leaves of a big tree can give shade.");
        add("🌹 13. Which plant is generally bushy and has many branches near the ground?","Shrub","Tree","Climber","Creeper",0,"Shrubs are bushy and branch close to the ground.");
        add("🍇 14. Which one cannot stand upright by itself?","Grapevine","Mango tree","Neem tree","Rose shrub",0,"Grapevine has a weak stem and needs support.");
        add("✅ 15. Which pair is correct?","Mango – Herb","Rose – Shrub","Pumpkin – Tree","Mint – Climber",1,"Rose is correctly classified as a shrub.");

        // B. True or False 16-25
        tf("✅❌ 16. All plants are of the same size.",false,"Plants come in many sizes, from small herbs to large trees.");
        tf("✅❌ 17. A mango plant is a tree.",true,"A mango plant grows into a large woody tree.");
        tf("✅❌ 18. Rose is a shrub.",true,"Rose is a bushy woody shrub.");
        tf("✅❌ 19. Herbs usually have soft stems.",true,"Herbs have soft and usually green stems.");
        tf("✅❌ 20. Climbers need support to grow upward.",true,"Climbers have weak stems and use support.");
        tf("✅❌ 21. Creepers spread on the ground.",true,"Creepers have weak stems that spread along the ground.");
        tf("✅❌ 22. Pumpkin is a climber.",false,"Pumpkin is usually taught as a creeper because it spreads on the ground.");
        tf("✅❌ 23. Money plant is a climber.",true,"Money plant climbs with the help of support.");
        tf("✅❌ 24. Trees usually have strong woody trunks.",true,"A strong woody trunk supports a tree.");
        tf("✅❌ 25. Mint is a herb.",true,"Mint is a small soft-stemmed herb.");

        // C. Fill in the blanks 26-35 as choice questions
        add("✏️ 26. Very big and strong plants are called ________.","trees","herbs","creepers","climbers",0,"Very big and strong plants are called trees.");
        add("✏️ 27. Small plants with soft stems are called ________.","trees","herbs","shrubs","climbers",1,"Small soft-stemmed plants are herbs.");
        add("✏️ 28. A rose plant is a ________.","tree","shrub","creeper","herb",1,"Rose is a shrub.");
        add("✏️ 29. Plants that need support to grow upward are called ________.","creepers","trees","climbers","shrubs",2,"Climbers need support to grow upward.");
        add("✏️ 30. Plants that spread along the ground are called ________.","creepers","trees","herbs","shrubs",0,"Creepers spread along the ground.");
        add("✏️ 31. Mango is an example of a ________.","tree","herb","creeper","climber",0,"Mango is a tree.");
        add("✏️ 32. Mint is an example of a ________.","tree","shrub","herb","climber",2,"Mint is a herb.");
        add("✏️ 33. Grapevine is an example of a ________.","tree","climber","creeper","shrub",1,"Grapevine is a climber.");
        add("✏️ 34. Pumpkin is an example of a ________.","creeper","tree","shrub","herb",0,"Pumpkin is a creeper.");
        add("✏️ 35. Plants give us fruits and ________.","stones","vegetables","plastic","metal",1,"Many plants give us fruits and vegetables.");

        // D. Match 36
        add("🔗 36. Which matching set is completely correct?","Mango–Tree, Rose–Shrub, Mint–Herb, Grapevine–Climber, Pumpkin–Creeper","Mango–Herb, Rose–Tree, Mint–Creeper, Grapevine–Shrub, Pumpkin–Climber","Mango–Shrub, Rose–Herb, Mint–Tree, Grapevine–Creeper, Pumpkin–Climber","Mango–Creeper, Rose–Climber, Mint–Shrub, Grapevine–Tree, Pumpkin–Herb",0,"Mango is a tree, Rose a shrub, Mint a herb, Grapevine a climber and Pumpkin a creeper.");

        // E. Who am I? 37-44
        add("🤔 37. I am very tall and have a strong woody trunk. Who am I?","Tree","Herb","Creeper","Climber",0,"A tall plant with a strong woody trunk is a tree.");
        add("🤔 38. I am small and have a soft green stem. Who am I?","Tree","Herb","Shrub","Climber",1,"A small soft-stemmed plant is a herb.");
        add("🤔 39. I am bushy and have many branches near the ground. Who am I?","Shrub","Tree","Creeper","Climber",0,"A bushy plant with branches near the ground is a shrub.");
        add("🤔 40. My stem is weak, so I need support to grow upward. Who am I?","Tree","Climber","Herb","Shrub",1,"A climber needs support because its stem is weak.");
        add("🤔 41. My stem is weak, so I spread along the ground. Who am I?","Tree","Shrub","Creeper","Herb",2,"A creeper spreads on the ground.");
        add("🥭 42. I give you sweet yellow fruits in summer. Who am I?","Mango tree","Rose plant","Mint plant","Grass",0,"Mango trees give sweet mango fruits.");
        add("🌹 43. I have beautiful flowers and thorns. Who am I?","Rose plant","Neem tree","Grass","Pumpkin",0,"Rose plants are known for beautiful flowers and thorns.");
        add("🍉 44. I spread along the ground and give a large green fruit. Who am I?","Watermelon plant","Mango tree","Rose shrub","Mint herb",0,"Watermelon is a creeper that gives large green fruits.");

        // F. Picture-style questions 45-54
        add("🖼️🌳 45. Look at this mango tree. What type of plant is it?","Herb","Tree","Creeper","Climber",1,"A mango plant is a tree.");
        add("🖼️🌹 46. Look at this rose plant. Which type of plant is shown?","Shrub","Tree","Herb","Creeper",0,"Rose is a shrub.");
        add("🖼️🌿 47. Look at this mint plant. Identify its plant type.","Herb","Tree","Climber","Shrub",0,"Mint is a herb.");
        add("🖼️🎃 48. Look at a pumpkin vine on the ground. How does it grow?","Climbs a wall","Spreads on the ground","Grows into a big tree","Grows underwater",1,"Pumpkin is a creeper and spreads on the ground.");
        add("🖼️🪴 49. A money plant is climbing a support. Why does it need support?","Its stem is weak","Its stem is very thick","It has no leaves","It grows underground",0,"Climbers need support because their stems are weak.");
        add("🖼️ 50. Imagine Mango, Rose, Mint and Pumpkin. Which one is a tree?","Mango","Rose","Mint","Pumpkin",0,"Mango is the tree in this group.");
        add("🖼️ 51. Imagine Rose, Neem, Grapevine and Mint. Which one is a shrub?","Rose","Neem","Grapevine","Mint",0,"Rose is a shrub.");
        add("🖼️ 52. Imagine Mint, Mango, Rose and Coconut. Which one is a herb?","Mint","Mango","Rose","Coconut",0,"Mint is a herb.");
        add("🖼️🍇 53. A grapevine is growing upward on a support. Is it a climber or creeper?","Climber","Creeper","Tree","Herb",0,"Grapevine is a climber.");
        add("🖼️🍉 54. A watermelon plant is spreading on the ground. What type of plant is it?","Tree","Shrub","Climber","Creeper",3,"Watermelon is a creeper.");

        // G. Application / thinking 55-60
        add("🧠 55. Riya wants to tie a swing to a plant. Which would be safest?","Mango tree","Mint plant","Money plant","Grass",0,"A strong tree trunk can support a swing better than small or weak-stemmed plants.");
        add("🧠 56. A plant has a weak stem and is growing on a fence. What type of plant is it?","Climber","Tree","Herb","Shrub",0,"A weak-stemmed plant using a fence for support is a climber.");
        add("🧠 57. A plant has a weak stem and is spreading over the soil. What is it?","Creeper","Tree","Shrub","Herb",0,"A plant spreading on the soil because of a weak stem is a creeper.");
        add("🧠 58. Which plant would you choose if you wanted shade in your garden?","Neem tree","Mint","Grass","Pumpkin",0,"A neem tree grows large and can provide shade.");
        add("🧠 59. Which plant would need a stick or fence for support?","Grapevine","Mango","Neem","Rose",0,"Grapevine is a climber and needs support.");
        add("🧠 60. Which group contains only trees?","Mango, Neem, Banyan","Rose, Mint, Mango","Pumpkin, Grass, Neem","Money plant, Rose, Mint",0,"Mango, Neem and Banyan are all trees.");
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
        TextView title=tv("🌱 Plants Around Us",28); title.setGravity(Gravity.CENTER); root.addView(title);
        TextView sub=tv("CBSE Class 2 • 60 Question Fun Quiz",18); sub.setGravity(Gravity.CENTER); root.addView(sub);
        LinearLayout top=new LinearLayout(this); top.setOrientation(LinearLayout.HORIZONTAL); top.setGravity(Gravity.CENTER_VERTICAL);
        progress=tv("",16); scoreView=tv("",16); LinearLayout.LayoutParams half=new LinearLayout.LayoutParams(0,-2,1); progress.setLayoutParams(half); scoreView.setLayoutParams(half); scoreView.setGravity(Gravity.END); top.addView(progress); top.addView(scoreView); root.addView(top);
        question=tv("",23); question.setGravity(Gravity.CENTER); question.setPadding(16,28,16,24); root.addView(question);
        speakButton=btn("🔊 Read Question"); root.addView(speakButton); speakButton.setOnClickListener(v -> speak());
        for(int i=0;i<4;i++){ final int choice=i; optionButtons[i]=btn(""); optionButtons[i].setOnClickListener(v -> answer(choice)); root.addView(optionButtons[i]); }
        feedback=tv("",18); feedback.setGravity(Gravity.CENTER); feedback.setPadding(14,22,14,18); root.addView(feedback);
        nextButton=btn("Next Question ➜"); nextButton.setVisibility(View.GONE); nextButton.setOnClickListener(v -> goNext()); root.addView(nextButton);
        scroll.addView(root); setContentView(scroll);
    }

    private void showQuestion(){
        Q q=questions.get(index); progress.setText("Question " + (index+1) + " / " + questions.size()); scoreView.setText("⭐ Score: " + score); question.setText(q.q); feedback.setText(""); nextButton.setVisibility(View.GONE);
        for(int i=0;i<4;i++){
            if(q.a[i]==null || q.a[i].isEmpty()) { optionButtons[i].setVisibility(View.GONE); }
            else { optionButtons[i].setVisibility(View.VISIBLE); optionButtons[i].setText((char)('A'+i) + ". " + q.a[i]); optionButtons[i].setEnabled(true); }
        }
    }

    private void answer(int choice){
        Q q=questions.get(index); for(Button b: optionButtons) b.setEnabled(false);
        if(choice==q.correct){ score++; feedback.setText("✅ Correct! Great job!\n\n" + q.explain); }
        else { feedback.setText("🌼 Nice try!\nCorrect answer: " + q.a[q.correct] + "\n\n" + q.explain); }
        scoreView.setText("⭐ Score: " + score); nextButton.setVisibility(View.VISIBLE);
    }

    private void goNext(){ index++; if(index<questions.size()) showQuestion(); else showResult(); }

    private void showResult(){
        question.setText("🏆 Quiz Complete!"); progress.setText("Finished"); scoreView.setText("⭐ Score: " + score + "/" + questions.size());
        double pct = score * 100.0 / questions.size();
        String msg = pct>=90 ? "🌟 Excellent! Plant Expert!" : pct>=75 ? "🌱 Very Good!" : pct>=50 ? "👍 Good! Keep learning!" : "🌼 Try again and grow your plant power!";
        feedback.setText(msg + "\nYou scored " + score + " out of " + questions.size() + ".");
        for(Button b:optionButtons) b.setVisibility(View.GONE); speakButton.setVisibility(View.GONE); nextButton.setText("🔄 Play Again"); nextButton.setVisibility(View.VISIBLE); nextButton.setOnClickListener(v -> restart());
    }

    private void restart(){
        index=0; score=0; Collections.shuffle(questions); speakButton.setVisibility(View.VISIBLE); nextButton.setText("Next Question ➜"); nextButton.setOnClickListener(v -> goNext()); showQuestion();
    }

    private void speak(){ if(tts!=null && index<questions.size()) tts.speak(questions.get(index).q.replaceAll("[^\\x00-\\x7F]", ""), TextToSpeech.QUEUE_FLUSH, null, "q"); }
    @Override public void onInit(int status){ if(status==TextToSpeech.SUCCESS) tts.setLanguage(Locale.ENGLISH); }
    @Override protected void onDestroy(){ if(tts!=null){tts.stop(); tts.shutdown();} super.onDestroy(); }
}
