from datetime import datetime
from sqlalchemy import Column, Integer, ForeignKey, DateTime, Text
from sqlalchemy.orm import relationship
from ..db import Base

class FlashcardSet(Base):
    __tablename__ = "flashcard_set"

    id = Column(Integer, primary_key=True)
    user_id = Column(Integer, ForeignKey("user.id", ondelete="CASCADE"), nullable=False)
    title = Column(Text, nullable=False)
    created_at = Column(DateTime, default=datetime.utcnow, nullable=False)

    flashcards = relationship("Flashcard", back_populates="set", cascade="all, delete-orphan")
    user = relationship("User", back_populates="flashcard_sets")


class Flashcard(Base):
    __tablename__ = "flashcard"

    id = Column(Integer, primary_key=True)
    set_id = Column(Integer, ForeignKey("flashcard_set.id", ondelete="CASCADE"), nullable=False)
    question = Column(Text, nullable=False)
    answer = Column(Text, nullable=False)
    created_at = Column(DateTime, default=datetime.utcnow, nullable=False)

    set = relationship("FlashcardSet", back_populates="flashcards")