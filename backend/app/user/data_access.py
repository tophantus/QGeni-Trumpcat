from werkzeug.security import generate_password_hash, check_password_hash
from .models import User
from sqlalchemy import and_
import uuid

class UserService:
    @staticmethod
    def exist_by_email(session, email):
        user = session.query(User).filter(User.email == email).first()
        if user:
            return True
        return False

    @staticmethod
    def create_user(session, email, name, password, phone):
        if UserService.exist_by_email(session, email):
            return None

        hashed_pw = generate_password_hash(password)
        new_user = User(name=name, password=hashed_pw, email=email, phone=phone)
        session.add(new_user)
        session.commit()
        session.refresh(new_user)
        return new_user

    @staticmethod
    def authenticate_user(session, email, password):
        user = session.query(User).filter(User.email == email).first()
        if user and check_password_hash(user.password, password):
            return user
        return None

    @staticmethod
    def get_user_info(session, usr_id):
        user = session.query(User).filter(User.id == usr_id).first()
        return user.as_dict() if user else None

    @staticmethod
    def update_user(session, usr_id, json_data):
        user = session.query(User).filter(User.id == usr_id).first()
        if user:
            name = json_data['username']
            if name:
                user.name = name

            email = json_data['email']
            if email:
                user.email = email

            phone = json_data['phoneNumber']
            if phone:
                user.phone = phone

            session.add(user)
            session.commit()
            session.refresh(user)
            return user.as_dict()

        return None

    @staticmethod
    def forgot_password(session, usr_id, email):
        user = (
            session
                .query(User)
                .filter(
                    and_(
                        User.id == usr_id,
                        User.email == email
                    )
                )
        )

        if user:
            new_password = str(uuid.uuid4())[:8]
            return {
                'newPassword': new_password
            }
        return None

