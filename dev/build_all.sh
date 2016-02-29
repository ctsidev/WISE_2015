#! /bin/sh
export JAVA_HOME=/opt/java/jdk1.7.0_79/

#fetch latest code
git pull origin master;

cd ..;
cd WiseShared;
ant dist;
cd ..;
cd WiseStudySpaceWizard;
ant dist;
cd ..;
cd Wise;
ant dist;
cd ..;
cd dev;
