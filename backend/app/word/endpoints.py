from flask import request, Blueprint, g
from flask_jwt_extended import jwt_required, get_jwt
from ..util import jsonify
from .data_access import WordService

word_bp = Blueprint('word_bp', __name__)


@word_bp.route("/api/word/<string:key>", methods=['GET'])
@jwt_required()
def word_get_description(key):
    usr_id = get_jwt()['sub']
    desc_as_dict = WordService.get_word_description(g.session, key, usr_id)
    status = 200 if desc_as_dict else 404
    return jsonify(desc_as_dict), status


@word_bp.route("/api/word", methods=['GET'])
def word_get_all():
    page_idx = request.args.get('pageIdx', default=1, type=int)
    page_size = request.args.get('pageSize', default=20, type=int)

    word_as_dicts = WordService.get_words_page(g.session, page_idx, page_size)
    return jsonify(word_as_dicts), 200


@word_bp.route("/api/word/suggest/<string:hint>", methods=['GET'])
def word_get_suggestions(hint):
    page_idx = request.args.get('pageIdx', default=1, type=int)
    page_size = request.args.get('pageSize', default=20, type=int)
    if not hint.strip():
        return []
    suggestions = WordService.get_suggestions(g.session, hint, page_idx, page_size)
    return suggestions, 200

@word_bp.route("/api/words/random/<int:length>", methods = ['Get'])
def get_random_word(length):
    if (length not in [5, 6, 7]):
        return jsonify({"error: Invalud word length"}), 400
    word = WordService.get_random_word_by_length(g.session, length)
    if word:
        return jsonify({"word": word}), 200
    else:
        return jsonify({"error": "No word found"}), 400


@word_bp.route("/api/words/check/<string:word_text>", methods = ['Get'])
def check_word(word_text):
    exist = WordService.is_word_exist(g.session, word_text)
    if exist:
        return jsonify({"exist": exist}), 200
    else:
        return jsonify({"error": "invalid word"}), 400
