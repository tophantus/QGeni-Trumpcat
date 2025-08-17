# 🌟 QGenI - Ứng dụng sinh câu hỏi IELTS
## 📝 Giới thiệu
QGenI là ứng dụng hỗ trợ học IELTS với tính năng nổi bật là tạo bộ câu hỏi từ một đoạn văn hoặc hình ảnh cho trước

## 🎯 Tính năng nổi bật: Tạo bộ câu hỏi IELTS tự động
### ✨ Tạo đề đọc
Người dùng sẽ dán một đoạn văn bản hoặc upload một file text (.txt), đồng thời nhập số câu hỏi. 
Hệ thống sẽ dựa trên phần văn bản đó, sinh ra đề bao gồm các câu hỏi dạng "True - False - Not Given". 

### ✨ Tạo đề nghe
Người dùng sẽ upload tối đa 4 hình aảnh, mỗi hình ảnh đại diện cho 1 chủ đề, mỗi chủ đề sẽ gồm 4 câu hỏi.
Hệ thống sẽ dựa trên các hình ảnh chủ đề để tìm kiếm các hình ảnh khác tương đương, sau đó tạo ra đề có dạng như sau:
Mỗi câu hỏi sẽ gồm 4 hình ảnh và 1 đoạn âm thanh, nhiệm vụ của người dùng là chọn hình ảnh đúng nhất với mô tả 
trong đoạn âm thanh đó.

## Video demo

Truy cập link sau để xem video demo ứng dụng: [QGenI](https://www.youtube.com/watch?v=fwUJMB63fGw)


---

# 🚀 Hướng dẫn cài đặt
## Yêu cầu
    - minSdk: 24 (Android 7.0 Nougat)
    - targetSdk: 34 (Tối ưu hóa cho Android 14)
    - JDK: 8 trở lên
    - Kotlin: 1.8 
## Trên Android Studio

1. Clone project và chạy trên Android Studio
2. Build và chạy 
                   
##  Trên thiết bị Android
1. Tải file APK tại [đây](https://github.com/LuongManhLinh/QGenI/releases/tag/QGenI)
2. Cài đặt file APK vừa tải về

## Lưu ý khi chạy
Khi chạy ứng dụng, ở màn hình đầu tiên, khi nhấn nút `Next` sẽ có một Dialog hiện ra, hãy nhập giá trị 
cho các trường `Host`, `Database Port`, `Image Generator Port` và `Host Control Port` dựa trên thông số mà 
Server đã được cài đặt. Đây là thông số cần thiết để ứng dụng có thể kết nối với Server.
Truy cập trang github cho QGenI Server tại [đây](https://github.com/LuongManhLinh/QGenI_Server)
