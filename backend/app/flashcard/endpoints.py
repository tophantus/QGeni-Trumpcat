from flask import Blueprint, request, g
from flask_jwt_extended import jwt_required, get_jwt
from ..util import jsonify
from .data_access import FlashcardService

flashcard_bp = Blueprint('flashcard_bp', __name__)

@flashcard_bp.route("/api/flashcards/sets", methods=["POST"])
@jwt_required()
def create_set():
    db = g.session
    user_id = get_jwt().get("sub")
    data = request.get_json()

    title = data.get("title")
    if not title:
        return jsonify({"msg": "Title is required", "status": "FAILED"}), 400

    fc_set = FlashcardService.create_set(db, user_id, title)
    return jsonify({
        "msg": "Flashcard set created",
        "set": {"id": fc_set.id, "title": fc_set.title},
        "status": "SUCCESS"
    }), 201

@flashcard_bp.route("/api/flashcards/<int:set_id>/cards", methods=["POST"])
@jwt_required()
def add_flashcard(set_id):
    db = g.session
    user_id = get_jwt().get("sub")
    #user_id = request.args.get('user_id')
    data = request.get_json()

    question = data.get("question")
    answer = data.get("answer")
    if not question or not answer:
        return jsonify({"msg": "Question and answer are required", "status": "FAILED"}), 400

    fc_set = FlashcardService.get_set(db, set_id, user_id)
    if not fc_set:
        return jsonify({"msg": "Flashcard set not found", "status": "FAILED"}), 404

    fc = FlashcardService.add_flashcard(db, set_id, question, answer)
    return jsonify({
        "msg": "Flashcard added",
        "flashcard": {"id": fc.id, "question": fc.question, "answer": fc.answer},
        "status": "SUCCESS"
    }), 201

@flashcard_bp.route("/api/flashcards/sets", methods=["GET"])
@jwt_required()
def list_sets():
    db = g.session
    user_id = get_jwt().get("sub")
    #user_id = request.args.get('user_id')
    sets = FlashcardService.get_sets_by_user(db, user_id)
    return jsonify({
        "sets": [{"id": s.id, "title": s.title, "created_at": s.created_at.isoformat()} for s in sets],
        "status": "SUCCESS"
    }), 200

@flashcard_bp.route("/api/flashcards/<int:set_id>", methods=["DELETE"])
@jwt_required()
def delete_set(set_id):
    db = g.session
    user_id = get_jwt().get("sub")
    #user_id = request.args.get('user_id')
    fc_set = FlashcardService.delete_set(db, set_id, user_id)
    if not fc_set:
        return jsonify({"msg": "Flashcard set not found", "status": "FAILED"}), 404

    return jsonify({
        "msg": "Flashcard set deleted",
        "status": "SUCCESS"
    }), 200

@flashcard_bp.route("/api/flashcards/cards/<int:card_id>", methods=["DELETE"])
@jwt_required()
def delete_flashcard(card_id):
    db = g.session
    user_id = get_jwt().get("sub")

    fc = FlashcardService.delete_flashcard(db, card_id, user_id)
    if not fc:
        return jsonify({
            "msg": "Flashcard not found or unauthorized",
            "status": "FAILED"
        }), 404

    return jsonify({
        "msg": "Flashcard deleted",
        "status": "SUCCESS"
    }), 200

@flashcard_bp.route("/api/flashcards/<int:set_id>/cards", methods=["GET"])
@jwt_required()
def list_flashcards(set_id):
    db = g.session
    user_id = get_jwt().get("sub")

    fc_set = FlashcardService.get_set(db, set_id, user_id)
    if not fc_set:
        return jsonify({"msg": "Flashcard set not found", "status": "FAILED"}), 404

    cards = FlashcardService.get_flashcards_in_set(db, set_id)
    return jsonify({
        "flashcards": [
            {"id": c.id, "question": c.question, "answer": c.answer}
            for c in cards
        ],
        "status": "SUCCESS"
    }), 200