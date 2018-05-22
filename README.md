## Project team 18

### Introduction
Paas Ops with Alexa automates routine AWS operations performed by AWS developer or performance engineer like deploying application or load scripts, scaling up / down clusters, querying performance metrics etc. by taking conversational approach with Alexa. AWS engineer can get this done by having a conversation with Alexa, instead of connected to computer and performing these operations on AWS console. This solutions involves integration of different component like Alexa Custom Skill, AWS Lambda Intents interpreter and EC2 provision request handler using AWS SDK.

### System Architecture
![System Architecture](https://github.com/SJSU272LabSP18/DevOpsBot/blob/master/DevOpsBot-Architecture.jpg)

### System Interaction
Use Invocation name to start: "Open Ops Helper".
Sample utterances for each action are listed below.
* Scale up cluster: Scale up <clusterName> cluster by <numbeOfInstances> VM instances 
  
* Scale down cluster: Scale down <clusterName> cluster by <numbeOfInstances> VM instances

* Resize cluster: Resize <clusterName> cluster to <numberOfInstances> instances

* Find pending requests: What is the status of my previous request

* Query size of cluster: What is the size of <clusterName> cluster

* Query VM instance type: What is the instance type of <clusterName> cluster

* Query CPU utilization: What is the average cpu utilization of <clusterName> cluster

