## HTML Element Finder

## General 

This program dedicated to find HTML elements by given example (from original HTML file) in set of provided target HTML files.
 
## How to run

At least one parameter - source HTML file with example element required. Element with id `make-everything-ok-button` 
from that file will be treated as example element. You can also define example element id as first parameter and source file path as second.

For example   

    java -jar html-element-finder-0.0.1.jar original.html target-1.html target-2.html ...
    java -jar html-element-finder-0.0.1.jar element-id original.html target-1.html target-2.html ...
       
## What in repository

    /src                   - programm sources
    /build/libs            - programm binaries
    comparation-output.txt - comparison output for sample pages 
       