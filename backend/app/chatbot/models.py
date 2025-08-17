from datetime import datetime
from sqlalchemy import Column, Integer, ForeignKey, DateTime, Text
from sqlalchemy.orm import relationship
from ..db import Base


class ChatConversation(Base):
    __tablename__ = "chat_conversation"

    id = Column(Integer, primary_key=True)
    user_id = Column(Integer, ForeignKey("user.id", ondelete="CASCADE"), nullable=False)
    created_at = Column(DateTime, default=datetime.utcnow, nullable=False)

    messages = relationship("ChatbotHistory", back_populates="conversation", cascade="all, delete-orphan")
    user = relationship("User", back_populates="chat_conversations")


class ChatbotHistory(Base):
    __tablename__ = "chatbot_history"

    id = Column(Integer, primary_key=True)
    conversation_id = Column(Integer, ForeignKey("chat_conversation.id", ondelete="CASCADE"), nullable=False)
    role = Column(Text, nullable=False)  # 'user' hoặc 'bot'
    content = Column(Text, nullable=False)
    created_at = Column(DateTime, default=datetime.utcnow, nullable=False)

    conversation = relationship("ChatConversation", back_populates="messages")