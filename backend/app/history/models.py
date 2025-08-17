from datetime import datetime

from sqlalchemy import Column, Integer, ForeignKey, Float, DateTime, UniqueConstraint
from sqlalchemy.orm import relationship

from ..db import Base


class UserReadingHistory(Base):
    __tablename__ = "user_reading_history"
    id = Column(Integer, primary_key=True)
    user_id = Column(Integer, ForeignKey("user.id", ondelete='CASCADE'), nullable=False)
    practice_id = Column(Integer, ForeignKey("reading.id", ondelete='CASCADE'), nullable=False)
    score = Column(Float, default=0.0, nullable=False)
    done_at = Column(DateTime, default=datetime.now, nullable=False, index=True)
    duration_second = Column(Integer, default=0, nullable=False)
    total_score = Column(Float, default=0.0, nullable=False)

    user = relationship("User", back_populates="reading_histories")
    practice = relationship("ReadingPractice", back_populates="histories")


class UserListeningHistory(Base):
    __tablename__ = "user_listening_history"
    id = Column(Integer, primary_key=True)
    user_id = Column(Integer, ForeignKey("user.id", ondelete='CASCADE'), nullable=False)
    practice_id = Column(Integer, ForeignKey("listening.id", ondelete='CASCADE'), nullable=False)
    score = Column(Float, default=0.0, nullable=False)
    done_at = Column(DateTime, default=datetime.now, nullable=False, index=True)
    duration_second = Column(Integer, default=0, nullable=False)
    total_score = Column(Float, default=0.0, nullable=False)

    user = relationship("User", back_populates="listening_histories")
    practice = relationship("ListeningPractice", back_populates="histories")


class UserWordAccessHistory(Base):
    __tablename__ = "user_word_search_history"
    id = Column(Integer, primary_key=True)
    user_id = Column(Integer, ForeignKey("user.id", ondelete='CASCADE'), nullable=False)
    word_id = Column(Integer, ForeignKey("word.id", ondelete="CASCADE"), nullable=False)
    access_at = Column(DateTime, default=datetime.now, nullable=False, index=True)

    user = relationship("User", back_populates="word_access_histories")
    word = relationship("Word", back_populates="access_histories")
