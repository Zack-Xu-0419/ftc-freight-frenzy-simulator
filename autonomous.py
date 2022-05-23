
from pynput.keyboard import Key, Controller
import time

from torch import preserve_format
keyboard = Controller()

time.sleep(2)


def pressKeyboard(key, length):
    keyboard.press(key)
    time.sleep(length)
    keyboard.release(key)


# Press and release space
pressKeyboard('d', 0.5)

# Goes against the wall
pressKeyboard(Key.left, 0.1)

pressKeyboard('e', 0.01)
pressKeyboard(Key.up, 1.5)

# Cycles
while True:
    pressKeyboard(Key.down, 1)
    pressKeyboard(Key.shift, 0.01)
    # rotate
    pressKeyboard('a', 0.2)
    # extend
    pressKeyboard('w', 1)
    # deposit
    pressKeyboard(Key.space, 0.01)
    # retract
    pressKeyboard('s', 1)
    # go back
    pressKeyboard(Key.left, 0.2)
    pressKeyboard('e', 0.01)
    pressKeyboard(Key.up, 1)
