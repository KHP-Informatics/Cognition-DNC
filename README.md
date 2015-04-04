<html>
<body>

<h1>
Welcome to Cognition!
</h1>
Cognition is an open source database processing pipeline. Its purpose is to convert common document formats into a common representation, and modify them in a customisable manner. Its primary use case is to facilitate electronic health record (EHR) informatics research by converting documents into a common format, and masking strong patient identifiers according to a customisable, regular expression based algorithm.

<h2>
The Problem
</h2>
Informatics research into EHRs is a rapidly growing discipline across the globe, driven by the expanding expectations of technology to drive down the costs of patient care and increase research output. One of the key challenges is enabling researchers, informaticians and data scientists access to large, rich datasets of clinical records, without compromising the information governance policies of care organisations, and ethical guidelines protecting patient confidentiality. Many existing research clinical datasets are unable to provide access to free text, as the potential risks of accidentally or maliciously identifying patients from their notes is too great. In areas such as mental health, this is highly problematic for researchers, as most of the variables of interest are contained within the clinical free-text.

<h2>Cognition</h2>

As part of other information governance controls, we propose the Cognition pipeline as a means to mask strong patient identifiers in the clinical text, while retaining as much structure as possible of the original document. The basic components of the pipeline proceed as follows:


1. Cognition receives input in the form of a JSON array. This array contains details of ‘co-ordinates’ from a source database - unique references to binary and character (free text) datatypes that need to be processed
2. Character data types are passed directly to the anonymisation process
  1. Binary objects such as MS Word documents are converted to XHTML with the Apache Tika library. PDFs undergo OCR with Tesseract (if installed)
  2. The XHTML files are passed through an anonymisation algorithm (based on regular expressions), to mask strong patient identifiers
3. The results are passed back into a database, with co-ordinate references to allow the processed document to be queried in place of the original document
 



<h3>
Build Instructions:
</h3>
To build from source, ensure Maven is installed and navigate to the Cognition-DNC directory:

` mvn clean install`

This will produce a .jar executable file in the following directory

`pipeline/target`

<h4>
Configuration:
</h4>

The following directory contains all of the files needed to configure Cognition:

`pipeline/target/config `

<h5>
Database Connection
</h5>

Edit 
`src/main/resources/applicationContext.xml`

so that:

1. The bean with id "sourceDataSource" points to the desired source DB (with username/password specified).
2. The bean with id "targetDataSource" points to the desired target DB (with username/password specified).
3. The bean with "sourceSessionFactory" uses the correct SQL dialect.
4. The bean with "targetSessionFactory" uses the correct SQL dialect.

<h5>
Query configuration
</h5>
Edit `pipeline/src/main/resources/queries/sqlserver-pipeline-named-queries.hbm.xml` so it respects your schema and patient model. For example, the following assumes that you have a patient table in the target DB with the patient key = ID, and the patient date of birth = “dob”:

```xml
<sql-query name="getPatient">
select ID as "id"
  , nhs_no as "NHSNumber"
  ,dob as "dateOfBirth" 
  from tblPatient 
  where ID = :patientId
</sql-query>
```

Cognition currently has support for the following common identifiers, but these are easily expandable to meet other requirements:

Identifier|
----------|
NHS number|
Date of Birth|
First Name|
Last Name|
Address|
Post Code|
Phone number|

<h5>
Masking Algorithm Customisation
</h5>
Finally, edit the files in `pipeline/target/config/anonymisation` to reflect the regular expressions you want to use to mask the identifiers. For example, you might want to add an expression to mask dates of birth in the format 

`yyyy.MM.dd`

To do this, simply add a new json object to the json array in the `dateOfBirthRules.json` file, such as

```json
  {"regexp": "(?i)${TimeUtil.getFormattedDate($patient.getDateOfBirth(), 'yyyy.MM.dd')}", "placeHolder" : "DDDDD"}
```

<h4>
Usage:
</h4>

Once the configuration is complete, the only input Cognition requires is a json file of database coordinates that it should process, This can be generated any number of ways, but check out our CognitionJobMaker project for a suggestion.

An example coordinates.json contains the following text:
```json
[
{"sourceTable":"PatientBinaryDocs","pkColumnName":"ID","patientId":1,
"type":"binary","idInSourceTable":43,"sourceColumn":"BinaryContent"},

{"sourceTable":"PatientPlainTextDocs","pkColumnName":"ID","patientId":2,
"type":"text","idInSourceTable":21,"sourceColumn":"TextContent"}
]
```
This file specifies two work coordinates. The first coordinate points to a binary file to be converted and anonymised.

JSON Key|JSON Value
--------|-----------
sourceTable|the name of the table of the target object
pkColumnName|the primary key name of the table of the target object
patientId|the primary key of the patient (used to gather patient specific identifiers)
type|either binary or text
idInSourceTable|the primary key value of the target object
sourceColumn|the name of the column that the target object is in


The second coordinate points to a plain text type to be anonymised.
 Cognition can then be executed with the following command:

`java -jar /path/to/jar/file.jar --createMode --file=coordinates.json`


That’s it! If you’re interested in/need help with using our Cognition pipeline, please get in touch richard.r.jackson@kcl.ac.uk, ismailemrekartoglu@gmail.com, richard.j.dobson@kcl.ac.uk 
</body>
</html>
