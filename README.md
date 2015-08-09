SoilWatExplorer
===============

A graphical interface for JSoilwat.

Features
- Reading in Soilwat Project from files_v30.in
- Writing Project data to file.
- Edit/Change project
- Execute project
- Graphs and other tools
- View output data/Save to file

## Building SoilWatExplorer
Gradle or eclipse can be used to build the project. If you use eclipse, you will need to download and include the dependencies. These can be found in the build.gradle file under dependencies. To use gradle you will need to install it.
### Build and Run process
The steps include installing gradle, cloning the repo, building the project, and running the jar file. The steps for ubuntu are given below.
```
sudo apt-get install gradle
git clone https://github.com/Burke-Lauenroth-Lab/SoilWatExplorer.git
cd SoilWatExplorer
gradle build
cd build/libs
java -jar SoilWatExplorer-1.0.jar
```


