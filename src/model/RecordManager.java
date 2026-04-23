package model;

import java.io.*;
import java.util.*;

/**
 * =========================================================
 * RECORD MANAGER
 * =========================================================
 * - Lưu leaderboard vào file txt
 * - Load + sort giống SQL ORDER BY
 * - Lấy top record nhanh
 * =========================================================
 */
public class RecordManager {

    private static final String FILE = "record.txt";

    // =========================================================
    // LOAD ALL RECORDS (SQL: SELECT * ORDER BY score DESC)
    // =========================================================
    public static List<Record> loadAll() {

        List<Record> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] p = line.split("\\|");

                if (p.length < 3) continue;

                int score = Integer.parseInt(p[1]);
                int time = Integer.parseInt(p[2]);

                list.add(new Record(score, time));
            }

        } catch (Exception e) {
            // file chưa tồn tại → return empty list
        }

        // SQL: ORDER BY score DESC, time DESC
        list.sort((a, b) -> {

            if (b.getScore() != a.getScore())
                return b.getScore() - a.getScore();

            return b.getTime() - a.getTime();
        });

        return list;
    }

    // =========================================================
    // GET TOP 1 (FAST - NO FULL SORT NEEDED)
    // =========================================================
    public static Record getBestRecord() {

        Record best = new Record(0, 0);

        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] p = line.split("\\|");

                if (p.length < 3) continue;

                int score = Integer.parseInt(p[1]);
                int time = Integer.parseInt(p[2]);

                if (score > best.getScore()) {
                    best = new Record(score, time);
                }
            }

        } catch (Exception e) {
            // ignore
        }

        return best;
    }

    // =========================================================
    // ADD NEW RECORD (APPEND MODE)
    // =========================================================
    public static void add(int score, int time) {

        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE, true))) {

            // format: id|score|time
            pw.println(UUID.randomUUID() + "|" + score + "|" + time);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================================================
    // CLEAR FILE (OPTIONAL DEBUG)
    // =========================================================
    public static void clear() {

        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {
            pw.print("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}