from .models import FlashcardSet, Flashcard

class FlashcardService:
    @staticmethod
    def create_set(session, user_id, title):
        fc_set = FlashcardSet(user_id=user_id, title=title)
        session.add(fc_set)
        session.commit()
        session.refresh(fc_set)
        return fc_set

    @staticmethod
    def add_flashcard(session, set_id, question, answer):
        fc = Flashcard(set_id=set_id, question=question, answer=answer)
        session.add(fc)
        session.commit()
        session.refresh(fc)
        return fc

    @staticmethod
    def delete_flashcard(session, set_id, question, answer):
        fc = Flashcard(set_id=set_id, question=question, answer=answer)
        session.add(fc)
        session.commit()
        session.refresh(fc)
        return fc

    @staticmethod
    def get_set(session, set_id, user_id):
        return session.query(FlashcardSet).filter_by(id=set_id, user_id=user_id).first()

    @staticmethod
    def get_sets_by_user(session, user_id):
        return session.query(FlashcardSet).filter_by(user_id=user_id).all()

    @staticmethod
    def delete_set(session, set_id, user_id):
        fc_set = session.query(FlashcardSet).filter_by(id=set_id, user_id=user_id).first()
        if fc_set:
            session.delete(fc_set)
            session.commit()
        return fc_set

    @staticmethod
    def delete_flashcard(session, card_id, user_id):
        fc = (
            session.query(Flashcard)
                .join(FlashcardSet)
                .filter(Flashcard.id == card_id, FlashcardSet.user_id == user_id)
                .first()
        )
        if fc:
            session.delete(fc)
            session.commit()
        return fc

    @staticmethod
    def get_flashcards_in_set(session, set_id):
        return session.query(Flashcard).filter_by(set_id=set_id).all()