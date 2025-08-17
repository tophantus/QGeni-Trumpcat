from flask import Flask, g, request
from app.db import init_db
from flask_jwt_extended import JWTManager
from .favorite import favorite_bp
from .file import file_bp
from .history import history_bp
from .practice import practice_bp
from .user import user_bp
from .word import word_bp
from .db import create_session
from .chatbot import chatbot_bp
from .flashcard import flashcard_bp
from .wordle import wordle_bp
from flask_cors import CORS
from .util import jsonify
from werkzeug.exceptions import HTTPException

app = Flask(__name__)
init_db()

CORS(app, resources={r"/*": {"origins": "*"}})  # Be more restrictive in production

CORS(app, expose_headers=["Authorization"], 
     allow_headers=["Authorization", "Content-Type"])

app.config.from_object("config.AppConfig")

_ = JWTManager(app)

for bp in [favorite_bp, file_bp, history_bp, practice_bp, user_bp, word_bp, chatbot_bp, flashcard_bp, wordle_bp]:
    app.register_blueprint(bp)


@app.before_request
def open_session():
    g.session = create_session()


@app.teardown_request
def close_session(exc):
    session = g.session
    if session:
        session.close()

@app.errorhandler(422)
def handle_422(e):
    # Log the actual error
    app.logger.error(f"422 error: {str(e)}")
    return jsonify(error=str(e)), 422

@app.errorhandler(Exception)
def handle_exception(e):
    app.logger.error(f"Unhandled exception: {str(e)}")
    if isinstance(e, HTTPException):
        return jsonify(error=str(e)), e.code
    return jsonify(error="Internal server error"), 500