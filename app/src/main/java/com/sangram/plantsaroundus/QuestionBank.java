package com.sangram.plantsaroundus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import com.sangram.plantsaroundus.MainActivity.Q;
import com.sangram.plantsaroundus.MainActivity.Section;
import com.sangram.plantsaroundus.MainActivity.MatchQ;

/** Large offline Class-2 EVS question bank based on the uploaded Food + Plants worksheet,
 * expanded with closely related age-appropriate practice. */
class QuestionBank {
    private static final Random R = new Random();

    static List<Section> createSession() {
        List<Section> full = createFullBank();
        List<Section> session = new ArrayList<>();
        session.add(sample(full.get(0), 20, true));
        session.add(sample(full.get(1), 14, false));
        session.add(sample(full.get(2), 14, true));
        session.add(sample(full.get(3), 12, true));
        session.add(sampleMatches(full.get(4), 4));
        session.add(sample(full.get(5), 14, true));
        return session;
    }

    static int totalQuestionUnits() {
        int total = 0;
        for (Section s : createFullBank()) {
            total += s.questions.size();
            for (MatchQ m : s.matchQuestions) total += m.left.length;
        }
        return total;
    }

    private static Section sample(Section src, int count, boolean shuffleAnswers) {
        Section out = new Section(src.title, src.subtitle, src.emoji);
        List<Q> pool = new ArrayList<>(src.questions);
        Collections.shuffle(pool, R);
        for (int i = 0; i < Math.min(count, pool.size()); i++) {
            out.questions.add(shuffleAnswers ? shuffledCopy(pool.get(i)) : copy(pool.get(i)));
        }
        return out;
    }

    private static Section sampleMatches(Section src, int count) {
        Section out = new Section(src.title, src.subtitle, src.emoji);
        List<MatchQ> pool = new ArrayList<>(src.matchQuestions);
        Collections.shuffle(pool, R);
        for (int i = 0; i < Math.min(count, pool.size()); i++) out.matchQuestions.add(pool.get(i));
        return out;
    }

    private static Q copy(Q q) {
        return new Q(q.q, q.a.clone(), q.correct, q.explain);
    }

    private static Q shuffledCopy(Q q) {
        if (q.a.length <= 2) return copy(q);
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < q.a.length; i++) order.add(i);
        Collections.shuffle(order, R);
        String[] ans = new String[q.a.length];
        int correct = 0;
        for (int i = 0; i < order.size(); i++) {
            int old = order.get(i);
            ans[i] = q.a[old];
            if (old == q.correct) correct = i;
        }
        return new Q(q.q, ans, correct, q.explain);
    }

    private static void q(Section s, String question, String correct, String d1, String d2, String d3, String explain) {
        s.questions.add(new Q(question, new String[]{correct, d1, d2, d3}, 0, explain));
    }

    private static void tf(Section s, String statement, boolean answer, String explain) {
        s.questions.add(new Q(statement, new String[]{"True", "False"}, answer ? 0 : 1, explain));
    }

    private static List<Section> createFullBank() {
        List<Section> sections = new ArrayList<>();
        sections.add(buildMcq());
        sections.add(buildTrueFalse());
        sections.add(buildFill());
        sections.add(buildWhoAmI());
        sections.add(buildMatch());
        sections.add(buildThinking());
        return sections;
    }

    private static Section buildMcq() {
        Section s = new Section("Section 1: Mixed MCQ", "Food and Plants - choose the correct answer.", "🟢");

        // Core questions closely following the worksheet.
        q(s,"Why do we need food?","To grow and stay healthy","To shout","To stay awake all night","To make toys","Food helps our body grow and stay healthy.");
        q(s,"Milk helps us build ______.","strong bones","plastic toys","stones","paper","Milk is a body-building food and supports strong bones.");
        q(s,"Green vegetables are rich in ______.","vitamins","plastic","stones","soil","Vegetables provide protective nutrients such as vitamins.");
        q(s,"Which food comes from plants?","Wheat","Fish","Egg","Meat","Wheat comes from a plant.");
        q(s,"Which type of food helps build muscles?","Body-building food","Energy-giving food","Junk food","Only sweets","Body-building foods help growth and muscles.");
        q(s,"We commonly get cooking oil from ______.","plants","plastic","stones","sand","Many cooking oils come from plant seeds and fruits.");
        q(s,"Which is a healthy snack?","Fruit","Candy","Chips","Chocolate bar","Fruit is a healthy choice.");
        q(s,"Rice and roti mainly give us ______.","energy","sleep","sickness","pain","Rice and roti are energy-giving foods.");
        q(s,"Which foods help protect us from diseases?","Fruits and vegetables","Only chocolates","Only chips","Only sweets","Fruits and vegetables are protective foods.");
        q(s,"We get food from ______.","plants and animals","only stones","only soil","only plastic","Food can come from both plants and animals.");
        q(s,"Which food helps build the body?","Milk and paneer","Candy and cola","Chips and sweets","Ice candy","Milk and paneer are body-building foods.");
        q(s,"Dinner is usually the ______ meal of the day.","last","first","morning","midday","Dinner is usually eaten at the end of the day.");
        q(s,"Which meal is eaten in the afternoon?","Lunch","Breakfast","Dinner","Midnight snack","Lunch is the afternoon meal.");
        q(s,"Which food would a vegetarian usually not eat?","Meat","Rice","Salad","Vegetables","A vegetarian diet does not include meat.");
        q(s,"How many main meals do we usually have in a day?","Three","One","Six","Ten","Breakfast, lunch and dinner are the three main meals.");
        q(s,"Which part of a plant grows under the ground?","Root","Leaf","Flower","Fruit","Roots usually grow under the soil.");
        q(s,"What does the stem help a plant do?","Stand upright","Fly","Sleep","Make noise","The stem supports the plant.");
        q(s,"Which of these is a tree?","Mango","Grass","Rose","Mint","Mango is a tree.");
        q(s,"Which part holds a plant firmly in the soil?","Root","Fruit","Flower","Leaf","Roots anchor a plant in the soil.");
        q(s,"Which part usually makes food for the plant?","Leaves","Roots","Flowers","Seeds","Leaves prepare food for the plant.");
        q(s,"Which food comes from a plant?","Carrot","Fish","Meat","Egg","Carrot is a plant food.");
        q(s,"A balanced diet helps us ______.","stay healthy","fall sick","become lazy","sleep all day","A balanced diet contains different useful food groups.");
        q(s,"Which plant gives us cotton?","Cotton plant","Mango tree","Neem tree","Tulsi plant","Cotton fibre comes from the cotton plant.");
        q(s,"Which plant is commonly used as a medicinal plant?","Tulsi","Plastic","Iron","Glass","Tulsi is commonly used as a medicinal plant.");
        q(s,"Pulses and eggs mainly help in ______.","growth","crying","sleeping only","making toys","Protein-rich foods help growth.");
        q(s,"Vegetables are mainly called ______ foods.","protective","junk","only energy-giving","sweet","Vegetables help protect the body.");
        q(s,"Plants with soft green stems are called ______.","herbs","trees","shrubs","rocks","Herbs have soft stems.");
        q(s,"A plant with a weak stem that needs support is a ______.","climber","tree","shrub","herb","Climbers need support to grow upward.");
        q(s,"Which is an energy-giving food?","Roti","Orange","Spinach","Cucumber","Roti mainly gives energy.");
        q(s,"A plant with many woody branches near the ground is a ______.","shrub","herb","climber","creeper","Shrubs are woody and branch near the ground.");
        q(s,"Which part of a flower later helps form fruit and seeds?","Flower","Root","Bark","Thorn","Flowers develop into fruits containing seeds.");
        q(s,"Which part carries water upward to other plant parts?","Stem","Flower","Fruit","Seed","The stem carries water to other parts.");
        q(s,"Sunflower is famous for its large ______.","flower","root","trunk","thorn","Sunflower has a large flower head.");
        q(s,"Which plant stores water in its thick stem?","Cactus","Rose","Tulip","Mango","Cactus stores water in its stem.");
        q(s,"Leaves use ______ to prepare food.","sunlight","moonlight","stars","plastic","Leaves need sunlight to make food.");

        // Expanded food concepts.
        String[][] energy = {{"rice","energy"},{"roti","energy"},{"potato","energy"},{"bread","energy"},{"sugar","energy"}};
        for (String[] x : energy) q(s,"What does " + x[0] + " mainly give our body?","Energy","Thorns","Roots","Flowers",capitalize(x[0])+" is mainly an energy-giving food.");
        String[][] body = {{"milk","body-building"},{"paneer","body-building"},{"pulses","body-building"},{"egg","body-building"}};
        for (String[] x : body) q(s,"Which group best describes " + x[0] + "?","Body-building food","Protective food only","Junk food","Plant part",capitalize(x[0])+" helps growth and body building.");
        String[][] protective = {{"orange","protective"},{"spinach","protective"},{"carrot","protective"},{"guava","protective"},{"tomato","protective"}};
        for (String[] x : protective) q(s,"Which type of food is " + x[0] + " mainly used as in Class 2 EVS?","Protective food","Junk food","Toy","Body part",capitalize(x[0])+" is a fruit or vegetable that helps protect health.");
        q(s,"Which breakfast is healthier?","Milk and poha","Chips and cola","Only candy","Only chocolate","A balanced breakfast is better than junk food.");
        q(s,"Which lunch is more balanced?","Roti, dal and vegetables","Only sweets","Only chips","Only ice cream","A balanced meal contains different food groups.");
        q(s,"Which drink is a healthy everyday choice?","Water","Soft drink all day","Syrup only","Cola only","Water is important for the body.");
        q(s,"Which should we wash before eating?","Fruits and vegetables","Pencils","Shoes only","Books","Washing fruits and vegetables helps keep them clean.");
        q(s,"What should we do before eating food?","Wash our hands","Play in mud","Touch the floor","Leave food uncovered","Clean hands help keep germs away.");
        q(s,"Which meal do we usually eat after waking up?","Breakfast","Dinner","Lunch","Supper at midnight","Breakfast is the first main meal of the day.");
        q(s,"Which food is obtained from an animal?","Milk","Wheat","Rice","Carrot","Milk comes from animals such as cows and buffaloes.");
        q(s,"Which food is obtained from a plant?","Pulses","Milk","Egg","Fish","Pulses come from plants.");

        // Expanded plant concepts.
        String[][] types = {{"Banyan","tree"},{"Neem","tree"},{"Coconut","tree"},{"Rose","shrub"},{"Hibiscus","shrub"},{"Mint","herb"},{"Coriander","herb"},{"Tulsi","herb"},{"Money plant","climber"},{"Grapevine","climber"},{"Pea plant","climber"},{"Pumpkin","creeper"},{"Watermelon","creeper"},{"Muskmelon","creeper"}};
        for (String[] x : types) q(s,"What type of plant is " + x[0] + "?",capitalize(x[1]),"Tree".equals(capitalize(x[1]))?"Herb":"Tree","Shrub","Creeper",x[0]+" is commonly classified as a "+x[1]+".");
        q(s,"What is the main work of roots?","Absorb water and hold the plant","Make the plant fly","Produce sound","Give light","Roots absorb water and anchor the plant.");
        q(s,"What is the main work of leaves?","Prepare food","Hold the plant in soil","Make cotton thread directly","Turn into stones","Leaves prepare food using sunlight, air and water.");
        q(s,"What is one important work of flowers?","Help form fruits and seeds","Absorb water from soil","Hold soil tightly","Carry school bags","Flowers help in forming fruits and seeds.");
        q(s,"What do seeds grow into?","New plants","Plastic","Rocks","Clouds","Seeds can grow into new plants.");
        q(s,"Where are seeds found in many plants?","Inside fruits","Inside stones","Inside plastic","Inside shoes","Many fruits contain seeds.");
        q(s,"Which plant part can we eat in carrot?","Root","Flower","Seed only","Bark","Carrot is an edible root.");
        q(s,"Which plant part can we eat in spinach?","Leaf","Root only","Wood","Thorn","Spinach leaves are eaten.");
        q(s,"Which plant part can we eat in cauliflower?","Flower","Root","Trunk","Thorn","Cauliflower is an edible flower part.");
        q(s,"Which useful thing do we get from trees such as mango and neem?","Shade","Plastic","Metal","Glass","Large trees can provide shade.");
        q(s,"Which plant grows well in a desert?","Cactus","Lotus","Rice plant in a pond","Water lily","Cactus is adapted to dry desert conditions.");
        q(s,"Which plant grows in water?","Lotus","Cactus","Neem","Rose","Lotus is an aquatic plant.");
        q(s,"Why do climbers need support?","Their stems are weak","Their roots are made of metal","They have no leaves","They are trees","Climbers have weak stems.");
        q(s,"Why do creepers spread on the ground?","Their stems are weak","They have strong trunks","They are made of wood","They grow underwater","Creepers have weak stems and spread along the ground.");
        return s;
    }

    private static Section buildTrueFalse() {
        Section s = new Section("Section 2: True or False", "Food and Plants - decide whether each statement is true.", "🔵");
        tf(s,"Food helps us grow and stay healthy.",true,"Food is needed for growth and health.");
        tf(s,"Milk helps make bones strong.",true,"Milk supports strong bones.");
        tf(s,"Green vegetables are protective foods.",true,"Vegetables provide vitamins and other nutrients.");
        tf(s,"Wheat comes from an animal.",false,"Wheat comes from a plant.");
        tf(s,"Rice and roti give us energy.",true,"They are energy-giving foods.");
        tf(s,"Chips and candy should be our main food every day.",false,"Healthy meals should include nutritious foods.");
        tf(s,"A balanced diet contains different kinds of useful foods.",true,"A balanced diet includes different food groups.");
        tf(s,"Lunch is usually eaten in the afternoon.",true,"Lunch is the afternoon meal.");
        tf(s,"Dinner is usually the first meal of the day.",false,"Breakfast is the first main meal; dinner is later.");
        tf(s,"We should wash our hands before eating.",true,"Clean hands help us stay healthy.");
        tf(s,"Pulses help our body grow.",true,"Pulses are body-building foods.");
        tf(s,"Fruits and vegetables help protect us from illness.",true,"They are protective foods.");
        tf(s,"All food comes only from plants.",false,"Food can come from plants and animals.");
        tf(s,"Egg is obtained from an animal source.",true,"Eggs come from birds such as hens.");
        tf(s,"Carrot is obtained from a plant.",true,"Carrot is a plant food.");
        tf(s,"Roots usually grow under the soil.",true,"Roots generally grow below the soil.");
        tf(s,"The stem helps hold a plant upright.",true,"The stem supports the plant.");
        tf(s,"Leaves prepare food for the plant.",true,"Leaves make food using sunlight, air and water.");
        tf(s,"Roots help hold the plant firmly in the ground.",true,"Roots anchor the plant.");
        tf(s,"Mango is a herb.",false,"Mango is a tree.");
        tf(s,"Rose is a shrub.",true,"Rose is a shrub.");
        tf(s,"Mint is a herb.",true,"Mint has a soft stem and is a herb.");
        tf(s,"Money plant is a climber.",true,"Money plant needs support to climb.");
        tf(s,"Pumpkin is commonly taught as a creeper.",true,"Pumpkin spreads along the ground.");
        tf(s,"Trees have strong woody trunks.",true,"Trees usually have strong woody stems or trunks.");
        tf(s,"Herbs usually have soft stems.",true,"Herbs are soft-stemmed plants.");
        tf(s,"Climbers have weak stems and need support.",true,"Climbers use support to grow upward.");
        tf(s,"Creepers grow straight up without support.",false,"Creepers spread on the ground.");
        tf(s,"Cotton comes from the cotton plant.",true,"Cotton fibre is obtained from the cotton plant.");
        tf(s,"Tulsi is commonly used as a medicinal plant.",true,"Tulsi is used traditionally as a medicinal plant.");
        tf(s,"Fruits often contain seeds.",true,"Many fruits have seeds inside them.");
        tf(s,"A seed can grow into a new plant.",true,"Seeds can germinate and grow.");
        tf(s,"The stem carries water to other parts of the plant.",true,"The stem transports water upward.");
        tf(s,"Cactus can store water in its stem.",true,"Cactus stores water in its thick stem.");
        tf(s,"Leaves use moonlight instead of sunlight to make food.",false,"Leaves use sunlight.");
        tf(s,"Lotus grows in water.",true,"Lotus is an aquatic plant.");
        tf(s,"Banyan is a tree.",true,"Banyan is a large tree.");
        tf(s,"Watermelon is a tree.",false,"Watermelon is a creeper.");
        tf(s,"Spinach is eaten for its leaves.",true,"We eat spinach leaves.");
        tf(s,"Carrot is an edible root.",true,"The carrot we eat is a root.");
        return s;
    }

    private static Section buildFill() {
        Section s = new Section("Section 3: Fill in the Blanks", "Choose the word that best completes each sentence.", "🟣");
        q(s,"We need food to grow and stay ______.","healthy","plastic","sleepy all day","noisy","Food helps us stay healthy.");
        q(s,"Milk helps make our ______ strong.","bones","books","pencils","shoes","Milk supports strong bones.");
        q(s,"Rice and roti are ______-giving foods.","energy","toy","flower","stone","Rice and roti give energy.");
        q(s,"Fruits and vegetables are ______ foods.","protective","plastic","junk-only","wooden","They help protect health.");
        q(s,"Milk, paneer and pulses are ______-building foods.","body","toy","house","paper","They support growth and body building.");
        q(s,"The first main meal of the day is ______.","breakfast","dinner","lunch","midnight snack","Breakfast is eaten after waking up.");
        q(s,"The afternoon meal is called ______.","lunch","breakfast","dinner","dessert","Lunch is eaten in the afternoon.");
        q(s,"The last main meal of the day is usually ______.","dinner","breakfast","lunch","brunch","Dinner is usually the last main meal.");
        q(s,"A diet with different useful food groups is a ______ diet.","balanced","plastic","sleepy","single-food","A balanced diet contains different nutrients.");
        q(s,"Food can come from plants and ______.","animals","stones","plastic","glass","We get foods from plants and animals.");
        q(s,"The part that grows under the soil is the ______.","root","leaf","flower","fruit","Roots usually grow under soil.");
        q(s,"The ______ holds the plant upright.","stem","fruit","seed","petal","The stem supports the plant.");
        q(s,"The ______ makes food for the plant.","leaf","root","seed","thorn","Leaves prepare food.");
        q(s,"Roots absorb water from the ______.","soil","sky","plastic","sun","Roots take water from soil.");
        q(s,"Big plants with strong woody trunks are called ______.","trees","herbs","creepers","grasses","Large woody plants are trees.");
        q(s,"Small plants with soft stems are called ______.","herbs","trees","rocks","fruits","Herbs have soft stems.");
        q(s,"Bushy woody plants with branches near the ground are ______.","shrubs","herbs","climbers","roots","Shrubs branch near the ground.");
        q(s,"Plants that need support to grow upward are ______.","climbers","trees","roots","flowers","Climbers need support.");
        q(s,"Plants that spread along the ground are ______.","creepers","trees","shrubs","flowers","Creepers spread on the ground.");
        q(s,"Mango is a ______.","tree","herb","creeper","climber","Mango is a tree.");
        q(s,"Rose is a ______.","shrub","tree","creeper","root","Rose is a shrub.");
        q(s,"Mint is a ______.","herb","tree","creeper","fruit","Mint is a herb.");
        q(s,"Grapevine is a ______.","climber","tree","shrub","root","Grapevine is a climber.");
        q(s,"Pumpkin is a ______.","creeper","tree","shrub","herb","Pumpkin is a creeper.");
        q(s,"Cotton fibre comes from the ______ plant.","cotton","mango","lotus","cactus","Cotton comes from cotton plants.");
        q(s,"Tulsi is often called a ______ plant.","medicinal","plastic","metal","stone","Tulsi is commonly used as a medicinal plant.");
        q(s,"Many fruits have ______ inside them.","seeds","stones only","plastic","leaves only","Many fruits contain seeds.");
        q(s,"A seed can grow into a new ______.","plant","stone","toy","cloud","Seeds can grow into plants.");
        q(s,"The stem carries ______ to other plant parts.","water","plastic","sand","toys","The stem transports water.");
        q(s,"A cactus can store ______ in its stem.","water","stones","plastic","air only","Cactus stores water.");
        q(s,"Leaves use ______ to prepare food.","sunlight","moonlight","plastic light","stars only","Leaves need sunlight.");
        q(s,"Lotus grows in ______.","water","desert sand only","a cupboard","plastic","Lotus grows in water.");
        q(s,"Carrot is an edible ______.","root","flower","seed","trunk","Carrot is a root.");
        q(s,"Spinach is eaten for its ______.","leaves","trunk","bark","thorns","Spinach leaves are eaten.");
        q(s,"Cauliflower is eaten for its ______ part.","flower","root","trunk","thorn","Cauliflower is a flower part.");
        return s;
    }

    private static Section buildWhoAmI() {
        Section s = new Section("Section 4: Who Am I?", "Guess the food, plant, part or plant type.", "🟠");
        q(s,"I am the first meal after you wake up. Who am I?","Breakfast","Lunch","Dinner","Dessert","Breakfast is the first main meal.");
        q(s,"I am eaten in the afternoon. Who am I?","Lunch","Breakfast","Dinner","Midnight snack","Lunch is the afternoon meal.");
        q(s,"I am usually the last main meal of the day. Who am I?","Dinner","Breakfast","Lunch","Snack","Dinner is usually eaten later in the day.");
        q(s,"I give energy and can be made into chapati. I come from wheat. Who am I?","Roti","Milk","Orange","Spinach","Roti is an energy-giving food made from wheat flour.");
        q(s,"I am white, can come from a cow or buffalo, and help build strong bones. Who am I?","Milk","Rice","Carrot","Mango","Milk supports growth and bones.");
        q(s,"I am made from milk and help build the body. Who am I?","Paneer","Chips","Candy","Soft drink","Paneer is a body-building food.");
        q(s,"I am a group that includes fruits and vegetables and helps protect health. Who am I?","Protective food","Junk food","Toy food","Plant stem","Fruits and vegetables are protective foods.");
        q(s,"I am a food plan containing different useful food groups. Who am I?","Balanced diet","Only sweets","Only rice","Only chips","A balanced diet contains varied healthy foods.");
        q(s,"I grow mostly under the soil and hold the plant firmly. Who am I?","Root","Leaf","Flower","Fruit","Roots anchor plants.");
        q(s,"I support the plant and carry water upward. Who am I?","Stem","Seed","Fruit","Petal","The stem supports and transports water.");
        q(s,"I am green in many plants and prepare food using sunlight. Who am I?","Leaf","Root","Seed","Fruit","Leaves make food.");
        q(s,"I may be colourful and later help make fruit and seeds. Who am I?","Flower","Root","Stem","Bark","Flowers help form fruits and seeds.");
        q(s,"I can grow into a new plant. Who am I?","Seed","Stone","Plastic bead","Leaf only","Seeds can grow into new plants.");
        q(s,"I am very tall and have a strong woody trunk. Who am I?","Tree","Herb","Creeper","Climber","Trees have strong woody trunks.");
        q(s,"I am small and have a soft green stem. Who am I?","Herb","Tree","Shrub","Rock","Herbs have soft stems.");
        q(s,"I am bushy with woody branches near the ground. Who am I?","Shrub","Tree","Creeper","Root","Shrubs are bushy woody plants.");
        q(s,"My stem is weak, so I need a stick or wall for support. Who am I?","Climber","Tree","Herb","Shrub","Climbers need support.");
        q(s,"My stem is weak, so I spread along the ground. Who am I?","Creeper","Tree","Shrub","Flower","Creepers spread on the ground.");
        q(s,"I give sweet yellow fruits in summer and I am a tree. Who am I?","Mango tree","Rose","Mint","Money plant","Mango trees give mangoes.");
        q(s,"I have flowers and thorns and I am a shrub. Who am I?","Rose plant","Neem tree","Grass","Pumpkin","Rose is a thorny flowering shrub.");
        q(s,"I climb with support and often grow in a pot. Who am I?","Money plant","Mango tree","Grass","Cactus","Money plant is a climber.");
        q(s,"I spread on the ground and give a large green fruit. Who am I?","Watermelon plant","Mango tree","Rose","Mint","Watermelon is a creeper.");
        q(s,"I grow in water and have a beautiful flower. Who am I?","Lotus","Cactus","Neem","Rose","Lotus grows in water.");
        q(s,"I store water in my thick stem and can live in a desert. Who am I?","Cactus","Lotus","Mint","Rose","Cactus stores water in its stem.");
        q(s,"People get fibre from me to make cotton cloth. Who am I?","Cotton plant","Mango tree","Lotus","Cactus","Cotton fibre comes from cotton plants.");
        q(s,"I am a common medicinal plant found in many Indian homes. Who am I?","Tulsi","Plastic plant","Iron rod","Stone","Tulsi is commonly used as a medicinal plant.");
        return s;
    }

    private static Section buildMatch() {
        Section s = new Section("Section 5: Match the Following", "Touch left and right items to connect correct pairs.", "🟡");
        s.matchQuestions.add(new MatchQ("Match each plant with its type.",new String[]{"Mango","Rose","Mint","Money plant","Pumpkin"},new String[]{"Tree","Shrub","Herb","Climber","Creeper"},new int[]{0,1,2,3,4}));
        s.matchQuestions.add(new MatchQ("Match each plant part with its work.",new String[]{"Root","Stem","Leaf","Flower"},new String[]{"Absorbs water","Carries water","Makes food","Helps form fruit"},new int[]{0,1,2,3}));
        s.matchQuestions.add(new MatchQ("Match the food with its main Class-2 food group.",new String[]{"Roti","Milk","Orange","Spinach"},new String[]{"Energy-giving","Body-building","Protective fruit","Protective vegetable"},new int[]{0,1,2,3}));
        s.matchQuestions.add(new MatchQ("Match the meal with the time.",new String[]{"Breakfast","Lunch","Dinner"},new String[]{"Morning","Afternoon","Evening/night"},new int[]{0,1,2}));
        s.matchQuestions.add(new MatchQ("Match each item with its source.",new String[]{"Wheat","Milk","Egg","Carrot"},new String[]{"Plant grain","Animal milk","Bird/animal source","Plant root"},new int[]{0,1,2,3}));
        s.matchQuestions.add(new MatchQ("Match the plant with a useful product or feature.",new String[]{"Cotton plant","Neem","Mango","Cactus"},new String[]{"Cotton fibre","Medicinal use","Fruit","Stores water"},new int[]{0,1,2,3}));
        s.matchQuestions.add(new MatchQ("Match the edible plant part.",new String[]{"Carrot","Spinach","Cauliflower","Mango"},new String[]{"Root","Leaf","Flower","Fruit"},new int[]{0,1,2,3}));
        s.matchQuestions.add(new MatchQ("Match the plant with where/how it grows.",new String[]{"Lotus","Cactus","Grapevine","Watermelon"},new String[]{"Water","Dry desert","Needs support","Spreads on ground"},new int[]{0,1,2,3}));
        s.matchQuestions.add(new MatchQ("Match the plant with its type.",new String[]{"Banyan","Hibiscus","Coriander","Pea plant"},new String[]{"Tree","Shrub","Herb","Climber"},new int[]{0,1,2,3}));
        return s;
    }

    private static Section buildThinking() {
        Section s = new Section("Section 6: Picture & Thinking", "Mixed visual-style and simple application questions.", "🔴");
        q(s,"🌳 A plant is tall with a thick woody trunk. What is it most likely?","Tree","Herb","Creeper","Climber","A thick woody trunk is a feature of a tree.");
        q(s,"🌿 A small plant has a soft green stem. What is it?","Herb","Tree","Rock","Creeper only","Soft-stemmed small plants are herbs.");
        q(s,"🌹 A bushy rose has woody branches near the ground. Which plant type is it?","Shrub","Tree","Herb","Creeper","Rose is a shrub.");
        q(s,"🪴 A money plant is growing up a stick. Why does it need the stick?","Its stem is weak","Its roots are metal","It has no leaves","It is a tree","Climbers have weak stems.");
        q(s,"🎃 A pumpkin vine is spreading across the soil. What type is it?","Creeper","Tree","Shrub","Herb","Pumpkin is a creeper.");
        q(s,"🌵 Which plant would be best suited to a very dry place?","Cactus","Lotus","Water lily","Rice plant","Cactus is adapted to dry places.");
        q(s,"🌸 Which plant would you expect to see growing in a pond?","Lotus","Cactus","Neem","Rose","Lotus grows in water.");
        q(s,"☀️ A plant is kept in darkness for a long time. Which thing is it missing for making food?","Sunlight","Plastic","Stones","Toys","Leaves need sunlight to prepare food.");
        q(s,"💧 Which part first absorbs water from the soil?","Roots","Flowers","Fruits","Seeds in a packet","Roots absorb water.");
        q(s,"⬆️ Water has entered through the roots. Which part carries it upward?","Stem","Fruit","Flower","Seed","The stem carries water to other parts.");
        q(s,"🍎 You cut open a fruit and see small structures inside. What are they likely to be?","Seeds","Roots","Trunks","Stems","Many fruits contain seeds.");
        q(s,"🌱 A seed gets water, air and suitable conditions. What can happen?","It can grow into a new plant","It turns into plastic","It becomes a stone","It becomes a toy","Seeds can germinate and grow.");
        q(s,"🍽️ Riya has only chips for lunch every day. Which is a better choice?","Roti, dal and vegetables","More chips","Only candy","Only cola","A balanced meal includes different healthy foods.");
        q(s,"🥛 Aarav wants food that helps his body grow. Which is a good choice?","Milk and paneer","Only candy","Only chips","Soft drink","Milk and paneer are body-building foods.");
        q(s,"⚡ Aarav needs energy for playing. Which meal item can help give energy?","Roti","Only cucumber water","Candy wrapper","Stone","Roti is an energy-giving food.");
        q(s,"🍊 Which snack is more protective and healthy?","Orange","Chips","Candy","Chocolate bar","Fruit is a protective food.");
        q(s,"🧼 Before eating an apple, what should you do?","Wash it and wash your hands","Drop it on the floor","Cover it with dust","Never wash it","Clean food and hands are healthier.");
        q(s,"🌳 You want shade in the garden. Which is the best choice?","Neem tree","Mint herb","Pumpkin creeper","Money plant","A large tree can provide shade.");
        q(s,"🧵 Which plant would a farmer grow to obtain cotton fibre?","Cotton plant","Mango tree","Lotus","Cactus","Cotton fibre comes from cotton plants.");
        q(s,"🌿 Which plant would you choose for a common medicinal herb at home?","Tulsi","Plastic flower","Iron rod","Stone","Tulsi is commonly used as a medicinal plant.");
        q(s,"🥕 You are eating carrot. Which plant part are you eating?","Root","Flower","Stem only","Seed","Carrot is a root.");
        q(s,"🥬 You are eating spinach. Which plant part are you eating?","Leaf","Root","Flower","Fruit","Spinach is eaten for its leaves.");
        q(s,"🥦 You are eating cauliflower. Which plant part is mainly eaten?","Flower","Root","Trunk","Thorn","Cauliflower is a flower part.");
        q(s,"🥭 You are eating mango. Which plant part is it?","Fruit","Root","Stem","Leaf","Mango is a fruit.");
        q(s,"🏡 A weak-stem plant is beside a fence and starts growing upward on it. What is it?","Climber","Tree","Shrub","Root","Climbers use support such as fences.");
        q(s,"🏞️ A weak-stem plant cannot climb and spreads across the ground. What is it?","Creeper","Tree","Shrub","Herb only","Creepers spread on the ground.");
        q(s,"🥗 Which plate looks most balanced?","Roti + dal + vegetables + salad","Only sweets","Only chips","Only ice cream","A balanced plate includes varied healthy foods.");
        q(s,"🌞 Which pair is needed by leaves to make food?","Sunlight and air","Plastic and stones","Toys and paper","Shoes and books","Leaves use sunlight, air and water to make food.");
        q(s,"🌱 If roots are badly damaged, which job becomes difficult first?","Taking water from soil","Making noise","Flying","Giving light","Roots absorb water from soil.");
        q(s,"🍉 A watermelon plant has a weak stem. Where will it usually spread?","Along the ground","High like a tree trunk","Underwater","Inside a cupboard","Watermelon is a creeper.");
        return s;
    }

    private static String capitalize(String v) {
        if (v == null || v.length() == 0) return v;
        return Character.toUpperCase(v.charAt(0)) + v.substring(1);
    }
}
