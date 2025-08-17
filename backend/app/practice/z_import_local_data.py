import os
import json
from ..db import create_session
from .util import import_listening, import_reading

session = create_session()


def import_local_listening(pr_dir, captions):
    i = 1
    for entry in os.listdir(pr_dir):
        folder_path = os.path.join(pr_dir, entry)
        print(f'Importing {folder_path}')
        import_listening(session, folder_path, captions, caption_idx=str(i))
        i += 1


def import_local_reading(json_file):
    with open(json_file, 'r', encoding='utf-8') as f:
        json_data = json.load(f)
    for obj in json_data:
        import_reading(session, obj)

