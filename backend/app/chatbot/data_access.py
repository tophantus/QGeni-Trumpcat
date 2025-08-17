from .models import ChatConversation, ChatbotHistory


class ChatbotHistoryService:
    @staticmethod
    def create_conversation(session, user_id):
        conv = ChatConversation(user_id=user_id)
        session.add(conv)
        session.commit()
        session.refresh(conv)
        return conv

    @staticmethod
    def save_message(session, conversation_id, role, content):
        msg = ChatbotHistory(
            conversation_id=conversation_id,
            role=role,
            content=content
        )
        session.add(msg)
        session.commit()

    @staticmethod
    def build_context(session, conversation_id, new_prompt):
        messages = (
            session.query(ChatbotHistory)
                .filter(ChatbotHistory.conversation_id == conversation_id)
                .order_by(ChatbotHistory.created_at.asc())
                .all()
        )
        context = "\n".join(
            [f"{m.role.capitalize()}: {m.content}" for m in messages]
        )

        prompt = f"""
Bạn là một trợ lý dạy tiếng Anh hiệu quả và thân thiện.
Nhiệm vụ của bạn là trả lời NGẮN GỌN, RÕ RÀNG các câu hỏi LIÊN QUAN ĐẾN VIỆC HỌC TIẾNG ANH từ phía người dùng.

Các câu hỏi liên quan có thể bao gồm (nhưng không giới hạn ở):
- DỊCH VĂN BẢN
- TÌM VĂN BẢN TƯƠNG ĐƯƠNG hoặc câu tương đương trong tiếng Anh
- DỊCH VÀ GIẢI THÍCH THÀNH NGỮ, TỤC NGỮ
- GIẢI NGHĨA TỪ VỰNG, CỤM TỪ TIẾNG ANH
- HỎI VỀ NGỮ PHÁP, CẤU TRÚC hoặc cách sử dụng tiếng Anh trong từng trường hợp
- Các câu hỏi GÓP Ý, TƯ VẤN PHƯƠNG PHÁP HỌC TIẾNG ANH hiệu quả

Nếu người dùng hỏi những nội dung KHÔNG liên quan đến tiếng Anh hoặc học tiếng Anh,
hãy đáp lại lịch sự rằng: "Tôi chỉ có thể trả lời các câu hỏi liên quan đến tiếng Anh hoặc học tiếng Anh."

Nếu người dùng hỏi các câu hỏi xã giao (ví dụ: chào hỏi, hỏi thăm), bạn hãy trả lời, chào hỏi lại một cách lịch sự.

Câu trả lời của bạn nên dựa trên lịch sử hội thoại giữa bạn và người dùng.

Dưới đây là lịch sử hội thoại giữa "BOT" (là bạn) và "USER" (là người dùng):
{context}

Dưới đây là câu hỏi mới nhất từ người dùng, hãy trả lời nó NGẮN GỌN, TRỰC TIẾP (không vòng vo):
{new_prompt}

Lưu ý: Chỉ trả về câu trả lời của bạn dưới dạng String duy nhất, không cần bất kỳ format đặc biệt nào.
        """
        return prompt
