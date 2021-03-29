import cv2
import numpy as np
from os.path import dirname, join
import base64
from com.chaquo.python import Python
from PIL import Image
import io
from decimal import Decimal


class Inference:
    def __init__(self):
        self.CONFIDENCE_THRESHOLD = 0.2
        self.NMS_THRESHOLD = 0.4
        self.COLORS = [(0, 255, 255), (255, 255, 0), (0, 255, 0), (255, 0, 0)]
        self.classes = ['1p', '2p', '5p', '10p', '20p', '50p', '1P', '2P']
        self.yoloWeights = self.readWeights()
        self.yoloCfg = self.readCfg()
        self.yoloNet = self.readNet()
        self.yoloModel = self.setModel()
        self.files_dir = str(
            Python.getPlatform().getApplication().getFilesDir())

    def readWeights(self):
        weights = join(dirname(__file__), "yolo.weights")
        return weights

    def readCfg(self):
        cfg = join(dirname(__file__), "yolo.cfg")
        return cfg

    def readNet(self):
        net = cv2.dnn.readNet(self.yoloWeights, self.yoloCfg)
        return net

    def setModel(self):
        model = cv2.dnn_DetectionModel(self.yoloNet)
        model.setInputParams(size=(608, 608), scale=1/255)
        return model

    def getItems(self):
        items = {"1p": 0.01, "2p": 0.02, "5p": 0.05, "10p": 0.10,
                 "20p": 0.20, "50p": 0.50, "1P": 1.0, "2P": 2.0}
        return items

    def decodeData(self, data):
        decoded_data = base64.b64decode(data)
        np_data = np.fromstring(decoded_data, np.uint8)
        frame = cv2.imdecode(np_data, cv2.IMREAD_UNCHANGED)
        return frame

    def setConfidence(self, val):
        self.CONFIDENCE_THRESHOLD = val

    def runInferenceAll(self, data):
        items = self.getItems()
        total = 0
        count = 0
        frame = self.decodeData(data)

        classes, scores, boxes = self.yoloModel.detect(
            frame, self.CONFIDENCE_THRESHOLD, self.NMS_THRESHOLD)
        for (classid, score, box) in zip(classes, scores, boxes):
            color = self.COLORS[int(classid) % len(self.COLORS)]
            label = "%s %.2f" % (self.classes[classid[0]], score)
            name = self.classes[classid[0]]
            total = Decimal(total)+Decimal(items.get(name))
            count += 1
            cv2.rectangle(frame, box, color, 2)
            cv2.putText(frame, label, (box[0], box[1] - 10),
                        cv2.FONT_HERSHEY_SIMPLEX, 2, color, 3)

        total = "%.2f" % total
        cv2.imwrite(self.files_dir+'/detection.jpg', frame)
        return str(total)+"-"+str(count)

    def runInferenceSpecific(self, data, classID):
        items = self.getItems()
        total = 0
        count = 0
        frame = self.decodeData(data)

        classes, scores, boxes = self.yoloModel.detect(
            frame, self.CONFIDENCE_THRESHOLD, self.NMS_THRESHOLD)
        for (classid, score, box) in zip(classes, scores, boxes):
            color = self.COLORS[int(classid) % len(self.COLORS)]
            label = "%s %.2f" % (self.classes[classid[0]], score)
            name = self.classes[classid[0]]
            if name == classID:
                total = Decimal(total)+Decimal(items.get(name))
                count += 1
                cv2.rectangle(frame, box, color, 2)
                cv2.putText(frame, label, (box[0], box[1] - 10),
                            cv2.FONT_HERSHEY_SIMPLEX, 2, color, 3)

        total = "%.2f" % total
        cv2.imwrite(self.files_dir+'/detection.jpg', frame)
        return str(total)+"-"+str(count)


inferenceObj = Inference()


def changeConfidence(conf):
    val = (int(conf))/100
    inferenceObj.setConfidence(val)


def getConfidence():
    conf = int(inferenceObj.CONFIDENCE_THRESHOLD * 100)
    return str(conf)


def main(data, item):

    # array = cv2.cvtColor(np.array(frame), cv2.COLOR_RGB2BGR)
    # pil_image = Image.fromarray(array)
    # buff = io.BytesIO()
    # pil_image.save(buff, format="PNG")
    # img_str = base64.b64encode(buff.getvalue())
    # return ""+str(img_str, 'utf-8')

    print(item)

    if item == "All":
        result = inferenceObj.runInferenceAll(data)
    else:
        result = inferenceObj.runInferenceSpecific(data, item)

    return result
