#!/bin/bash

# Crawler script cloning all git repos containing at least one pom.xml of the
# eclise github account and runs the vulnerability-assessment-tool and creates
# the maven dependency tree file of each of them.
#
# Author: Frederik Dennig <dennig@dbvis.inf.uni-konstanz.de>
# Copyright 2019 - 2020

# Constants
PWD=`pwd`
SPARTA_DIR="$HOME/sparta"								# The sparta directory
URL='https://api.github.com/users/eclipse/repos?page='	# eclipse account repos
LAST=22													# crawl pages 1 - 22
VULAS_PART="$PWD/vulas.part.txt"						# pom.xml profile part 
VULAS_PROFILE="$PWD/vulas.profile.txt"					# pom.xml full profile
TIMEOUT='1h'											# Limit vulas runtime
TIMEOUT_TRACE='3h'										# Limit trace runtime

# Crawls all repositories addresses of the eclispe github account and writes
# them line by line to repos.txt
determineAllRepoAddresses() {
	for i in `seq 1 $LAST`; do
		echo "Downloading: $URL$i"
		wget -O $i'.json' $URL$i
	done
	echo '' > all.json
	for i in `seq 1 $LAST`; do
		cat $i'.json' >> all.json
	done
	grep '"html_url": "https://github.com/eclipse/' all.json | # find repo urls
	cut -d'"' -f4 > repos.txt # write addresses to repo.txt
}

# Checks whether a repository contains a pom.xml file. If no pom.xml is found
# the directory is deleted. 
checkForPom() {
	cd "$SPARTA_DIR/$1"
	pwd
    POM_FILES=`find . -name 'pom.xml'`
    if [ -z "$POM_FILES" ]; then
		echo 'No pom.xml found! Deleting...'
		cd ..
		rm -rf $1
	else
		echo 'pom.xml found! Keeping...'
		cd $PWD
	fi
}

# Downloads a git repository and checks for a pom.xml file. If non is found the
# cloned repo is deleted.
downloadReposAndCheckForPomFile() {
	for REPO in `cat repos.txt`; do
		NAME=`echo $REPO | cut -d'/' -f5`
		if [ -d "$NAME" ]; then
			echo "$NAME already exists."
		else
			git clone $REPO "$SPARTA_DIR/$NAME"
			checkForPom $NAME
		fi
	done
}
 
# Produces a backup of the orignal pom.xml file named pom.xml.backup.
backupPom() {
	if [ ! -f 'pom.xml.backup' ]; then
		cp pom.xml pom.xml.backup
	fi
}

# Injects a pom.xml snippet into an existing pom.xml.
injectVulasProfile() {
	PROFILES=`grep '<profiles>' pom.xml`
	if [ -z "$PROFILES" ]; then
		injectVulasProfileInPomProject
	else
		injectVulasProfileInPomProfiles
	fi
	runVulasCheckerAndReporter
}

# Injects the pom.xml snippet into an existing pom.xml with a profiles tag.
injectVulasProfileInPomProfiles() {
	csplit pom.xml.backup '/<profiles>/'
	tail -n +2 xx01 > end.part
	cat xx00 $VULAS_PART end.part > pom.xml
	rm xx00 xx01 end.part
	echo "Modified profile."
}

# Injects the pom.xml snippet into an existing pom.xml without a profiles tag.
injectVulasProfileInPomProject() {
	csplit pom.xml.backup '/</project>/'
	cat xx00 $VULAS_PROFILE > pom.xml
	echo '</project>' >> pom.xml
	echo "Added profile."
}

# Executes all maven commands, i.e, runs the vulnerability-assessment-tool and
# generates a file containing the dependency tree.
runVulasCheckerAndReporter() {
	echo `pwd`
	echo "Compiling with vulas:app."
	timeout -k $TIMEOUT $TIMEOUT mvn -Dvulas compile vulas:app
	echo "Running vulas:report."
	timeout -k $TIMEOUT $TIMEOUT mvn -Dvulas vulas:report
}

# Goes to every pom.xml of the directories, then injects the vulas
# snippet into it if a profiles tag exists. After this it runs all maven
# commands to create a report.
findAndModifyPomFiles() {
	cd $SPARTA_DIR
	for DIR in `ls -d */`; do
		cd $DIR
		POM_FILES=`find . -name 'pom.xml'`
		if [ -z "$POM_FILES" ]; then
			echo 'No pom.xml found!'
		else
			for POM in $POM_FILES; do
				cd `dirname $POM`
				backupPom
				injectVulasProfile
				cd $SPARTA_DIR
				cd $DIR
			done
		fi
		cd $SPARTA_DIR
	done
}

# Resets all downloaded repositories to their original state.
resetAllGitProjects() {
	cd $SPARTA_DIR
	for DIR in `ls -d */`; do
		echo $DIR
		cd $DIR
		git clean -f
		git reset --hard
		cd ..
	done
	cd $PWD
}

# Resets all pom.xml files to their original state.
resetPomChangesOnly() {
	cd $SPARTA_DIR
	for FILE in `find -name pom.xml.backup`; do
		echo "Reseting $FILE..."
		NEW=`echo $FILE | sed 's/.backup$//'`
		cp $FILE $NEW
	done
	cd $PWD
}

createDatabaseFile() {
	java -jar db-importer.jar -i -d $SPARTA_DIR
}

addMetainfoToDatabaseFile() {
	java -jar db-importer.jar -m -d $SPARTA_DIR
}

# Pauses the script until [ENTER] is pushed and shows specified text. 
pause() {
   read -p "$*"
}

# Runs the full crawling process.
main() {
	echo 'You are about to start the full crawling process!'
	pause 'Press [Enter] key to continue...'
	mkdir -p $SPARTA_DIR
	determineAllRepoAddresses
	downloadReposAndCheckForPomFile
	findAndModifyPomFiles
	createDatabaseFile
	addMetainfoToDatabaseFile
}

# Prints the usage and help about available parameters.
function usage {
	cat <<-'EOF'
		usage: $./crawler.sh [-adhprs]
		-a      Run all steps of the crawling process
		-c      Create the database file
		-d      Download all repositories in the repos.txt file
		-e      Gather extra data from GitHub and LGTM
		-h      Display this help
		-p      Modify and process pom.xml files of all repositories
		-r      Reset and update all repositories, removing all modifications
		-s      Synchronize the repository list
		-t      Reset all pom.xml files
	EOF
}

while getopts acdhprst OPT; do
	case $OPT in
		a) main; exit;;
		c) createDatabaseFile; exit;;
		d) downloadReposAndCheckForPomFile; exit;;
		e) addMetainfoToDatabaseFile; exit;;
		h) usage; exit;;
		p) findAndModifyPomFiles; exit;;
		r) resetAllGitProjects; exit;;
		s) determineAllRepoAddresses; exit;;
		t) resetPomChangesOnly; exit;;
	esac
done

usage
exit 1
