from flask import Blueprint, request, g
from flask_jwt_extended import jwt_required, get_jwt
from .data_access import HistoryService
from ..util import jsonify

history_bp = Blueprint('history_bp', __name__)


@history_bp.route("/api/history/<pr_type>/<int:pr_id>", methods=['GET', 'POST'])
@jwt_required()
def user_practice_history(pr_type, pr_id):
    usr_id = get_jwt()['sub']
    if request.method == 'GET':
        history = HistoryService.get_a_practice_history(
            g.session,
            pr_type,
            pr_id,
            usr_id
        )
        return jsonify(history), 200

    elif request.method == 'POST':
        body = request.get_json()
        score = body['score']
        duration_second = body['durationSecond']

        total_score = HistoryService.save_practice_history(
            g.session,
            pr_type=pr_type,
            pr_id=pr_id,
            usr_id=usr_id,
            score=score,
            duration_second=duration_second
        )
        return jsonify({'totalScore': total_score}), 201


@history_bp.route("/api/history/<pr_type>/<int:pr_id>/ranking", methods=['GET'])
def get_ranking_for_practice(pr_type, pr_id):
    return jsonify(
        HistoryService.get_ranking_by_practice(
            g.session,
            pr_type=pr_type,
            pr_id=pr_id
        )
    ), 200


@history_bp.route("/api/history/<pr_type>", methods=['GET'])
@jwt_required()
def get_user_practices_histories(pr_type):
    usr_id = get_jwt()['sub']
    histories = HistoryService.get_all_practice_histories(
        g.session,
        pr_type=pr_type,
        usr_id=usr_id
    )
    return jsonify(histories)


@history_bp.route("/api/history/word", methods=['GET'])
@jwt_required()
def get_user_words_access_history():
    usr_id = get_jwt()['sub']
    return jsonify(
        HistoryService.get_words_access_history(
            g.session,
            usr_id
        )
    ), 200


@history_bp.route("/api/history/word/<int:w_id>", methods=['DELETE'])
@jwt_required()
def user_word_access_history(w_id):
    usr_id = get_jwt()['sub']
    # if request.method == 'POST':
    #     HistoryService.save_word_access_history(g.session, w_id, usr_id)
    #     return jsonify({'status': 'SUCCESS'}), 201
    # if request.method == 'DELETE':
    HistoryService.delete_word_access_history(g.session, w_id, usr_id)
    return jsonify({'status': 'SUCCESS'}), 204


@history_bp.route("/api/history", methods=['GET'])
@jwt_required()
def get_activity_history():
    usr_id = get_jwt()['sub']
    start_iso = request.args['start_iso']
    end_iso = request.args['end_iso']
    activities = HistoryService.get_user_activity_counts(
        g.session, start_iso=start_iso, end_iso=end_iso, usr_id=usr_id)
    return jsonify(
        activities
    ), 200


