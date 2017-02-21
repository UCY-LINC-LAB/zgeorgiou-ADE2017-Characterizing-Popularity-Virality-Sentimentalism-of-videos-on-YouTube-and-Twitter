#!/bin/bash
pandoc -s README.md -S --toc -c style.css  -o readme.html
