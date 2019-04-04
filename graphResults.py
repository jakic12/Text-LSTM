import json

data = json.loads(open("output.json").read())["errors"]

import numpy as np
import matplotlib.pyplot as plt

x = range(len(data))
plt.plot(x,data)
plt.show()