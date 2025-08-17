from ..db import Base
from sqlalchemy import BLOB, Column, Integer, Text


class File(Base):
    __tablename__ = 'file'
    id = Column(Integer, primary_key=True)
    name = Column(Text, unique=True, index=True)
    data = Column(BLOB)
    mimetype = Column(Text)
