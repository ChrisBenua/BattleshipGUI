SET PATH_TO_FX="C:\Program Files\Java\Distribs\javafx-sdk-11.0.2"
SET LIB_ROOT="C:/Users/Christian/.m2/repository/"
SET PATH_TO_JACKSON_DATABIND="${LIB_ROOT}com/fasterxml/jackson/core/jackson-databind/2.9.8/jackson-databind-2.9.8.jar"
SET PATH_TO_JACKSON_ANNOTATIONS="${LIB_ROOT}com/fasterxml/jackson/core/jackson-annotations/2.9.0/jackson-annotations-2.9.0.jar"
SET PATH_TO_JACKSON_CORE="${LIB_ROOT}com/fasterxml/jackson/core/jackson-core/2.9.8/jackson-core-2.9.8.jar"

java --module-path $PATH_TO_FX/lib:$PATH_TO_JACKSON_ANNOTATIONS:$PATH_TO_JACKSON_CORE:$PATH_TO_JACKSON_DATABIND --add-modules javafx.graphics,javafx.controls,com.fasterxml.jackson.core,com.fasterxml.jackson.core -jar BattleshipGUI.jar client
