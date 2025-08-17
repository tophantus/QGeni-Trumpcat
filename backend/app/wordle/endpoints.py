from flask import Blueprint, request, g
from flask_jwt_extended import jwt_required, get_jwt
from ..util import jsonify
from .data_access import WordleService

wordle_bp = Blueprint("wordle_bp", __name__)

@wordle_bp.route("/api/wordle/submit", methods=["POST"])
@jwt_required()
def submit_result():
    db = g.session
    user_id = get_jwt().get("sub")
    data = request.get_json()

    word_length = data.get("word_length")
    guesses = data.get("guesses")
    time_taken = data.get("time_taken")

    if not all([word_length, guesses, time_taken]):
        return jsonify({"msg": "Missing data", "status": "FAILED"}), 400

    result = WordleService.save_result(db, user_id, word_length, guesses, time_taken)
    return jsonify({
        "msg": "Result saved",
        "result": {
            "id": result.id,
            "word_length": result.word_length,
            "guesses": result.guesses,
            "time_taken": result.time_taken
        },
        "status": "SUCCESS"
    }), 201

@wordle_bp.route("/api/wordle/leaderboard/<int:word_length>", methods=["GET"])
@jwt_required()
def leaderboard(word_length):
    db = g.session
    results = WordleService.get_leaderboard(db, word_length)

    return jsonify({
        "leaderboard": [
            {
                "user_id": r.user_id,
                "user_name": r.user.name,
                "guesses": r.guesses,
                "time_taken": r.time_taken,
                "created_at": r.created_at.isoformat()
            }
            for r in results
        ],
        "status": "SUCCESS"
    }), 200