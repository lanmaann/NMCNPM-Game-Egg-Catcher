# USE CASE DESCRIPTION
## Game: Egg Catcher (Desktop Offline)

---

## Actors
- **User1 – Player**: Người chơi tương tác trực tiếp với hệ thống

---

## UC1 – Start Game

- **Use Case ID**: UC1  
- **Use Case Name**: Start Game  
- **Primary Actor**: User1 – Player  
- **Description**: Người chơi bắt đầu trò chơi từ màn hình chính  

### Preconditions
- Ứng dụng đã được khởi động  
- Màn hình Main Menu đang hiển thị  

### Postconditions
- Game được khởi tạo thành công  
- Màn hình chuyển sang chế độ chơi  

### Main Flow
1. User1 chọn “Start Game”  
2. Hệ thống tải dữ liệu High Score từ bộ nhớ cục bộ  
3. Hệ thống khởi tạo các giá trị ban đầu (score = 0, lives mặc định, level = 1)  
4. Hệ thống chuyển sang màn hình chơi  

### Alternative Flows
- **AF1 – Không tìm thấy dữ liệu High Score**  
  - 2a. Hệ thống không tìm thấy file dữ liệu  
  - 2b. Hệ thống tạo dữ liệu mặc định (High Score = 0)  
  - 2c. Tiếp tục bước 3  

---

## UC2 – Play Game

- **Use Case ID**: UC2  
- **Use Case Name**: Play Game  
- **Primary Actor**: User1 – Player  
- **Description**: Người chơi điều khiển giỏ để bắt trứng và ghi điểm  

### Preconditions
- UC1 đã hoàn tất (game đã bắt đầu)  

### Postconditions
- Điểm số được cập nhật  
- Game tiếp tục hoặc chuyển sang trạng thái kết thúc  

### Main Flow
1. User1 sử dụng phím ← hoặc → để di chuyển giỏ  
2. Hệ thống tạo trứng rơi từ phía trên màn hình  
3. Hệ thống kiểm tra va chạm giữa giỏ và trứng  
4. Nếu trứng được bắt → hệ thống tăng điểm  
5. Hệ thống tiếp tục cập nhật trạng thái game  
6. Lặp lại cho đến khi người chơi hết mạng  

### Alternative Flows
- **AF1 – Người chơi không bắt được trứng**  
  - 3a. Trứng rơi xuống đất  
  - 3b. Hệ thống trừ 1 mạng  
  - 3c. Nếu còn mạng → quay lại bước 2  
  - 3d. Nếu hết mạng → chuyển sang UC3  

---

## UC3 – Game Over

- **Use Case ID**: UC3  
- **Use Case Name**: Game Over  
- **Primary Actor**: User1 – Player  
- **Description**: Hệ thống kết thúc trò chơi và hiển thị kết quả  

### Preconditions
- Người chơi đã hết mạng  

### Postconditions
- Điểm số được hiển thị  
- High Score được cập nhật (nếu cần)  

### Main Flow
1. Hệ thống dừng vòng lặp game  
2. Hệ thống hiển thị điểm số  
3. Hệ thống so sánh với High Score  
4. Nếu điểm cao hơn → cập nhật High Score  
5. Hiển thị tùy chọn “Play Again” hoặc “Exit”  

### Alternative Flows
- **AF1 – Điểm không vượt High Score**  
  - 4a. Không cập nhật dữ liệu  
  - 4b. Tiếp tục bước 5  

---

## UC4 – Exit Game

- **Use Case ID**: UC4  
- **Use Case Name**: Exit Game  
- **Primary Actor**: User1 – Player  
- **Description**: Người chơi thoát khỏi trò chơi  

### Preconditions
- Game đang ở Main Menu hoặc Game Over  

### Postconditions
- Ứng dụng được đóng  

### Main Flow
1. User1 chọn “Exit”  
2. Hệ thống đóng ứng dụng  

---
