import urllib.request
from os.path import dirname, join

def docs_download(request_id):
    url = "https://naveropenapi.apigw.ntruss.com/doc-trans/v1/download?requestId=" + request_id

    opener = urllib.request.build_opener()
    opener.addheaders = [('X-NCP-APIGW-API-KEY-ID', "ga1qsyxi8j"), ('X-NCP-APIGW-API-KEY', "WpVJszzjYwkD1wogEKwSwCW7L0PH09EAgBJhtkZD")]
    urllib.request.install_opener(opener)

    urllib.request.urlretrieve(url, "b.docx")