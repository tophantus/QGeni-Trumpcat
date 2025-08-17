from datetime import datetime
from sqlalchemy import Column, Integer, ForeignKey, DateTime, Float, Text
from sqlalchemy.orm import relationship
from ..db import Base

class WordleGameResult(Base):
    __tablename__ = "wordle_game_result"

    id = Column(Integer, primary_key=True)
    user_id = Column(Integer, ForeignKey("user.id", ondelete="CASCADE"), nullable=False)
    word_length = Column(Integer, nullable=False)
    guesses = Column(Integer, nullable=False)
    time_taken = Column(Float, nullable=False)  # seconds
    created_at = Column(DateTime, default=datetime.utcnow, nullable=False)

    user = relationship("User", back_populates="wordle_results")

