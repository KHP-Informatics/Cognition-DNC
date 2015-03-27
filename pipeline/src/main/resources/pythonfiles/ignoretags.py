from bs4 import BeautifulSoup as bs
import re
from psuedonymisation import *
import sys

line = sys.argv[1]
jsonfile = sys.argv[2]


def notags():
    global line
    global jsonfile
    fileop = open(line, "rb")
    data = jsonprocessor(jsonfile)
    soup = bs(fileop)
    structured = soup.prettify()
    splitstruct = structured.splitlines()
    for line in splitstruct:
        ind = splitstruct.index(line)
        line = line.strip()
        if len(line) > 0 and line[0] != "<":
            fname = data["foreNames"]
            sname = data["surnames"]
            line = replacename(line,fname,sname)
            phone = data["phoneNumbers"]
            for num in phone:
                line = replacenum(line, num)
            nhsid = data["NHSNumber"]
            line = replaceid(line,nhsid)
        splitstruct[ind] = line
    output = ' '.join(splitstruct)
    soupout = bs(output)
    fileop.close()
    return soupout

out = notags()
print out