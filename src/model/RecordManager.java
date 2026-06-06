package model;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
/**
 * ============================================================================
 * UC 1.4: XEM BẢNG XẾP HẠNG (Lớp quản lý dữ liệu)
 * Chịu trách nhiệm chính trong việc giao tiếp với tệp tin lưu trữ (đọc/ghi dữ liệu điểm).
 * ============================================================================
 */
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

        //UC 1.4 - Bước 1.4.2a.1 (Luồng thay thế): Hệ thống nhận diện danh sách trả về rỗng
        //nếu không tìm thấy tệp tin lưu trữ điểm số.

        if (!file.exists()) return list;

        //UC 1.4 - Bước 1.4.2: Hệ thống thực hiện gửi yêu cầu truy xuất dữ liệu
        //bằng cách đọc từng dòng trong tệp tin lưu trữ (record.txt).

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
        //UC 1.4 - Bước 1.4.2a.1 (Trường hợp file trống): Hệ thống nhận diện danh sách trả về rỗng.
        if (list.isEmpty()) {
            return list;
        }

        //UC 1.4 - Bước 1.4.3: Hệ thống sắp xếp danh sách điểm số theo thứ tự giảm dần.
        //Phục vụ cho việc trích xuất Top 3 ở tầng View.

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