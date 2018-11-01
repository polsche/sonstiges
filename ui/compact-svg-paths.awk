# Rundet den übergebenen Wert
function round( num) {
	return 0 + sprintf("%.2f", num);
}

# Gibt die übergebene Zeile mit rekursiv ersetzten Koordinaten aus.
function parseLine( rest) {
	do {
		idx = match(rest, /[" \t][eE0-9.-]+,[eE0-9.-]+[" \t\r\n]/);
		if (idx > 0) {
			printf "%s", substr(rest, 1, idx);
			rest1 = substr(rest, idx+1);
			endIdx = match(rest1, /[" \t\r\n]/);
			point = substr(rest1, 1, endIdx-1);
			split(point, koords, /,/);
			printf "%s,%s", round(koords[1]), round(koords[2]);
			parseLine(substr(rest1, endIdx));
		} else {
			print rest;
		}
	} while (idx > 1);
}

# Zeilenweise Verarbeitung der Datei
{
	if (match($0, /(^.*(x|y|rx|ry|width|height)=")([eE0-9.-]+)(".*$)/, matches)) {
		line = matches[1] round(matches[3]) matches[4];
	} else {
		line = $0;
	}
	parseLine(line);
}
