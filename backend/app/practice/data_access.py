from .models import (ReadingPractice, ReadingQuestion, ListeningPractice, ListeningQuestion)
from sqlalchemy.orm import aliased
from sqlalchemy import func, and_
from ..history.models import UserReadingHistory, UserListeningHistory
from ..favorite.models import UserFavoriteReading, UserFavoriteListening


def get_classes(pr_type):
    if pr_type == 'reading':
        return ReadingPractice, UserReadingHistory, UserFavoriteReading
    elif pr_type == 'listening':
        return ListeningPractice, UserListeningHistory, UserFavoriteListening
    else:
        raise TypeError("pr_type must be either 'reading' or 'listening'")


class PracticeService:
    @staticmethod
    def get_all_practice_summaries(session, pr_type, usr_id):
        pr_cls, hist_cls, fav_cls = get_classes(pr_type)
        subq_max_score = (
            session.query(
                hist_cls.practice_id.label("practice_id"),
                func.max(hist_cls.total_score).label("max_score")
            )
                .filter(hist_cls.user_id == usr_id)
                .group_by(hist_cls.practice_id)
                .subquery()
        )

        subq_latest_done = (
            session.query(
                hist_cls.practice_id.label("practice_id"),
                func.max(hist_cls.done_at).label("latest_done_at")
            )
                .filter(hist_cls.user_id == usr_id)
                .group_by(hist_cls.practice_id)
                .subquery()
        )
        hist_cls_2 = aliased(hist_cls)
        # Now, for each practice, LEFT OUTER JOIN the max-score and latest-done info
        q = (
            session.query(
                pr_cls,
                hist_cls.total_score.label('latest_score'),  # Most recent attempt's score
                hist_cls.done_at.label('latest_done_at'),  # Most recent attempt's time
                hist_cls_2.total_score.label('max_score'),  # Score when highest was achieved
                hist_cls_2.done_at.label('max_score_done_at'),  # Latest time highest score achieved
                fav_cls.id.label('is_favorite')
            )
                .outerjoin(subq_latest_done, pr_cls.id == subq_latest_done.c.practice_id)
                .outerjoin(
                    hist_cls,
                    and_(
                        hist_cls.practice_id == pr_cls.id,
                        hist_cls.user_id == usr_id,
                        hist_cls.done_at == subq_latest_done.c.latest_done_at
                    )
                )
                .outerjoin(subq_max_score, pr_cls.id == subq_max_score.c.practice_id)
                .outerjoin(
                    hist_cls_2,
                    and_(
                        hist_cls_2.practice_id == pr_cls.id,
                        hist_cls_2.user_id == usr_id,
                        hist_cls_2.total_score == subq_max_score.c.max_score,
                    )
                )
                .outerjoin(
                    fav_cls,
                    and_(
                        fav_cls.obj_id == pr_cls.id,
                        fav_cls.user_id == usr_id
                    )
                )
        )

        # If you want only practices user has attempted, add .filter(subq_latest_done.c.latest_done_at != None)
        results = []
        for pr, latest_score, latest_done_at, max_score, max_score_done_at, is_favorite in q:
            result = pr.as_dict()
            result['highestScore'] = max_score
            result['highestDoneAt'] = max_score_done_at.isoformat() if max_score_done_at else None
            result['latestScore'] = latest_score
            result['latestDoneAt'] = latest_done_at.isoformat() if latest_done_at else None
            result['isFavorite'] = is_favorite is not None
            result['isNew'] = latest_score is None
            results.append(result)

            print("Processed practice:", result['title'], "Latest score:", latest_score, "Max score:", max_score)
        return results

    @staticmethod
    def get_all_practices(session, pr_type):
        pr_cls, _, _ = get_classes(pr_type)
        practices = session.query(pr_cls).all()
        return [pr.as_dict() for pr in practices]

    @staticmethod
    def save_practice(session, pr_type, json_data):
        if pr_type == 'reading':
            practice = ReadingPractice(
                title=json_data["title"],
                passage=json_data["passage"],
                questions=[
                    ReadingQuestion(
                        statement=question["statement"],
                        answer=question["answer"],
                        explanation=question['explanation']
                    )
                    for question in json_data["questions"]
                ]
            )
        elif pr_type == 'listening':
            practice = ListeningPractice(
                title=json_data["title"],
                questions=[
                    ListeningQuestion(
                        image_urls=question["imageUrls"],
                        answer_idx=question["answerIdx"],
                        mp3_url=question["mp3Url"],
                        caption=question['caption']
                    )
                    for question in json_data["questions"]
                ]
            )
        else:
            raise TypeError("pr_type must be either 'reading' or 'listening'")

        session.add(practice)
        session.commit()

    @staticmethod
    def get_practice(session, pr_type, practice_id):
        pr_cls, _, _ = get_classes(pr_type)
        practice = session.query(pr_cls).filter(
            pr_cls.id == practice_id).first()
        return practice.as_dict() if practice else None
