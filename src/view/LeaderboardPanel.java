package view;

import model.Record;
import model.RecordManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Lớp giao diện bảng xếp hạng của trò chơi.
 * 
 * Chức năng:
 * - Hiển thị top điểm cao nhất
 * - Đọc dữ liệu record từ file
 * - Hiển thị danh sách leaderboard
 * - Quay lại menu chính
 */
public class LeaderboardPanel extends JPanel {

    /**
     * Constructor khởi tạo leaderboard panel.
     * 
     * @param onBack callback quay lại menu chính
     */
    public LeaderboardPanel(Runnable onBack) {

        // Layout chính
        setLayout(new BorderLayout());

        // Màu nền panel
        setBackground(new Color(20, 25, 45));

        /**
         * Header gồm:
         * - Nút BACK
         * - Tiêu đề leaderboard
         */
        JPanel header =
                createHeader(onBack);

        add(header, BorderLayout.NORTH);

        /**
         * Panel hiển thị danh sách record.
         */
        JPanel listPanel =
                new JPanel();

        listPanel.setLayout(
                new BoxLayout(
                        listPanel,
                        BoxLayout.Y_AXIS
                )
        );

        listPanel.setBackground(
                new Color(20, 25, 45)
        );

        /**
         * Hệ thống tải toàn bộ dữ liệu
         * từ lớp RecordManager.
         */
        List<Record> list =
                RecordManager.loadAll();

        /**
         * Trường hợp chưa có dữ liệu.
         */
        if (list.isEmpty()) {

            JLabel empty =
                    new JLabel(
                            "NO RECORD YET!",
                            SwingConstants.CENTER
                    );

            empty.setForeground(
                    new Color(255, 215, 0)
            );

            empty.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            24
                    )
            );

            empty.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );

            listPanel.add(
                    Box.createVerticalStrut(80)
            );

            listPanel.add(empty);
        }

        /**
         * Hiển thị top 3 record.
         */
        else {

            for (
                    int i = 0;
                    i < Math.min(3, list.size());
                    i++
            ) {

                Record r = list.get(i);

                int rank = i + 1;

                JPanel row =
                        createRow(rank, r);

                listPanel.add(row);

                listPanel.add(
                        Box.createVerticalStrut(8)
                );
            }
        }

        /**
         * Scroll pane chứa leaderboard.
         */
        JScrollPane scroll =
                new JScrollPane(listPanel);

        scroll.setBorder(null);

        scroll.getVerticalScrollBar()
                .setUnitIncrement(12);

        scroll.setBackground(
                new Color(20, 25, 45)
        );

        add(scroll, BorderLayout.CENTER);
    }

    /**
     * Tạo phần header của leaderboard.
     * 
     * @param onBack callback quay lại menu
     * @return JPanel header
     */
    private JPanel createHeader(
            Runnable onBack
    ) {

        /**
         * Nút BACK.
         */
        JButton back =
                new JButton("BACK");

        back.setFocusPainted(false);

        back.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        back.setBackground(
                new Color(230, 230, 230)
        );

        back.setPreferredSize(
                new Dimension(90, 35)
        );

        /**
         * Người chơi quay lại
         * màn hình chính.
         */
        back.addActionListener(e -> {

            if (onBack != null) {

                onBack.run();
            }
        });

        /**
         * Tiêu đề leaderboard.
         */
        JLabel title =
                new JLabel(
                        "TOP 3 LEADERBOARD",
                        SwingConstants.CENTER
                );

        title.setForeground(Color.WHITE);

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        26
                )
        );

        /**
         * Panel header.
         */
        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(
                new Color(20, 25, 45)
        );

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

        return header;
    }

    /**
     * Tạo card hiển thị record.
     * 
     * @param rank thứ hạng
     * @param r dữ liệu record
     * @return JPanel chứa record
     */
    private JPanel createRow(
            int rank,
            Record r
    ) {

        JPanel panel =
                new JPanel();

        panel.setLayout(
                new BorderLayout()
        );

        panel.setPreferredSize(
                new Dimension(300, 45)
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        8,
                        15,
                        8,
                        15
                )
        );

        // Background card
        panel.setBackground(
                new Color(40, 45, 70)
        );

        /**
         * Nội dung record.
         */
        JLabel text =
                new JLabel(
                        rank
                                + ".  Score: "
                                + r.getScore()
                                + "   |   Time: "
                                + r.getTime()
                                + "s"
                );

        text.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        /**
         * Màu hiển thị theo thứ hạng.
         */
        if (rank == 1) {

            // Gold
            text.setForeground(
                    new Color(255, 215, 0)
            );
        }

        else if (rank == 2) {

            // Silver
            text.setForeground(
                    new Color(180, 180, 180)
            );
        }

        else if (rank == 3) {

            // Bronze
            text.setForeground(
                    new Color(205, 127, 50)
            );
        }

        else {

            text.setForeground(Color.WHITE);
        }

        panel.add(text, BorderLayout.CENTER);

        return panel;
    }
}