# Jocasta-Nu
## Introduction
Jocasta-Nu is a file manager that I wrote for the Programmazione a Oggetti exam.  
It uses a simple gui (or, if requested by the user, a command line interface) to create archives.
The current methods of archiving are the following:
- Split in n°files;
- Split in files of a choosen size in kb;
- Zip
- Encrypt  


The file manager is also capable of putting back together the files. 
The name "Jocasta-Nu" is a reference to the 2002 movie "Attack of the clones": when Obi-Wan Kenobi is looking for Kamino, he has a talk with an elderly lady, whose name is Jocasta-Nu.

## How does it work?
When a user creates the jobs, it creates a JobDescriptor instance, which contains the list of needed jobs inside a queue.
As soon as the user starts the job, for each JobDescriptor a thread is spawned, which will then execute each job.
The JobThread updates the main table to show the progress.
As soon as all the jobs are done, the thread updates the table for the last time and then quits.
