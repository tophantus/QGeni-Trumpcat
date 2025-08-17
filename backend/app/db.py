from sqlalchemy import create_engine, event
from sqlalchemy.orm import sessionmaker
from sqlalchemy.orm import declarative_base

Base = declarative_base()
engine = create_engine("sqlite:///data/sqlite/app.db")


def create_session():
    Session = sessionmaker(bind=engine)
    return Session()


def init_db():
    Base.metadata.create_all(bind=engine)
    print("Database init successfully")


@event.listens_for(engine, "connect")
def set_sqlite_pragma(dbapi_connection, _):
    cursor = dbapi_connection.cursor()
    cursor.execute("PRAGMA foreign_keys=ON")
    cursor.close()