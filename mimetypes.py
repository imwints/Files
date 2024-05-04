#!/usr/bin/env python3

import json
import urllib.request


def get_mimetype_kotlin():
    str = ''
    with urllib.request.urlopen("https://github.com/patrickmccallum/mimetype-io/raw/master/src/mimeData.json") as data:
        # Load the mimetype 'database' as a json object
        mimetype_json_data = json.loads(data.read().decode("utf-8"))

        list_type_ext = []

        # Loop over each mime type entry extracting the mimetype and associated file extension
        for type in mimetype_json_data:
            type_ext = (type["name"], type["fileTypes"])
            list_type_ext.append(type_ext)

        # Print a kotlin object holding a list with all mimetype
        str += "// Automatically parsed from: https://github.com/patrickmccallum/mimetype-io/raw/master/src/mimeData.json\n"
        str += "object MimeTypes {\n"
        str += "\tval Map = persistentListOf(\n"
        for type, ext in sorted(list_type_ext):
            type, slash, subtype = type.partition("/")
            str += '\t\tMimeType("{}", "{}", "{}"),\n'.format(type, subtype, '", "'.join(ext))

        str += "\t)\n"
        str += "}\n"

    return str

print(get_mimetype_kotlin())
