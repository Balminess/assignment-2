# Assignment 2

## Pengfei He 7999201

[comment]: <> (Code Llama models are compatible with the scripts in llama-recipes)

## Install the Jar file to calculate readability
run following commands to install read-tse to your maven local repostory
```
mvn install:install-file -Dfile=your_jar_path\read-tse-1.0.0.jar -DgroupId=
'ca.umanitoba' -DartifactId=read-tse -Dversion='1.0.0' -Dpackaging=jar -DgeneratePom=true
```
add the dependency to pom file
```
<dependency>
    <groupId>ca.umanitoba</groupId>
        <artifactId>read-tse</artifactId>
    <version>1.0.0</version>
</dependency>
```
## Package
run following commands to obtain a packaged artifact 

```
mvn clean package assembly:single
```
The generated .jar is in the "target" folder.
## Output the csv file

Run the following commend to generate CSV file, you need to specify the directory of the JSON files and the output directory
where the CSV files are stored:

```
java -jar Java-1.0-SNAPSHOT-jar-with-dependencies.jar path_of_json path_of_output
 
```


## Result analysis

run the analysis.ipynb script to get the correlation coefficient between code metric and number of changes.





