# BUSINESS REQUIREMENTS DOCUMENT (BRD)
## Game: Egg Catcher (Desktop Offline)

---

## 1. Giới thiệu
“Egg Catcher” là một trò chơi giải trí đơn giản chạy trên máy tính (desktop), trong đó người chơi điều khiển một chiếc giỏ để bắt các quả trứng rơi từ phía trên màn hình. Mục tiêu của trò chơi là đạt điểm số cao nhất bằng cách bắt được càng nhiều trứng càng tốt và tránh để trứng rơi xuống đất.

Trò chơi được thiết kế hoạt động hoàn toàn offline, không yêu cầu kết nối Internet.

---

## 2. Mục tiêu kinh doanh (Business Requirements)

- **BR1 – Cung cấp trải nghiệm giải trí đơn giản**  
  Hệ thống phải mang lại trải nghiệm chơi dễ hiểu, dễ tiếp cận cho mọi đối tượng người dùng.

- **BR2 – Đảm bảo hoạt động offline**  
  Trò chơi phải hoạt động hoàn toàn offline trên máy tính mà không cần kết nối Internet.

- **BR3 – Tăng mức độ tương tác và giữ chân người chơi**  
  Trò chơi cần có cơ chế điểm số và độ khó tăng dần để khuyến khích người chơi quay lại.

- **BR4 – Phù hợp với nhiều đối tượng người dùng**  
  Trò chơi phải phù hợp với trẻ em, thanh thiếu niên và người chơi casual.

- **BR5 – Khả năng mở rộng trong tương lai**  
  Hệ thống cần cho phép mở rộng thêm các tính năng như leaderboard, multiplayer hoặc phiên bản online.

---

## 3. Đối tượng người dùng
- Trẻ em và thanh thiếu niên  
- Người chơi casual  
- Người dùng máy tính cá nhân (Windows / laptop)

---

## 4. Phạm vi hệ thống

### 4.1 Trong phạm vi (In-Scope)
- Game chạy offline trên desktop  
- Điều khiển bằng bàn phím  
- Đồ họa 2D cơ bản  
- Lưu trữ dữ liệu cục bộ  

### 4.2 Chức năng chính
- Điều khiển nhân vật để bắt trứng  
- Hệ thống tính điểm  
- Cấp độ khó tăng dần  
- Giao diện người dùng  
- Lưu điểm số cao nhất  

---

## 5. Yêu cầu chức năng (Functional Requirements)

### 5.1 Gameplay cơ bản
- **FR1**: Hệ thống cho phép người chơi di chuyển giỏ sang trái/phải bằng bàn phím.  
- **FR2**: Hệ thống tạo trứng rơi từ phía trên màn hình.  
- **FR3**: Hệ thống tăng điểm khi bắt được trứng.  
- **FR4**: Hệ thống trừ mạng hoặc kết thúc game khi trứng rơi xuống đất.  

### 5.2 Hệ thống điểm
- **FR5**: Mỗi trứng thường mang lại +1 điểm.  
- **FR6**: Hệ thống có thể có trứng đặc biệt với điểm cao hơn.  
- **FR7**: Hiển thị điểm theo thời gian thực.  
- **FR8**: Lưu và hiển thị điểm cao nhất.  

### 5.3 Cấp độ
- **FR9**: Tốc độ rơi của trứng tăng theo thời gian.  
- **FR10**: Số lượng trứng xuất hiện tăng dần.  
- **FR11**: Có thể thêm trứng đặc biệt (thưởng/phạt).  

### 5.4 Điều khiển
- **FR12**: Hệ thống hỗ trợ phím ← và → để di chuyển.  
- **FR13**: Phản hồi điều khiển nhanh, không độ trễ đáng kể.  

### 5.5 Giao diện người dùng
- **FR14**: Hiển thị màn hình chính (Start, Exit).  
- **FR15**: Hiển thị màn hình chơi game.  
- **FR16**: Hiển thị màn hình Game Over.  
- **FR17**: Hiển thị Score và Lives trong khi chơi.  

### 5.6 Lưu trữ dữ liệu
- **FR18**: Lưu điểm cao nhất vào file cục bộ.  
- **FR19**: Tải dữ liệu khi khởi động game.  

### 5.7 Quảng cáo (Optional)
- **FR20**: Hiển thị quảng cáo mô phỏng (banner/popup).  
- **FR21**: Quảng cáo xuất hiện khi Game Over hoặc Pause.  
- **FR22**: Không yêu cầu kết nối Internet.  

---

## 6. Yêu cầu phi chức năng (Non-functional Requirements)

### 6.1 Hiệu năng
- **NFR1**: Game chạy mượt trên máy cấu hình trung bình.  
- **NFR2**: Thời gian phản hồi < 100ms.  

### 6.2 Khả dụng
- **NFR3**: Dễ chơi, dễ hiểu.  
- **NFR4**: Giao diện đơn giản, thân thiện.  

### 6.3 Tương thích
- **NFR5**: Hỗ trợ Windows 10 trở lên.  

### 6.4 Độ ổn định
- **NFR6**: Không bị crash khi chơi lâu.  
- **NFR7**: Xử lý lỗi ổn định.  

---

## 7. Phạm vi mở rộng (Future Scope)
- Leaderboard  
- Multiplayer (local)  
- Power-ups  
- Âm thanh và animation nâng cao  
- Phiên bản online/mobile  

---

## 8. Tiêu chí thành công
- Người chơi có thể chơi ngay không cần hướng dẫn  
- Game hoạt động ổn định  
- Người chơi quay lại chơi nhiều lần  
- Hoàn thành đầy đủ chức năng  

---
