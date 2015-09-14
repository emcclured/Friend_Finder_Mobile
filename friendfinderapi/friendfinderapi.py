##############################################################################################
#  Author: Drea McClure                                                                      #
#  Class: CS496                                                                              #
#  Name: friendfinderapi.py                                                                  #
#  Description: This is a simple friend finder restful services api                          #
#                using gae endpoints and ndb datastore                                       #
#  Due Date: 14 Aug 2015                                                                     #
##############################################################################################

###############
#  Libraries  # 
###############

import endpoints

from google.appengine.ext import ndb
from protorpc import remote

# third party gae endpoints library, see README.md for description

from endpoints_proto_datastore.ndb import EndpointsModel

#######################
#  Class Definitions  # 
#######################

# Class Name: Friend
# Description:  This class describes the Friend in the friend datastore

class Friend(EndpointsModel):
  _message_fields_schema = ('id', 'email', 'name', 'address', 'phone_number', 'user_id', 'signup_time')

  # id field is defined in the base ndb.Model 
  email = ndb.StringProperty()
  name = ndb.StringProperty()
  address = ndb.StringProperty()
  phone_number = ndb.StringProperty()
  # user_id is the user this friend is associated with
  user_id = ndb.IntegerProperty()
  signup_time = ndb.DateTimeProperty(auto_now_add=True)

# Class Name: User
# Description: This class describes the User in the friend datastore

class User(EndpointsModel):
  _message_fields_schema = ('id', 'user_name', 'user_password')

  # id field is defined in the base ndb.Model
  user_name = ndb.StringProperty()
  user_password = ndb.StringProperty()

# Class Name: Login
# Description: This class describes if any User is Logged in to the datastore

class Login(EndpointsModel):
  _message_fields_schema = ('id', 'user_cname', 'user_cpassword', 'user_login_id', 'user_logged_in')

  # id field is defined in the base ndb.Model
  user_cname = ndb.StringProperty()
  user_cpassword = ndb.StringProperty()
  user_login_id = ndb.IntegerProperty()
  user_logged_in = ndb.IntegerProperty()

# Class Name: FriendFinderApi
# Description: This class describes the friend api using gae endpoints

@endpoints.api(name='friendfinderapi', version='v1', description='Friend Finder API')
class FriendFinderApi(remote.Service):

###########################
#  API Methods/Functions  #
###########################

################ Friend methods ########################

# Method Name: friend/insert
# Description: inserts a new friend using POST

  @Friend.method(path='friend/insert', http_method='POST', name='friend.insert')
  def FriendInsert(self, my_friend):

    # check to see if friend exists already

    # create a temp friend to test

    if my_friend.id > 0:
      friend = Friend.get_by_id(my_friend.id)

    if my_friend.id > 0:
      if friend:
        raise endpoints.NotFoundException('Friend already exits.')

    # check to see if the fields are not empty

    if not my_friend.email:
      raise endpoints.NotFoundException('email can not be empty.')

    if not my_friend.name:
      raise endpoints.NotFoundException('name can not be empty.')

    if not my_friend.address: 
      raise endpoints.NotFoundException('address can not be empty.')

    if not my_friend.phone_number:
      raise endpoints.NotFoundException('phone_number can not be empty.')

    if not my_friend.user_id:
      raise endpoints.NotFoundException('user_id can not be empty.')

    # insert new friend

    my_friend.put()

    return my_friend

# Method Name: friend/retrieve
# Description: gets a single friend friend using GET

  @Friend.method(request_fields=('id',),
                  path='friend/retrieve/{id}', http_method='GET', name='friend.get')
  def FriendGet(self, my_friend):

    # check to see if friend is in the datastore

    if not my_friend.from_datastore:
      raise endpoints.NotFoundException('Friend not found.')

    return my_friend

# Method Name: friend/delete
# Description: deletes a single friend friend using DELETE

  @Friend.method(request_fields=('id',),
                  path='friend/delete/{id}', http_method='DELETE', name='friend.delete')
  def FriendDelete(self, my_friend):

    if not my_friend.from_datastore:
      raise endpoints.NotFoundException('Friend not found.')

    # delete friend

    my_friend._key.delete()

    return my_friend

# Method Name: friend/update
# Description: updates a single friend friend using PUT

  @Friend.method(request_fields=('id', 'email', 'name', 'address', 'phone_number', 'user_id', ),
                  path='friend/update', http_method='PUT', name='friend.put')
  def FriendPut(self, my_friend):

#   print "id = %d" % my_friend.id
#   print "email = %s" % my_friend.email
#   print "name = %s" % my_friend.name
#   print "address = %s" % my_friend.address
#   print "phone number = %s" % my_friend.phone_number
#   print "user_id = %d" % my_friend.user_id

    # check to see if friend is in the datastore

    if not my_friend.from_datastore:
      raise endpoints.NotFoundException('Friend not found.')

    # check to see if the fields are not empty

    if not my_friend.email:
      raise endpoints.NotFoundException('email can not be empty.')

    if not my_friend.name:
      raise endpoints.NotFoundException('name can not be empty.')

    if not my_friend.address: 
      raise endpoints.NotFoundException('address can not be empty.')

    if not my_friend.phone_number:
      raise endpoints.NotFoundException('phone_number can not be empty.')

    if not my_friend.user_id:
      raise endpoints.NotFoundException('user_id can not be empty.')

    # update the friend

    my_friend.put()

    return my_friend

# Method Name: friends/list
# Description: list all friend friends

  @Friend.query_method(path='friends/list', name='friend.list')
  def FriendList(self, query):
    return query

################### User methods #######################

# Method Name: user/insert
# Description: inserts a new user using POST

  @User.method(path='user/insert', http_method='POST', name='user.insert')
  def UserInsert(self, my_user):

    # check to see if user already exists

    if my_user.from_datastore:
      raise endpoints.NotFoundException('User already exits.')

    # check to see if the fields are not empty

    if not my_user.user_name:
      raise endpoints.NotFoundException('user_name can not be empty.')

    if not my_user.user_password:
      raise endpoints.NotFoundException('user_password can not be empty.')

    # insert new user into the database

    my_user.put()

    return my_user

# Method Name: user/retrieve
# Description: gets a single user using GET

  @User.method(request_fields=('id',),
                  path='user/retrieve/{id}', http_method='GET', name='user.get')
  def UserGet(self, my_user):

    # check to see if user is in the datastore

    if not my_user.from_datastore:
      raise endpoints.NotFoundException('User not found.')

    return my_user

# Method Name: user/delete
# Description: deletes a single user using DELETE

  @User.method(request_fields=('id',),
                  path='user/delete/{id}', http_method='DELETE', name='user.delete')
  def UserDelete(self, my_user):

    # check to see if user is in the datastore

    if not my_user.from_datastore:
      raise endpoints.NotFoundException('User not found.')

    # delete user

    my_user._key.delete()

    return my_user

# Method Name: user/update
# Description: updates a single user using PUT

  @User.method(request_fields=('id', 'user_name', 'user_password', ),
                  path='user/update', http_method='PUT', name='user.put')
  def UserPut(self, my_user):

#   print "id = %d" % my_user.id
#   print "user_name = %s" % my_user.user_name
#   print "user_password = %s" % my_user.user_password

    # check to see if user is in the datastore

    if not my_user.from_datastore:
      raise endpoints.NotFoundException('User not found.')

    # check to see if the fields are not empty

    if not my_user.user_name:
      raise endpoints.NotFoundException('user_name can not be empty.')

    if not my_user.user_password:
      raise endpoints.NotFoundException('user_password can not be empty.')

    # update the user

    my_user.put()

    return my_user

# Method Name: users/list
# Description: list all users

  @User.query_method(path='users/list', name='user.list')
  def UserList(self, query):
    return query

################### Login methods #######################

# Method Name: Login
# Description: Login using PUT

  @Login.method(request_fields=('id', 'user_cname', 'user_cpassword' ),
                  path='login/login', http_method='PUT', name='login.login')
  def LoginLogin(self, my_login):

    # check to see if user exits and password is good

    user = User.query(User.user_name==my_login.user_cname, User.user_password==my_login.user_cpassword).get()

    if not user:
	raise endpoints.NotFoundException('user and/or password can not be found.')

    # set the log in flag

    my_login.user_logged_in = 1

    my_login.user_login_id = user.id

    my_login.put()

    return my_login

# Method Name: Login logout
# Description: Logout using PUT

  @Login.method(request_fields=('id', ),
                  path='login/logout', http_method='PUT', name='login.logout')
  def LoginLogout(self, my_login):

    # reset all fields

    my_login.user_cname = ""
    my_login.user_cpassword = ""
    my_login.user_login_id = 0
    my_login.user_logged_in = 0

    # update the login

    my_login.put()

    return my_login

#################### End of Methods ########################

application = endpoints.api_server([FriendFinderApi], restricted=False)
