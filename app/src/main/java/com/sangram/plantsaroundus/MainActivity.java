package com.sangram.plantsaroundus;

import android.app.Activity;
import java.util.ArrayList;
import java.util.List;

/**
 * Compatibility/model holder retained for the original package structure.
 * The personalized game launches from GameActivity.
 */
public class MainActivity extends Activity {
    static class Q {
        String q;
        String[] a;
        int correct;
        String explain;

        Q(String q, String[] a, int correct, String explain) {
            this.q = q;
            this.a = a;
            this.correct = correct;
            this.explain = explain;
        }
    }

    static class MatchQ {
        String prompt;
        String[] left;
        String[] right;
        int[] correctMap;

        MatchQ(String prompt, String[] left, String[] right, int[] correctMap) {
            this.prompt = prompt;
            this.left = left;
            this.right = right;
            this.correctMap = correctMap;
        }
    }

    static class Section {
        String title;
        String subtitle;
        String emoji;
        List<Q> questions = new ArrayList<>();
        List<MatchQ> matchQuestions = new ArrayList<>();

        Section(String title, String subtitle, String emoji) {
            this.title = title;
            this.subtitle = subtitle;
            this.emoji = emoji;
        }
    }
}
