from ..word.models import Word, WordType, Meaning, Example
from ..favorite.models import UserFavoriteWord
from sqlalchemy import and_, func
from ..history.models import UserWordAccessHistory


class WordService:
    @staticmethod
    def get_word_description(session, word_text, usr_id=None):
        word_text = word_text.lower()
        if not usr_id:
            word = session.query(Word).filter(
                Word.text == word_text).first()
            if not word:
                return None
            word_dict = word.as_dict()
            word_dict['isFavorite'] = False
            return word_dict
        else:
            word, is_favorite = (
                session.query(
                    Word,
                    UserFavoriteWord.id
                )
                .outerjoin(
                    UserFavoriteWord,
                    and_(
                        Word.id == UserFavoriteWord.obj_id,
                        UserFavoriteWord.user_id == usr_id
                    )
                )
                .filter(Word.text == word_text)
                .first()
            )
            if not word:
                return None

            history = UserWordAccessHistory(
                word_id=word.id,
                user_id=usr_id
            )
            session.add(history)
            session.commit()

            word_dict = word.as_dict()
            word_dict['isFavorite'] = True if is_favorite else False
            return word_dict

    @staticmethod
    def get_suggestions(session, word_hint, page_index=1, page_size=20):
        if page_index < 1:
            page_index = 1
        offset = (page_index - 1) * page_size
        words = session.query(Word).filter(
            Word.text.like(f"{word_hint}%")).offset(offset).limit(page_size).all()
        return [word.text for word in words]

    @staticmethod
    def get_words_page(session, page_index=1, page_size=20):
        """
        Returns a list of Word objects for the given page.
        page_index: 1-based index (first page is 1)
        page_size: number of items per page
        """

        if page_index < 1:
            page_index = 1
        offset = (page_index - 1) * page_size
        words = session.query(Word).order_by(Word.id).offset(offset).limit(page_size).all()
        return [word.as_dict() for word in words]

    @staticmethod
    def save_many_words(session, json_data):
        words = []
        for obj in json_data:
            word = Word(
                text=obj['text'],
                pronunciation=obj['pronunciation'],
                types=[
                    WordType(
                        text=word_type['text'],
                        meanings=[
                            Meaning(
                                text=meaning['text'],
                                examples=[
                                    Example(
                                        src=example['src'],
                                        tgt=example['tgt']
                                    )
                                    for example in meaning['examples']
                                ]
                            )
                            for meaning in word_type['meanings']
                        ]
                    )
                    for word_type in obj['types']
                ]
            )
            words.append(word)

        session.add_all(words)
        session.commit()

    @staticmethod
    def get_random_word_by_length(session, word_length):
        word = session.query(Word).filter(func.char_length(Word.text) == word_length).order_by(func.random()).first()
        return word.text

    @staticmethod
    def is_word_exist(session, word_text):
        word = session.query(Word).filter(Word.text == word_text).first()
        return word is not None
