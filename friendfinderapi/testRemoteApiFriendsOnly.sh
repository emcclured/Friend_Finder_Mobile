#!/bin/bash
###############################################
# Test Script for FriendFinder API using curl #
###############################################

curl -X DELETE https://assignment5-1030.appspot.com/_ah/api/friendfinderapi/v1/friend/delete/1
curl -X DELETE https://assignment5-1030.appspot.com/_ah/api/friendfinderapi/v1/friend/delete/2
curl -X DELETE https://assignment5-1030.appspot.com/_ah/api/friendfinderapi/v1/friend/delete/3
curl -X DELETE https://assignment5-1030.appspot.com/_ah/api/friendfinderapi/v1/friend/delete/4

curl --header "Content-Type: application/json" -X POST -d '{"id": "1", "email": "berry@live.com", "name": "berry", "address": "47.6197 -122.3331", "phone_number": "555-555-5555", "user_id": "1"}' https://assignment5-1030.appspot.com/_ah/api/friendfinderapi/v1/friend/insert
curl --header "Content-Type: application/json" -X POST -d '{"id": "2", "email": "valentine@live.com", "name": "val", "address": "47.6097 -122.3431", "phone_number": "555-555-5555", "user_id": "1"}' https://assignment5-1030.appspot.com/_ah/api/friendfinderapi/v1/friend/insert
curl --header "Content-Type: application/json" -X POST -d '{"id": "3", "email": "katy@live.com", "name": "katy", "address": "47.6297 -122.3351", "phone_number": "555-555-5555", "user_id": "2"}' https://assignment5-1030.appspot.com/_ah/api/friendfinderapi/v1/friend/insert
curl --header "Content-Type: application/json" -X POST -d '{"id": "4", "email": "nick@live.com", "name": "nick", "address": "47.6255 -122.3431", "phone_number": "555-555-5555", "user_id": "2"}' https://assignment5-1030.appspot.com/_ah/api/friendfinderapi/v1/friend/insert

# list FriendFinder friends

echo "###################################################################"
echo "List FriendFinder frineds"
echo "###################################################################"

# need to sleep to allow for datastore to catch up
sleep 2

curl https://assignment5-1030.appspot.com/_ah/api/friendfinderapi/v1/friends/list

echo ""
