#! /bin/sh
source /opt/wise/github/WISE_2015/dev/WISE.rc
rm $TOMCAT_HOME/webapps/WISE.war
rm $TOMCAT_HOME/webapps/WiseStudySpaceWizard.war
$TOMCAT_HOME/bin/startup.sh
echo "sleeping for tomcat startup"
sleep 10
cp /opt/wise/github/WISE_2015/WiseStudySpaceWizard/dist/WiseStudySpaceWizard.war $TOMCAT_HOME/webapps
echo "sleeping for 20s while deploying WiseStudySpaceWizard"
sleep 20
cp /opt/wise/github/WISE_2015/Wise/dist/WISE.war $TOMCAT_HOME/webapps
echo "WISE deployment complete"
