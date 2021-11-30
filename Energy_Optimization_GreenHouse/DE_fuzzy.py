#------------------------------------------------------------------------------+
#
#   Sehrish Malik
#   Simple Particle Swarm Optimization (PSO) with Python
#   August, 2020
#
#------------------------------------------------------------------------------+

#--- IMPORT DEPENDENCIES ------------------------------------------------------+

from __future__ import division
import random
import math
from Get_params import getUserPrefTemp_Min, getUserPrefTemp_Max
from Get_params import getUserPrefHumid_Min, getUserPrefHumid_Max
from Get_params import getCurrentTemp, getCurrentHumid, getCurrentEnergy, getCurrentTime
from FuzzyModule import FuzzyControl

energy_limit_max = 10.0

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
    elif(currTemp >= 25.0):
        BasicEnergyCost = 0.98
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
#--- Minimize Energy Consumption COST FUNCTION ------------------------------------------------------------+

def EnergyMin(x):
    energycost = 0
    for i in range(len(x)):
        energyTemp = BasicCoolingCost_Rules(x[0])
        energyHumid = BasicHumidCost_Rules(x[1])
        #print("Temperature Value: "+str(x[0]) +"  Temperature Energy Cost: "+str(energyTemp)+" - - - Humidity Value: "+str(x[1]) +" Humidity Energy Cost: "+str(energyHumid)) 
        energycost = energyTemp + energyHumid
    return energycost

#--- COST FUNCTION ------------------------------------------------------------+

# function we are attempting to optimize (minimize)
def func1(x):
    total=0
    for i in range(len(x)):
        total+=x[i]**2
    return total

#--- MAIN ---------------------------------------------------------------------+

class Particle:
    def __init__(self,x0):
        self.position_i=[]          # particle position
        self.velocity_i=[]          # particle velocity
        self.pos_best_i=[]          # best position individual
        self.err_best_i=-1          # best error individual
        self.err_i=-1               # error individual

        for i in range(0,num_dimensions):
            self.velocity_i.append(random.uniform(-1,1))
            self.position_i.append(x0[i])

    # evaluate current fitness
    def evaluate(self,costFunc):
        self.err_i=costFunc(self.position_i)

        # check to see if the current position is an individual best
        if self.err_i < self.err_best_i or self.err_best_i==-1:
            self.pos_best_i=self.position_i
            self.err_best_i=self.err_i

    # update new particle velocity
    def update_velocity(self,pos_best_g):
        w=0.5       # constant inertia weight (how much to weigh the previous velocity)
        c1=1        # cognative constant
        c2=2        # social constant

        for i in range(0,num_dimensions):
            r1=random.random()
            r2=random.random()

            vel_cognitive=c1*r1*(self.pos_best_i[i]-self.position_i[i])
            vel_social=c2*r2*(pos_best_g[i]-self.position_i[i])
            self.velocity_i[i]=w*self.velocity_i[i]+vel_cognitive+vel_social

    # update the particle position based off new velocity updates
    def update_position(self,bounds):
        for i in range(0,num_dimensions):
            self.position_i[i]=self.position_i[i]+self.velocity_i[i]

            # adjust maximum position if necessary
            if self.position_i[i]>bounds[i][1]:
                self.position_i[i]=bounds[i][1]

            # adjust minimum position if neseccary
            if self.position_i[i] < bounds[i][0]:
                self.position_i[i]=bounds[i][0]
                

def PSO(costFunc,x0,bounds,num_particles,maxiter):
        global num_dimensions

        num_dimensions=len(x0)
        err_best_g=-1                   # best error for group
        pos_best_g=[]                   # best position for group

        # establish the swarm
        swarm=[]
        for i in range(0,num_particles):
            swarm.append(Particle(x0))

        # begin optimization loop
        i=0
        while i < maxiter:
            #print i,err_best_g
            # cycle through particles in swarm and evaluate fitness
            for j in range(0,num_particles):
                swarm[j].evaluate(costFunc)

                # determine if current particle is the best (globally)
                if swarm[j].err_i < err_best_g or err_best_g == -1:
                    pos_best_g=list(swarm[j].position_i)
                    err_best_g=float(swarm[j].err_i)

            # cycle through swarm and update velocities and position
            for j in range(0,num_particles):
                swarm[j].update_velocity(pos_best_g)
                swarm[j].update_position(bounds)
            i+=1
        return pos_best_g, err_best_g

    
#--- RUN ----------------------------------------------------------------------+

# temp , humid
print(" ---------- Optimization Programs starts -----------")
min_temp = getUserPrefTemp_Min()
max_temp = getUserPrefTemp_Max()
min_humid = getUserPrefHumid_Min()
max_humid = getUserPrefHumid_Max()

sim = 4        # set simulation iterations
for index in range (0,sim):
    print("/n")
    print("-------------- Simulation iteration : "+str(sim)+" -----------------")
    curr_temp = float(getCurrentTemp(index))
    curr_humid = float(getCurrentHumid(index))

    print ('Current Temperature: ' + str(curr_temp))
    print ('Current Humidity: ' + str(curr_humid))

    f = open("tasks.txt", "a")
    f.writelines("TempRead "+str(curr_temp)+"\n")
    f.writelines("humidRead "+str(curr_humid)+"\n")
    f.close()

    initial=[curr_temp,curr_humid]               # initial starting location [temp,humidity...]
    bounds=[(min_temp,max_temp),(min_humid,max_humid)]  # input bounds [(temp_min,temp_max),(humidity_min,humidity_max)...]

    print ('User Desired Range for Temperature [Min, Max]: [' + str(min_temp)+", "+ str(max_temp)+"]")
    print ('User Desired Range for Humidity [Min, Max]: [' + str(min_humid)+", "+ str(max_humid)+"]")

    pos_best_g, err_best_g = PSO(EnergyMin,initial,bounds,num_particles=15,maxiter=100)

    # print final results
    print ('FINAL PSO positions [T, H]: ' + str(pos_best_g))
    print ('FINAL PSO error: ' + str(err_best_g))

    FuzzyTemp, FuzzyHumid = FuzzyControl(pos_best_g[0], pos_best_g[1])
    print("Fuzzy Set Temperature: " + str(FuzzyTemp))
    print("Fuzzy Set Humidity: " + str(FuzzyHumid))

    print("Generated Tasks:")
    print("Set Temperature Control to : "+str(FuzzyTemp))
    print("Set Humidity Control to : "+str(FuzzyHumid))

    f = open("tasks.txt", "a")
    f.writelines("tempControl "+str(FuzzyTemp)+"\n")
    f.writelines("humidControl "+str(FuzzyHumid)+"\n")
    f.close()

#--- END ----------------------------------------------------------------------+
