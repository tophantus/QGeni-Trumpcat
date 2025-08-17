from sqlalchemy import Column, Integer, String, DateTime
from sqlalchemy.orm import relationship
from ..db import Base
from datetime import datetime


class User(Base):
    __tablename__ = 'user'
    id = Column(Integer, primary_key=True)
    created_at = Column(DateTime, default=datetime.utcnow, nullable=False)
    email = Column(String(64), nullable=False, unique=True, index=True)
    name = Column(String(64), nullable=False)
    password = Column(String(64), nullable=False)
    phone = Column(String(16))

    listening_histories = relationship("UserListeningHistory", back_populates="user", cascade="all, delete-orphan")
    reading_histories = relationship("UserReadingHistory", back_populates="user", cascade="all, delete-orphan")
    word_access_histories = relationship("UserWordAccessHistory", back_populates="user", cascade="all, delete-orphan")

    favorite_readings = relationship("UserFavoriteReading", back_populates="user", cascade="all, delete-orphan")
    favorite_listenings = relationship("UserFavoriteListening", back_populates="user", cascade="all, delete-orphan")
    favorite_words = relationship("UserFavoriteWord", back_populates="user", cascade="all, delete-orphan")

    chat_conversations = relationship("ChatConversation", back_populates="user", cascade="all, delete-orphan")
    flashcard_sets = relationship("FlashcardSet", back_populates="user", cascade="all, delete-orphan")
    wordle_results = relationship("WordleGameResult", back_populates="user", cascade="all, delete-orphan")

    def as_dict(self):
        return {
            'id': self.id,
            'username': self.name,
            'email': self.email,
            'phoneNumber': self.phone,
            'createdAt': self.created_at.isoformat()
        }
