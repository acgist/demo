import json

from reportlab.lib import colors
from reportlab.pdfbase import pdfmetrics
from reportlab.platypus import Paragraph, SimpleDocTemplate
from reportlab.lib.styles import getSampleStyleSheet
from reportlab.lib.pagesizes import letter
from reportlab.pdfbase.ttfonts import TTFont
from xml.sax.saxutils import escape

pdfmetrics.registerFont(TTFont('SimSun', 'D:/download/SimSun.ttf'))

@staticmethod
def draw_title(title: str):
    style = getSampleStyleSheet()
    ct = style['Title']
    ct.fontName  = 'SimSun'
    ct.fontSize  = 15
    ct.leading   = 30
    ct.textColor = colors.red
    return Paragraph(escape(title), ct)

@staticmethod
def draw_text(text: str):
    style = getSampleStyleSheet()
    ct = style['Normal']
    ct.fontName  = 'SimSun'
    ct.fontSize  = 12
    ct.wordWrap  = 'CJK'
    ct.alignment = 0
    ct.firstLineIndent = 32
    ct.leading = 25
    return Paragraph(escape(text), ct)

i = 0

pdf = SimpleDocTemplate('D:/download/docs.pdf', pagesize = letter)
page = []
with open("D:/download/dev.json", 'r', encoding = "utf-8") as docs_json:
    while True:
        line = docs_json.readline()
        if not line:
            break
        line = line.strip()
        line_json = json.loads(line)
        question  = line_json["question"]
        answer    = "\n".join(list(map(lambda v: v["paragraph_text"], line_json["answer_paragraphs"])))
        page.append(draw_title(question))
        page.append(draw_text(answer))
        i = i + 1
        if i >= 20:
            break;
pdf.build(page)
