fs = require('fs')
readline = require('readline')
google = require('googleapis')
googleAuth = require('google-auth-library')
# If modifying these scopes, delete your previously saved credentials
# at ~/.credentials/calendar-nodejs-quickstart.json
SCOPES = [ 'https://www.googleapis.com/auth/calendar.readonly' ]
TOKEN_DIR = (process.env.HOME or process.env.HOMEPATH or process.env.USERPROFILE) + '/.credentials/'
TOKEN_PATH = TOKEN_DIR + 'calendar-nodejs-quickstart.json'
# Load client secrets from a local file.

###*
# Create an OAuth2 client with the given credentials, and then execute the
# given callback function.
#
# @param {Object} credentials The authorization client credentials.
# @param {function} callback The callback to call with the authorized client.
###

authorize = (credentials, callback) ->
  clientSecret = credentials.installed.client_secret
  clientId = credentials.installed.client_id
  redirectUrl = credentials.installed.redirect_uris[0]
  auth = new googleAuth
  oauth2Client = new (auth.OAuth2)(clientId, clientSecret, redirectUrl)
  # Check if we have previously stored a token.
  fs.readFile TOKEN_PATH, (err, token) ->
    if err
      getNewToken oauth2Client, callback
    else
      oauth2Client.credentials = JSON.parse(token)
      callback oauth2Client
    return
  return

###*
# Get and store new token after prompting for user authorization, and then
# execute the given callback with the authorized OAuth2 client.
#
# @param {google.auth.OAuth2} oauth2Client The OAuth2 client to get token for.
# @param {getEventsCallback} callback The callback to call with the authorized
#     client.
###

getNewToken = (oauth2Client, callback) ->
  authUrl = oauth2Client.generateAuthUrl(
    access_type: 'offline'
    scope: SCOPES)
  console.log 'Authorize this app by visiting this url: ', authUrl
  rl = readline.createInterface(
    input: process.stdin
    output: process.stdout)
  rl.question 'Enter the code from that page here: ', (code) ->
    rl.close()
    oauth2Client.getToken code, (err, token) ->
      if err
        console.log 'Error while trying to retrieve access token', err
        return
      oauth2Client.credentials = token
      storeToken token
      callback oauth2Client
      return
    return
  return

###*
# Store token to disk be used in later program executions.
#
# @param {Object} token The token to store to disk.
###

storeToken = (token) ->
  try
    fs.mkdirSync TOKEN_DIR
  catch err
    if err.code != 'EEXIST'
      throw err
  fs.writeFile TOKEN_PATH, JSON.stringify(token)
  console.log 'Token stored to ' + TOKEN_PATH
  return

###*
# Lists the next 10 events on the user's primary calendar.
#
# @param {google.auth.OAuth2} auth An authorized OAuth2 client.
###

listEvents = (auth) ->
  calendar = google.calendar('v3')

  calendar.events.list {
    auth: auth
    calendarId: 'primary'
    timeMin: (new Date).toISOString()
    maxResults: 10
    singleEvents: true
    orderBy: 'startTime'
  }, (err, response) ->
    if err
      console.log 'The API returned an error: ' + err
      return
    events = response.items
    if events.length == 0
      console.log 'No upcoming events found.'
    else
      console.log 'Upcoming 10 events:'
      i = 0
      while i < events.length
        event = events[i]
        start = event.start.dateTime or event.start.date
        console.log '%s - %s', start, event.summary
        i++
    return
  return

fs.readFile "#{TOKEN_DIR}/client_secret.json", (err, content) ->
  if err
    console.log 'Error loading client secret file: ' + err
    return
  # Authorize a client with the loaded credentials, then call the
  # Google Calendar API.
  authorize JSON.parse(content), listEvents
  # authorize(JSON.parse(content), listCalendars);
  return
#
# function listCalendars(auth){
#     var calendar = google.calendar('v3');
#     calendar.calendarList.list({
#         auth:auth,
#         maxResults:10
#     },function (err,response){
#         console.log(response);
#     }
# );
#
# }
