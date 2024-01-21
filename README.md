# Beam pipeline from API Response to BigQuery table.
## The custom Apache Beam Java SDK for fetching the data from REST API to BigQuery using cloud dataflow. ##
*	All the essential commands are included inside Resource directory.

What we are doing basically:
1.	Make the connection from java client.
2.	get the response from API, you can use POST/GET as per requirement.
3.	We fetch JSOn response from API in short batches for example:-
	1.	take 100 Employee records from Employee ID 1 to 100, then in next call 101,200 and so on
 	2.	URL we are using this public domain: https://hub.dummyapis.com/employee?noofRecords=100&idStarts=1
4.	Transform the data from api response to bigquery table compatible rows.
5.	Write the data to the sink.
<img width="965" alt="Screenshot 2024-01-21 at 11 17 57 PM" src="https://github.com/SiddharthSoni596/ApiToBqApacheBeam/assets/38645282/501362a8-b201-462a-aea6-873f720c1033">

### -----------
Prerequisite  | 
------------- |
Java 11 | 
Maven  |
GCP Account(Dataflow, Google cloud storage) |
gcloud sdk installed |
IDE used: Intellij |

### -----------
STEPS TO RUN  |
------------- |
clone the repository. | 
Run: mvn clean install inside cloned directory  |
gcloud auth application-default login |
mvn command for running in local/dataflow location: ***src/main/resources/essential_commands.txt*** |
