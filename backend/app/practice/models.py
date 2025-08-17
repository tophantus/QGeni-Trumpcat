from sqlalchemy import Column, Integer, Text, ForeignKey, DateTime, String, JSON
from sqlalchemy.orm import relationship
from ..db import Base
from datetime import datetime


class ReadingQuestion(Base):
    __tablename__ = 'reading_question'

    id = Column(Integer, primary_key=True)
    practice_id = Column(Integer, ForeignKey('reading.id', ondelete='CASCADE'))
    statement = Column(Text, nullable=False)
    answer = Column(Integer, nullable=False)
    explanation = Column(Text, nullable=True)
    practice = relationship('ReadingPractice', back_populates='questions')

    def as_dict(self):
        return {
            "id": self.id,
            "statement": self.statement,
            "answer": ReadingQuestion.answer2str(self.answer),
            "explanation": self.explanation
        }

    def copy(self):
        return ReadingQuestion(
            statement=self.statement,
            answer=self.answer
        )

    @staticmethod
    def answer2str(answer):
        if answer == -1:
            return 'FALSE'
        elif answer == 1:
            return 'TRUE'
        else:
            return 'NOT GIVEN'


class ReadingPractice(Base):
    __tablename__ = 'reading'

    id = Column(Integer, primary_key=True)
    title = Column(Text, nullable=False, unique=True)
    created_at = Column(DateTime, default=datetime.now, nullable=False)
    passage = Column(Text, nullable=False)
    questions = relationship("ReadingQuestion", back_populates="practice", cascade="all, delete-orphan")
    histories = relationship("UserReadingHistory", back_populates="practice", cascade="all, delete-orphan")
    favorites = relationship("UserFavoriteReading", back_populates="practice", cascade="all, delete-orphan")

    def as_dict(self):
        result = {
            "id": self.id,
            "title": self.title,
            "creationDate": self.created_at.isoformat(),
            "passage": self.passage,
            "questionList": [question.as_dict() for question in self.questions]
        }
        return result

    def copy(self):
        return ReadingPractice(
            title=self.title,
            passage=self.passage,
            questions=[question.copy() for question in self.questions]
        )


class ListeningQuestion(Base):
    __tablename__ = 'listening_question'

    id = Column(Integer, primary_key=True)
    practice_id = Column(Integer, ForeignKey('listening.id', ondelete='CASCADE'))
    image_urls = Column(JSON, nullable=False)
    answer_idx = Column(Integer, nullable=False)
    mp3_url = Column(String, nullable=False)
    caption = Column(Text, nullable=True)
    practice = relationship('ListeningPractice', back_populates='questions')

    def as_dict(self):
        return {
            "imageList": self.image_urls,
            "answerIndex": self.answer_idx,
            "mp3File": self.mp3_url,
            "caption": self.caption
        }


class ListeningPractice(Base):
    __tablename__ = 'listening'

    id = Column(Integer, primary_key=True)
    title = Column(Text, nullable=False, unique=True)
    created_at = Column(DateTime, default=datetime.now, nullable=False)
    questions = relationship('ListeningQuestion', back_populates='practice', cascade='all, delete-orphan')

    histories = relationship("UserListeningHistory", back_populates="practice", cascade="all, delete-orphan")
    favorites = relationship("UserFavoriteListening", back_populates="practice", cascade="all, delete-orphan")

    def as_dict(self):
        result = {
            "id": self.id,
            "title": self.title,
            "creationDate": self.created_at.isoformat(),
            "questionList": [q.as_dict() for q in self.questions]
        }
        return result


