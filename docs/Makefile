documentation : The-Pseu-Programming-Langauge.html PSAM.html Syntactic-and-other-extensions.html

%.html : %.md
	pandoc -s -t html --toc --toc-depth=3 --title-prefix="$^" --css docs.css -o $@ $^

clean : 
	rm *.html
