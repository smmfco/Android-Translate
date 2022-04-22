import requests
from requests_toolbelt import MultipartEncoder
import uuid
import json
from os.path import dirname, join

def docsTranslate():

  filenames = join(dirname(__file__),"a.docx")

  data = {
    'source': 'en',
    'target': 'ko',
    'file': (filenames, open(filenames, 'rb'), 'application/octet-stream', {'Content-Transfer-Encoding': 'binary'})
  }
  m = MultipartEncoder(data, boundary=uuid.uuid4())

  headers = {
    "Content-Type": m.content_type,
    "X-NCP-APIGW-API-KEY-ID": "ga1qsyxi8j",
    "X-NCP-APIGW-API-KEY": "WpVJszzjYwkD1wogEKwSwCW7L0PH09EAgBJhtkZD"
  }

  url = "https://naveropenapi.apigw.ntruss.com/doc-trans/v1/translate"
  res = requests.post(url, headers=headers, data=m.to_string())
  resObj = json.loads(res.text)
  imageStr = resObj.get("data").get("requestId")
  return imageStr

