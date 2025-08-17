from flask import Blueprint, g
from .data_access import FavoriteService
from flask_jwt_extended import jwt_required, get_jwt
from ..util import jsonify

favorite_bp = Blueprint('favorite_bp', __name__)


@favorite_bp.route('/api/favorite/<f_type>/<int:obj_id>', methods=['PATCH'])
@jwt_required()
def change_favorite(f_type, obj_id):
    usr_id = get_jwt()['sub']
    try:
        FavoriteService.change_favorite(
            g.session,
            f_type=f_type,
            usr_id=usr_id,
            obj_id=obj_id
        )
        return jsonify({'status': 'SUCCESS'}), 200
    except Exception as e:
        print(e)
        return jsonify({'msg': f'Error: {e}', 'status': 'FAILED'}), 409


@favorite_bp.route('/api/favorite/<f_type>', methods=['GET'])
@jwt_required()
def get_favorites(f_type):
    usr_id = get_jwt()['sub']
    return jsonify(FavoriteService.get_favorites(g.session, f_type, usr_id)), 200
