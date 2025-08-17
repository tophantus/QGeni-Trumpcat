from .models import UserFavoriteWord, UserFavoriteReading, UserFavoriteListening
from sqlalchemy import and_


def get_class(f_type):
    if f_type == 'reading':
        return UserFavoriteReading
    elif f_type == 'listening':
        return UserFavoriteListening
    elif f_type == 'word':
        return UserFavoriteWord
    else:
        raise ValueError("f_type must be either 'reading', 'listening' or 'word'")


class FavoriteService:
    @staticmethod
    def change_favorite(session, f_type, usr_id, obj_id):
        f_cls = get_class(f_type)

        obj = session.query(f_cls).filter(
            f_cls.obj_id == obj_id, f_cls.user_id == usr_id).first()
        if obj is not None:
            session.delete(obj)
        else:
            obj = f_cls(user_id=usr_id, obj_id=obj_id)
            session.add(obj)
        session.commit()

    @staticmethod
    def get_favorites(session, f_type, usr_id):
        f_cls = get_class(f_type)
        objects = session.query(f_cls).filter(f_cls.user_id == usr_id).all()
        return [obj.obj_id for obj in objects]
