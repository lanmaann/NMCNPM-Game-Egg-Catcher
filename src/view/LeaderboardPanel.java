package view;

import model.Record;
import model.RecordManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * =========================================================
 * LEADERBOARD PANEL
 * =========================================================
 * giao diện hiển thị bảng xếp hạng
 * 
 * chức năng:
 * - hiển thị top điểm cao nhất
 * - đọc dữ liệu record từ file
 * - hiển thị danh sách leaderboard
 * - quay lại menu chính
 * =========================================================
 */
public class LeaderboardPanel extends JPanel {

    /**
     * constructor khởi tạo leaderboard panel
     */
    public LeaderboardPanel(Runnable onBack) {

        // layout chính
        setLayout(new BorderLayout());

        // màu nền
        setBackground(new Color(20, 25, 45));

        // =========================================================
        // HEADER
        // =========================================================

        // nút back
        JButton back = new JButton("BACK");

        back.setFocusPainted(false);

        back.setFont(new Font("Arial", Font.BOLD, 14));

        back.setBackground(new Color(230, 230, 230));

        back.setPreferredSize(new Dimension(90, 35));

        /**
         * 1.4.5
         * người chơi nhấn nút quay lại
         * để quay lại màn hình bắt đầu game
         */
        back.addActionListener(e -> onBack.run());

        // title
        JLabel title =
                new JLabel(
                        "TOP 3 LEADERBOARD",
                        SwingConstants.CENTER
                );

        title.setForeground(Color.WHITE);

        title.setFont(new Font("Arial", Font.BOLD, 26));

        // panel header
        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(new Color(20, 25, 45));

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        10,
                        10,
                        10
                )
        );

        header.add(back, BorderLayout.WEST);

        header.add(title, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);

        // =========================================================
        // LIST PANEL
        // =========================================================

        JPanel listPanel = new JPanel();

        listPanel.setLayout(
                new BoxLayout(
                        listPanel,
                        BoxLayout.Y_AXIS
                )
        );

        listPanel.setBackground(new Color(20, 25, 45));

        // =========================================================
        // LOAD RECORDS
        // =========================================================

        /**
         * 1.4.2
         * hệ thống gửi yêu cầu truy xuất dữ liệu
         * đến lớp quản lý dữ liệu RecordManager
         */
        List<Record> list = RecordManager.loadAll();

        // =========================================================
        // EMPTY RECORD
        // =========================================================

        if (list.isEmpty()) {

            /**
             * ALT 1.4.2a.2
             * hiển thị thông báo không có dữ liệu
             */
            JLabel empty =
                    new JLabel(
                            "NO RECORD YET!",
                            SwingConstants.CENTER
                    );

            empty.setForeground(new Color(255, 215, 0));

            empty.setFont(new Font("Arial", Font.BOLD, 24));

            empty.setAlignmentX(Component.CENTER_ALIGNMENT);

            listPanel.add(Box.createVerticalStrut(80));

            listPanel.add(empty);
        }

        // =========================================================
        // SHOW TOP RECORDS
        // =========================================================

        else {

            /**
             * 1.4.4
             * hiển thị top 3 điểm cao nhất
             */
            for (int i = 0; i < Math.min(3, list.size()); i++) {

                Record r = list.get(i);

                int rank = i + 1;

                JPanel row = createRow(rank, r);

                listPanel.add(row);

                listPanel.add(Box.createVerticalStrut(8));
            }
        }

        // =========================================================
        // SCROLL PANE
        // =========================================================

        JScrollPane scroll = new JScrollPane(listPanel);

        scroll.setBorder(null);

        scroll.getVerticalScrollBar().setUnitIncrement(12);

        scroll.setBackground(new Color(20, 25, 45));

        add(scroll, BorderLayout.CENTER);
    }

    // =========================================================
    // RECORD ROW
    // =========================================================

    /**
     * tạo card hiển thị record
     */
    private JPanel createRow(int rank, Record r) {

        JPanel panel = new JPanel();

        panel.setLayout(new BorderLayout());

        panel.setPreferredSize(new Dimension(300, 45));

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        8,
                        15,
                        8,
                        15
                )
        );

        // màu nền card
        panel.setBackground(new Color(40, 45, 70));

        // text record
        JLabel text = new JLabel(
                rank + ".  Score: " + r.getScore() +
                "   |   Time: " + r.getTime() + "s"
        );

        text.setFont(new Font("Arial", Font.BOLD, 14));

        // =========================================================
        // RANK COLOR
        // =========================================================

        // hạng 1 - vàng
        if (rank == 1) {

            text.setForeground(new Color(255, 215, 0));
        }

        // hạng 2 - bạc
        else if (rank == 2) {

            text.setForeground(new Color(180, 180, 180));
        }

        // hạng 3 - đồng
        else if (rank == 3) {

            text.setForeground(new Color(205, 127, 50));
        }

        // mặc định
        else {

            text.setForeground(Color.WHITE);
        }

        panel.add(text, BorderLayout.CENTER);

        return panel;
    }
}