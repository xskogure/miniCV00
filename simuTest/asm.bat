@echo off

set "FULLARG=%~1"
set "FILEANDEXT=%~nx1"
set "FILENAME=%~n1"

set "DIRNAME=%~p1"
call set "DIRNAME=%%FULLARG:%FILEANDEXT%=%%"

set "OUTFILE=%DIRNAME%%FILENAME%.bin"

java -jar sep3asm.jar %1 > %OUTFILE%

type %OUTFILE%

echo =====
echo make %OUTFILE%