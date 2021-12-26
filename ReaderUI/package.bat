@echo off
"C:\Program Files\Java\jdk-17.0.1\bin\jpackage.exe" ^
--type exe ^
--verbose ^
--dest target/package ^
--name CardReader ^
--module net.exoa.readerui/net.exoa.readerui.HelloApplication ^
--runtime-image target/CardReaderImage ^
--win-dir-chooser ^
--win-menu ^
--win-shortcut ^
--app-version 1.0 ^
--icon src/main/resources/malteser.ico

pause