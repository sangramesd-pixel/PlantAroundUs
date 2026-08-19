package com.sangram.plantsaroundus;

import java.util.ArrayList;
import java.util.List;
import com.sangram.plantsaroundus.MainActivity.Q;
import com.sangram.plantsaroundus.MainActivity.Section;
import com.sangram.plantsaroundus.MainActivity.MatchQ;

class QuestionBank {
    static List<Section> create() {
        List<Section> sections = new ArrayList<>();

        Section mcq = new Section("Section 1: MCQ Zone", "Choose the correct answer.", "🟢");
        mcq.questions.add(new Q("Which of these is a tree?", new String[]{"Rose", "Mango", "Mint", "Pumpkin"}, 1, "Mango is a tree."));
        mcq.questions.add(new Q("Which of these is a shrub?", new String[]{"Banyan", "Rose", "Grass", "Money plant"}, 1, "Rose is a shrub."));
        mcq.questions.add(new Q("Which of these is a herb?", new String[]{"Mint", "Mango", "Neem", "Banyan"}, 0, "Mint is a herb."));
        mcq.questions.add(new Q("Which plant needs support to grow?", new String[]{"Mango", "Grapevine", "Neem", "Banyan"}, 1, "Grapevine is a climber."));
        mcq.questions.add(new Q("Which plant spreads along the ground?", new String[]{"Pumpkin", "Rose", "Mango", "Tulsi"}, 0, "Pumpkin is a creeper."));
        mcq.questions.add(new Q("Which plant has a thick hard trunk?", new String[]{"Tree", "Herb", "Creeper", "Climber"}, 0, "Trees have thick hard trunks."));
        mcq.questions.add(new Q("Which plant has a soft green stem?", new String[]{"Herb", "Tree", "Shrub", "None"}, 0, "Herbs have soft green stems."));
        mcq.questions.add(new Q("Which of these is a creeper?", new String[]{"Watermelon", "Grapevine", "Rose", "Neem"}, 0, "Watermelon grows on the ground."));
        mcq.questions.add(new Q("Which of these is a climber?", new String[]{"Money plant", "Mango", "Grass", "Pumpkin"}, 0, "Money plant climbs with support."));
        mcq.questions.add(new Q("Which is the biggest type of plant?", new String[]{"Herb", "Shrub", "Tree", "Creeper"}, 2, "Trees are the biggest plants."));
        mcq.questions.add(new Q("Which plant gives us mangoes?", new String[]{"Mango tree", "Rose plant", "Mint plant", "Money plant"}, 0, "Mango tree gives us mangoes."));
        mcq.questions.add(new Q("Which plant gives shade?", new String[]{"Big tree", "Grass", "Creeper", "Mint"}, 0, "Big trees give shade."));
        mcq.questions.add(new Q("Which one cannot stand straight by itself?", new String[]{"Grapevine", "Mango tree", "Neem tree", "Rose shrub"}, 0, "Grapevine needs support."));
        mcq.questions.add(new Q("Which pair is correct?", new String[]{"Mango – Herb", "Rose – Shrub", "Pumpkin – Tree", "Mint – Climber"}, 1, "Rose is a shrub."));
        mcq.questions.add(new Q("Which one is NOT a plant?", new String[]{"Tree", "Rose", "Dog", "Grass"}, 2, "Dog is an animal."));
        sections.add(mcq);

        Section tf = new Section("Section 2: True or False", "Read and choose True or False.", "🔵");
        tf.questions.add(new Q("All plants are of the same size.", new String[]{"True", "False"}, 1, "Plants can be big or small."));
        tf.questions.add(new Q("A mango plant is a tree.", new String[]{"True", "False"}, 0, "Mango is a tree."));
        tf.questions.add(new Q("Rose is a shrub.", new String[]{"True", "False"}, 0, "Rose is a shrub."));
        tf.questions.add(new Q("Herbs usually have soft stems.", new String[]{"True", "False"}, 0, "Herbs have soft stems."));
        tf.questions.add(new Q("Climbers need support to grow upward.", new String[]{"True", "False"}, 0, "Climbers need support."));
        tf.questions.add(new Q("Creepers spread on the ground.", new String[]{"True", "False"}, 0, "Creepers spread along the ground."));
        tf.questions.add(new Q("Pumpkin is a climber.", new String[]{"True", "False"}, 1, "Pumpkin is a creeper."));
        tf.questions.add(new Q("Money plant is a climber.", new String[]{"True", "False"}, 0, "Money plant is a climber."));
        tf.questions.add(new Q("Trees usually have strong woody trunks.", new String[]{"True", "False"}, 0, "Trees have strong trunks."));
        tf.questions.add(new Q("Mint is a herb.", new String[]{"True", "False"}, 0, "Mint is a herb."));
        sections.add(tf);

        Section fill = new Section("Section 3: Fill in the Blanks", "Choose the best word to complete the sentence.", "🟣");
        fill.questions.add(new Q("Very big and strong plants are called ________.", new String[]{"herbs", "trees", "creepers", "flowers"}, 1, "Big and strong plants are trees."));
        fill.questions.add(new Q("Small plants with soft stems are called ________.", new String[]{"shrubs", "trees", "herbs", "climbers"}, 2, "Small soft-stem plants are herbs."));
        fill.questions.add(new Q("A rose plant is a ________.", new String[]{"shrub", "tree", "herb", "creeper"}, 0, "Rose is a shrub."));
        fill.questions.add(new Q("Plants that need support to grow upward are called ________.", new String[]{"climbers", "creepers", "trees", "roots"}, 0, "Climbers need support."));
        fill.questions.add(new Q("Plants that spread along the ground are called ________.", new String[]{"climbers", "trees", "creepers", "buds"}, 2, "Creepers spread on the ground."));
        fill.questions.add(new Q("Mango is an example of a ________.", new String[]{"tree", "shrub", "herb", "climber"}, 0, "Mango is a tree."));
        fill.questions.add(new Q("Mint is an example of a ________.", new String[]{"tree", "herb", "shrub", "creeper"}, 1, "Mint is a herb."));
        fill.questions.add(new Q("Grapevine is an example of a ________.", new String[]{"shrub", "climber", "tree", "creeper"}, 1, "Grapevine is a climber."));
        fill.questions.add(new Q("Pumpkin is an example of a ________.", new String[]{"climber", "tree", "herb", "creeper"}, 3, "Pumpkin is a creeper."));
        fill.questions.add(new Q("Plants give us fruits and ________.", new String[]{"stones", "vegetables", "pencils", "bags"}, 1, "Plants give us fruits and vegetables."));
        sections.add(fill);

        Section who = new Section("Section 4: Who Am I?", "Guess the plant or plant type.", "🟠");
        who.questions.add(new Q("I am very tall and have a strong woody trunk. Who am I?", new String[]{"Tree", "Herb", "Climber", "Creeper"}, 0, "A tall plant with a trunk is a tree."));
        who.questions.add(new Q("I am small and have a soft green stem. Who am I?", new String[]{"Shrub", "Tree", "Herb", "Fruit"}, 2, "A soft stem plant is a herb."));
        who.questions.add(new Q("I am bushy and have many branches near the ground. Who am I?", new String[]{"Shrub", "Tree", "Root", "Leaf"}, 0, "A bushy plant is a shrub."));
        who.questions.add(new Q("My stem is weak, so I need support to grow upward. Who am I?", new String[]{"Creeper", "Climber", "Tree", "Herb"}, 1, "A plant that needs support is a climber."));
        who.questions.add(new Q("My stem is weak, so I spread along the ground. Who am I?", new String[]{"Shrub", "Tree", "Creeper", "Flower"}, 2, "A plant that spreads on the ground is a creeper."));
        who.questions.add(new Q("I give you sweet yellow fruits in summer. Who am I?", new String[]{"Rose plant", "Mango tree", "Mint plant", "Money plant"}, 1, "Mango tree gives mangoes."));
        who.questions.add(new Q("I have beautiful flowers and thorns. Who am I?", new String[]{"Rose plant", "Lotus", "Neem", "Grass"}, 0, "Rose plant has flowers and thorns."));
        who.questions.add(new Q("I spread along the ground and give a large green fruit. Who am I?", new String[]{"Mint", "Watermelon plant", "Rose", "Coconut"}, 1, "Watermelon plant spreads on the ground."));
        who.questions.add(new Q("I grow in water and have beautiful pink petals. Who am I?", new String[]{"Cactus", "Lotus", "Rose", "Mint"}, 1, "Lotus grows in water."));
        who.questions.add(new Q("I store water and grow in the desert. Who am I?", new String[]{"Cactus", "Pumpkin", "Money plant", "Tulsi"}, 0, "Cactus stores water."));
        sections.add(who);

        Section match = new Section("Section 5: Match the Following", "Connect the correct pair by touch.", "🟡");
        match.matchQuestions.add(new MatchQ("Match each plant with its type.",
                new String[]{"Mango", "Rose", "Mint", "Money plant", "Pumpkin"},
                new String[]{"Tree", "Shrub", "Herb", "Climber", "Creeper"},
                new int[]{0, 1, 2, 3, 4}));
        match.matchQuestions.add(new MatchQ("Match the plant part with its work.",
                new String[]{"Root", "Leaf", "Flower", "Stem"},
                new String[]{"Makes food", "Supports plant", "Becomes fruit", "Takes water"},
                new int[]{3, 0, 2, 1}));
        match.matchQuestions.add(new MatchQ("Match the plant with the place where it grows best.",
                new String[]{"Lotus", "Cactus", "Grapevine", "Banyan"},
                new String[]{"Needs support", "Water", "Desert", "Gives shade"},
                new int[]{1, 2, 0, 3}));
        sections.add(match);

        Section pic = new Section("Section 6: Picture & Thinking", "Observe and think before answering.", "🔴");
        pic.questions.add(new Q("🌳 What type of plant is a mango tree?", new String[]{"Herb", "Tree", "Creeper", "Climber"}, 1, "A mango tree is a tree."));
        pic.questions.add(new Q("🌹 Which type of plant is a rose?", new String[]{"Shrub", "Tree", "Herb", "Creeper"}, 0, "Rose is a shrub."));
        pic.questions.add(new Q("🌿 Identify the type of mint plant.", new String[]{"Herb", "Tree", "Climber", "Shrub"}, 0, "Mint is a herb."));
        pic.questions.add(new Q("🎃 How does a pumpkin plant grow?", new String[]{"Climbs a wall", "Spreads on the ground", "Grows into a big tree", "Grows underwater"}, 1, "Pumpkin spreads on the ground."));
        pic.questions.add(new Q("🪴 Why does a money plant need support?", new String[]{"Its stem is weak", "Its stem is thick", "It has no leaves", "It grows underground"}, 0, "It has a weak stem."));
        pic.questions.add(new Q("Riya wants to tie a swing to a plant. Which is safest?", new String[]{"Mango tree", "Mint plant", "Money plant", "Grass"}, 0, "A strong tree is safest."));
        pic.questions.add(new Q("A plant has a weak stem and is growing on a fence. What type is it?", new String[]{"Climber", "Tree", "Herb", "Shrub"}, 0, "A plant on a fence is a climber."));
        pic.questions.add(new Q("A plant has a weak stem and is spreading over the soil. What is it?", new String[]{"Creeper", "Tree", "Shrub", "Herb"}, 0, "A plant spreading on the ground is a creeper."));
        pic.questions.add(new Q("Which plant would you choose for shade in a garden?", new String[]{"Neem tree", "Mint", "Grass", "Pumpkin"}, 0, "Neem tree gives shade."));
        pic.questions.add(new Q("Which plant would need a stick or fence for support?", new String[]{"Grapevine", "Mango", "Neem", "Rose"}, 0, "Grapevine needs support."));
        pic.questions.add(new Q("Which group has only trees?", new String[]{"Mango, Neem, Banyan", "Rose, Mint, Mango", "Pumpkin, Grass, Neem", "Money plant, Rose, Mint"}, 0, "All three are trees."));
        pic.questions.add(new Q("Which plant usually grows in water?", new String[]{"Lotus", "Cactus", "Rose", "Mint"}, 0, "Lotus grows in water."));
        sections.add(pic);

        return sections;
    }
}
