@echo off
echo Starting Faculty Management System (FMS)...
echo Compiling source files...
javac -cp "resources/mysql-connector-j-9.0.0.jar" -d out src/Main.java src/model/*.java src/view/*.java src/controller/*.java
if %errorlevel% neq 0 (
    echo Compilation failed. Please check errors.
    pause
    exit /b %errorlevel%
)
echo Compilation successful. Launching FMS...
java -cp "out;resources/mysql-connector-j-9.0.0.jar" Main
if %errorlevel% neq 0 (
    echo.
    echo Application exited with an error. Please ensure MySQL is running in XAMPP.
    pause
)
