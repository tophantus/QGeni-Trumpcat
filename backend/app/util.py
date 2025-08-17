import json

from flask import Response
import datetime
import urllib.parse


def jsonify(data):
    json_str = json.dumps(data, ensure_ascii=False, indent=4)
    return Response(json_str, content_type='application/json; charset=utf-8')


def parse_isoformat(isofmt):
    s_decoded = urllib.parse.unquote(isofmt)
    datetime_str, tz_region = s_decoded.split('[')
    datetime_str = datetime_str.strip()
    dt = datetime.datetime.fromisoformat(datetime_str)
    return dt


def iterate_dates(start_date, end_date):
    """
    Iterate over all dates between start_date and end_date (inclusive).

    Args:
        start_date (datetime.date): The start date.
        end_date (datetime.date): The end date.

    Yields:
        datetime.date: The current date in the iteration.
    """
    current_date = start_date
    while current_date <= end_date:
        yield current_date
        current_date += datetime.timedelta(days=1)
