"""
pip install PyPDF2

纵向打印
横向打印
"""
import os
from PyPDF2 import PdfWriter, PdfReader, PageObject, Transformation
from PyPDF2.generic import RectangleObject

index = 0
merge = PdfWriter()
# w      = 415
# h      = 276
w      = 290
h      = 194
width  = 595
height = 842
merge_page = None
for file in os.listdir("."):
    if not file.endswith(".pdf") or file == "merge.pdf":
        continue
    print(f"处理文件：{file}")
    # if index % 4 == 0 and merge_page is not None:
    if index % 8 == 0 and merge_page is not None:
        merge.add_page(merge_page)
        index      = 0
        merge_page = None
    if merge_page is None:
        merge_page = PageObject.create_blank_page(merge, width, height)
    reader = PdfReader(file)
    page   = reader.pages[0]
    full   = float(page.mediabox.height) >= height
    page.artbox   = RectangleObject((0, 0, width, height / 2))
    page.cropbox  = RectangleObject((0, 0, width, height / 2))
    page.trimbox  = RectangleObject((0, 0, width, height / 2))
    page.bleedbox = RectangleObject((0, 0, width, height / 2))
    page.mediabox = RectangleObject((0, 0, width, height / 2))
    page.add_transformation(
        Transformation()
        # .scale(w / float(page.mediabox.width), h / float(page.mediabox.height))
        # .rotate(-90)
        # .translate(
        #     tx = (5 - h if index % 2 == 0 else 10) if full else (5 if index % 2 == 0 else 10 + h),
        #     ty = height - (w + 4) * ((index // 2)) - 4
        # )
        .scale(w / float(page.mediabox.width), h / float(page.mediabox.height))
        .translate(
            tx = 5 if index % 2 == 0 else 10 + w,
            ty = height - (h + 5) * ((index // 2) + (2 if full else 1))
        )
    )
    page.artbox   = RectangleObject((0, 0, width, height))
    page.cropbox  = RectangleObject((0, 0, width, height))
    page.trimbox  = RectangleObject((0, 0, width, height))
    page.bleedbox = RectangleObject((0, 0, width, height))
    page.mediabox = RectangleObject((0, 0, width, height))
    merge_page.merge_page(page)
    index += 1
if merge_page is not None:
    merge.add_page(merge_page)
merge.write("merge.pdf")
merge.close()
