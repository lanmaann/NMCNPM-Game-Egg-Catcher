package model;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * =========================================================
 * RECORD MANAGER
 * =========================================================
 * lớp quản lý leaderboard và file lưu điểm
 * 
 * chức năng:
 * - tạo file lưu dữ liệu
 * - lưu record mới
 * - đọc danh sách record
 * - sắp xếp leaderboard
 * - lấy high score
 * =========================================================
 */
public class RecordManager {

    // thư mục lưu dữ liệu game
    private static final String DIR =
            System.getProperty("user.home") + "/EggCatcher";

    // file lưu leaderboard
    private static final String FILE =
            DIR + "/record.txt";

    // =========================================================
    // STATIC INIT
    // =========================================================

    /**
     * khởi tạo thư mục và file dữ liệu
     */
    static {

        File dir = new File(DIR);

        // tạo folder nếu chưa tồn tại
        if (!dir.exists()) {

            dir.mkdirs();
        }

        try {

            File file = new File(FILE);

            // tạo file nếu chưa tồn tại
            if (!file.exists()) {

                file.createNewFile();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =========================================================
    // SAVE RECORD
    // =========================================================

    /**
     * lưu record mới vào file
     */
    public static void add(int score, int time) {

        try (PrintWriter pw = new PrintWriter(

                new OutputStreamWriter(
                        new FileOutputStream(FILE, true),
                        StandardCharsets.UTF_8))) {

            // format: score|time
            pw.println(score + "|" + time);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =========================================================
    // LOAD ALL RECORDS
    // =========================================================

    /**
     * đọc toàn bộ record từ file
     */
    public static List<Record> loadAll() {

        List<Record> list = new ArrayList<>();

        File file = new File(FILE);

        // file chưa tồn tại
        if (!file.exists()) {

            return list;
        }

        try (BufferedReader br = new BufferedReader(

                new InputStreamReader(
                        new FileInputStream(file),
                        StandardCharsets.UTF_8))) {

            String line;

            // đọc từng dòng
            while ((line = br.readLine()) != null) {

                // loại bỏ ký tự thừa
                line = line.trim().replace("\r", "");

                // bỏ dòng rỗng
                if (line.isEmpty()) continue;

                // tách dữ liệu
                String[] p = line.split("\\|");

                // dữ liệu không hợp lệ
                if (p.length < 2) continue;

                try {

                    int score = Integer.parseInt(p[0].trim());

                    int time = Integer.parseInt(
                            p[1].trim().replace("\r", "")
                    );

                    // add record vào list
                    list.add(new Record(score, time));

                } catch (Exception e) {

                    System.out.println("❌ FAIL LINE: " + line);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        // =========================================================
        // SORT LEADERBOARD
        // =========================================================

        // sắp xếp điểm giảm dần
        list.sort((a, b) -> b.getScore() - a.getScore());

        return list;
    }

    // =========================================================
    // GET BEST RECORD
    // =========================================================

    /**
     * lấy record cao nhất
     */
    public static Record getBestRecord() {

        return loadAll()

                .stream()

                .max(Comparator.comparingInt(Record::getScore))

                // mặc định nếu chưa có record
                .orElse(new Record(0, 0));
    }

    // =========================================================
    // GET HIGH SCORE
    // =========================================================

    /**
     * lấy điểm cao nhất
     */
    public static int getHighScore() {

        return getBestRecord().getScore();
    }
}