from io import BytesIO

from flask import send_file, request, Blueprint, g, make_response

from ..util import jsonify
from .data_access import FileService

file_bp = Blueprint('file_bp', __name__)


@file_bp.route('/api/file/<name>', methods=['GET'])
def get_file(name):
    file = FileService.get_file_data(g.session, name)
    if not file:
        return jsonify({'msg': 'File not found'}), 404
    file_stream = BytesIO(file.data)
    file_stream.seek(0)
    #
    # response = make_response(file_stream.getvalue())
    # response.headers['Content-Type'] = file.mimetype
    # response.headers['Content-Length'] = str(len(file.data))
    # response.headers['Cache-Control'] = 'no-cache'
    # return response
    return send_file(file_stream, mimetype=file.mimetype)


@file_bp.route('/file/upload', methods=['GET', 'POST'])
def upload_file():
    if request.method == 'POST':
        file = request.files['myfile']
        name = FileService.save_file(g.session, file.read(), str(file.mimetype))
        return f'Upload successful with name {name}'
    return '''
    <form method="post" enctype="multipart/form-data">
      <input type="file" name="myfile">
      <input type="submit">
    </form>
    '''