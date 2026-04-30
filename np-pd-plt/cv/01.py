import cv2

video = cv2.VideoCapture("D:/tmp/video.mp4")
while video.isOpened():
    ret, frame = video.read()
    if not ret:
        break
    cv2.imshow("frame", frame)
    cv2.waitKey(20)
video.release()
