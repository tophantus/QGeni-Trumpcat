from .models import File
import uuid


class FileService:
    @staticmethod
    def save_file(session, blob, mimetype):
        file_name = str(uuid.uuid4())
        file = File(name=file_name, data=blob, mimetype=mimetype)
        session.add(file)
        session.commit()
        session.refresh(file)
        return file_name

    @staticmethod
    def get_file_data(session, file_name):
        file = session.query(File).filter_by(name=file_name).first()
        return file if file else None
