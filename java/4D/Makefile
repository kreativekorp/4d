all: clean bin osxclean jars

eclipseall: eclipseclean osxclean jars

clean:
	rm -rf bin
	rm -rf jar

eclipseclean:
	rm -rf jar

bin:
	mkdir -p bin
	javac -target 1.5 -sourcepath src src/*.java src/*/*/*.java src/*/*/*/*/*.java src/*/*/*/*/*/*.java -d bin
	cp src/com/kreative/fourdee/fourfs/res/*.png bin/com/kreative/fourdee/fourfs/res/

osxclean:
	export COPYFILE_DISABLE=true
	rm -f src/.DS_Store
	rm -f src/*/.DS_Store
	rm -f src/*/*/.DS_Store
	rm -f src/*/*/*/.DS_Store
	rm -f src/*/*/*/*/.DS_Store
	rm -f src/*/*/*/*/*/.DS_Store
	rm -f src/*/*/*/*/*/*/.DS_Store
	rm -f src/*/*/*/*/*/*/*/.DS_Store
	rm -f src/*/*/*/*/*/*/*/*/.DS_Store
	rm -f bin/.DS_Store
	rm -f bin/*/.DS_Store
	rm -f bin/*/*/.DS_Store
	rm -f bin/*/*/*/.DS_Store
	rm -f bin/*/*/*/*/.DS_Store
	rm -f bin/*/*/*/*/*/.DS_Store
	rm -f bin/*/*/*/*/*/*/.DS_Store
	rm -f bin/*/*/*/*/*/*/*/.DS_Store
	rm -f bin/*/*/*/*/*/*/*/*/.DS_Store

jars: ExtractGCI.jar FourFS.jar GCIView.jar LS4FS.jar Make4DX.jar Make4FS.jar MakeGCI.jar Unmake4DX.jar Unmake4FS.jar
	chmod a+x jar/*.jar

ExtractGCI.jar: osxclean
	mkdir -p jar
	jar cmf dep/MANIFEST-ExtractGCI.MF jar/ExtractGCI.jar -C bin com/kreative/fourdee/common -C bin com/kreative/fourdee/file -C bin com/kreative/fourdee/image -C bin com/kreative/fourdee/extractgci

FourFS.jar: osxclean
	mkdir -p jar
	jar cmf dep/MANIFEST-FourFS.MF jar/FourFS.jar -C bin com/kreative/fourdee/common -C bin com/kreative/fourdee/file -C bin com/kreative/fourdee/image -C bin com/kreative/fourdee/fourfs -C bin com/kreative/fourdee/gciview

GCIView.jar: osxclean
	mkdir -p jar
	jar cmf dep/MANIFEST-GCIView.MF jar/GCIView.jar -C bin com/kreative/fourdee/common -C bin com/kreative/fourdee/file -C bin com/kreative/fourdee/image -C bin com/kreative/fourdee/gciview

LS4FS.jar: osxclean
	mkdir -p jar
	jar cmf dep/MANIFEST-LS4FS.MF jar/LS4FS.jar -C bin com/kreative/fourdee/common -C bin com/kreative/fourdee/file -C bin com/kreative/fourdee/image -C bin com/kreative/fourdee/make4fs

Make4DX.jar: osxclean
	mkdir -p jar
	jar cmf dep/MANIFEST-Make4DX.MF jar/Make4DX.jar -C bin com/kreative/fourdee/common -C bin com/kreative/fourdee/file -C bin com/kreative/fourdee/image -C bin com/kreative/fourdee/dx

Make4FS.jar: osxclean
	mkdir -p jar
	jar cmf dep/MANIFEST-Make4FS.MF jar/Make4FS.jar -C bin com/kreative/fourdee/common -C bin com/kreative/fourdee/file -C bin com/kreative/fourdee/image -C bin com/kreative/fourdee/make4fs

MakeGCI.jar: osxclean
	mkdir -p jar
	jar cmf dep/MANIFEST-MakeGCI.MF jar/MakeGCI.jar -C bin com/kreative/fourdee/common -C bin com/kreative/fourdee/file -C bin com/kreative/fourdee/image -C bin com/kreative/fourdee/makegci

Unmake4DX.jar: osxclean
	mkdir -p jar
	jar cmf dep/MANIFEST-Unmake4DX.MF jar/Unmake4DX.jar -C bin com/kreative/fourdee/common -C bin com/kreative/fourdee/file -C bin com/kreative/fourdee/image -C bin com/kreative/fourdee/dx

Unmake4FS.jar: osxclean
	mkdir -p jar
	jar cmf dep/MANIFEST-Unmake4FS.MF jar/Unmake4FS.jar -C bin com/kreative/fourdee/common -C bin com/kreative/fourdee/file -C bin com/kreative/fourdee/image -C bin com/kreative/fourdee/make4fs

.PHONY: all eclipseall clean eclipseclean osxclean jars
