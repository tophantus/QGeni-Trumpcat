from sqlalchemy import Column, Integer, Text, ForeignKey, String
from sqlalchemy.orm import relationship
from ..db import Base


class Example(Base):
    __tablename__ = 'example'
    id = Column(Integer, primary_key=True)
    meaning_id = Column(Integer, ForeignKey('meaning.id'))
    src = Column(Text)
    tgt = Column(Text)

    def as_dict(self):
        return {
            "src": self.src,
            "tgt": self.tgt
        }


class Meaning(Base):
    __tablename__ = 'meaning'
    id = Column(Integer, primary_key=True)
    type_id = Column(Integer, ForeignKey('word_type.id'))
    text = Column(Text)
    examples = relationship("Example", backref="meaning", cascade="all, delete-orphan")

    def as_dict(self):
        return {
            "text": self.text,
            "examples": [ex.as_dict() for ex in self.examples]
        }


class WordType(Base):
    __tablename__ = 'word_type'
    id = Column(Integer, primary_key=True)
    word_id = Column(Integer, ForeignKey('word.id'))
    text = Column(String)
    meanings = relationship("Meaning", backref="word_type", cascade="all, delete-orphan")

    def as_dict(self):
        return {
            "text": self.text,
            "meanings": [m.as_dict() for m in self.meanings]
        }


class Word(Base):
    __tablename__ = 'word'
    id = Column(Integer, primary_key=True)
    text = Column(String, index=True)
    pronunciation = Column(Text)

    types = relationship("WordType", backref="word", cascade="all, delete-orphan")
    favorites = relationship("UserFavoriteWord", back_populates="word", cascade="all, delete-orphan")
    access_histories = relationship("UserWordAccessHistory", back_populates="word", cascade="all, delete-orphan")

    def as_dict(self):
        return {
            "id": self.id,
            "text": self.text,
            "pronunciation": self.pronunciation,
            "types": [t.as_dict() for t in self.types]
        }
