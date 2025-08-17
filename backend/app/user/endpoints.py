from flask import request, Blueprint, g
from flask_jwt_extended import create_access_token, jwt_required, get_jwt

from ..util import jsonify
from .data_access import UserService

user_bp = Blueprint('user_bp', __name__)


@user_bp.route("/api/auth/register", methods=['POST'])
def register():
    data = request.get_json()
    name = data.get('username')
    email = data.get('email')
    phone = data.get('phoneNumber')
    password = data.get('password')

    if not email:
        return jsonify({'msg': 'email is required', 'status': 'FAILED'}), 400

    if not name:
        return jsonify({'msg': 'username is required', 'status': 'FAILED'}), 400

    if not password:
        return jsonify({'msg': 'password is required', 'status': 'FAILED'}), 400

    user = UserService.create_user(g.session, email, name, password, phone)
    if user is None:
        return jsonify({'msg': 'User already exists', 'status': 'FAILED'}), 409

    return jsonify({'msg': 'User registered successfully', 'status': 'SUCCESS'}), 201


@user_bp.route('/api/auth/login', methods=['POST'])
def login():
    data = request.get_json()
    email = data.get('email')
    password = data.get('password')
    if not email or not password:
        return jsonify({'msg': 'Missing name or password', 'status': 'FAILED'}), 400

    user = UserService.authenticate_user(g.session, email, password)
    if not user:
        return jsonify({'msg': 'Invalid credentials', 'status': 'FAILED'}), 401

    access_token = create_access_token(
        identity=str(user.id),
    )
    return jsonify({'msg': 'Login success', 'accessToken': access_token, 'status': 'SUCCESS'}), 200


@user_bp.route('/api/user/info', methods=['GET', 'PATCH'])
@jwt_required()
def get_info():
    usr_id = get_jwt()['sub']
    if request.method == 'GET':
        print('Getting user with id', usr_id)
        user = UserService.get_user_info(g.session, usr_id)
        if not user:
            return jsonify({'msg': 'Invalid user', 'status': 'FAILED'}), 401
        else:
            return jsonify(user), 200
    else:
        json_data = request.get_json()
        user = UserService.update_user(g.session, usr_id, json_data)
        if user is None:
            return jsonify({'status': 'FAILED'}), 401
        return jsonify(user), 200



@user_bp.route('/api/user/forgot-password', methods=['POST'])
@jwt_required()
def forgot_password():
    usr_id = get_jwt()['sub']
    email = request.get_json()['email']
    newPasswordDict = UserService.forgot_password(g.session, usr_id, email)
    if not newPasswordDict:
        return jsonify({'msg': 'Invalid user', 'status': 'FAILED'}), 401
    else:
        newPasswordDict['status'] = 'SUCCESS'
        return jsonify(newPasswordDict), 200

