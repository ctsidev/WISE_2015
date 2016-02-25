#! /bin/sh
source /opt/github/WISE_2015/aws/WISE.rc
rm -rf $TOMCAT_HOME/webapps/WISE
rm -rf $TOMCAT_HOME/webapps/WiseStudySpaceWizard
rm $TOMCAT_HOME/webapps/WISE.war
rm $TOMCAT_HOME/webapps/WiseStudySpaceWizard.war
sudo service tomcat8 start
echo "sleeping for tomcat startup"
sleep 10
cp /opt/github/WISE_2015/WiseStudySpaceWizard/dist/WiseStudySpaceWizard.war $TOMCAT_HOME/webapps
echo "sleeping for 20s while deploying WiseStudySpaceWizard"
sleep 20
cp /opt/github/WISE_2015/Wise/dist/WISE.war $TOMCAT_HOME/webapps
sleep 10
tail /var/log/tomcat8/catalina.out
echo "WISE deployment complete"
