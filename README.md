1. First of all Raspberry Pi 4 is configured and Ubuntu OS is installed
2. The Putty is used to access the IoT device from the IoT platform
3. Ubuntu is preferred instead of raspbian operating system because we have lot of support for IoTivity for ubuntu
![](https://github.com/atifrizwan91/Greenhouse/blob/main/Images/0.PNG)
- [X]  The card is then injected in the Raspberry pi to start the operating system

# Installation of required software
## Putty
1. Install putty to connect with Raspberry pi 4 remotely.
2. The putty initiate the SSH command which allow the IoT platform to send the instructions remotely
3. There are some other software are also available to use the SSH request
4. The windows terminal can also be sued to connect the IoT device with IoT Platform
![](https://github.com/atifrizwan91/Greenhouse/blob/main/Images/1.PNG)
## WINSCP
1. WinSCP is installed to move files from local system to Raspberry pi
   -Host name: IP address of Raspberry pi
   -Username and password entered
   -Hit Login Button
2. The winSCP is a FTP based system which is used to sed the files from the platform to the devicen
3. The device need some file like Iotivity 2.2.2 and some python scripts 
4. These files are transferred from the system to the device using Winscp
![](https://github.com/atifrizwan91/Greenhouse/blob/main/Images/2.PNG)

# Build and Install IoTivity on device
- [x] Open Putty and connect it with device (Raspberry pi)
1. Installed IoTivity version 2.2.2 from GitHub using
   ```git clone --recursive --depth 1 --single-branch --branch 2.2.2 https://github.com/iotivity/iotivity-lite.git iotivity-222```
2. Build and Run sample client and server
   - To build and run the sample execute the following commands. 
   - ``` ./build--server-lite.sh ```
   - ``` ./run-server-lite.sh```

![](https://github.com/atifrizwan91/Greenhouse/blob/main/Images/3.PNG)

# IoT Platform
##  Eclipse IDE
  1. Open Eclipse 
     - Search for the remote systems to connect remotely with IoT Device
     - The remote system will install remote shell and remote terminal to compile the Iotivity java code from Eclipse
     - The device will use remote compiler and execute the java code
      ![](https://github.com/atifrizwan91/Greenhouse/blob/main/Images/4.PNG)
     - Follow the procedure
       - The SSH connection will require some parameter to be set before the connection with the given IP address
       - Firstly Linux is selected as a operating system and the IP address of the Device will be provided in next step of the configuration
       - Dstore.files will be selected next and the linux shell will be selected after that point. 
       - Finally by selecting the ssh.shell the configuration will be completed and the connection will be established with the IoT device

     
![](https://github.com/atifrizwan91/Greenhouse/blob/main/Images/5.PNG)

- Open the SSH connection and connect with device
![](https://github.com/atifrizwan91/Greenhouse/blob/main/Images/6.PNG)
![](https://github.com/atifrizwan91/Greenhouse/blob/main/Images/7.PNG)

- After the connection, run the IoTivity server andf client code.
![](https://github.com/atifrizwan91/Greenhouse/blob/main/Images/8.PNG)

Final Output of Client and server communication

![](https://github.com/atifrizwan91/Greenhouse/blob/main/Images/OCF%20Diagrams%20-%20Prof.png)

- Energy Saving percentage of proposed model

![](https://github.com/atifrizwan91/Greenhouse/blob/main/Images/Energy%20Comparison.png)


