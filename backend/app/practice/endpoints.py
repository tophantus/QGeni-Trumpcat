from io import BytesIO

from flask import request, render_template_string, Blueprint, g
from flask_jwt_extended import jwt_required, get_jwt

from .util import practice_process_uploaded_files
from ..util import jsonify
from .data_access import PracticeService

practice_bp = Blueprint('practice_bp', __name__)


@practice_bp.route("/api/practice/<pr_type>", methods=['GET'])
@jwt_required()
def user_practice_summaries(pr_type):
    jwt = get_jwt()
    usr_id = jwt['sub']
    summaries = PracticeService.get_all_practice_summaries(
        g.session,
        pr_type,
        usr_id
    )

    return jsonify(summaries), 200


@practice_bp.route("/api/practice/<pr_type>/<int:pr_id>", methods=['GET'])
@jwt_required()
def practice_get(pr_type, pr_id):
    practice = PracticeService.get_practice(
        g.session,
        pr_type,
        pr_id
    )

    return jsonify(practice), 200


@practice_bp.route("/api/practice/<pr_type>/all", methods=['GET'])
def practice_test_get_all(pr_type):
    all_pr = PracticeService.get_all_practices(g.session, pr_type)
    return jsonify(all_pr)


@practice_bp.route('/practice/<pr_type>/upload', methods=['GET', 'POST'])
def practice_upload_zip(pr_type):
    if request.method == 'POST':
        uploaded_file = request.files.get('zipfile')
        if not uploaded_file or uploaded_file.filename == '':
            return 'No file selected', 400
        if not uploaded_file.filename.lower().endswith('.zip'):
            return 'Please upload a ZIP file.', 400

        zip_bytes = BytesIO(uploaded_file.read())
        try:
            practice_process_uploaded_files(g.session, zip_bytes, pr_type)
        except Exception as e:
            return jsonify({'msg': f'FAILED, error: {e}'}), 400
        return jsonify({'msg': 'OK'}), 200
    else:
        # Show upload form
        return render_template_string('''
            <!doctype html>
            <title>Upload zipped folder</title>
            <h3>Upload a ZIP file:</h3>
            <form method=post enctype=multipart/form-data>
              <input type=file name=zipfile>
              <input type=submit value=Upload>
            </form>
        ''')


