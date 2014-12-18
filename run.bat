del src\*.class
javac -cp lib\lwjgl.jar;lib\lwjgl_util.jar;lib\slick.jar;src src\Launch.java
java  -cp lib\lwjgl.jar;lib\lwjgl_util.jar;lib\slick.jar;src -Djava.library.path=native Launch
PAUSE