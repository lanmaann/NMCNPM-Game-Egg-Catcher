# 🥚 Egg Catcher Game (Desktop - Java Swing)

## 📌 Giới thiệu

**Egg Catcher** là một trò chơi giải trí đơn giản chạy trên máy tính (desktop), được phát triển bằng Java Swing.
Người chơi điều khiển một chiếc giỏ để bắt các quả trứng rơi từ phía trên màn hình nhằm đạt điểm số cao nhất.

Trò chơi hoạt động hoàn toàn **offline**, không yêu cầu kết nối Internet.

---

## 🎯 Mục tiêu

* Tạo một game giải trí nhẹ, dễ chơi
* Áp dụng lập trình Java (OOP, GUI - Swing)
* Minh họa quy trình phát triển phần mềm (BRD, Use Case, UML)

---

## 👤 Đối tượng người dùng

* Trẻ em và thanh thiếu niên
* Người chơi casual
* Người dùng máy tính cá nhân (Windows)

---

## 🎮 Gameplay

* Di chuyển giỏ bằng phím **← / →**
* Bắt trứng để tăng điểm
* Tránh để trứng rơi xuống đất
* Mỗi lần rơi trứng sẽ mất mạng
* Game kết thúc khi hết mạng

---

## ⚙️ Tính năng chính

* Hệ thống tính điểm (Score)
* Lưu điểm cao nhất (High Score)
* Độ khó tăng dần theo thời gian
* Lưu dữ liệu cục bộ
* Quảng cáo mô phỏng (optional)

---

## 🖥️ Công nghệ sử dụng

* Java
* Java Swing
* Lập trình hướng đối tượng (OOP)

---

## 📂 Cấu trúc project

```text
EggCatcher/
│── src/
│   ├── main/
│   │   ├── GamePanel.java
│   │   ├── GameFrame.java
│   │   ├── Player.java
│   │   ├── Egg.java
│   │   ├── GameManager.java
│   │
│   ├── utils/
│   │   ├── FileManager.java
│
│── assets/
│   ├── images/
│   ├── sounds/
│
│── README.md
```

---

## 🚀 Cách chạy chương trình

### Yêu cầu

* Java JDK 8 trở lên

### Chạy

```bash
javac *.java
java GameFrame
```

Hoặc chạy bằng IDE (IntelliJ IDEA, Eclipse, NetBeans)
