from datetime import timedelta


class AppConfig:
    DEBUG = True
    with open('data/security/JWT_SECRET_KEY', 'r', encoding='utf-8') as f:
        JWT_SECRET_KEY = f.read().strip()
    JWT_ACCESS_TOKEN_EXPIRES = timedelta(days=30)
    JWT_REFRESH_TOKEN_EXPIRES = timedelta(days=30)


