from sqlalchemy import Column, Integer, ForeignKey, UniqueConstraint
from sqlalchemy.orm import relationship

from ..db import Base


class UserFavoriteReading(Base):
    __tablename__ = "user_favorite_reading"
    id = Column(Integer, primary_key=True)
    user_id = Column(Integer, ForeignKey("user.id", ondelete='CASCADE'), nullable=False)
    obj_id = Column(Integer, ForeignKey("reading.id", ondelete='CASCADE'), nullable=False)

    user = relationship("User", back_populates="favorite_readings")
    practice = relationship("ReadingPractice", back_populates="favorites")

    __table_args__ = (UniqueConstraint("user_id", "obj_id", name="_user_obj_uc"),)


class UserFavoriteListening(Base):
    __tablename__ = "user_favorite_listening"
    id = Column(Integer, primary_key=True)
    user_id = Column(Integer, ForeignKey("user.id", ondelete='CASCADE'), nullable=False)
    obj_id = Column(Integer, ForeignKey("listening.id", ondelete='CASCADE'), nullable=False)

    user = relationship("User", back_populates="favorite_listenings")
    practice = relationship("ListeningPractice", back_populates="favorites")

    __table_args__ = (UniqueConstraint("user_id", "obj_id", name="_user_obj_uc"),)


class UserFavoriteWord(Base):
    __tablename__ = "user_favorite_word"
    id = Column(Integer, primary_key=True)
    user_id = Column(Integer, ForeignKey("user.id", ondelete='CASCADE'), nullable=False)
    obj_id = Column(Integer, ForeignKey("word.id", ondelete='CASCADE'), nullable=False)

    user = relationship("User", back_populates="favorite_words")
    word = relationship("Word", back_populates="favorites")

    __table_args__ = (UniqueConstraint("user_id", "obj_id", name="_user_obj_uc"),)