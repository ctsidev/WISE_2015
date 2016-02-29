#! /bin/sh
DEV_CODE_BASE=/opt/wise/github/WISE_2015/
PROD_CODE_BASE=/opt/github/WISE_2015/
KEY=$1
PROD_SERVER=ec2-user@ec2-50-18-27-76.us-west-1.compute.amazonaws.com
ssh -i $KEY $PROD_SERVER "sudo sh /opt/github/WISE_2015/aws/wise_tomcat_stop.sh; ps aux|grep java;"
scp -i $KEY $DEV_CODE_BASE/Wise/dist/*.war $PROD_SERVER:$PROD_CODE_BASE/Wise/dist/
scp -i $KEY $DEV_CODE_BASE/WiseStudySpaceWizard/dist/*.war $PROD_SERVER:$PROD_CODE_BASE/WiseStudySpaceWizard/dist/
ssh -i $KEY $PROD_SERVER "sudo sh /opt/github/WISE_2015/aws/wise_tomcat_start.sh; sudo tail -100 /var/log/tomcat8/catalina.out"
