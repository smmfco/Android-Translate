import sys
import requests
from requests_toolbelt import MultipartEncoder
from os.path import dirname, join
from com.chaquo.python import Python
import uuid
import json
import base64

def translate(doc_dir,file_name,efile_name):

    doc_dirs = str(doc_dir)
    filenames = join(dirname(doc_dirs),file_name)

    data = {
        'source': 'en',
        'target': 'ko',
        'image': (filenames, open(filenames, 'rb'), 'application/octet-stream', {'Content-Transfer-Encoding': 'binary'})
    }
    m = MultipartEncoder(data, boundary=uuid.uuid4())

    headers = {
        "Content-Type": m.content_type,
        "X-NCP-APIGW-API-KEY-ID": "vwqskje0y7",
        "X-NCP-APIGW-API-KEY": "pBG4LVIJe0uLmsMy99kH8DcoUzjxp4ZWXXu77WNw"
    }

    url = "https://naveropenapi.apigw.ntruss.com/image-to-image/v1/translate"
    res = requests.post(url, headers=headers, data=m.to_string())

    resObj = json.loads(res.text)
    imageStr = resObj.get("data").get("renderedImage")
    imgdata = base64.b64decode(imageStr)

    return imageStr

