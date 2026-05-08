package model;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class RecordManager {

    private static final String DIR =
            System.getProperty("user.home") + "/EggCatcher";

    private static final String FILE =
            DIR + "/record.txt";

    static {
        File dir = new File(DIR);
        if (!dir.exists()) dir.mkdirs();

        try {
            File file = new File(FILE);
            if (!file.exists()) file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // SAVE
    // =========================
    public static void add(int score, int time) {

        try (PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(
                        new FileOutputStream(FILE, true),
                        StandardCharsets.UTF_8))) {

            pw.println(score + "|" + time);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // LOAD
    // =========================
    public static List<Record> loadAll() {

        List<Record> list = new ArrayList<>();

        File file = new File(FILE);

        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(file),
                        StandardCharsets.UTF_8))) {

            String line;

            while ((line = br.readLine()) != null) {

                line = line.trim().replace("\r", "");

                if (line.isEmpty()) continue;

                String[] p = line.split("\\|");

                if (p.length < 2) continue;

                try {
                    int score = Integer.parseInt(p[0].trim());
                    int time = Integer.parseInt(p[1].trim().replace("\r", ""));

                    list.add(new Record(score, time));

                } catch (Exception e) {
                    System.out.println("❌ FAIL LINE: " + line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        list.sort((a, b) -> b.getScore() - a.getScore());

        return list;
    }

    // =========================
    // BEST
    // =========================
    public static Record getBestRecord() {

        return loadAll()
                .stream()
                .max(Comparator.comparingInt(Record::getScore))
                .orElse(new Record(0, 0));
    }
    public static int getHighScore() {
        return getBestRecord().getScore();
    }
}