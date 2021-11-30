import numpy as np
import skfuzzy as fuzz
from skfuzzy import control as ctrl

def FuzzyControl(Optimal_Temp, Optimal_Humid):
    # New Antecedent/Consequent objects hold universe variables and membership functions
    temperature = ctrl.Antecedent(np.arange(0, 41, 1), 'temperature')
    Control_Temp = ctrl.Consequent(np.arange(16, 30, 1), 'Control_Temp')

    humidity = ctrl.Antecedent(np.arange(0, 101, 1), 'humidity')
    Control_Humid = ctrl.Consequent(np.arange(45, 65, 1), 'Control_Humid')

    # Auto-membership function population is possible with .automf(3, 5, or 7)
    names1 = ['too-low temp', 'low temp', 'average temp', 'high temp', 'too-high temp']
    temperature.automf(names = names1)

    names2 = ['too-low humid', 'low humid', 'average humid', 'high humid', 'too-high humid']
    humidity.automf(names = names2)


    # Custom membership functions can be built interactively with a familiar,
    Control_Temp['cool temp'] = fuzz.trimf(Control_Temp.universe, [16, 16, 25])
    Control_Temp['no-change in temp'] = fuzz.trimf(Control_Temp.universe, [25, 25, 25])
    Control_Temp['heat temp'] = fuzz.trimf(Control_Temp.universe, [25, 30, 30])

    Control_Humid['decresease humid'] = fuzz.trimf(Control_Humid.universe, [45, 45, 53])
    Control_Humid['no-change in humid'] = fuzz.trimf(Control_Humid.universe, [53, 53, 56])
    Control_Humid['increase humid'] = fuzz.trimf(Control_Humid.universe, [56, 65, 65])

    # You can see how these look with .view()
    temperature['average temp'].view()
    Control_Temp.view()

    humidity['average humid'].view()
    Control_Humid.view()

    # rules
    rule1 = ctrl.Rule(temperature['too-low temp'] | temperature['low temp'], Control_Temp['heat temp'])
    rule2 = ctrl.Rule(temperature['high temp'] | temperature['too-high temp'], Control_Temp['cool temp'])
    rule3 = ctrl.Rule(temperature['average temp'], Control_Temp['no-change in temp'])
    #rule3 = ctrl.Rule(service['good'] | quality['good'], tip['high'])

    rule4 = ctrl.Rule(humidity['too-low humid'] | humidity['low humid'], Control_Humid['increase humid'])
    rule5 = ctrl.Rule(humidity['high humid'] | humidity['too-high humid'], Control_Humid['decresease humid'])
    rule6 = ctrl.Rule(humidity['average humid'], Control_Humid['no-change in humid'])

    TempControlling_ctrl = ctrl.ControlSystem([rule1, rule2, rule3])
    TempControlling = ctrl.ControlSystemSimulation(TempControlling_ctrl)

    HumidControlling_ctrl = ctrl.ControlSystem([rule4, rule5, rule6])
    HumidControlling = ctrl.ControlSystemSimulation(HumidControlling_ctrl)

    TempControlling.input['temperature'] = Optimal_Temp
    HumidControlling.input['humidity'] = Optimal_Humid


    # Crunch the numbers
    TempControlling.compute()
    HumidControlling.compute()

    ## Once computed, we can view the result as well as visualize it.

    #print(TempControlling.output['Control_Temp'])
    Control_Temp.view(sim=TempControlling)

    #print(HumidControlling.output['Control_Humid'])
    Control_Humid.view(sim=HumidControlling)

    return TempControlling.output['Control_Temp'], HumidControlling.output['Control_Humid']
