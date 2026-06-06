package view;

import model.Record;
import model.RecordManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;
/**
 * ============================================================================
 * UC 1.4: XEM BẢNG XẾP HẠNG
 * Màn hình hiển thị danh sách 3 mức điểm số cao nhất mà người chơi đã đạt được.
 * ============================================================================
 */
public class LeaderboardPanel extends JPanel {

    public LeaderboardPanel(Runnable onBack) {

        setLayout(new BorderLayout());
        setBackground(new Color(20, 25, 45));

     // =========================
     // HEADER (nút BACK + TITLE)
     // =========================
     JButton back = new JButton("BACK");
     back.setFocusPainted(false);
     back.setFont(new Font("Arial", Font.BOLD, 14));
     back.setBackground(new Color(230, 230, 230));
     back.setPreferredSize(new Dimension(90, 35));

     //UC 1.4 - Bước 1.4.5: Người chơi nhấn nút quay lại để quay lại màn hình bắt đầu game.

     back.addActionListener(e -> onBack.run());

     JLabel title = new JLabel("TOP 3 LEADERBOARD", SwingConstants.CENTER);
     title.setForeground(Color.WHITE);
     title.setFont(new Font("Arial", Font.BOLD, 26));

     JPanel header = new JPanel(new BorderLayout());

     header.setBackground(new Color(20, 25, 45));
     header.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));

     header.add(back, BorderLayout.WEST);
     header.add(title, BorderLayout.CENTER);

     add(header, BorderLayout.NORTH);

        // =========================
        // CENTER LIST (SCROLL)
        // =========================
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(new Color(20, 25, 45));
        //UC 1.4 - Bước 1.4.2: Hệ thống gửi yêu cầu truy xuất dữ liệu đến lớp quản lý dữ liệu
        //(RecordManager) và đọc tệp tin lưu trữ.
        List<Record> list = RecordManager.loadAll();
        //UC 1.4 - Bước 1.4.2a.1: Hệ thống nhận diện danh sách trả về rỗng (hoặc không thấy file).
        //Áp dụng Luồng thay thế (Alternative Flow) khi dữ liệu trống.

        if (list.isEmpty()) {
            //UC 1.4 - Bước 1.4.2a.2: Hệ thống hiển thị thông báo "No Record Yet".
            //UC 1.4 - Bước 1.4.2a.3: Kết thúc Use Case.
            JLabel empty = new JLabel("NO RECORD YET!", SwingConstants.CENTER);

            empty.setForeground(new Color(255, 215, 0));
            empty.setFont(new Font("Arial", Font.BOLD, 24));

            empty.setAlignmentX(Component.CENTER_ALIGNMENT);

            listPanel.add(Box.createVerticalStrut(80));
            listPanel.add(empty);

        } else {
            //UC 1.4 - Bước 1.4.3: Hệ thống sắp xếp danh sách điểm số theo thứ tự giảm dần.
            //(Giả định danh sách trả về từ RecordManager.loadAll() đã được sắp xếp giảm dần).
            //UC 1.4 - Bước 1.4.4 & Postconditions: Hệ thống hiển thị danh sách Top 3 điểm số
            //lên một màn hình mới (Duyệt tối đa 3 bản ghi đầu tiên).
            for (int i = 0; i < Math.min(3, list.size()); i++) {

                Record r = list.get(i);
                int rank = i + 1;

                JPanel row = createRow(rank, r);

                listPanel.add(row);
                listPanel.add(Box.createVerticalStrut(8));
            }
        }

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        scroll.setBackground(new Color(20, 25, 45));

        add(scroll, BorderLayout.CENTER);

      
    }

    // =========================
    // ROW UI CARD
    // =========================
    private JPanel createRow(int rank, Record r) {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(300, 45));

        panel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        // background card
        panel.setBackground(new Color(40, 45, 70));

        JLabel text = new JLabel(
                rank + ".  Score: " + r.getScore() +
                "   |   Time: " + r.getTime() + "s"
        );

        text.setFont(new Font("Arial", Font.BOLD, 14));

        // =========================
        // COLOR RANK STYLE
        // =========================
        if (rank == 1) text.setForeground(new Color(255, 215, 0));      // gold
        else if (rank == 2) text.setForeground(new Color(180, 180, 180)); // silver
        else if (rank == 3) text.setForeground(new Color(205, 127, 50));  // bronze
        else text.setForeground(Color.WHITE);

        panel.add(text, BorderLayout.CENTER);

        return panel;
    }
}