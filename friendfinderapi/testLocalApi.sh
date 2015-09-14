#!/bin/bash
###############################################
# Test Script for FriendFinder API using curl #
###############################################

echo "###################################################################"
echo "Begin testing of FriendFinder API"
echo "###################################################################"

echo ""

# add user

echo "###################################################################"
echo "Adding FriendFinder user"
echo "###################################################################"

echo ""

curl --header "Content-Type: application/json" -X POST -d '{"id": "1", "user_name": "tom", "user_password": "tom"}' http://localhost:8080/_ah/api/friendfinderapi/v1/user/insert

echo ""

curl --header "Content-Type: application/json" -X POST -d '{"id": "2", "user_name": "jones", "user_password": "jones"}' http://localhost:8080/_ah/api/friendfinderapi/v1/user/insert

echo ""

curl --header "Content-Type: application/json" -X POST -d '{"id": "3", "user_name": "mary", "user_password": "mary"}' http://localhost:8080/_ah/api/friendfinderapi/v1/user/insert

echo ""

curl --header "Content-Type: application/json" -X POST -d '{"id": "4", "user_name": "sue", "user_password": "sue"}' http://localhost:8080/_ah/api/friendfinderapi/v1/user/insert

echo ""

# list user

echo "###################################################################"
echo "List FriendFinder user"
echo "###################################################################"

# need to sleep to allow for datastore to catch up
sleep 2

curl http://localhost:8080/_ah/api/friendfinderapi/v1/users/list

echo ""

# update user

echo "###################################################################"
echo "Update FriendFinder user tom"
echo "###################################################################"

curl --header "Content-Type: application/json" -X PUT -d '{"user_name": "tom", "user_password": "tom1", "id": "1"}' http://localhost:8080/_ah/api/friendfinderapi/v1/user/update

echo ""
echo "###################################################################"


# add friends

echo "###################################################################"
echo "Adding FriendFinder friends"
echo "###################################################################"

curl --header "Content-Type: application/json" -X POST -d '{"id": "1", "email": "berry@live.com", "name": "berry", "address": "101 Fabulous", "phone_number": "555-555-5555", "user_id": "1"}' http://localhost:8080/_ah/api/friendfinderapi/v1/friend/insert

echo ""

curl --header "Content-Type: application/json" -X POST -d '{"id": "2", "email": "valentine@live.com", "name": "val", "address": "101 Fabulous", "phone_number": "555-555-5555", "user_id": "2"}' http://localhost:8080/_ah/api/friendfinderapi/v1/friend/insert

echo ""

curl --header "Content-Type: application/json" -X POST -d '{"id": "3", "email": "tom@live.com", "name": "tom", "address": "101 Fabulous", "phone_number": "555-555-5555", "user_id": "3"}' http://localhost:8080/_ah/api/friendfinderapi/v1/friend/insert

echo ""

curl --header "Content-Type: application/json" -X POST -d '{"id": "4", "email": "nita@live.com", "name": "nita", "address": "101 Fabulous", "phone_number": "555-555-5555", "user_id": "4"}' http://localhost:8080/_ah/api/friendfinderapi/v1/friend/insert

echo ""

# list friends

echo "###################################################################"
echo "List FriendFinder friends"
echo "###################################################################"

# need to sleep to allow for datastore to catch up
sleep 2

curl http://localhost:8080/_ah/api/friendfinderapi/v1/friends/list

echo ""

echo "###################################################################"
echo "Get 1 FriendFinder friend"
echo "###################################################################"

curl --header "Content-Type: application/json" -X GET http://localhost:8080/_ah/api/friendfinderapi/v1/friend/retrieve/1

echo ""

# update friend 

echo "###################################################################"
echo "Update FriendFinder friend tom"
echo "###################################################################"

curl --header "Content-Type: application/json" -X PUT -d '{"email": "tom@gmail.com", "name": "tomolicious", "address": "101 Fabulous", "phone_number": "555-555-5555", "id": "3", "user_id": "3"}' http://localhost:8080/_ah/api/friendfinderapi/v1/friend/update

echo ""

# list friends

echo "###################################################################"
echo "List FriendFinder friends"
echo "###################################################################"

# need to sleep to allow for datastore to catch up
sleep 2

curl http://localhost:8080/_ah/api/friendfinderapi/v1/friends/list

echo ""

# delete friend

echo "###################################################################"
echo "Delete FriendFinder friend tom"
echo "###################################################################"

curl -X DELETE http://localhost:8080/_ah/api/friendfinderapi/v1/friend/delete/3

echo ""

# list friends

echo "###################################################################"
echo "List FriendFinder friends"
echo "###################################################################"

# need to sleep to allow for datastore to catch up
sleep 2

curl http://localhost:8080/_ah/api/friendfinderapi/v1/friends/list

echo ""

# test bad creation of friends

echo "###################################################################"
echo "Testing for bad creation of FriendFinder friends"
echo "Duplicate"
echo "###################################################################"

curl --header "Content-Type: application/json" -X POST -d '{"id": "1", "email": "berry@live.com", "name": "berry", "address": "101 Fabulous", "phone_number": "555-555-5555", "user_id": "1"}' http://localhost:8080/_ah/api/friendfinderapi/v1/friend/insert

echo ""

echo "###################################################################"
echo "Testing for bad creation of FriendFinder friends"
echo "Bad Email"
echo "###################################################################"

curl --header "Content-Type: application/json" -X POST -d '{"id": "12", "email": "", "name": "berry", "address": "101 Fabulous", "phone_number": "555-555-5555", "user_id": "12"}' http://localhost:8080/_ah/api/friendfinderapi/v1/friend/insert

echo ""

echo "###################################################################"
echo "Testing for bad creation of FriendFinder friends"
echo "Bad Name"
echo "###################################################################"

curl --header "Content-Type: application/json" -X POST -d '{"id": "12", "email": "berry@live.com", "name": "", "address": "101 Fabulous", "phone_number": "555-555-5555", "user_id": "12"}' http://localhost:8080/_ah/api/friendfinderapi/v1/friend/insert

echo ""

echo "###################################################################"
echo "Testing for bad creation of FriendFinder friends"
echo "Bad Address"
echo "###################################################################"

curl --header "Content-Type: application/json" -X POST -d '{"id": "12", "email": "berry@live.com", "name": "berry", "address": "", "phone_number": "555-555-5555", "user_id": "12"}' http://localhost:8080/_ah/api/friendfinderapi/v1/friend/insert

echo ""

echo "###################################################################"
echo "Testing for bad creation of FriendFinder friends"
echo "Bad Phone"
echo "###################################################################"

curl --header "Content-Type: application/json" -X POST -d '{"id": "12", "email": "berry@live.com", "name": "berry", "address": "101 Fabulous", "phone_number": ""}' http://localhost:8080/_ah/api/friendfinderapi/v1/friend/insert

echo ""

# test bad updates of friends

echo "###################################################################"
echo "Testing for bad updates of FriendFinder friends"
echo "Bad Email"
echo "###################################################################"

curl --header "Content-Type: application/json" -X PUT -d '{"id": "1", "email": "", "name": "berry", "address": "101 Fabulous", "phone_number": "555-555-5555"}' http://localhost:8080/_ah/api/friendfinderapi/v1/friend/update

echo ""

echo "###################################################################"
echo "Testing for bad creation of FriendFinder friends"
echo "Bad Name"
echo "###################################################################"

curl --header "Content-Type: application/json" -X PUT -d '{"id": "1", "email": "berry@live.com", "name": "", "address": "101 Fabulous", "phone_number": "555-555-5555"}' http://localhost:8080/_ah/api/friendfinderapi/v1/friend/update

echo ""

echo "###################################################################"
echo "Testing for bad creation of FriendFinder friends"
echo "Bad Address"
echo "###################################################################"

curl --header "Content-Type: application/json" -X PUT -d '{"id": "1", "email": "berry@live.com", "name": "berry", "address": "", "phone_number": "555-555-5555"}' http://localhost:8080/_ah/api/friendfinderapi/v1/friend/update

echo ""

echo "###################################################################"
echo "Testing for bad creation of FriendFinder friends"
echo "Bad Phone"
echo "###################################################################"

curl --header "Content-Type: application/json" -X PUT -d '{"id": "1", "email": "berry@live.com", "name": "berry", "address": "101 Fabulous", "phone_number": ""}' http://localhost:8080/_ah/api/friendfinderapi/v1/friend/update

echo ""



echo "Get one FriendFinder user"
echo "###################################################################"

curl --header "Content-Type: application/json" -X GET http://localhost:8080/_ah/api/friendfinderapi/v1/user/retrieve/1

echo ""

echo "###################################################################"
echo "List FriendFinder user"
echo "###################################################################"

# need to sleep to allow for datastore to catch up
sleep 2

curl http://localhost:8080/_ah/api/friendfinderapi/v1/user/list

echo ""

# delete user

echo "###################################################################"
echo "Delete FriendFinder user 7"
echo "###################################################################"

curl -X DELETE http://localhost:8080/_ah/api/friendfinderapi/v1/user/delete/4

echo ""

echo "###################################################################"
echo "List FriendFinder user"
echo "###################################################################"

# need to sleep to allow for datastore to catch up
sleep 2

curl http://localhost:8080/_ah/api/friendfinderapi/v1/users/list

echo ""

echo "###################################################################"
echo "Testing Bad Login Login"
echo "###################################################################"

curl --header "Content-Type: application/son" -X PUT -d '{"id":"1", "user_cname": "tom", "user_cpassword": "tom"}' http://localhost:8080/_ah/api/friendfinderapi/v1/login/login

echo ""

echo "###################################################################"
echo "Testing Good Login Login"
echo "###################################################################"

curl --header "Content-Type: application/son" -X PUT -d '{"id":"1", "user_cname": "tom", "user_cpassword": "tom1"}' http://localhost:8080/_ah/api/friendfinderapi/v1/login/login

echo ""

echo "###################################################################"
echo "Testing Login Logout"
echo "###################################################################"

curl --header "Content-Type: application/json" -X PUT -d '{"id":"1"}' http://localhost:8080/_ah/api/friendfinderapi/v1/login/logout


echo ""



