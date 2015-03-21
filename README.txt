=== INSTALLATION & RUNNING ===

If you are not using an IDE to run our code, please follow the steps below to compile and run the application.

Commands assume that you have cloned the repository and have a terminal window open inside the top directory.


Linux:

 (once) mkdir bin

 (once) javac -d bin/ -cp lib/StudentData.jar:lib/htmlunit.jar:lib/javax.mail.jar:lib/jcommon.jar:lib/jedit.jar:lib/jfreechart.jar:lib/jide-oss.jar src/*/*.java

	java -cp lib/StudentData.jar:lib/htmlunit.jar:lib/javax.mail.jar:lib/jcommon.jar:lib/jedit.jar:lib/jfreechart.jar:lib/jide-oss.jar:bin gui.MainApp


Windows:

 (once) MD bin
 (once) javac -d bin\ -cp lib\StudentData.jar;lib\htmlunit.jar;lib\javax.mail.jar;lib\jcommon.jar;lib\jedit.jar;lib\jfreechart.jar;lib\jide-oss.jar src\*\*.java

	java -cp lib\StudentData.jar;lib\htmlunit.jar;lib\javax.mail.jar;lib\jcommon.jar;lib\jedit.jar;lib\jfreechart.jar;lib\jide-oss.jar;bin gui.MainApp

	
	
	
=== KNOWN ISSUES ===

We're not exactly sure what the required conditions are to trigger the error, but very
occasionally a JTabbedPane throws an ArrayIndexOutOfBounds error.

The stack trace is very similar to this one: https://bugs.eclipse.org/bugs/show_bug.cgi?format=multiple&id=352634

One possible solution which has been implemented is described here: https://community.oracle.com/message/5874003#5874003
although this doesn't seem to always prevent it.

NOTE: as far as we can tell, this error does not affect the normal running of the application in any way so don't worry if it happens.
