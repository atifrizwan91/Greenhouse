
from random import randint
from scipy.optimize import differential_evolution
import pandas as pd
from matplotlib.pyplot import plot
import matplotlib.pyplot as plt
import numpy as np
from Rules import *

TempP = [20,25]
HumP = [45,50]
CO2P = [45,55]



unitEnergyChiller = 0.15
unitEnergyHeater = 0.20

unitEnergyFogg = 0.19
unitEnergyDehumedifier = 0.07

unitEnergyGenerator = 0.13
unitEnergyForcedVentilator = 0.16

zones = []

def getPreferedValues():
    tempC = randint(TempP[0], TempP[1])
    humC = randint(HumP[0], HumP[1])
    co2C = randint(CO2P[0], CO2P[1])
    return tempC, humC, co2C

def getPreferedValues():
    tempC = randint(15, 25) #randint(TempP[0], TempP[1])
    humC = randint(60, 80) #randint(HumP[0], HumP[1])
    co2C = randint(65, 80) #randint(CO2P[0], CO2P[1])
    return tempC, humC, co2C

def BasicCoolingCost_Rules(currTemp):
    BasicEnergyCost = 0
    # difference 14kW cooling capacity
    if(currTemp < 21.0):
        BasicEnergyCost = 1.5
    elif(currTemp <= 22.0 and currTemp >= 21.0):
        BasicEnergyCost = 1.34 
    elif(currTemp <= 23.0 and currTemp >= 22.0):
        BasicEnergyCost = 1.22
    elif(currTemp <= 24.0 and currTemp >= 23.0):
        BasicEnergyCost = 1.08
    elif(currTemp <= 25.0 and currTemp >= 24.0):
        BasicEnergyCost = 0.99
    elif(currTemp >= 25.0 and currTemp <= 26.0) :
        BasicEnergyCost = 0.98
    elif(currTemp >= 26.0 and currTemp <= 27.0):
        BasicEnergyCost = 0.95
    elif(currTemp >= 27.0 and currTemp <= 28.0):
        BasicEnergyCost = 0.93
    elif(currTemp >= 28.0 and currTemp <= 29.0):
        BasicEnergyCost = 0.92
    elif(currTemp >= 29.0 and currTemp <= 30.0):
        BasicEnergyCost = 0.1
    
    return BasicEnergyCost

#--- Humidity Basic Energy Consumption Rules ------------------------------------------------------------+
def BasicHumidCost_Rules(currHumid):
    BasicEnergyCost = 0
                        # difference 14kW cooling capacity
    if(currHumid < 40.0):
        BasicEnergyCost = 0.98
    elif(currHumid <= 45.0 and currHumid >= 40.0):
        BasicEnergyCost = 0.99 
    elif(currHumid <= 50.0 and currHumid >= 45.0):
        BasicEnergyCost = 1.08
    elif(currHumid <= 55.0 and currHumid >= 50.0):
        BasicEnergyCost = 1.22
    elif(currHumid <= 60.0 and currHumid >= 55.0):
        BasicEnergyCost = 1.34
    elif(currHumid >= 60.0):
        BasicEnergyCost = 1.5
    return BasicEnergyCost

def BasicCO2Cost_Rules(currCO2):
    BasicEnergyCost = 0
    # difference 14kW cooling capacity
    if(currCO2 >= 35.0 and currCO2 <= 40.0):
        BasicEnergyCost = 0.98
    elif(currCO2 <= 45.0 and currCO2 >= 40.0):
        BasicEnergyCost = 0.99 
    elif(currCO2 <= 50.0 and currCO2 >= 45.0):
        BasicEnergyCost = 1.08
    elif(currCO2 <= 55.0 and currCO2 >= 50.0):
        BasicEnergyCost = 1.22
    elif(currCO2 <= 60.0 and currCO2 >= 55.0):
        BasicEnergyCost = 1.34
    elif(currCO2 >= 60.0 and currCO2 <= 65.0):
        BasicEnergyCost = 1.5
    elif(currCO2 >= 70.0 and currCO2 <= 80.0):
        BasicEnergyCost = 1.5
    elif(currCO2 >= 80.0 and currCO2 <= 85.0):
        BasicEnergyCost = 1.5
    return BasicEnergyCost



def EnergyMin(x):
    energycost = 0
    for i in range(len(x)):
        energyTemp = BasicCoolingCost_Rules(x[0])
        energyHumid = BasicHumidCost_Rules(x[1])
        #print("Temperature Value: "+str(x[0]) +"  Temperature Energy Cost: "+str(energyTemp)+" - - - Humidity Value: "+str(x[1]) +" Humidity Energy Cost: "+str(energyHumid)) 
        energycost = energyTemp + energyHumid
    return energycost

def lineplot(orig,optimized):
    fig = plt.figure()
    ax = plt.axes()
    x = np.linspace(0, 100, 100)
    ax.plot(x, orig, "r-")
    ax.plot(x, optimized, "g-")
    fig.show()
    


def outerParameters():
    tempO = randint(10, 40)
    humO = randint(60, 90)
    co2O = randint(35, 65)
    return tempO, humO, co2O

def getEnergyCost(tempC, humC, co2C):
    EnergyT = 0
    EnergyH = 0
    EnergyC = 0
    
    # if tempC >= TempP[0] and tempC <= TempP[1]:
    #     # EnergyT = unitEnergyHeater  * BasicCoolingCost_Rules(tempC)
    #     EnergyT =  BasicCoolingCost_Rules(tempC)
    if tempC < TempP[0]:
        DeltaT = abs(tempC - TempP[0])
        EnergyT = unitEnergyHeater * DeltaT * BasicCoolingCost_Rules(tempC)
    if tempC > TempP[1]:
        DeltaT = abs(tempC - TempP[1])
        EnergyT = unitEnergyChiller * DeltaT * BasicCoolingCost_Rules(tempC)
        
    # if humC >= HumP[0] and  humC <= HumP[1]:
    #     # EnergyH =  unitEnergyFogg * BasicHumidCost_Rules(humC)
    #     EnergyH =   BasicHumidCost_Rules(humC)
    if humC < HumP[0]:
        DeltaH = abs(humC - HumP[0])
        EnergyH = DeltaH * unitEnergyFogg * BasicHumidCost_Rules(tempC)
    if humC > HumP[1]:
        DeltaH = abs(humC - HumP[1])
        EnergyH = DeltaH * unitEnergyDehumedifier * BasicHumidCost_Rules(tempC)
        
    # if co2C >= CO2P[0] and co2C <= CO2P[1]:
    #     # EnergyC = unitEnergyGenerator * BasicCO2Cost_Rules(co2C)
    #     EnergyC =  BasicCO2Cost_Rules(co2C)
    if co2C < CO2P[0]:
        DeltaC = abs(co2C - CO2P[0])
        EnergyC = DeltaC * unitEnergyGenerator * BasicCO2Cost_Rules(tempC)
    if co2C > CO2P[1]:
        DeltaC = abs(co2C - CO2P[1])
        EnergyC = DeltaC * unitEnergyForcedVentilator * BasicCO2Cost_Rules(tempC)
    
    
    return EnergyT + EnergyH + EnergyC
    
def energyOutIn(tempC, humC, co2C):
    
    if(tempC < tempO and tempO < TempP[0]):
        

def objective(x):
    r = getEnergyCost(x[0], x[1], x[2])
    return r
 

#bounds = [(21.0, 25.0), (40.0, 65.0)];
bounds = [TempP, HumP, CO2P]
result  = differential_evolution(objective,bounds)

orignalEnergyCost = []
optimizaedEnergyCost = []

for i in range(0,100):
    t,h,c = getPreferedValues()
    energy = objective([t,h,c])
    print("Energy:", energy)
    orignalEnergyCost.append([t,h,c,energy])
    result  = differential_evolution(objective,bounds)
    solution = result['x']
    evaluation = objective(solution)
    optimizaedEnergyCost.append([solution[0],solution[1], solution[2], evaluation])


orignalEnergyCost = pd.DataFrame(orignalEnergyCost, columns=['Temp', 'Humidity', 'CO2' , 'Energy'])
optimizaedEnergyCost = pd.DataFrame(optimizaedEnergyCost, columns=['Temp', 'Humidity', 'CO2' , 'Energy'])

lineplot(orignalEnergyCost['Energy'],optimizaedEnergyCost['Energy'])

print('Status : %s' % result['message'])
print('Total Evaluations: %d' % result['nfev'])
# evaluate solution
solution = result['x']
evaluation = EnergyMin(solution)
print('Solution: f(%s) = %.5f' % (solution, evaluation))




print(getPreferedValues())
print(objective([22,50,52]))

