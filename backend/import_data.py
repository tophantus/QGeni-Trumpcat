from app.practice.z_import_local_data import import_local_listening, import_local_reading
from app.word.z_import_local_data import import_words
import json


if __name__ == '__main__':
    import_local_reading('data/raw_data/practice/reading_done.json')
    with open('data/raw_data/practice/listening/caption.json', 'r', encoding='utf-8') as f:
        captions = json.load(f)
    import_local_listening('data/raw_data/practice/listening/exam', captions)
    import_words('data/raw_data/dict/dict.txt', segment_size=5000)

