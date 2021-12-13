# PhotoLabeler

## How to start

clone repo and create a shortcut on Desktop.
- Linux or Mac

```
$ git clone https://github.com/kawa-yo/PhotoLabeler && ln -s ./app/PhotoLabeler.jar ~/Desktop/PhotoLabeler
```

- Windows

Listen to Cortana, your best friend.

---

This app requires JRE14+.

If you don't have it yet, please download 
[Oracle JDK](https://www.oracle.com/java/technologies/javase)
or
[Open JDK](https://jdk.java.net/).

---

On Linux, if you want to run app by double clicking,

create a file named `jdk-14.desktop` (jdk 14 version) such as
```
[Desktop Entry]
Name=OpenJDK Java 14 Runtime
Comment=OpenJDK Java 14 Runtime
Keywords=java;runtime
Exec=cautious-launcher %f /PATH/TO/JDK14/bin/java -jar
Terminal=false
Type=Application
Icon=openjdk-14
MimeType=application/x-java-archive;application/java-archive;application/x-jar;
NoDisplay=true
```
and execute
```
$ sudo desktop-file-install jdk-14.desktop
$ sudo update-desktop-database
```
(then `/usr/share/applications/jdk-14.desktop` is created.)

Note that you cannot use the characters '~' or '$' to specify the path.
