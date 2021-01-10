# Lab 3
Task: creating & setting up ELK docker image for gathering and aggregating data from public streaming API. More specified task - set up ELK stack for observing and aggregating some metrics of data from Wikipedia RecentChanges streaming API^ geolocation and edited articles by anonymous users.  
Tech: ELK, docker.  

# Requirements  
1. RPM-based distribution, Linux kernel 3.10 or higher.
2. docker.
3. Internet connection.

# Build  and launch
1. Download repository  
2. Launch in repo directory   
    ```console  
    foo@bar:./big_red_button.sh
	```    
This will build docker container with ELK stack 'wikidozor', set up the stack and launch container 'wikidozor-stack'.  