import random
from readData import readData, read_UserParams_Temp, read_UserParams_Humid

def getUserPrefTemp_Min():
  data = read_UserParams_Temp()
  temp_min = float(data[0][0])
  return temp_min

def getUserPrefTemp_Max():
  data = read_UserParams_Temp()
  temp_max = float(data[0][1])
  return temp_max

def getUserPrefHumid_Min():
  data = read_UserParams_Humid()
  humid_min = float(data[0][0])
  return humid_min

def getUserPrefHumid_Max():
  data = read_UserParams_Humid()
  humid_max = float(data[0][1])
  return humid_max

def getCurrentTime(i):
    data = readData()
    curr_time = data[i][0]
    return curr_time
    
def getCurrentEnergy(i):
    data = readData()
    curr_energy = data[i][1]
    return curr_energy
    
def getCurrentTemp(i):
    data = readData()
    curr_temp = data[i][2]
    return curr_temp

def getCurrentHumid(i):
    data = readData()
    curr_humid = data[i][3]
    return curr_humid
