from flask import Blueprint, request, g
from flask_jwt_extended import jwt_required, get_jwt
import requests
from ..util import jsonify
from .data_access import ChatbotHistoryService, ChatConversation
import json
import os

chatbot_bp = Blueprint('chatbot_bp', __name__)

gemini_API_key = os.getenv("GEMINI_API_KEY")

@chatbot_bp.route("/api/chatbot", methods=['POST'])
@jwt_required()
def ask():
    db = g.session
    user_id = get_jwt().get("sub")
    # usr_id = request.args.get('user_id')
    data = request.get_json()

    prompt = data.get("prompt")

    is_new = data.get("isNewChat", False)
    chat_id = data.get("chatId")

    if not prompt:
        return jsonify({"msg": "Prompt is required", "status": "FAILED"}), 400

    if is_new or not chat_id:
        conv = ChatbotHistoryService.create_conversation(db, user_id)
    else:
        conv = db.query(ChatConversation).filter_by(id=chat_id, user_id=user_id).first()
        if not conv:
            return jsonify({"msg": "Invalid chat session", "status": "FAILED"}), 400

    # Xây context cho conversation
    context = ChatbotHistoryService.build_context(db, conv.id, prompt)
    # if check(prompt) == 1:
    try:
        url = "https://gemini-proxy.politesand-ba5b0a1c.japaneast.azurecontainerapps.io/ask?api_key=" + gemini_API_key + "&"
        params = {"prompt": context}
        res = requests.get(url, params=params)
        res.raise_for_status()
        result = res.json()['response'].strip()

        # Lưu
        ChatbotHistoryService.save_message(db, conv.id, "user", prompt)
        ChatbotHistoryService.save_message(db, conv.id, "bot", result)

        return jsonify({
            "response": result,
            "chatId": conv.id,
            "status": "SUCCESS"
        }), 200
    except Exception as e:
        return jsonify({"msg": str(e), "status": "FAILED"}), 500
    # else:
    #     ChatbotHistoryService.save_message(db, conv.id, "user", prompt)
    #     ChatbotHistoryService.save_message(db, conv.id, "bot",
    #                                        "Tôi chỉ có thể trả lời các câu hỏi liên quan đến tiếng Anh")
    #
    #     return jsonify({
    #         "response": "Tôi chỉ có thể trả lời các câu hỏi liên quan đến tiếng Anh",
    #         "chatId": conv.id,
    #         "status": "SUCCESS"
    #     }), 200


# def check(prompt):
#     url = "http://107.98.86.154:5001/predict"
#     response = requests.post(url, json={"prompt": prompt})
#     response.raise_for_status()
#     return response.json().get('prediction', 0)
