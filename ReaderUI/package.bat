@echo off
"C:\Program Files\Java\jdk-17.0.1\bin\jpackage.exe" ^
--type exe ^
--verbose ^
--dest D:\Development\Workspace\CardReader\ReaderUI\target\package ^
--name CardReader ^
--module net.exoa.readerui/net.exoa.readerui.HelloApplication ^
--runtime-image D:\Development\Workspace\CardReader\ReaderUI\target\CardReaderImage ^
--win-dir-chooser ^
--win-menu ^
--win-shortcut ^
--app-version 1.0 ^
--vendor "Hendrik Jongebloed" ^
--icon D:\Development\Workspace\CardReader\ReaderUI\src/main/resources/CardReader.ico

pause