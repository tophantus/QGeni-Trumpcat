import re
from ..db import create_session
from .data_access import WordService

session = create_session()


def parse_dict(filename):
    with open(filename, encoding='utf-8') as f:
        lines = [line.rstrip('\n') for line in f]

    entries = []
    i = 0
    N = len(lines)
    while i < N:
        # Find next word entry start
        if not lines[i].startswith("@"):
            i += 1
            continue
        # Parse head line:
        m = re.match(r"@(.+?)( /.*?/)?$", lines[i])
        if not m:
            i += 1
            continue
        word_text = m.group(1).strip()
        pronunciation = (m.group(2) or "").strip().strip("/")
        i += 1
        # Prepare entry
        types = []
        curr_type = None
        curr_meaning = None
        # Parse all lines until next word or end
        while i < N and not lines[i].startswith("@"):
            line = lines[i]
            # Type
            if line.startswith("*"):
                type_text = line.lstrip("*").strip()
                curr_type = {'text': type_text, 'meanings': []}
                types.append(curr_type)
                curr_meaning = None
            elif line.startswith("-"):
                meaning_text = line.lstrip("-").strip()
                curr_meaning = {'text': meaning_text, 'examples': []}
                if curr_type:
                    curr_type['meanings'].append(curr_meaning)
            elif line.startswith("="):
                examples = line.lstrip("=").strip()
                if "+" in examples:
                    src, tgt = examples.split("+", 1)
                    if curr_meaning:
                        curr_meaning['examples'].append({
                            'src': src.strip(),
                            'tgt': tgt.strip()
                        })
            # Else: skip
            i += 1

        entries.append({'text': word_text, 'pronunciation': pronunciation, 'types': types})
    return entries


def import_words(file_path, segment_size=1000):
    entries = parse_dict(file_path)
    print(f"Parsed {len(entries)} entries.")
    idx = 0
    entries_len = len(entries)
    while idx < entries_len:
        next_segment_size = min(segment_size, entries_len - idx)
        WordService.save_many_words(session, entries[idx:idx + next_segment_size])
        print(f'Imported words with ids from {idx} to {idx + next_segment_size}')
        idx += segment_size




