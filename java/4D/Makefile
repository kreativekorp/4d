SRCFILES := $(shell find src -name \*.java)
RESFILES := $(shell find src -type f \! -name .\* \! -name \*.java | sed s/^src\\///)
PACKAGES := $(shell find src -name \*.java | sed -E s/^src\\/\|\\/[^\\/]+$$//g | grep -v \\.java$$ | sort | uniq | tr / .)

all: clean bin doc jars

eclipseall: eclipseclean jars

clean:
	rm -rf bin
	rm -rf doc
	rm -rf jar

eclipseclean:
	rm -rf jar

bin: src
	mkdir -p bin
ifeq ($(origin JAVACFLAGS), undefined)
	$(eval JAVACFLAGS = $(shell dep/minJavaVerOpts))
endif
	javac $(JAVACFLAGS) -classpath dep/ual.jar -sourcepath src $(SRCFILES) -d bin
	$(foreach res,$(RESFILES),cp src/$(res) bin/$(res);)

doc: src
	mkdir -p doc
	javadoc $(JAVADOCFLAGS) -classpath dep/ual.jar -sourcepath src $(PACKAGES) -d doc

jars: ExtractGCI.jar FourFS.jar GCIView.jar LS4FS.jar Make4DX.jar Make4FS.jar MakeGCI.jar Unmake4DX.jar Unmake4FS.jar

ExtractGCI.jar: bin
	export COPYFILE_DISABLE=true
	find bin -name .DS_Store -delete
	mkdir -p jar
	jar cmf dep/MANIFEST-ExtractGCI.MF jar/ExtractGCI.jar -C bin com/kreative/fourdee/common -C bin com/kreative/fourdee/file -C bin com/kreative/fourdee/image -C bin com/kreative/fourdee/extractgci
	chmod +x jar/ExtractGCI.jar

FourFS.jar: bin
	export COPYFILE_DISABLE=true
	find bin -name .DS_Store -delete
	mkdir -p jar
	jar cmf dep/MANIFEST-FourFS.MF jar/FourFS.jar -C bin com/kreative/fourdee/common -C bin com/kreative/fourdee/file -C bin com/kreative/fourdee/image -C bin com/kreative/fourdee/fourfs -C bin com/kreative/fourdee/gciview
	chmod +x jar/FourFS.jar

GCIView.jar: bin
	export COPYFILE_DISABLE=true
	find bin -name .DS_Store -delete
	mkdir -p jar
	jar cmf dep/MANIFEST-GCIView.MF jar/GCIView.jar -C bin com/kreative/fourdee/common -C bin com/kreative/fourdee/file -C bin com/kreative/fourdee/image -C bin com/kreative/fourdee/gciview
	chmod +x jar/GCIView.jar

LS4FS.jar: bin
	export COPYFILE_DISABLE=true
	find bin -name .DS_Store -delete
	mkdir -p jar
	jar cmf dep/MANIFEST-LS4FS.MF jar/LS4FS.jar -C bin com/kreative/fourdee/common -C bin com/kreative/fourdee/file -C bin com/kreative/fourdee/image -C bin com/kreative/fourdee/make4fs
	chmod +x jar/LS4FS.jar

Make4DX.jar: bin
	export COPYFILE_DISABLE=true
	find bin -name .DS_Store -delete
	mkdir -p jar
	jar cmf dep/MANIFEST-Make4DX.MF jar/Make4DX.jar -C bin com/kreative/fourdee/common -C bin com/kreative/fourdee/file -C bin com/kreative/fourdee/image -C bin com/kreative/fourdee/dx
	chmod +x jar/Make4DX.jar

Make4FS.jar: bin
	export COPYFILE_DISABLE=true
	find bin -name .DS_Store -delete
	mkdir -p jar
	jar cmf dep/MANIFEST-Make4FS.MF jar/Make4FS.jar -C bin com/kreative/fourdee/common -C bin com/kreative/fourdee/file -C bin com/kreative/fourdee/image -C bin com/kreative/fourdee/make4fs
	chmod +x jar/Make4FS.jar

MakeGCI.jar: bin
	export COPYFILE_DISABLE=true
	find bin -name .DS_Store -delete
	mkdir -p jar
	jar cmf dep/MANIFEST-MakeGCI.MF jar/MakeGCI.jar -C bin com/kreative/fourdee/common -C bin com/kreative/fourdee/file -C bin com/kreative/fourdee/image -C bin com/kreative/fourdee/makegci
	chmod +x jar/MakeGCI.jar

Unmake4DX.jar: bin
	export COPYFILE_DISABLE=true
	find bin -name .DS_Store -delete
	mkdir -p jar
	jar cmf dep/MANIFEST-Unmake4DX.MF jar/Unmake4DX.jar -C bin com/kreative/fourdee/common -C bin com/kreative/fourdee/file -C bin com/kreative/fourdee/image -C bin com/kreative/fourdee/dx
	chmod +x jar/Unmake4DX.jar

Unmake4FS.jar: bin
	export COPYFILE_DISABLE=true
	find bin -name .DS_Store -delete
	mkdir -p jar
	jar cmf dep/MANIFEST-Unmake4FS.MF jar/Unmake4FS.jar -C bin com/kreative/fourdee/common -C bin com/kreative/fourdee/file -C bin com/kreative/fourdee/image -C bin com/kreative/fourdee/make4fs
	chmod +x jar/Unmake4FS.jar

.PHONY: all eclipseall clean eclipseclean jars
