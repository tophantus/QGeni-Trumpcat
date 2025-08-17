from .models import WordleGameResult

class WordleService:
    @staticmethod
    def save_result(session, user_id, word_length, guesses, time_taken):
        result = WordleGameResult(
            user_id=user_id,
            word_length=word_length,
            guesses=guesses,
            time_taken=time_taken
        )
        session.add(result)
        session.commit()
        session.refresh(result)
        return result

    @staticmethod
    def get_leaderboard(session, word_length, limit=100):
        return (
            session.query(WordleGameResult)
            .filter_by(word_length=word_length)
            .order_by(WordleGameResult.guesses.asc(), WordleGameResult.time_taken.asc())
            .limit(limit)
            .all()
        )