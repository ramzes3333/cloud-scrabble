nl -s';' raw_polish_dictionary.csv > raw_polish_dictionary_with_ids.csv
split --verbose -dl 100000 --additional-suffix=.csv raw_polish_dictionary_with_ids.csv polish_dictionary
sed -i '1s/^/id;value\n/' polish_dictionary*
