import re
import json
from json import *
from pprint import pprint

#This is looking for a PREFIX that does not contain letters or numbers, or a start of string or line
#It will not include the char in the match, and the group is non capturing (FASTER)
beginningWord = '(?:(?<=[^A-Za-z])|^)'
dateBeginningWord = '(?:(?<=[^A-Za-z0-9])|^)'

#This is looking for a SUFFIX that does not contain letters or numbers, or the end of string or line
#It will not include the char in the match, and the group is non capturing (FASTER)
endWord = '(?:(?=[^A-Za-z])|$)'
dateEndWord = '(?:(?=[^A-Za-z0-9])|$)'

def jsonprocessor (readfile):
    jsonfile = open(readfile,"rb")
    data = json.load(jsonfile)
    return data
    jsonfile.close()

def replacename(sentence,fname,sname):
    fnamere = NameVariations(fname)
    snamere = NameVariations(sname)
    fnamere = re.compile(fnamere[0])
    snamere = re.compile(snamere[0])
    sentence = fnamere.sub("XXXXX",sentence)
    sentence = snamere.sub("XXXXX",sentence)
    return sentence

def replacenum(sentence,num):
    numre = TelephoneNumberVariations(num)
    numre = re.compile(numre[0])
    sentence = numre.sub("FFFFF",sentence)
    return sentence

def replaceid(sentence,nhsid):
    nhsre = NHSIDVariations(nhsid)
    nhsre = re.compile(nhsre[0])
    sentence = nhsre.sub("HHHHH",sentence)
    return sentence
    
def NameVariations(listOfNameStrings):
    output = []
    nameString = ''
    for element in listOfNameStrings:
        #clean up name string and put spaces where all of the weird chars are
        cleaningExpression = re.compile('[^a-zA-Z\']') 
        element = cleaningExpression.sub(' ', element, 0) 
        #now split on spaces
        for name in element.split():
            if len(name) > 1:
                nameString = nameString + NameHyphenationManager(name.strip()) + 's?|'        
    #don't add the terms if there aren't enough of them
    if len(nameString) > 1:
        # don't forget to subtract the last | from the nameString
        output.append(beginningWord + '(?:' + nameString[0:-1] + ')' + endWord)
    return output

def NameHyphenationManager(name):
    #We are using the double escaped version of ' here as FAST escapes 's when converting from attachments
    return re.sub('\'', '(?:\s|\'|&amp;rsquo;)*', name, 0)

def AddressVariations(address, aliases):
    output = []
    count = 0
    outputField = ''
    cleaningExpression = re.compile('(?:,|/|-|\\.|\\(|\\))') 
    address = cleaningExpression.sub(' ', address, 0).strip() # get rid of all of the commas and slashes 
    if len(address) > 1:
        for addressTerm in address.split():
            #skip the whole process if the address term is a gt, lt, or amp
            if addressTerm.upper() <> 'GT' and addressTerm.upper <> 'LT' and addressTerm.upper <> 'AMP':
                count = count + 1   
                #Puts the address aliases in if they match any term.
                if aliases.has_key(addressTerm):
                    addressTerm = '(?:' + aliases[addressTerm] + ')'
                else:
                    #only need to escape if it's not one of ours.
                    addressTerm = EscapeRegEx(addressTerm)
                outputField = outputField + beginningWord + addressTerm + '(?:\s|,|/|-|;|:|\\.|\\(|\\))+|' 
    if count > 1: 
        outputField = '(?:' + outputField[0:-1] + '){2,}'
    elif count == 1:
        #Cut the whole end off the string
        outputField =  outputField[0:-27] + endWord
    if outputField != '':
        output.append(outputField) 
    return output

def NHSIDVariations(nhsid):
    output = []
    cleaningExpression = re.compile('[^0-9]') 
    nhsid = cleaningExpression.sub('', str(nhsid), 0) # get rid of all of the commas and slashes
 
    tempString = ''
    if len(nhsid) > 3:
        for x in range(0, len(nhsid)):
            tempString = tempString + str(nhsid[x]) + '(?:\s|-|\.|/)*'
        tempString = tempString[0:-14]    
        output.append(tempString)
    return output

def DateVariations(year, month, day):
    output = []
    if int(day) < 10:
        day = str(day)[0]
    
    if int(month) < 10:
        month = str(month)[0]
    #create a date with just the correct month in it.
    #tempDate = datetime(2000, int(month), 1, 0, 0, 0, 0)
    shortYear = str(year)[2:4]  
    #create a date with just the correct month in it.
    tempDate = datetime(2000, int(month), 1, 0, 0, 0, 0)
    
    #all options, day month year, delimiters, space, ., -, / and \
    #includes possible 0's in front of days and months.
    output.append(dateBeginningWord + "0?" + str(day) + "(?:\s|\\.|-|/|\\\)+0?" + str(month) + "(?:\s|\\.|-|/|\\\)?(?:" + str(shortYear) + "|" + str(year) + ")?" + dateEndWord) 
    #same as above month day year
    output.append(dateBeginningWord + "0?" + str(month) + "(?:\s|\\.|-|/|\\\)+0?" + str(day) +"(?:\s|\\.|-|/|\\\)?(?:" + str(shortYear) + "|" + str(year) + ")?" + dateEndWord) 
    #same as above, year month day.
    output.append(dateBeginningWord + "(?:" + str(shortYear) + "|" + str(year) + ")(?:\s|\\.|-|/|\\\)+0?" + str(month) +"(?:\s|\\.|-|/|\\\)+0?" + str(day) + dateEndWord)
    #25th of Feb, or Feburary, and both year types.
    output.append(strftime(dateBeginningWord + "0?" + str(day) +"\s*(?:th|st|nd|rd)?\s*(?:of|,)?\s*(?:%b|%B)\s*,?\s*(?:" + str(shortYear) + "|" + str(year) + ")?" + dateEndWord, tempDate.timetuple()))
    #Feburary (or feb) the 25(th)(,) 2008, and both year types.
    output.append(strftime(dateBeginningWord + "(?:%b|%B)\s*(?:the|,)?\s*0?" + str(day) + "\s*(?:th|st|nd|rd)?\s*,?\s*(?:" + str(shortYear) + "|" + str(year) + ")?" + dateEndWord, tempDate.timetuple()))
    return output


def TelephoneNumberVariations(number):
    output = []
    #remove anything that isn't a number
    number = re.sub("[^0-9]", "", str(number))
    
    if len(number) == 11:
        number = str(number)
        output.append('(?:(?:(?:\(?%s\)?\s?%s\s?%s)|(?:\(?%s\)?\s?%s\s?%s)|(?:\(?%s\)?\s?%s\s?%s))(?:\s?\#(?:\d{4}|\d{3}))?)' % \
                      (number[0:5], number[5:8], number[8:11], number[0:4], number[4:7], number[7:11], number[0:3], number[3:7], number[7:11]))
    return output
  
def PostcodeVariations(postcode):  
    output = []
    #split if we have multiples, and remove leading and trailing spaces from EACH element
    for element in postcode.split(','):
        element = element.strip()
        element = EscapeRegEx(element)
        # replace any spaces with optional spaces
        element = re.sub('\s+', '\s*', element)
    
    output.append(element)
    return output

def EscapeRegEx(cleanWord):
        return re.sub(r"(\\|\.|\^|\$|\*|\+|\?|\{|\[|\]|\||\(|\))",
                               r"\\\1", cleanWord)
