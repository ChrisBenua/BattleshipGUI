SET PATH_TO_FX=C:\Program Files\Java\Distribs\javafx-sdk-11.0.2\lib
SET PATH_TO_JACKSON=C:\Users\irusi\.m2\repository\com\fasterxml\jackson
SET PATH_TO_DATABIND=%PATH_TO_JACKSON%\core\jackson-databind\2.9.8\jackson-databind-2.9.8.jar
SET PATH_TO_ANNOTATIONS=%PATH_TO_JACKSON%\core\jackson-annotations\2.9.0\jackson-annotations-2.9.0.jar
SET PATH_TO_CORE=%PATH_TO_JACKSON%\core\jackson-core\2.9.8\jackson-core-2.9.8.jar

java --module-path ^"%PATH_TO_FX%;%PATH_TO_DATABIND%;%PATH_TO_ANNOTATIONS%;%PATH_TO_CORE%^" --add-modules javafx.graphics,javafx.controls,com.fasterxml.jackson.databind,com.fasterxml.jackson.core -jar BattleshipGUI.jar client