=== INSTALLATION & RUNNING ===

If you are not using an IDE to run our code, please follow the steps below to compile and run the application.

Commands assume that you have cloned the repository and have a terminal window open inside the top directory.


Linux:

 (once) mkdir bin
 (once) javac -d bin/ -cp src src/*/*.java
	java -cp lib/StudentData.jar:lib/htmlunit.jar:lib/javax.mail.jar:lib/jcommon.jar:lib/jedit.jar:lib/jfreechart.jar:lib/jide-oss.jar:bin gui.MainInterface


Windows:

 (once) MD bin
 (once) javac -d bin\ -cp src src\*\*.java
	java -cp lib\StudentData.jar;lib\htmlunit.jar;lib\javax.mail.jar;lib\jcommon.jar;lib\jedit.jar;lib\jfreechart.jar;lib\jide-oss.jar;bin gui.MainInterface
