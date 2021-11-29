1. First of all Raspberry Pi 4 is configured and Ubuntu OS is installed
2. The Putty is used to access the IoT device from the IoT platform
3. Ubuntu is preferred instead of raspbian operating system because we have lot of support for IoTivity for ubuntu
![](https://www.linkpicture.com/q/Capture_125.png)
- [X]  The card is then injected in the Raspberry pi to start the operating system

# Installation of required software
## Putty
1. Install putty to connect with Raspberry pi 4 remotely.
2. The putty initiate the SSH command which allow the IoT platform to send the instructions remotely
3. There are some other software are also available to use the SSH request
4. The windows terminal can also be sued to connect the IoT device with IoT Platform
![](https://www.linkpicture.com/q/1Capture_1.png)
## WINSCP
1. WinSCP is installed to move files from local system to Raspberry pi
   -Host name: IP address of Raspberry pi
   -Username and password entered
   -Hit Login Button
2. The winSCP is a FTP based system which is used to sed the files from the platform to the devicen
3. The device need some file like Iotivity 2.2.2 and some python scripts 
4. These files are transferred from the system to the device using Winscp
![](https://www.linkpicture.com/q/2Capture.png)

# Build and Install IoTivity on device
- [x] Open Putty and connect it with device (Raspberry pi)
1. Installed IoTivity version 2.2.2 from GitHub using
   ```git clone --recursive --depth 1 --single-branch --branch 2.2.2 https://github.com/iotivity/iotivity-lite.git iotivity-222```
2. Build and Run sample client and server
   - To build and run the sample execute the following commands. 
   - ``` ./build--server-lite.sh ```
   - ``` ./run-server-lite.sh```

![](https://www.linkpicture.com/q/3Capture.png)

# IoT Platform
##  Eclipse IDE
  1. Open Eclipse 
     - Search for the remote systems to connect remotely with IoT Device
     - The remote system will install remote shell and remote terminal to compile the Iotivity java code from Eclipse
     - The device will use remote compiler and execute the java code
      ![](https://www.linkpicture.com/q/4-Capture.png)
     - Follow the procedure
       - The SSH connection will require some parameter to be set before the connection with the given IP address
       - Firstly Linux is selected as a operating system and the IP address of the Device will be provided in next step of the configuration
       - Dstore.files will be selected next and the linux shell will be selected after that point. 
       - Finally by selecting the ssh.shell the configuration will be completed and the connection will be established with the IoT device

     
![](https://www.linkpicture.com/q/5-Capture.png)

- Open the SSH connection and connect with device
![](https://www.linkpicture.com/q/6-Capture.png)
![](https://www.linkpicture.com/q/7-Capture.png)

- After the connection, run the IoTivity server andf client code.
![](https://www.linkpicture.com/q/8-Capture.png)

Final Output of Client and server communication

![](https://www.linkpicture.com/q/OCF-Diagrams-Prof.png)

- Energy Saving percentage of proposed model

![](https://www.linkpicture.com/q/Energy-Comparison.png)


