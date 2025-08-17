from datetime import datetime

from sqlalchemy import and_, func
from sqlalchemy.orm import aliased

from ..favorite.models import UserFavoriteWord
from ..history.models import UserReadingHistory, UserListeningHistory, UserWordAccessHistory
from ..practice.models import ReadingPractice, ListeningPractice
from ..util import parse_isoformat, iterate_dates
from ..word.models import Word, WordType, Meaning
from ..user.models import User


def get_classes(pr_type):
    if pr_type == 'reading':
        return ReadingPractice, UserReadingHistory
    elif pr_type == 'listening':
        return ListeningPractice, UserListeningHistory
    else:
        raise TypeError("pr_type must be either 'reading' or 'listening'")


def calculate_total_score(score, duration, c=60):
    return score + score * (c / (duration + c))


class HistoryService:
    @staticmethod
    def get_a_practice_history(session, pr_type, pr_id, usr_id):
        pr_cls, hist_cls = get_classes(pr_type)
        q = (
            session.query(
                pr_cls,
                hist_cls
            )
                .join(hist_cls, pr_cls.id == hist_cls.practice_id)
                .filter(pr_cls.id == pr_id)
                .filter(hist_cls.user_id == usr_id)
                .order_by(hist_cls.done_at.desc())
                .all()
        )
        return [
            {
                "score": his.total_score,
                "doneAt": his.done_at.isoformat()
            }
            for _, his in q
        ]

    @staticmethod
    def get_all_practice_histories(session, pr_type, usr_id):
        pr_cls, hist_cls = get_classes(pr_type)
        q = (
            session.query(
                pr_cls,
                hist_cls
            )
                .join(hist_cls, pr_cls.id == hist_cls.practice_id)
                .filter(hist_cls.user_id == usr_id)
                .order_by(hist_cls.done_at.desc())
                .all()
        )

        return [
            {
                "id": pr.id,
                "title": pr.title,
                "score": hist.total_score,
                "doneAt": hist.done_at.isoformat()
            }
            for pr, hist in q
        ]

    @staticmethod
    def save_practice_history(session, pr_type, pr_id, usr_id, score, duration_second):
        pr_cls, hist_cls = get_classes(pr_type)
        total_score = calculate_total_score(score, duration_second)
        print("Total score calculated:", total_score)
        history = hist_cls(
            user_id=usr_id,
            practice_id=pr_id,
            score=score,
            duration_second=duration_second,
            total_score=total_score
        )
        session.add(history)
        session.commit()

        return total_score

    @staticmethod
    def get_words_access_history(session, usr_id):
        subq_1 = (
            session.query(
                Word.id.label("id"),
                Word.text.label("text"),
                Word.pronunciation.label("pronunciation"),
                UserFavoriteWord.id.label("favorite_id")
            )
            .join(
                UserWordAccessHistory, Word.id == UserWordAccessHistory.word_id
            )
            .outerjoin(
                UserFavoriteWord, and_(
                    UserFavoriteWord.user_id == usr_id,
                    UserFavoriteWord.obj_id == Word.id
                )
            )
            .filter(UserWordAccessHistory.user_id == usr_id)
            .group_by(Word.id)
            .order_by(UserWordAccessHistory.access_at.desc())
            .subquery()
        )

        subq_2 = (
            session.query(
                subq_1.c.id.label("id"),
                func.min(WordType.id).label("type_id"),
                func.min(Meaning.id).label("meaning_id"),
            )
                .join(WordType, WordType.word_id == subq_1.c.id)
                .join(Meaning, Meaning.type_id == WordType.id)
                .group_by(subq_1.c.id)
                .subquery()
        )

        subq_3 = (
            session.query(
                subq_2.c.id.label("id"),
                WordType.text.label("type_text"),
                Meaning.text.label("meaning_text")
            )
            .join(WordType, WordType.id == subq_2.c.type_id)
            .join(Meaning, Meaning.id == subq_2.c.meaning_id)
            .subquery()
        )

        q = (
            session.query(
                subq_1.c.id.label("id"),
                subq_1.c.text.label("text"),
                subq_1.c.pronunciation.label("pronunciation"),
                subq_3.c.type_text.label("type_text"),
                subq_3.c.meaning_text.label("meaning_text"),
                subq_1.c.favorite_id.label("favorite_id"),
            )
                .join(subq_3, subq_1.c.id == subq_3.c.id)
                .all()
        )

        return [
            {
                "id": _id,  # Id of the word, not the history
                "text": text,
                "pronunciation": pronunciation,
                "type": type_text,
                "meaning": meaning_text,
                "isFavorite": favorite_id is not None
            }
            for _id, text, pronunciation, type_text, meaning_text, favorite_id in q
        ]

    @staticmethod
    def delete_word_access_history(session, w_id, usr_id):
        history = (
            session.query(
                UserWordAccessHistory
            )
            .filter(and_(
                UserWordAccessHistory.word_id == w_id,
                UserWordAccessHistory.user_id == usr_id
            ))
            .first()
        )
        if history:
            session.delete(history)
            session.commit()

    @staticmethod
    def get_user_activity_counts(session, start_iso, end_iso, usr_id):
        # Prepare the list of day labels (in ISO format)
        start_time = parse_isoformat(start_iso)
        end_time = parse_isoformat(end_iso)
        today = datetime.now(start_time.tzinfo)
        if start_time >= end_time or start_time > today:
            return []

        if end_time > today:
            end_time = today

        # Query word accesses, group by date
        word_counts = dict(
            session.query(
                func.date(UserWordAccessHistory.access_at).label('date'),
                func.count(UserWordAccessHistory.id)
            )
                .filter(
                    UserWordAccessHistory.user_id == usr_id,
                    UserWordAccessHistory.access_at >= start_time,
                    UserWordAccessHistory.access_at <= end_time
                )
                .group_by('date')
                .all()
        )

        reading_counts = dict(
            session.query(
                func.date(UserReadingHistory.done_at).label('date'),
                func.count(UserReadingHistory.id)
            )
                .filter(
                    UserReadingHistory.user_id == usr_id,
                    UserReadingHistory.done_at >= start_time,
                    UserReadingHistory.done_at <= end_time
                )
                .group_by('date')
                .all()
        )

        listening_counts = dict(
            session.query(
                func.date(UserListeningHistory.done_at).label('date'),
                func.count(UserListeningHistory.id)
            )
                .filter(
                    UserListeningHistory.user_id == usr_id,
                    UserListeningHistory.done_at >= start_time,
                    UserListeningHistory.done_at <= end_time
                )
                .group_by('date')
                .all()
        )

        # Prepare the final result
        result = []
        for date in iterate_dates(start_time.date(), end_time.date()):
            key_date = date.isoformat()
            word_count = 0 if key_date not in word_counts else word_counts[key_date]
            reading_count = 0 if key_date not in reading_counts else reading_counts[key_date]
            listening_count = 0 if key_date not in listening_counts else listening_counts[key_date]
            result.append({
                "label": key_date,
                "wordCount": word_count,
                "practiceCount": reading_count + listening_count
            })
    
        return result

    @staticmethod
    def get_ranking_by_practice(session, pr_type, pr_id):
        _, h_cls = get_classes(pr_type)

        subq_1 = (
            session.query(
                h_cls.user_id.label('user_id'),
                func.max(h_cls.total_score).label('score'),
            )
            .filter(h_cls.practice_id == pr_id)
            .group_by('user_id')
            .order_by(h_cls.total_score)
            .subquery()
        )

        h_aliased = aliased(h_cls)
        subq_2 = (
            session.query(
                subq_1.c.user_id.label('user_id'),
                subq_1.c.score.label('score'),
                h_aliased.duration_second.label('time')
            )
            .join(h_aliased,
                  and_(
                      h_aliased.user_id == subq_1.c.user_id,
                      h_aliased.total_score == subq_1.c.score,
                      h_aliased.practice_id == pr_id
                  )
            )
            .subquery()
        )

        q = (
            session.query(
                subq_2.c.user_id,
                subq_2.c.time,
                subq_2.c.score,
                User.name
            )
            .join(User, User.id == subq_2.c.user_id)
            .all()
        )

        result = []
        top = 1
        for usr_id, time, score, name in q:
            result.append(
                {
                    "userId": usr_id,
                    "name": name,
                    "score": score,
                    "time": str(time),
                    "top": top
                }
            )

      
            top += 1

        return result




