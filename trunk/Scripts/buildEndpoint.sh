#! /bin/bash

echo -===Building Endpoint Server===-

# Find all the java files in the Endpoint Program and store them in javasrc.txt
find ../EndpointProgram/src/ -name "*.java" > ./javasrc.txt

mkdir ./javaclasses

# Include the log4j configuration in the jar
# Copy the external libraries to the local dir
cp ../EndpointProgram/src/log4j.xml ./javaclasses/
cp ../EndpointProgram/libraries/gson-1.7.1.jar ./
cp ../EndpointProgram/libraries/log4j-1.2.16.jar ./

# Compile the java files into the javaclasses folder
javac -d ./javaclasses/ -cp ./gson-1.7.1.jar:./log4j-1.2.16.jar @javasrc.txt

# Create the Jar
jar -cmf ../EndpointProgram/src/META-INF/MANIFEST.MF Endpoint.jar -C ./javaclasses/ .

echo -===Cleaning up build temp files===-

rm ./javasrc.txt
rm -rf ./javaclasses
