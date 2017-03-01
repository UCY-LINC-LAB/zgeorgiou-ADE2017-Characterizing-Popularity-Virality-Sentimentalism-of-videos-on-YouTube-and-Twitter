#!/bin/bash
###################################################################
#																																	#
# This scripts assumes that collector service runs on port 8001		#
#																																	#
###################################################################

clear 

if [[ "$#" != 3 ]]; then
	echo -e "Usage:"
	echo -e "\targ1:\t\t  Host address"
	echo -e "\targ2:\t\t  Token file"
	echo -e "\targ3:\t\t  YouTube Key file"
	exit 1
fi

HOST="$1"
TOKEN_FILE="$2"
KEY_FILE="$3"

# Check token file if exists
if  ! [[ -s "$TOKEN_FILE" ]] ; then
	echo -e "$TOKEN_FILE doesn't exists or permissions to read needed!"
	exit 1
fi

# Check key file if exists
if  ! [[ -s "$KEY_FILE" ]] ; then
	echo -e "$KEY_FILE doesn't exists or permissions to read needed!"
	exit 1
fi

# Read token from token file
while IFS='' read -r line || [[ -n "$line" ]]; do
    TOKEN="$line"
		break
done < "$TOKEN_FILE"

# Read key from key file
while IFS='' read -r line || [[ -n "$line" ]]; do
    KEY="$line"
		break
done < "$KEY_FILE"
echo "[Info] Using token: $TOKEN"
echo "[Info] Using key: $KEY"
echo "{\"api_key\":\"$KEY\"}"

curl  -X PUT -H "Content-Type: application/x-www-form-urlencoded" --data-urlencode "api_key=$KEY" -H "token: $TOKEN" "http://$1:8001/youtubeKey"
