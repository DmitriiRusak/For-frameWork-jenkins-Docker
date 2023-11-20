#-------------------------------------------------------------------
#  This script expects the following environment variables
#     BROWSER
#     HUB_HOST
#     TEST_SUITE
#-------------------------------------------------------------------
# Let's print what we have received, echo- is just print statement
echo "-------------------------------------------"
echo "BROWSER       : ${BROWSER:-chrome}"
echo "HUB_HOST      : ${HUB_HOST:-hub}"
echo "TEST_SUITE    : ${TEST_SUITE}"
echo "-------------------------------------------"
# Do not start the tests immediately. Hub has to be ready with browser nodes
#echo "Checking if hub is ready..!"
count=0
while [ "$( curl -s http://${HUB_HOST:-hub}:4444/status | jq -r .value.ready )" != "true" ]
#while [ "$( curl -s http://${HUB_HOST:10.0.2.15}:4444/status | jq -r .value.ready )" != "true" ]
do
  count=$((count+1))
  echo "Not ready, Attempt # is : ${count}"
  if [ "$count" -ge 30 ]
  then
      echo "**** HUB IS NOT READY WITHIN 30 SECONDS ****"
      exit 1
  fi
  sleep 1
done
# At this point, selenium grid should be up!
echo "Selenium Grid is up and running. Running the test...."
# Start the java command, если при старте значения не переписываются то используются значения по умолчанию.
java -Dbrowser=${BROWSER:chrome} -Dselenium_grid_enabled=true -Dselenium_grid_hubHost=${HUB_HOST:-hub} -cp 'libs/*' org.testng.TestNG test_sutes/${TEST_SUITE}