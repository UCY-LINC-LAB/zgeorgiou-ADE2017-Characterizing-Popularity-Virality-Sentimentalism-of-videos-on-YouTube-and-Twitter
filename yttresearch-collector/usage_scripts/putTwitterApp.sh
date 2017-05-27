#!/bin/bash
###################################################################
#																																	#
# This script assume that collector service runs on port 8001		#
#																																	#
###################################################################

clear 

if [[ "$#" != 3 ]]; then
	echo -e "Usage:"
	echo -e "\targ1:\t\t  Host address"
	echo -e "\targ2:\t\t  Token file"
	echo -e "\targ3:\t\t  Twitter App file"
	exit 1
fi

HOST="$1"
TOKEN_FILE="$2"
APP_FILE="$3"

# Check token file if exists
if  ! [[ -s "$TOKEN_FILE" ]] ; then
	echo -e "$TOKEN_FILE doesn't exists or permissions to read needed!"
	exit 1
fi

# Check key file if exists
if  ! [[ -s "$APP_FILE" ]] ; then
	echo -e "$APP_FILE doesn't exists or permissions to read needed!"
	exit 1
fi

# Read app keys from App file
count=0
while IFS='' read -r line || [[ -n "$line" ]]; do
		if [[ "$count" == "0" ]] ; then NAME="$line" ; fi
		if [[ "$count" == "1" ]] ; then CONSUMER_KEY="$line" ; fi
		if [[ "$count" == "2" ]] ; then CONSUMER_SECRET="$line" ; fi
		if [[ "$count" == "3" ]] ; then TW_TOKEN="$line" ; fi
		if [[ "$count" == "4" ]] ; then TOKEN_SECRET="$line" ; fi
		count=$((count+1))
done < "$APP_FILE"

# Read token from token file
while IFS='' read -r line || [[ -n "$line" ]]; do
    TOKEN="$line"
		break
done < "$TOKEN_FILE"
echo "[Info] Using token: $TOKEN"
echo "[Info] Using name : $NAME"
echo "[Info] Using consumer key: $CONSUMER_KEY"
echo "[Info] Using consumer secret: $CONSUMER_SECRET"
echo "[Info] Using twitter token: $TW_TOKEN"
echo "[Info] Using token secret: $TOKEN_SECRET"

curl  -X PUT -H "Content-Type: application/x-www-form-urlencoded" -d "name=$NAME" -d "consumer_key=$CONSUMER_KEY" -d "token=$TW_TOKEN" -d "consumer_secret=$CONSUMER_SECRET" -d "token_secret=$TOKEN_SECRET" -H "token: $TOKEN" "http://$1:8001/twitterApp"
