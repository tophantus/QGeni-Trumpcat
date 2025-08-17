import json
import mimetypes
from zipfile import ZipFile
import os

from ..file.data_access import FileService
from .data_access import PracticeService


def practice_process_uploaded_files(session, zip_bytes, pr_type):
    temp_folder = 'temp'
    os.makedirs(temp_folder)
    with ZipFile(zip_bytes) as zip_file:
        zip_file.extractall(temp_folder)

    process_import(session, temp_folder, pr_type)
    os.remove(temp_folder)


def process_import(session, temp_folder, pr_type):
    if pr_type == 'reading':
        found = False
        for entry in os.listdir(temp_folder):
            if entry.endswith('.json'):
                json_data = json.loads(os.path.join(temp_folder, entry))
                import_reading(session, json_data)
                found = True
                break
        if not found:
            raise ValueError('Cannot find any json file')
    elif pr_type == 'listening':
        for entry in os.listdir(temp_folder):
            import_listening(session, os.path.join(temp_folder, entry), "1", "1")
    else:
        raise ValueError(f'Unsupported practice type: {pr_type}')


def import_reading(session, json_data):
    PracticeService.save_practice(
        session,
        pr_type='reading',
        json_data=json_data
    )


def import_listening(session, folder, captions, caption_idx):
    found_json = False
    json_data = None

    for entry in os.listdir(folder):
        if entry.endswith('.json'):
            with open(os.path.join(folder, entry), 'r', encoding='utf-8') as f:
                json_data = json.load(f)
            found_json = True
            break
    if not found_json:
        raise ValueError('Cannot find any json file')

    questions = json_data['questions']

    for q_idx in range(len(questions)):
        question = questions[q_idx]
        img_urls = question['imageUrls']
        for img_idx in range(len(img_urls)):
            img_url = img_urls[img_idx]
            with open(os.path.join(folder, img_url), 'rb') as f:
                img_urls[img_idx] = FileService.save_file(
                    session,
                    blob=f.read(),
                    mimetype=mimetypes.guess_type(img_url)[0]
                )

        mp3Url = question['audioUrls']
        with open(os.path.join(folder, mp3Url), 'rb') as f:
            mp3Url = FileService.save_file(
                session,
                blob=f.read(),
                mimetype=mimetypes.guess_type(mp3Url)[0]
            )

        question['imageUrls'] = img_urls
        question['mp3Url'] = mp3Url
        question['caption'] = captions[caption_idx][q_idx]['caption']
        questions[q_idx] = question

    json_data['questions'] = questions
    PracticeService.save_practice(
        session,
        pr_type='listening',
        json_data=json_data
    )
