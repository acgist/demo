import sys
import logging

logger = logging.getLogger("example")
logger.setLevel(logging.DEBUG)
fmt = logging.Formatter("%(name)s: %(asctime)s | %(levelname)s | %(filename)s:%(lineno)s | %(process)d >>> %(message)s")
stdoutHandler  = logging.StreamHandler(stream = sys.stdout)
warningHandler = logging.FileHandler("warning.log")
stdoutHandler.setLevel(logging.DEBUG)
warningHandler.setLevel(logging.WARN)
stdoutHandler.setFormatter(fmt)
warningHandler.setFormatter(fmt)
logger.addHandler(stdoutHandler)
logger.addHandler(warningHandler)

logger.info("1234")
logger.warning("1234")
