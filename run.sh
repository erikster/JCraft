# warning: requires jdk 8u25+
# uncomment the following lines if you want to properly rebuild, or leave blank to execute
# del src/*.class
# javac -cp lib/lwjgl.jar:lib/lwjgl_util.jar:lib/slick.jar:src src/Launch.java
java -cp lib/lwjgl.jar:lib/lwjgl_util.jar:lib/slick.jar:src -Djava.library.path=native Launch