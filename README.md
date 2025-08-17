# 📚 QGenI - English Learning Application

[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://android.com)
[![Backend](https://img.shields.io/badge/Backend-Python%20Flask-blue.svg)](https://flask.palletsprojects.com/)
[![Frontend](https://img.shields.io/badge/Frontend-Kotlin%20Jetpack%20Compose-orange.svg)](https://developer.android.com/jetpack/compose)
[![Database](https://img.shields.io/badge/Database-SQLite-lightgrey.svg)](https://sqlite.org/)

QGenI là một ứng dụng học tiếng Anh toàn diện được phát triển trong kỳ thực tập, tích hợp các tính năng từ điển, luyện tập đề thi, trò chơi giáo dục và trợ lý AI thông minh.

## 🌟 Tính năng chính

- **📖 Từ điển**: Tra cứu từ vựng với gợi ý và lịch sử tìm kiếm
- **📝 Giải đề**: Giải đề với các dạng bài đọc hiểu và nghe hiểu
- **🎮 Trò chơi**: Game Wordle tiếng Anh
- **🤖 Trợ lý**: Chatbot hỗ trợ học tập và giải đáp thắc mắc

## 🛠️ Công nghệ sử dụng

### Frontend

- **Kotlin** với **Jetpack Compose** - UI framework hiện đại cho Android
- **Material Design 3** - Thiết kế giao diện theo chuẩn Google

### Backend

- **Python Flask** - Web framework nhẹ và linh hoạt
- **RESTful API** - Kiến trúc API chuẩn REST
- **JWT Authentication** - Bảo mật xác thực người dùng

### Database

- **SQLite** - Cơ sở dữ liệu nhẹ và hiệu quả
- **ORM Models** - Quản lý dữ liệu thông qua các model

## 📱 Demo giao diện

[Video demo](https://drive.google.com/file/d/17mOcv0rEja6cdYkt8UbPh7QZeuzRd9S_/view?usp=sharing)

### 1. Màn hình chào mừng và đăng nhập

<div align="center">
  <img src="imgs/1-welcome.png" width="300" alt="Welcome Screen"/>
  <img src="imgs/2.1-login.png" width="300" alt="Login Screen"/>
  <img src="imgs/2.2-signup.png" width="300" alt="Signup Screen"/>
</div>

Giao diện chào mừng và hệ thống đăng nhập/đăng ký an toàn với xác thực JWT.

### 2. Trang chủ với trợ lý AI

<div align="center">
  <img src="imgs/3-home-with-assistant.png" width="300" alt="Home with Assistant"/>
</div>

Trang chủ tích hợp trợ lý AI.

### 3. Tính năng từ điển (Dictionary)

<div align="center">
  <img src="imgs/4.1-dict-nav.png" width="250" alt="Dictionary Navigation"/>
  <img src="imgs/4.2-dict-suggestions.png" width="250" alt="Dictionary Suggestions"/>
  <img src="imgs/4.3-dict-word-description.png" width="250" alt="Word Description"/>
</div>

<div align="center">
  <img src="imgs/4.4-dict-hist.png" width="300" alt="Dictionary History"/>
  <img src="imgs/4.5-dict-hist-deleting.png" width="300" alt="Deleting History"/>
</div>

Từ điển thông minh với tính năng:

- Gợi ý từ khi nhập
- Hiển thị định nghĩa chi tiết
- Lưu lịch sử tra cứu
- Quản lý lịch sử

### 4. Tính năng luyện tập (Practice)

<div align="center">
  <img src="imgs/5.1-pr-nav.png" width="200" alt="Practice Navigation"/>
  <img src="imgs/5.2-pr-list.png" width="200" alt="Practice List"/>
  <img src="imgs/5.3-pr-reading.png" width="200" alt="Reading Practice"/>
  <img src="imgs/5.4-pr-listening.png" width="200" alt="Listening Practice"/>
</div>

<div align="center">
  <img src="imgs/5.5-pr-explain-correct.png" width="300" alt="Correct Answer Explanation"/>
  <img src="imgs/5.6-pr-explain-incorrect.png" width="300" alt="Incorrect Answer Explanation"/>
</div>

<div align="center">
  <img src="imgs/5.7-pr-ranking.png" width="300" alt="Practice Ranking"/>
</div>

Hệ thống luyện tập đa dạng bao gồm:

- Bài tập đọc hiểu (Reading)
- Bài tập nghe hiểu (Listening)
- Giải thích chi tiết đáp án
- Hệ thống xếp hạng

### 5. Chatbot trợ lý

<div align="center">
  <img src="imgs/6-chatbot.png" width="300" alt="Chatbot Interface"/>
</div>

Trợ lý AI hỗ trợ học tập, giải đáp thắc mắc.

### 6. Trò chơi Wordle

<div align="center">
  <img src="imgs/7-wordle.png" width="300" alt="Wordle Game"/>
</div>

Game Wordle tiếng Anh giúp cải thiện vốn từ vựng.

### 7. Thông tin người dùng và thống kê

<div align="center">
  <img src="imgs/8.1-usr-inf.png" width="300" alt="User Information"/>
  <img src="imgs/8.2-stat.png" width="300" alt="Statistics"/>
</div>

Quản lý thông tin cá nhân và theo dõi thống kê học tập chi tiết.

## 🏗️ Cấu trúc dự án

```
QGenI-Trumpcat/
├── backend/                 # Python Flask Backend
│   ├── app/                # Application modules
│   │   ├── chatbot/       # Chatbot functionality
│   │   ├── favorite/      # Favorites management
│   │   ├── file/          # File handling
│   │   ├── history/       # History tracking
│   │   ├── practice/      # Practice exercises
│   │   ├── user/          # User management
│   │   ├── word/          # Dictionary features
│   │   └── wordle/        # Wordle game
│   ├── data/              # Application data
│   └── test/              # Unit tests
├── frontend/              # Android Kotlin App
│   └── app/              # Main application
└── imgs/                 # Demo screenshots
```

## 🚀 Cài đặt và chạy ứng dụng

### Yêu cầu hệ thống

- Python 3.8+
- Android Studio Arctic Fox+
- Java 11+
- SQLite 3

### Backend Setup

```bash
cd backend
pip install -r requirements.txt
GEMINI_API_KEY=your_gemini_api_key python run.py
```

### Frontend Setup

1. Mở Android Studio
2. Import project từ thư mục `frontend/`
3. Sync Gradle dependencies
4. Cập nhật endpoint API (Backend URL) trong file `build.gradle` (và `/res/xml/network_security_config.xml` nếu như dùng `HTTP` thay vì `HTTPS`)
5. Chạy ứng dụng trên thiết bị/emulator

## 🔧 API Documentation

Backend cung cấp RESTful API với các endpoint chính:

- `/api/auth/` - Xác thực người dùng
- `/api/word/` - Tra cứu từ điển
- `/api/practice/` - Bài tập luyện tập
- `/api/chatbot/` - Trợ lý AI
- `/api/wordle/` - Game Wordle
- `/api/user/` - Quản lý người dùng

## 📊 Database Schema

Ứng dụng sử dụng SQLite với các bảng chính:

- `user` - Thông tin người dùng
- `word` - Từ vựng và định nghĩa
- `reading` và `listening` - Bài tập và câu hỏi
- `history` - Lịch sử hoạt động
