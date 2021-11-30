def readData():
    # Using readlines() 
    file1 = open('data.txt', 'r') 
    Lines = file1.readlines()

    count = len(Lines)
    data = []
    # Strips the newline character 
    for i in range (1,count):
            line = Lines[i].rstrip("\n")
            data.append(line.split(","))

    return data

def read_UserParams_Temp():
# Using readlines() 
    file1 = open('UserParams.txt', 'r') 
    Lines = file1.readlines()

    data = []
    # Strips the newline character 
    line = Lines[1].rstrip("\n")
    data.append(line.split(","))

    return data

def read_UserParams_Humid():
# Using readlines() 
    file1 = open('UserParams.txt', 'r') 
    Lines = file1.readlines()

    data = []
    # Strips the newline character 
    line = Lines[3].rstrip("\n")
    data.append(line.split(","))

    return data


        

